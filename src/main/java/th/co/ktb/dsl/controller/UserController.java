package th.co.ktb.dsl.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.user.LoanInfo;
import th.co.ktb.dsl.model.user.UserInfo;

@Api(tags="0.1. DSL : User API", description="API หมวดเกี่ยวกับข้อมูลผู้ใช้งานระบบ")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final String getUserInfo = "getUserInfo";
	@Testable
	@ApiOperation(value=getUserInfo,
			notes="API สำหรับดึงข้อมูล profile, contact ของผู้ใช้ และข้อมูลสิทธิ์ทีจำเป็นในการแสดงผลในบางเมนู " + 
			"/ ใช้เรียกหลังจากผู้ใช้ login สำเร็จเรียบร้อย")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/info" , produces=MediaType.APPLICATION_JSON_VALUE)
	public UserInfo getUserInfo() {
		return new UserInfo();
	}
	
	private final String getUserAccount = "getUserAccount";
	@ApiOperation(value=getUserAccount,
			notes="API สำหรับดึงข้อมูลรายการบัญขีกู้ยืมทั้งหมดของผู้ใชู้ " + 
			"/ ใช้เรียกหลังจากผู้ใช้ login สำเร็จเรียบร้อย")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/account" , produces=MediaType.APPLICATION_JSON_VALUE)
	public List<LoanInfo> getUserAccount() {
		return LoanInfo.getExampleLoanInfo();
	}
}












