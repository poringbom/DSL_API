package th.co.ktb.dsl.model.user;

import java.util.ArrayList;
import java.util.List;

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

	@ApiModelProperty(position = 10, required=true)
	Boolean gracePeriod;
	
	public static List<LoanInfo> getExampleLoanInfo() {
		List<LoanInfo> lst = new ArrayList<LoanInfo>();
		lst.add(new LoanInfo(LoanType.F101,"1234567890"));
		lst.add(new LoanInfo(LoanType.F201,"0987654321"));
		return lst;
	}
}
