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
import th.co.ktb.dsl.apidoc.ApiDocPathSuspendRequestNo;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.model.common.LoanType;
import th.co.ktb.dsl.model.suspend.SuspendDetail;
import th.co.ktb.dsl.model.suspend.SuspendFormExample;
import th.co.ktb.dsl.model.suspend.SuspendReason;
import th.co.ktb.dsl.model.suspend.SuspendRequestCreate;
import th.co.ktb.dsl.model.suspend.SuspendRequestUpdate;
import th.co.ktb.dsl.model.suspend.SuspendSummary;

@Api(tags="2.4. DSL-DMS : Suspend API", description="API หมวดเกี่ยวกับคำร้องเพื่อระงับการชำระเงิน")
@RestController
@RequestMapping("/api/v1/dms/suspend")
public class SuspendController {

	@ApiOperation(nickname="getLoneStatus",
			value="API สำหรับดึงข้อมูลสถานะร้องขอ ณ ปัจจุบัน "
			+ "/ ใช้เรียกหลังจากผู้ใช้มีการเข้าถึงเมนู 'ระงับการชำระเงิน' "
			+ "โดยข้อมูลจะประกอบด้วยการร้องขอระงับการชำระเงินปัจจุบัน และประวัติการร้องขอ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}/", produces=MediaType.APPLICATION_JSON_VALUE)
	public SuspendSummary getLoneStatus(		
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo
	) {
		return new SuspendSummary();
	}

	@ApiOperation(value="API สำหรับดึงข้อมูลรายละเอียดการร้องขอระงับการชำระเงิน")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}/{requestNo}", produces=MediaType.APPLICATION_JSON_VALUE)
	public SuspendDetail getSuspendRequest(		
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiDocPathSuspendRequestNo @PathVariable("requestNo") String requestNo
	) {
		return new SuspendDetail();
	}
	
	@ApiOperation(value="API สำหรับดึงข้อมูลเอกสารเกี่ยวข้องที่จำเป็นสำหรับการยื่นขอระงับการชำระเงิน")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/document/{reason}", produces=MediaType.APPLICATION_JSON_VALUE)
	public SuspendFormExample[] getRequiredDocument(			
		@PathVariable("reason") SuspendReason reason
	) {
		return null;
	}

	@ApiOperation(value="API สำหรับสร้างรายการร้องขอระงับการชำระเงิน "
			+ "/ ข้อมูลประกอบด้วยรายละเอียดการร้องขอ พร้อมทั้งเอกสารแนบประกอบสำหรับแต่ละเหตุผลการร้องขอ "
			+ " โดยหากผลการดำเนินเรียก API สำเร็จจะคืนผลลัพธ์หมายเลขอ้างอิงคำขอ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path="/{loanType}/{acctNo}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public SuspendRequestUpdate createSuspendRequest(	
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiParam(type="body", value="Suspend request data", required=true) @RequestBody SuspendRequestCreate request
	) {
		return new SuspendRequestUpdate(); // only PostponeRequest.requestNo
	}

	@ApiOperation(value="API สำหรับยื่นเพิ่มเติมเอกสารประกอบคำร้อง หรือตามเรียกขอจากเจ้าหน้าที่ สำหรับขอระงับการชำระเงิน "
			+ "/ ข้อมูลแก้ไขการร้องขอ พร้อมทั้งเอกสารแนบเพิ่มเติมหรือเปลี่ยนแปลงระกอบสำหรับแต่ละเหตุผลการร้องขอ "
			+ " โดยหากผลการดำเนินเรียก API สำเร็จจะคืนผลลัพธ์หมายเลขอ้างอิงคำขอ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PatchMapping(path="/{loanType}/{acctNo}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public SuspendRequestUpdate updateSuspendRequest(	
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiParam(type="body", value="Suspend request data", required=true) @RequestBody SuspendRequestUpdate request
	) {
		return new SuspendRequestUpdate();
	}

	@ApiOperation(value="API สำหรับยกเลิกคำร้องรับงับการชำระเงิน "
			+ "/ ข้อมูลยกเลิกร้องขอประกอบด้วย request id ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@DeleteMapping(path="/{loanType}/{acctNo}/{requestNo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelSuspendRequest(	
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiDocPathSuspendRequestNo @PathVariable("requestNo") String reqNo
	) {
		return;
	}
}
