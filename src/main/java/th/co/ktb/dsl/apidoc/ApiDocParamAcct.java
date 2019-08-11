package th.co.ktb.dsl.apidoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import th.co.ktb.dsl.model.common.LoanType;

@Inherited
@Target(value={ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@ApiImplicitParams({
	@ApiImplicitParam(name = "acctNo", value = "Loan account number", required = true, 
			allowEmptyValue = false, paramType = "path", dataTypeClass = String.class, 
			example = "1234567890"),
	@ApiImplicitParam(name = "loanType", value = "Account Type", required = true, 
			allowEmptyValue = false, paramType = "path", dataTypeClass = LoanType.class) 
//			example = "src=andriod(spec...), dest=dsl-dms, service=PaymentInfo")
})
public @interface ApiDocParamAcct {}
