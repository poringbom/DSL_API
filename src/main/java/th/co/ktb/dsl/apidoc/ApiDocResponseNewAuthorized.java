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
//    @ApiResponse(code = 200, message = "OK", responseHeaders = {
//    		@ResponseHeader(name="Authorization", description="JWT Access Token", response=String.class), 
//	}),
    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
    @ApiResponse(code = 500, message = "Internal server error", response = ApiResponseError.class)
})
public @interface ApiDocResponseNewAuthorized {
	ApiResponse[] value() default {};
}
