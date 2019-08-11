package th.co.ktb.dsl.apidoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiParam;

@Target(value={ElementType.PARAMETER,ElementType.METHOD,ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ApiDocParamSize {
	ApiParam param = ApiDocAnnotation.getInstanceOfApiParam(ParamName.SIZE);
}
