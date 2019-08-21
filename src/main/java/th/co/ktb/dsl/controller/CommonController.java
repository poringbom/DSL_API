package th.co.ktb.dsl.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import th.co.ktb.dsl.apidoc.ApiDocParamAcctNo;
import th.co.ktb.dsl.apidoc.ApiDocParamLoanType;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.apidoc.Team;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.common.DocumentType;
import th.co.ktb.dsl.model.common.DownloadableDocument;
import th.co.ktb.dsl.model.common.LoanType;
import th.co.ktb.dsl.model.common.RequestDocFormExample;
import th.co.ktb.dsl.model.common.RequestReason;

@Api(tags="2.1. DSL-DMS : Common API", description="API ทั่วไปอาจถูกนำไปใช้ในหลาย module")
@RestController
@RequestMapping("/api/v1/dms")
public class CommonController {
	
	private final String uploadRequestDocument = "uploadRequestDocument";
	@Testable
	@ApiOperation(value=uploadRequestDocument + Team.DMS_TEAM,
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
	@ApiOperation(value=removeRequestDocument + Team.DMS_TEAM,
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
	@ApiOperation(value=getDocument + Team.DMS_TEAM,
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

	private final String getRequestSummary = "getRequestSummary";
	@Testable
	@ApiOperation(value=getRequestSummary + Team.DMS_TEAM,
			notes="API สำหรับดึงข้อมูลสถานะร้องขอ ณ ปัจจุบัน "
			+ "โดยข้อมูลจะประกอบด้วยการร้องขอผ่อนผัน/ระงับ ปัจจุบัน และประวัติการร้องขอ"
			+ "(แทน API-getPostponeRequestStatus() และ )​")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}", produces=MediaType.APPLICATION_JSON_VALUE)
	public RequestSummary getRequestStatus(		
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo
	) {
		return new RequestSummary();
	}
	
	private final String getRequiredDocument = "getRequiredDocument";
	@Testable
	@ApiOperation(value=getRequiredDocument + Team.DMS_TEAM,
			notes="API สำหรับดึงข้อมูลเอกสารเกี่ยวข้องที่จำเป็นสำหรับการยื่นขอผ่อนผัน/ระงับ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/document/{reason}", produces=MediaType.APPLICATION_JSON_VALUE)
	public RequestDocFormExample[] getRequiredDocument(			
		@PathVariable("reason") RequestReason reason
	) {
		return RequestDocFormExample.getExample(reason);
	}
}
