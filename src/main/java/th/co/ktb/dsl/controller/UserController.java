package th.co.ktb.dsl.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.model.common.ApiResponseError;
import th.co.ktb.dsl.model.user.LoanInfo;
import th.co.ktb.dsl.model.user.UserInfo;

@Api(tags="DSL - User API", description="API หมวดเกี่ยวกับข้อมูลผู้ใช้งานระบบ")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@ApiOperation(value="API สำหรับการ Authentication โดยผลสำเร็จจะแนบ Token กลับมาด้วยใน header response ")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "OK", responseHeaders = {
		        @ResponseHeader(name = "Authorization", description = "JWT Token", response = String.class),
		    }),
		    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
		})
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public void login(
		@RequestParam("userID") String userID,
		@RequestParam("password") String password
	) {
		
	}
	
	@ApiOperation(value="API สำหรับการ logout ")
	@ApiResponses(value = {
		    @ApiResponse(code = 401, message = "Unauthorized", response = ApiResponseError.class),
		})
	@PostMapping("/logout")
	public void logout(
		@RequestHeader("Authorization") String token
	) {}
	
	@ApiOperation(value="API สำหรับดึงข้อมูล profile, contact ของผู้ใช้ และข้อมูลสิทธิ์ทีจำเป็นในการแสดงผลในบางเมนู " + 
			"/ ใช้เรียกหลังจากผู้ใช้ login สำเร็จเรียบร้อย")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/info" , produces=MediaType.APPLICATION_JSON_VALUE)
	public UserInfo getUser() {
		return new UserInfo();
	}
	
	@ApiOperation(value="API สำหรับดึงข้อมูลรายการบัญขีกู้ยืมทั้งหมดของผู้ใชู้ " + 
			"/ ใช้เรียกหลังจากผู้ใช้ login สำเร็จเรียบร้อย")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/account" , produces=MediaType.APPLICATION_JSON_VALUE)
	public List<LoanInfo> getAccount() {
		return LoanInfo.getExampleLoanInfo();
	}
	
}

