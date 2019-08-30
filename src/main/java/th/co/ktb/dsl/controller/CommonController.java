package th.co.ktb.dsl.controller;

import java.util.Date;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocParamAcctNo;
import th.co.ktb.dsl.apidoc.ApiDocParamLoanType;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorizedFile;
import th.co.ktb.dsl.apidoc.Team;
import th.co.ktb.dsl.exception.BadRequestException;
import th.co.ktb.dsl.mock.ServiceSQL;
import th.co.ktb.dsl.mock.ServiceSQL.UploadFile;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.common.DocumentType;
import th.co.ktb.dsl.model.common.DownloadableDocument;
import th.co.ktb.dsl.model.common.LoanType;
import th.co.ktb.dsl.model.common.RequestDocFormExample;
import th.co.ktb.dsl.model.common.RequestReason;
import th.co.ktb.dsl.model.postpone.PostponeReason;
import th.co.ktb.dsl.model.postpone.PostponeStatus;
import th.co.ktb.dsl.model.postpone.PostponeSummary;
import th.co.ktb.dsl.model.suspend.SuspendSummary;

@Api(tags="2.1. DSL-DMS : Common API", description="API ทั่วไปอาจถูกนำไปใช้ในหลาย module")
@RestController
@RequestMapping("/api/v1/dms")
@Slf4j
public class CommonController {
	
	@Autowired ServiceSQL serviceSQL;
	
	private final String uploadRequestDocument = "uploadRequestDocument";
	@ApiOperation(value=uploadRequestDocument + Team.DMS_TEAM, 
			notes="API สำหรับอัพโหลดเอกสาร ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PostMapping(path="/document")
	@ResponseStatus(HttpStatus.CREATED)
	public DownloadableDocument uploadRequestDocument(
		@ApiParam(value="File to upload", required=true) @RequestParam(name="file") MultipartFile file,
		@ApiParam(value="Alias name") @RequestParam(name="alias", required=false) String alias, 
		@ApiParam(value="Document or category type", required=true) @RequestParam(name="docType", required=true) DocumentType docType,
		@ApiParam(value="Reference to document", required=true) @RequestParam(name="refID", required=false) String refID
	) throws Exception {
		UploadFile uploadFile = new UploadFile();
		uploadFile.setContent(file.getBytes());
		uploadFile.setFileName(file.getOriginalFilename());
		uploadFile.setDocType(docType.name());
		uploadFile.setAlias(alias);
		uploadFile.setRefID(refID);
		log.info("Add upload file.");
		serviceSQL.addUplaodFile(uploadFile);
		log.info("File added with ID: {}",uploadFile.getFileID());
		String fileID = uploadFile.getFileID().toString();
        return new DownloadableDocument(fileID, uploadFile.getFileName(), file.getContentType(), file.getSize());
	}

	private final String removeRequestDocument = "removeRequestDocument";
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
		log.info("Remove upload fiile with ID: {}",docID);
		serviceSQL.removeUplaodFile(Integer.valueOf(docID));
	}

	private final String getDocument = "getDocument";
	@ApiOperation(value=getDocument + Team.DMS_TEAM, 
			notes="API สำหรับดาวน์โหลดเอกสาร")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorizedFile
	@GetMapping(path="/document/{docID:.+}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<byte[]> getDocument(
		@PathVariable("docID") String docID
	) throws BadRequestException {
		UploadFile file = serviceSQL.getUplaodFile(Integer.valueOf(docID));
		if (file == null) throw new BadRequestException("Invalid docID");
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		ContentDisposition cd = ContentDisposition.builder("attachment").filename(file.getAlias()!=null?file.getAlias():file.getFileName()).build();
		headers.setContentDisposition(cd);

		Tika tika = new Tika();
	    String mimeType = tika.detect(file.getContent());
		headers.setContentType(MediaType.valueOf( mimeType ));

		log.info("Get upload fiile with ID: {}",docID);
		return new ResponseEntity<byte[]>(file.getContent(),headers,HttpStatus.OK);
	}

	private final String getRequestStatus = "getRequestStatus";
	@Testable
	@ApiOperation(value=getRequestStatus + Team.DMS_TEAM, 
			notes="API สำหรับดึงข้อมูลสถานะร้องขอ ณ ปัจจุบัน "
			+ "โดยข้อมูลจะประกอบด้วยการร้องขอผ่อนผัน/ระงับ ปัจจุบัน และประวัติการร้องขอ"
			+ "(แทน API-getPostponeRequestStatus() และ )​")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}", produces=MediaType.APPLICATION_JSON_VALUE)
	public RequestStatusRs getRequestStatus(		
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo
	) {
		return new RequestStatusRs();
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

	private final String getSummaryRequestStatus = "getSummaryRequestStatus";
	@Testable
	@ApiOperation(value=getSummaryRequestStatus + Team.DMS_TEAM, 
			notes="API สำหรับเรียกดูสถานะคำขอของผู้กู้ (ผ่อนผัน/ระงับ) รองรับการเรียก API จาก module Dashboard ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}/status")
	@ResponseStatus(HttpStatus.OK)
	public RequestSummary getSummaryRequestStatus(
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo) 
	{
		return null;
	}
	
	private final String getAccountStatementHistory = "getAccountStatementHistory";
	@Testable
	@ApiOperation(value=getAccountStatementHistory, 
			notes="API สำหรับดึงข้อมูลประวัติบัญชีกู้ยืม รองรับการเรียก API จาก module Dashboard ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}/hist", produces=MediaType.APPLICATION_JSON_VALUE)
	public AccountStatementHistoryRs getAccountStatementHistory(
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		
		@ApiParam(type="query", value="Data offset", required=false, defaultValue="1") 
		@RequestParam(name="offset", required=false) String offset,
		
		@ApiParam(type="query", value="Limit data size (-1 = unlimit)", required=false, defaultValue="-1") 
		@RequestParam(name="size", required=false) String size,
		
		@ApiParam(type="query", value="Filter year (-1 = all, default is current year)", required=false, defaultValue="2019") 
		@RequestParam(name="year", required=false) String year,
		
		@ApiParam(type="query", required=false,
			value="Filter history type (ประเภทประวัติรายการ ตัวอย่าง ผู้กู้ชำระเงิน, รับเงินค่าเทอม, รับเงินค่าครองชีพ)") 
		@RequestParam(name="histType", required=false) StatementHistoryType histType
	) {
		return null;
	}
}

enum StatementHistoryType {
	PAY,
	REC_TUITION_FEE,
	REC_MONTHLY
}

@Data
@EqualsAndHashCode(callSuper=false)
class AccountStatementHistoryRs {

	@ApiModelProperty(position = 1)
	List<String> filterYear;
	
	@JsonProperty("total") 
	@ApiModelProperty(position = 2)
	int totalRecord = 0;

	@ApiModelProperty(position = 3)
	List<StatementItem> history;
	
}

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
class StatementItem {
	@ApiModelProperty(position = 1, example="2019-08-07 22:55:00")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")  Date statementDTM;
	
	@ApiModelProperty(position = 2)
	String refID;
	
	@ApiModelProperty(position = 3)
	StatementHistoryType statementType;
	
	@ApiModelProperty(position = 4)
	Double amount;
	
	@ApiModelProperty(position = 5)
	Double principal;
	
	@ApiModelProperty(position = 6)
	String receipt; // link, id, ... 
}

@Data
class RequestStatusRs {
	@ApiModelProperty(position = 1, required=false)
	PostponeSummary postponeStatus;

	@ApiModelProperty(position = 2, required=false)
	SuspendSummary suspendStatus;	
}


@Data
class RequestSummary {
	@ApiModelProperty(position = 1, required=false)
	RequestStatusSummary postponeStatus;

	@ApiModelProperty(position = 2, required=false)
	RequestStatusSummary suspendStatus;	
}

@Data 
class RequestStatusSummary {
	@ApiModelProperty(position = 1, required=true)
	String requestNo;

	@ApiModelProperty(position = 2, required=true)
	PostponeReason requestReason;

	@ApiModelProperty(position = 3, required=true)
	PostponeStatus requestStatus;
	
	@ApiModelProperty(position = 4, required=false)
	String statusInfo;
}

enum UserInfoFilter {
	ALL, ADDRESS_INFO, WORKING_INFO, SPOUSE_INFO
}
