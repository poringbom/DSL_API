package th.co.ktb.dsl.mock;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONException;
import org.json.JSONObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.config.ApiMetadataArgumentResolver;
import th.co.ktb.dsl.model.annotation.ApiMetadata;
import th.co.ktb.dsl.model.common.ApiMetadataRequest;
import th.co.ktb.dsl.model.common.ApiResponseError;

@Aspect
@Configuration
@MapperScan("th.co.ktb.dsl.mock")
@Slf4j
public class ApiTestableAspect {
	
	@Autowired MockResponseSQL mockSQL;
	@Autowired ApiMetadataArgumentResolver apiMetadataResolver;
	
	//private final String regExResponseHeader = "([^\\n]+)?";
	private final String regExResponseHeader = "(.*)\\s*:\\s*(.*)";
	
	private final String DoRestApi = ""
			+ "@annotation(th.co.ktb.dsl.mock.Testable) &&"
			+ "execution(* th.co.ktb.dsl.controller.**.*(..))";
	@SuppressWarnings("rawtypes")
	@Around(DoRestApi)
	public Object interceptProcess(ProceedingJoinPoint jp) throws Throwable{
		MessageFormat mf = new MessageFormat("MockID={0} ; ResponseTxID={1}");
		try {
		    MethodSignature signature = (MethodSignature) jp.getSignature();
		    Method method = signature.getMethod();
		    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
		    Testable testable = method.getAnnotation(Testable.class);
		    if (testable == null) {
		    		log.info("No @Testable"); return jp.proceed();
		    } else if (apiOperation == null) {
	    			log.info("No @apiOperation"); return jp.proceed();
		    } else if ("".equals(apiOperation.value())) {
		    		log.info("No @apiOpeation.value"); return jp.proceed();
		    } else {
		    		HttpServletRequest httpRequest = 
		    				((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
		    				.getRequest();
		    		HttpServletResponse httpResponse = 
		    				((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
		    				.getResponse();
		    		String apiName = apiOperation.value();
		    		String scenario = httpRequest.getHeader("Test-Scenario");
		    		String metaData = httpRequest.getHeader(ApiMetadata.HEADER_NAME);
		    		ApiMetadataRequest apiMetaData = apiMetadataResolver.matches(metaData);
		    		String channel = "";
		    		if (apiMetaData != null) channel = apiMetaData.getSrc();
		    		
		    		log.info("Service = {}, Channel = {}, Test-Scenario = {}", apiName, channel, scenario);
		    		ObjectMapper mapper = new ObjectMapper();
		    		Class returnType = method.getReturnType();
//		    		log.info("({} != {}) is {}",returnType,ResponseEntity.class,(returnType != ResponseEntity.class));
		    		if (returnType == ResponseEntity.class) {
		    			log.info("return type -> ResponseEntity");
		    			return new ResponseEntity(HttpStatus.OK);
		    		} else {
		    			log.info("return type -> {}",returnType);
		    			String testDev = httpRequest.getHeader("Test-Dev");
		    			String temp;
		    			if (testDev != null && !"".equals(testDev) && (temp = mockSQL.getTestScenario(testDev)) != null) {
		    				log.info("Override 'Test-Scenario' by 'Test-Dev' config -> {}",temp);
		    				scenario = temp;
		    			}
		    			MockResponse response = mockSQL.getMockResponse(apiName, channel, scenario);
		    			if (response != null) {
		    				String mockID = String.valueOf(response.getMockID());
		    				String responseTxID = UUID.randomUUID().toString();
		    				httpResponse.addHeader("Mock-Respone", mf.format(new Object[] {mockID, responseTxID}));
		    				if (response.getResponseHeader() != null) {
		    					Pattern p = Pattern.compile(regExResponseHeader,Pattern.MULTILINE);
		    					Matcher m = p.matcher(response.getResponseHeader());
		    					while(m.find()) {
		    						httpResponse.addHeader(m.group(1), m.group(2));
		    					}
		    				}

		    				StringBuffer url = httpRequest.getRequestURL();
		    				if (httpRequest.getQueryString() != null) url.append("?").append(httpRequest.getQueryString());
		    				String requestUrl = url.toString();
		    				String remoteAddr = httpRequest.getRemoteAddr();
		    				String remoteHost = httpRequest.getRemoteHost();
		    				String reqData = extractRequest(httpRequest);
		    				
		    				mockSQL.addMockRequest(new MockRequestLog(responseTxID,
		    						remoteHost, remoteAddr, requestUrl, 
		    						reqData, response.getMockID()));
		    				String responseBody;
		    				if (response.getResponseBodyID() != null) {
		    					responseBody = mockSQL.getMockResponseBody(response.getResponseBodyID());
		    				} else {
		    					responseBody = null;
		    				}
			    			if (response.responseStatus >= 200 && response.responseStatus < 300) {
			    				log.error("Return MockID -> {}, (ResponseID: {})",response.getMockID(), responseTxID);
			    				Object o = mapper.readValue(responseBody, method.getReturnType());
				    			return o;
			    			} else {
			    				httpResponse.setStatus(response.responseStatus);
			    				ApiResponseError error = mapper.readValue(responseBody, ApiResponseError.class);
			    				log.error("ApiResonseError -> {}",error);
			    				throw new TestableException(response.responseStatus, error);
			    			}
		    			} else {
		    				log.info("No matched mock response"); return jp.proceed();
		    			}
		    		}
		    }
		} catch (Throwable t) {
			if (t instanceof TestableException) throw t;
			else {
				Utilities.filterStackTrace(t);
				log.error(t.getMessage(),t);
				throw new TestableException(t);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String extractRequest(HttpServletRequest request) {
		try {
			HttpServletRequest wrappedRequest = new HttpRequestWrapper((HttpServletRequest)request);
			StringBuilder b = new StringBuilder();
			String rawRq = "";
			Boolean multiPartData = Boolean.FALSE;
			if (wrappedRequest.getContentType() != null &&
					wrappedRequest.getContentType().toLowerCase().contains("multipart/form-data")){
				multiPartData = Boolean.TRUE;
				log.debug("request is multipart/form-data");
			}
			
			// ----- Header -----
			b.append("{ root: {")
				.append("\"header\":").append("{\n")
					.append(buildHeaderJson(wrappedRequest))
				.append("}\n").append(",")
			// ----- Body Parameter -----
					.append("\"body\":").append("{\n")
						.append("\"parameters\":").append("{\n");
							if(multiPartData) {
	//									wrappedRequest.get
								Map m = new HashMap();
								m.put("description", wrappedRequest.getParameter("description"));
							    Part filePart = wrappedRequest.getPart("file"); // Retrieves <input type="file" name="file">
							    m.put("content-type", filePart.getContentType());
							    m.put("size", filePart.getSize());
							    m.put("name", filePart.getName());
							    m.put("filename", filePart.getSubmittedFileName());
							    b.append(buildMapJson(m));
							} else {
								b.append(buildParameterJson(wrappedRequest));
							}
						b.append("},\n")
			// ----- Body JSON -----
						.append("\"json\":");
						if (multiPartData){
							b.append("{ \"multipart_form-data\" : true }");
						} else {
							b.append(buildBodyDataJson(wrappedRequest));
						}
					b.append("}\n")
				.append("} }\n");
	//				log.debug("before format: \n"+b);
	
			try {
				// ----- JSON String to XML String -----
				JSONObject json = new JSONObject(b.toString());
				rawRq = json.toString(4);
			} catch (JSONException e) {
				log.warn("Error while collect raw request as json format",e);
			}				
	
			log.trace("### raw REQUEST json format: \n"+rawRq);
			return rawRq;
		} catch (Throwable ex) {
			ex = Utilities.filterStackTrace(ex);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			String sStackTrace = sw.toString(); 
			return (sStackTrace);
		}
	}
	

	private String getFullURL(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}
	
	private StringBuilder buildHeaderJson(HttpServletRequest request) {
		StringBuilder b = new StringBuilder();
		Enumeration<String> h = request.getHeaderNames();
		while (h.hasMoreElements()) {
			String key = h.nextElement();
			String value = null;
			if (NumberUtils.isNumber(request.getHeader(key))) {
				value = (new BigDecimal(request.getHeader(key))).toString();
			} else {
				value = "\""+request.getHeader(key)+"\"";
			}
			b.append("\"").append(key.toLowerCase()).append("\":").append(value).append(",\n");
		}
			b.append("\"url\":\"").append(getFullURL(request)).append("\",\n")
			.append("\"method\":\"").append(request.getMethod()).append("\"\n");
		return b;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private StringBuilder buildMapJson(Map m) {
		StringBuilder b = new StringBuilder();
		java.util.Iterator<String> iter = m.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Object value = m.get(key);
			b.append("\"").append(key).append("\":");
			if (value == null) {
				b.append("\"\",\n");
			} else if (value instanceof Number) {
				b.append(value).append(",\n");
			} else {
				b.append("\"").append(value).append("\",\n");
			}
		}
		b.append("\"data\":\"\"");
		return b;
	}
	
	private StringBuilder buildParameterJson(HttpServletRequest request) {
		StringBuilder b = new StringBuilder();
		Enumeration<String> p = request.getParameterNames();
		while (p.hasMoreElements()) {
			String key = p.nextElement();
			String value = null;
			if (NumberUtils.isNumber(request.getParameter(key))) {
				BigDecimal bd = new BigDecimal(request.getParameter(key));
				DecimalFormat df = new DecimalFormat("##########.##");
				df.setMaximumFractionDigits(2);
				df.setMinimumFractionDigits(0);
				df.setGroupingUsed(false);
				value = df.format(bd);
				if (value.endsWith(".00")) value = (value.substring(0, value.indexOf(".00")));
			} else {
				value = "\""+request.getParameter(key)+"\"";
			}
			b.append("\"").append(key).append("\":").append(value).append("\n");
			if (p.hasMoreElements()) b.append(",");
		}
		return b;
	}
	
	private StringBuilder buildBodyDataJson(HttpServletRequest request) throws IOException{
		String s;
		BufferedReader br = request.getReader();
		StringBuilder b = new StringBuilder();
		while ((s = br.readLine()) != null) {
			b.append(s);
		}
		if (b.length() == 0) {
			return b.append("{}");
		}
			return b;
	}
}



