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
			+ "src=<channel>/<version>, dest=<service-destination>, service=<api-sevice-name>", required = true, 
			allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, 
			example = "src=android/v1, dest=dsl-dms, service=payment"),

//	@ApiImplicitParam(name = "Service-Group", value = "Meta data information to process service request", required = true, 
//	allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, 
//	example = "Myapp/1 Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo 1610 Build/MMB29M)")	
	
//	@ApiImplicitParam(name = "User-Agent", value = "Meta data information to process service request", required = true, 
//			allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, 
//			example = "Myapp/1 Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo 1610 Build/MMB29M)")
	//https://www.scientiamobile.com/correctly-form-user-agents-for-mobile-apps/
/*
 * for Browser: Mozilla/<version> (<system-information>) <platform> (<platform-details>) <extensions>
 *          ex: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36
 *          ex: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Safari/605.1.15
 *          
 * for iOS: <AppName/<version> <iDevice platform><Apple model identifier>  iOS/<OS version> CFNetwork/<version> Darwin/<version>
 * 		ex: MyApp/1 iPhone5,2 iOS/10_1 CFNetwork/808.3 Darwin/16.3.0
 * 
 * for Android: <AppName>/<version> Dalvik/<version> (Linux; U; Android <android version>; <device ID> Build/<buildtag>
 *          ex: Myapp/1 Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo 1610 Build/MMB29M)
 */
	
})
public @interface ApiDocHeaderAuthorized {}
