package th.co.ktb.dsl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.config.ApiMetadata;
import th.co.ktb.dsl.model.annotation.ApiMetadataRequest;
import th.co.ktb.dsl.model.common.TestModel;
import th.co.ktb.dsl.service.TestService;


@Api(hidden=true)
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TestAPI {
	@Autowired TestService service;
	
	@ApiOperation("Get opeation.")
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
			@ApiMetadata ApiMetadataRequest apiMeta,
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
		return tm;
	}
	
	@GetMapping(path="/changelog", produces=MediaType.TEXT_PLAIN_VALUE)
	public String getChangeLog() throws IOException {
		Resource r = new ClassPathResource("messages/change_log.txt");
		File f = r.getFile();
		byte[] buff = new byte[(int)f.length()];
		try (FileInputStream fr = new FileInputStream(f)){
			fr.read(buff);
		} 
		return new String(buff,"UTF-8");
	}
}
