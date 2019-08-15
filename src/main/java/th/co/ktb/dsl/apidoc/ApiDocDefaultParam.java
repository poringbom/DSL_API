package th.co.ktb.dsl.apidoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiImplicitParam;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.PARAMETER,ElementType.METHOD,ElementType.FIELD})
public @interface ApiDocDefaultParam {
	public static ApiImplicitParam apiMetadata = ApiDocAnnotation.getIntanceOfApiImplicitParam(ParamName.API_METADATA); 
}
