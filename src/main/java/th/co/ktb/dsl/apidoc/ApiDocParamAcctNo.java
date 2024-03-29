package th.co.ktb.dsl.apidoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiParam;

@Inherited
@Target(value={ElementType.PARAMETER,ElementType.METHOD,ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ApiDocParamAcctNo {
	public ApiParam param = ApiDocAnnotation.getInstanceOfApiParam(ParamName.ACCT_NO);
}
