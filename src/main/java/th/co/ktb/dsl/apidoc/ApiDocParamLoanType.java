package th.co.ktb.dsl.apidoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiParam;

@Target(value={ElementType.PARAMETER,ElementType.METHOD,ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ApiDocParamLoanType {
	ApiParam param = ApiDocAnnotation.getInstanceOfApiParam(ParamName.LOAN_TYPE);
	String value() default "";
}
