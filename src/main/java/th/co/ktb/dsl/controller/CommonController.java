package th.co.ktb.dsl.controller;

import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.net.HttpHeaders;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import th.co.ktb.dsl.model.common.ApiResponseError;
import th.co.ktb.dsl.model.common.DocumentType;
import th.co.ktb.dsl.model.common.DownloadableDocument;
import th.co.ktb.dsl.model.payment.Payment;

@Api(tags="DSL - Common API", description="API ทั่วไปอาจถูกนำไปใช้ในหลาย module")
@RestController
@RequestMapping("/api/v1")
public class CommonController {

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
        ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/api/v1")
			.path("/document")
			.path("/"+docID)
			.toUriString();
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
