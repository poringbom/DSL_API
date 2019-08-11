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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocParamAcctNo;
import th.co.ktb.dsl.apidoc.ApiDocParamLoanType;
import th.co.ktb.dsl.apidoc.ApiDocPathPostponeRequestID;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.model.common.LoanType;
import th.co.ktb.dsl.model.postpone.PostponeDetail;
import th.co.ktb.dsl.model.postpone.PostponeFormExample;
import th.co.ktb.dsl.model.postpone.PostponeReason;
import th.co.ktb.dsl.model.postpone.PostponeRequestCreate;
import th.co.ktb.dsl.model.postpone.PostponeRequestUpdate;
import th.co.ktb.dsl.model.postpone.PostponeSummary;

@Api(tags="DMS - Postpone API", description="API หมวดเกี่ยวกับคำร้องเพื่อการผ่อนผัน")
@RestController
@RequestMapping("/api/v1/dms/postpone")
public class PostponeController {

	@ApiOperation(value="API สำหรับดึงข้อมูลสถานะร้องขอ ณ ปัจจุบัน "
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

	@ApiOperation(value="API สำหรับดึงข้อมูลรายละเอียดการร้องขอผ่อนผันที่กำหนด")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}/{requestID}", produces=MediaType.APPLICATION_JSON_VALUE)
	public PostponeDetail getPostponeRequest(		
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiDocPathPostponeRequestID @PathVariable("requestID") String requestID
	) {
		return new PostponeDetail();
	}
	
	@ApiOperation(value="API สำหรับดึงข้อมูลเอกสารเกี่ยวข้องที่จำเป็นสำหรับการยื่นขอผ่อนผัน")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/document/{reason}", produces=MediaType.APPLICATION_JSON_VALUE)
	public PostponeFormExample[] getRequiredDocument(			
//		@ApiDocPathPostponeRequestID @PathVariable("requestID") String requestID
		@PathVariable("reason") PostponeReason reason
	) {
		return PostponeFormExample.getExample(reason);
	}

	@ApiOperation(value="API สำหรับสร้างรายการร้องขอผ่อนผันการชำระเงิน "
			+ "/ ข้อมูลประกอบด้วยรายละเอียดการร้องขอ พร้อมทั้งเอกสารแนบประกอบสำหรับแต่ละเหตุผลการร้องขอ "
			+ " โดยหากผลการดำเนินเรียก API สำเร็จจะคืนผลลัพธ์หมายเลขอ้างอิงคำขอ")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer <access_token>"),
	})
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

	@ApiOperation(value="API สำหรับยื่นเพิ่มเติมเอกสารประกอบคำร้องขอผ่อนผันการชำระเงิน "
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

	@ApiOperation(value="API สำหรับยกเลิกคำร้องขอผ่อนผันการชำระเงิน "
			+ "/ ข้อมูลยกเลิกร้องขอประกอบด้วย request id ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@DeleteMapping(path="/{loanType}/{acctNo}/{requestID}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelPostponeRequest(	
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiDocPathPostponeRequestID @PathVariable("requestID") String reqID
	) {
		return;
	}
}


