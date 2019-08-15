package th.co.ktb.dsl.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.common.DocumentType;
import th.co.ktb.dsl.model.common.DownloadableDocument;

@Api(tags="DSL-DMS : Common API", description="API ทั่วไปอาจถูกนำไปใช้ในหลาย module")
@RestController
@RequestMapping("/api/v1/dms")
public class CommonController {
	
	private final String uploadRequestDocument = "uploadRequestDocument";
	@Testable
	@ApiOperation(value=uploadRequestDocument,
			notes="API สำหรับอัพโหลดเอกสาร ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PostMapping(path="/document")
	@ResponseStatus(HttpStatus.CREATED)
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

	private final String removeRequestDocument = "removeRequestDocument";
	@Testable
	@ApiOperation(value=removeRequestDocument,
			notes="API สำหรับลบเอกสาร")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@DeleteMapping(path="/document/{docID}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeRequestDocument(
		@ApiParam(value="File to upload", required=true) @PathVariable("docID") String docID,
		@ApiParam(value="Reference to document", required=true) @RequestParam(name="refID", required=true) String refID
	) {
		return;
	}

	private final String getDocument = "getDocument";
	@Testable
	@ApiOperation(value=getDocument,
			notes="API สำหรับดาวน์โหลดเอกสาร")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/document/{docID:.+}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<byte[]> getDocument(
		@PathVariable("docID") String docID
	) {
        Resource resource=new ClassPathResource("DSL_WOW.pdf");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = resource.getInputStream()){
            byte[] buf = new byte[2048]; int cnt = 0;
        		while ((cnt=is.read(buf)) > 0) {
        			baos.write(buf,0,cnt);
        		}
        } catch (IOException ex) {}
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(baos.toByteArray());
	}
}
