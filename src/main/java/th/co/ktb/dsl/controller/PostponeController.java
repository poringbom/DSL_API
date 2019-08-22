package th.co.ktb.dsl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocParamAcctNo;
import th.co.ktb.dsl.apidoc.ApiDocParamLoanType;
import th.co.ktb.dsl.apidoc.ApiDocPathPostponeRequestNo;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.apidoc.Team;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.common.LoanType;
import th.co.ktb.dsl.model.postpone.PostponeDetail;
import th.co.ktb.dsl.model.postpone.PostponeFormExample;
import th.co.ktb.dsl.model.postpone.PostponeReason;
import th.co.ktb.dsl.model.postpone.PostponeRequestCreate;
import th.co.ktb.dsl.model.postpone.PostponeRequestUpdate;
import th.co.ktb.dsl.model.postpone.PostponeSummary;

@Api(tags="2.3. DSL-DMS : Postpone API", description="API หมวดเกี่ยวกับคำร้องเพื่อการผ่อนผัน")
@RestController
@RequestMapping("/api/v1/dms/postpone")
public class PostponeController {

	private final String getPostponeRequestStatus = "getPostponeRequestStatus";
	@Testable
	@ApiOperation(value=getPostponeRequestStatus+" - DEPRECATION !!!!", tags=getPostponeRequestStatus,
			notes="API สำหรับดึงข้อมูลสถานะร้องขอ ณ ปัจจุบัน "
			+ "/ ใช้เรียกหลังจากผู้ใช้มีการเข้าถึงเมนู 'ลดหย่อนและผ่อนชำระ' "
			+ "โดยข้อมูลจะประกอบด้วยการร้องขอผ่อนผันปัจจุบัน และประวัติการร้องขอ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}", produces=MediaType.APPLICATION_JSON_VALUE)
	public PostponeSummary getPostponeRequestStatus(		
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo
	) {
		return new PostponeSummary();
	}

	private final String getPostponeRequest = "getPostponeRequest";
	@Testable
	@ApiOperation(value=getPostponeRequest+Team.DMS_TEAM, tags=getPostponeRequest,
			notes="API สำหรับดึงข้อมูลรายละเอียดการร้องขอผ่อนผันที่กำหนด")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}/{requestNo}", produces=MediaType.APPLICATION_JSON_VALUE)
	public PostponeDetail getPostponeRequest(		
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiDocPathPostponeRequestNo @PathVariable("requestNo") String requestNo
	) {
		return new PostponeDetail();
	}
	
	private final String getPostponeRequiredDocument = "getPostponeRequiredDocument";
	@Testable
	@ApiOperation(value=getPostponeRequiredDocument+" - DEPRECATION !!!!", tags=getPostponeRequiredDocument,
			notes="API สำหรับดึงข้อมูลเอกสารเกี่ยวข้องที่จำเป็นสำหรับการยื่นขอผ่อนผัน")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/document/{reason}", produces=MediaType.APPLICATION_JSON_VALUE)
	public PostponeFormExample[] getPostponeRequiredDocument(			
		@PathVariable("reason") PostponeReason reason
	) {
		return PostponeFormExample.getExample(reason);
	}

	private final String createPostponeRequest = "createPostponeRequest";
	@Testable
	@ApiOperation(value=createPostponeRequest+Team.DMS_TEAM, tags=createPostponeRequest,
			notes="API สำหรับสร้างรายการร้องขอผ่อนผันการชำระเงิน "
			+ "/ ข้อมูลประกอบด้วยรายละเอียดการร้องขอ พร้อมทั้งเอกสารแนบประกอบสำหรับแต่ละเหตุผลการร้องขอ "
			+ " โดยหากผลการดำเนินเรียก API สำเร็จจะคืนผลลัพธ์หมายเลขอ้างอิงคำขอ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path="/{loanType}/{acctNo}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public PostponeRequestUpdate createPostponeRequest(	
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiParam(type="body", value="Postpone request data", required=true) @RequestBody PostponeRequestCreate request
	) {
		return new PostponeRequestUpdate(); // only PostponeRequest.requestID
	}

	private final String updatePostponeRequest = "updatePostponeRequest";
	@Testable
	@ApiOperation(value=updatePostponeRequest+Team.DMS_TEAM, tags=updatePostponeRequest,
			notes="API สำหรับยื่นเพิ่มเติมเอกสารประกอบคำร้องขอผ่อนผันการชำระเงิน "
			+ "/ ข้อมูลแก้ไขการร้องขอ พร้อมทั้งเอกสารแนบเพิ่มเติมหรือเปลี่ยนแปลงระกอบสำหรับแต่ละเหตุผลการร้องขอ "
			+ " โดยหากผลการดำเนินเรียก API สำเร็จจะคืนผลลัพธ์หมายเลขอ้างอิงคำขอ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PatchMapping(path="/{loanType}/{acctNo}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public PostponeRequestUpdate updatePostponeRequest(	
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiParam(type="body", value="Postpone request data", required=true) @RequestBody PostponeRequestUpdate request
	) {
		return new PostponeRequestUpdate();
	}

	private final String cancelPostponeRequest = "cancelPostponeRequest";
	@Testable
	@ApiOperation(value=cancelPostponeRequest+Team.DMS_TEAM, tags=cancelPostponeRequest,
			notes="API สำหรับยกเลิกคำร้องขอผ่อนผันการชำระเงิน "
			+ "/ ข้อมูลยกเลิกร้องขอประกอบด้วย request id ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@DeleteMapping(path="/{loanType}/{acctNo}/{requestNo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelPostponeRequest(	
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiDocPathPostponeRequestNo @PathVariable("requestNo") String requestNo
	) {
		return;
	}
}


