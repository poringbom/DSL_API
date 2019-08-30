package th.co.ktb.dsl.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import th.co.ktb.dsl.DateUtil;
import th.co.ktb.dsl.model.common.LoanType;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(value=Include.NON_EMPTY)
public class LoanInfo {

	@ApiModelProperty(position = 1, required=true)
	@NonNull LoanType loanType;

	@ApiModelProperty(position = 2, required=true)
	@NonNull String acctNo;

	@ApiModelProperty(position = 3, required=true)
	AccountStatus acctStatus = AccountStatus.NORMAL;

	@ApiModelProperty(position = 4, required=true)
	ReceiveingAccount receivingAcct = new ReceiveingAccount();

	@ApiModelProperty(position = 5, required=true)
	Double totalPaid = 40000d ;

	@ApiModelProperty(position = 6, required=true)
	Double principal = 60000d;

	@ApiModelProperty(position = 7, required=true)
	Double totalDebt = 100000d;

	@ApiModelProperty(position = 9, example="2019-08-07 22:55:00")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") 
	Date lastUpdateDTM = DateUtil.currTime();

	@ApiModelProperty(position = 10, example="2019-08-07 22:55:00")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") 
	Date lastPayment = DateUtil.currTime();;
	

	@ApiModelProperty(position = 11)
	Double lastPaymentAmount = 2000d;

	@ApiModelProperty(position = 20, required=true)
	Boolean gracePeriod = false;
	
	public static List<LoanInfo> getExampleLoanInfo() {
		List<LoanInfo> lst = new ArrayList<LoanInfo>();
		lst.add(new LoanInfo(LoanType.F101,"1234567890"));
		lst.add(new LoanInfo(LoanType.F201,"0987654321"));
		return lst;
	}
	
	public static enum AccountStatus {
		NORMAL,
		CLOSE
	}
	
	public static enum Bank {
		KTB,
		SCB,
		BBL,
		KBANK
	}
	
	public static enum AccountType {
		SAVING,
		FIX_DEPOSIT,
		BBL,
		KBANK
	}
	
	@Data
	@NoArgsConstructor
	public static class ReceiveingAccount {
		Bank bank = Bank.KTB;
		String acctNo = "1112223334";
		AccountType acctType = AccountType.SAVING;
	}
}
