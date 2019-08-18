package th.co.ktb.dsl.apidoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import th.co.ktb.dsl.model.common.ApiResponseError;

@Target(value={ElementType.METHOD,ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@ApiResponses(value = {
	    @ApiResponse(code = 500, message = "Internal server error", response = ApiResponseError.class)
	})
public @interface ApiDocResponseNoAuthorized {
	ApiResponse[] value() default {};
}
