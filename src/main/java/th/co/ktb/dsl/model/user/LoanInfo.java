package th.co.ktb.dsl.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import th.co.ktb.dsl.model.common.LoanType;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LoanInfo {

	@ApiModelProperty(position = 1, required=true)
	@NonNull LoanType loanType;

	@ApiModelProperty(position = 2, required=true)
	@NonNull String acctNo;

	@ApiModelProperty(position = 3, required=true)
	Double totalPaid;

	@ApiModelProperty(position = 4, required=true)
	Double principal;

	@ApiModelProperty(position = 5, required=true)
	Double totalDebt;

	@ApiModelProperty(position = 9, example="2019-08-07 22:55:00")
	Date lastUpdateDTM;

	@ApiModelProperty(position = 10, example="2019-08-07 22:55:00")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date lastPayment;
	

	@ApiModelProperty(position = 11)
	Double lastPaymentAmount;

	@ApiModelProperty(position = 20, required=true)
	Boolean gracePeriod;
	
	public static List<LoanInfo> getExampleLoanInfo() {
		List<LoanInfo> lst = new ArrayList<LoanInfo>();
		lst.add(new LoanInfo(LoanType.F101,"1234567890"));
		lst.add(new LoanInfo(LoanType.F201,"0987654321"));
		return lst;
	}
}
