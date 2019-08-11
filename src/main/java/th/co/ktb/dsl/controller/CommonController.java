package th.co.ktb.dsl.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.HttpHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.model.common.ApiResponseError;
import th.co.ktb.dsl.model.common.DocumentType;
import th.co.ktb.dsl.model.common.TestModel;
import th.co.ktb.dsl.model.common.DownloadableDocument;
import th.co.ktb.dsl.model.payment.Payment;

@Api(tags="Common API", description="API ทั่วไปอาจถูกนำไปใช้ในหลาย module")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CommonController {
	
	@PostMapping("/test")
	public TestModel test (@RequestBody TestModel m) {
		log.info(m.toString());
		TestModel tm = new TestModel();
		tm.setAmount(new BigDecimal(4.9));
		tm.setDtm(Calendar.getInstance().getTime());
		log.info(tm.toString());
		return tm;
	}
	
	@ApiOperation(value="API สำหรับการ Authentication โดยผลสำเร็จจะแนบ Token กลับมาด้วยใน header response ")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "OK", responseHeaders = {
		        @ResponseHeader(name = "Authorization", description = "JWT Token", response = String.class),
		    }),
		    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
		})
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public void login(
		@RequestParam("userID") String userID,
		@RequestParam("password") String password
	) {
		
	}
	
	@ApiOperation(value="API สำหรับการ logout ")
	@ApiResponses(value = {
		    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
		})
	@PostMapping("/logout")
	public void logout(
		@RequestHeader("Authorization") String token
	) {}
	

	@ApiOperation(value="API สำหรับอัพโหลดเอกสาร ")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer <access_token>")
	})
	@ApiResponses(value = {
	    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
	})
	@PostMapping(path="/document")
	public DownloadableDocument uploadRequestDocument(
		@ApiParam(value="File to upload", required=true) @RequestParam(name="file") MultipartFile file,
		@ApiParam(value="Alias name") @RequestParam(name="alias", required=false) String alias, 
		@ApiParam(value="Document or category type", required=false) @RequestParam(name="docType", required=true) DocumentType docType,
		@ApiParam(value="Reference to document", required=true) @RequestParam(name="refID", required=false) String refID
	) {
        String name = file.getOriginalFilename();
        String docID = UUID.randomUUID().toString();
//        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
//			.path("/api/v1")
//			.path("/document")
//			.path("/"+docID)
//			.toUriString();
        return new DownloadableDocument(docID, name, file.getContentType(), file.getSize());
	}

	@ApiOperation(value="API สำหรับลบเอกสาร")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer <access_token>"),
	})
	@ApiResponses(value = {
	    @ApiResponse(code = 204, message = "No content", response = Payment.class),
	    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
	})
	@DeleteMapping(path="/document/{docID:.+}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeRequestDocument(
		@ApiParam(value="File to upload", required=true) @PathVariable("docID") String docID,
		@ApiParam(value="Reference to document") @RequestParam(name="refID", required=false) String refID
	) {
		return;
	}
		
	@ApiOperation(value="API สำหรับดาวน์โหลดเอกสาร")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer <access_token>"),
	})
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "OK", response = byte[].class),
		    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
		    @ApiResponse(code = 404, message = "Not found", response = ApiResponseError.class),
		})
	@GetMapping(path="/document/{docID:.+}")
	public ResponseEntity<Resource> getDocument(
		@PathVariable("docID") String docID
	) {
        Resource resource=new ClassPathResource("DSL_WOW.pdf");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}
}
