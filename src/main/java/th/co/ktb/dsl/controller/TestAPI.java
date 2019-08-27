package th.co.ktb.dsl.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.DateUtil;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.config.security.UserToken;
import th.co.ktb.dsl.mock.ServiceSQL;
import th.co.ktb.dsl.mock.ServiceSQL.UploadFile;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.annotation.ApiMetadata;
import th.co.ktb.dsl.model.common.ApiMetadataRequest;
import th.co.ktb.dsl.model.common.TestModel;
import th.co.ktb.dsl.service.TestService;


@Api(description="${TEST.DESCRIPTION}", value="${TEST.API}")
@RestController
@RequestMapping("/api")
@Slf4j
public class TestAPI {
	@Autowired TestService service;
	@Autowired ServiceSQL sql;
	
	private final String GET_TEST = "getTest()";
	@Testable
	@ApiOperation(nickname=GET_TEST, value="acknowledgeNotification")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successful"),
	        @ApiResponse(code = 401, message = "No authorization"),
	        @ApiResponse(code = 404, message = "Not found"),
	        @ApiResponse(code = 500, message = "Failure.")
	})
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping("/")
	public String getTest(
			@ApiMetadata(desName="",serviceName="") ApiMetadataRequest apiMeta,
			@ApiParam(value="Param of operation.", allowEmptyValue=false, defaultValue="World", required=true) 
			@RequestParam(required=true) String name,
			@RequestParam(required=false, defaultValue="false") Boolean error) throws Exception{

		log.info("apiMeta -> {}",apiMeta);
		if (error) service.doError();
		return service.doHello(name);
	}

	@PostMapping("/")
	public TestModel test (@RequestBody TestModel m) {
		log.info(m.toString());
		TestModel tm = new TestModel();
		tm.setAmount(new BigDecimal(4.9));
		tm.setDtm(Calendar.getInstance().getTime());
		log.info(tm.toString());
		

		UserToken token = new UserToken();
		token.setUserID(1);
		token.setExpiredTime(DateUtil.currTime());
		token.setTokenValue("xxx");
		token.setAction("Test");
		token.setLogin("pongchet");
		sql.addNewToken(token);
		log.info("token: {}", token);
		
		UploadFile upload = new UploadFile();
		upload.setAlias("alias");
		upload.setDocType("xx.txt");
		upload.setRefID("1");
		upload.setFileName("yy.txt");
		upload.setContent(new byte[]{});
		sql.addUplaodFile(upload);
		log.info("upload: {}", upload);
		return tm;
	}
	
	@ApiOperation(hidden=true, value="getChangeLog")
	@GetMapping(path="/changelog", produces=MediaType.TEXT_PLAIN_VALUE)
	public String getChangeLog() throws IOException {
		Resource r = new ClassPathResource("messages/change_log.txt");
		byte[] buff = new byte[4096];
		int cnt;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InputStream fr = r.getInputStream()){
			while (-1 != (cnt=fr.read(buff))) {
				baos.write(buff, 0, cnt);
			};
		} 
		return new String(baos.toByteArray(),"UTF-8");
	}
}
