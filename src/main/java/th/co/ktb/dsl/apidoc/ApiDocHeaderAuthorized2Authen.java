package th.co.ktb.dsl.apidoc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import th.co.ktb.dsl.model.annotation.ApiMetadata;

@Inherited
@Target(value={ElementType.METHOD,ElementType.ANNOTATION_TYPE,ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@ApiImplicitParams({
	@ApiImplicitParam(name = "Authorization", value = "JWT Access Token", required = true, 
			allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, 
			example = "Bearer <access_token>"),
	@ApiImplicitParam(name = ApiMetadata.HEADER_NAME, value = "Metadata information of service request. syntax: "
			+ "src=<channel>;dest=<service-destination>;service=<api-sevice-name>", required = true, 
			allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, 
			example = "src=android;dest=dsl-dms;service=payment"),
	@ApiImplicitParam(name = "Verify-Token-Action", value = "Token สำหรับตรวจสอบเพื่อดำเนินการบริการบางประเภท", required = true, 
			allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, 
			example = "Token <action_token>"),
})
public @interface ApiDocHeaderAuthorized2Authen {}
