package th.co.ktb.dsl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import th.co.ktb.dsl.model.payment.InstallmentSchedule;
import th.co.ktb.dsl.model.payment.PaymentHistory;
import th.co.ktb.dsl.model.payment.PaymentInfo;
import th.co.ktb.dsl.model.payment.PaymentRequest;
import th.co.ktb.dsl.model.payment.PaymentRequestATM;
import th.co.ktb.dsl.model.payment.PaymentRequestQR;
import th.co.ktb.dsl.model.payment.PaymentRequestTeller;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocParamAcctNo;
import th.co.ktb.dsl.apidoc.ApiDocParamLoanType;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.common.LoanType;

@Api(tags="2.2. DSL-DMS : Payment API", description="API หมวดเกี่ยวกับการชำระเงิน/ข้อมูลการชำระเงิน")
@RestController
@RequestMapping("/api/v1/dms/payment")
public class PaymentController {

	private final String getPaymentInfo = "getPaymentInfo";
	@Testable
	@ApiOperation(value=getPaymentInfo, 
			notes="API สำหรับดึงข้อมูลกำหนดการชำระเงินตามผู้ใช้ปัจจุบัน และบัญชีกู้ยืมที่กำหนด "
			+ "/ ใช้เรียกเมื่อมีการเข้าถึงเมนู 'ชำระเงิน'")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}", produces=MediaType.APPLICATION_JSON_VALUE)
	public PaymentInfo getPaymentInfo(
		@ApiDocParamLoanType @PathVariable(name="loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo
	) {
		return new PaymentInfo();
	}

	private final String createPaymentQR = "createPaymentQR";
	@Testable
	@ApiOperation(value=createPaymentQR,
			notes="API สำหรับสร้างข้อมูลการชำระเงินผ่านทาง QR Code "
			+ "/ ใช้เรียกเมื่อผู้ใช้ตัดสินใจเลือกช่องทางการชำระเงินตามที่กำหนด "
			+ "โดยคืนรูปภาพและคำแนะนำสำหรับการชำระเงิน")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path="/{loanType}/{acctNo}/qr")
	public PaymentRequestQR createPaymentQR (
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiParam(value="Amount to pay", required=true)  @RequestBody PaymentRequest payment
	) {
		return PaymentRequestQR.getExampleQRResponse();
	}
	
	private final String createPaymentTeller = "createPaymentTeller";
	@Testable
	@ApiOperation(value=createPaymentTeller,
			notes="API สำหรับสร้างข้อมูลการชำระเงินผ่านทาง Bank Teller "
			+ "/ ใช้เรียกเมื่อผู้ใช้ตัดสินใจเลือกช่องทางการชำระเงินตามที่กำหนด "
			+ "โดยคืนรูปภาพแบบฟอร์มสำหรับการชำระเงินและคำแนะนำสำหรับการชำระเงินผ่านทางเคาว์เตอร์ธนาคาร")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path="/{loanType}/{acctNo}/teller")
	public PaymentRequestTeller createPaymentTeller (
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiParam(value="Amount to pay", required=true)  @RequestBody PaymentRequest payment
	) {
		return PaymentRequestTeller.getExampleTellerResponse();
	}

	private final String createPaymentATM = "createPaymentATM";
	@Testable
	@ApiOperation(value=createPaymentATM,
			notes="API สำหรับสร้างข้อมูลการชำระเงินผ่านทาง ATM "
			+ "/ ใช้เรียกเมื่อผู้ใช้ตัดสินใจเลือกช่องทางการชำระเงินตามที่กำหนด "
			+ "โดยคืนคำแนะนำสำหรับการชำระเงินผ่านช่องทาง ATM")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path="/{loanType}/{acctNo}/atm")
	public PaymentRequestATM createPaymentATM (
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		@ApiParam(value="Amount to pay", required=true)  @RequestBody PaymentRequest payment
	) {
		return PaymentRequestATM.getExampleATMResponse();
	}

	private final String getInstallmentSchedule = "getInstallmentSchedule";
	@Testable
	@ApiOperation(value=getInstallmentSchedule,
			notes="API สำหรับดึงข้อมูลตารางการผ่อนชำระตามผู้ใช้ปัจจุบัน และบัญชีกู้ยืมที่กำหนด "
			+ "/ ใช้เรียกเมื่อมีการเข้าถึงเมนู 'ตารางผ่อนชำระ' "
			+ "โดยคืนเป็นตารางลำดับการชำระเงินรายปี")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}/schedule", produces=MediaType.APPLICATION_JSON_VALUE)
	public InstallmentSchedule getInstallmentSchedule(
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo
	) {
		return InstallmentSchedule.getExampleInstallmentSchedule();
	}
	
	private final String getPaymentHistory = "getPaymentHistory";
	@Testable
	@ApiOperation(value=getPaymentHistory,
			notes="API สำหรับดึงข้อมูลประวัติการชำระเงินตามผู้ใช้ปัจจุบัน และบัญชีกู้ยืมที่กำหนด "
			+ "/ ใช้เรียกเมื่อมีการเข้าถึงเมนู 'รายการย้อนหลัง'")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/{loanType}/{acctNo}/hist", produces=MediaType.APPLICATION_JSON_VALUE)
	public PaymentHistory getPaymentHistory(
		@ApiDocParamLoanType @PathVariable("loanType") LoanType loanType,
		@ApiDocParamAcctNo @PathVariable("acctNo") String acctNo,
		
		@ApiParam(type="query", value="Data offset", required=false, defaultValue="1") 
		@RequestParam(name="offset", required=false) String offset,
		
		@ApiParam(type="query", value="Limit data size (-1 = unlimit)", required=false, defaultValue="-1") 
		@RequestParam(name="size", required=false) String size,
		
		@ApiParam(type="query", value="Filter year (-1 = all, default is current year)", required=false, defaultValue="2019") 
		@RequestParam(name="year", required=false) String year,
		
		@ApiParam(type="query", value="Include money receive record from DSL", required=false, defaultValue="false") 
		@RequestParam(name="includeRec", required=false, defaultValue="false") Boolean includeReceive
	) {
		return PaymentHistory.getExamplePaymentHistory(Integer.parseInt(acctNo));
	}

}













	