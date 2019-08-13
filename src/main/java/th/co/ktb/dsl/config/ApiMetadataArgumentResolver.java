package th.co.ktb.dsl.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import th.co.ktb.dsl.exception.BadRequestException;
import th.co.ktb.dsl.model.annotation.ApiMetadata;
import th.co.ktb.dsl.model.common.ApiMetadataRequest;
import th.co.ktb.dsl.model.common.ApiRequestInputType;

public class ApiMetadataArgumentResolver implements HandlerMethodArgumentResolver {
	public final String p = "(src=(.*))(dest=(.*))(service=(.*))";
	public final Pattern pattern;
	
	public ApiMetadataArgumentResolver() {
		this.pattern = Pattern.compile(p);
	}
	
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(ApiMetadataRequest.class) && 
        		(parameter.getParameterAnnotation(ApiMetadata.class) != null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, 
    		ModelAndViewContainer mavContainer, NativeWebRequest webRequest, 
    		WebDataBinderFactory binderFactory) throws Exception {
    	
    		ApiMetadata header = parameter.getParameterAnnotation(ApiMetadata.class);
        String apiMetaData = webRequest.getHeader(header.value());
        if (apiMetaData != null) {
	    		Matcher m = pattern.matcher(apiMetaData);
	    		if (m.matches() && m.groupCount() >= 6) {
	    	        if (!"".equals(header.desName()) && !header.desName().equals(m.group(4))) {
	    	        		throw new BadRequestException("Expecting 'des'='"+header.desName()+"' for '"+header.value()+"' from HTTP_HEADER");
	    	        }
	    	        if (!"".equals(header.serviceName()) && !header.desName().equals(m.group(6))) {
	    	        		throw new BadRequestException("Expecting 'service'='"+header.serviceName()+"' for '"+header.value()+"' from HTT_HEADER");
	    	        }
	    			return new ApiMetadataRequest(m.group(2),m.group(4),m.group(6));
	    		} else {
	    			throw new BadRequestException(ApiRequestInputType.HTTP_HEADER,header.value());
	    		}
        } else {
        		throw new BadRequestException(ApiRequestInputType.HTTP_HEADER,header.value());
//            return WebArgumentResolver.UNRESOLVED;
        }
    }
}
