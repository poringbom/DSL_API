package th.co.ktb.dsl.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized2Authen;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.user.LoanInfo;
import th.co.ktb.dsl.model.user.PersonalInfo;
import th.co.ktb.dsl.model.user.SpouseInfo;
import th.co.ktb.dsl.model.user.UserAddresses;
import th.co.ktb.dsl.model.user.UserInfo;
import th.co.ktb.dsl.model.user.WorkInfo;

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
	@ResponseStatus(HttpStatus.OK)
	public UserInfo getUserInfo(
		@ApiParam(required=false,type="query",value="Filter list information (all, address, spouse, work)") 
		@RequestParam(name="filter", required=false, defaultValue="ALL") 
		UserInfoFilter[] filter
	) {
		return new UserInfo();
	}
	
	private final String listUserAccount = "listUserAccount";
	@ApiOperation(value=listUserAccount,
			notes="API สำหรับดึงข้อมูลรายการบัญขีกู้ยืมทั้งหมดของผู้ใช้ " + 
			"/ ใช้เรียกหลังจากผู้ใช้ login สำเร็จเรียบร้อย (Dashboard)")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/accounts" , produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<LoanInfo> listUserAccount() {
		return LoanInfo.getExampleLoanInfo();
	}
	
	private final String updateUserInfo = "updateUserInfo";
	@ApiOperation(value=updateUserInfo,
			notes="API สำหรับปรับปรุงข้อมูลพื้นฐานของผู้ใช้ (รหัสบัตรประชาชน, ชื่อ, สกุล, วันเกิด, ข้อมูลติดต่อ) ")
	@ApiDocHeaderAuthorized2Authen
	@ApiDocResponseAuthorized
	@PutMapping(path="/info")
	@ResponseStatus(HttpStatus.OK)
	public void updateUserInfo(
		@ApiParam(required=true,type="body",value="Updated user information") 
		@RequestBody PersonalInfo userInfo
	) {
		return;
	}
	
	private final String updateUserAddress = "updateUserAddress";
	@ApiOperation(value=updateUserAddress,
			notes="API สำหรับปรับปรุงข้อมูลที่อยู่ (ที่อยู่ตามทะเบียนบ้าน, ที่อยู่ปัจจุบัน, ที่อยู่จัดส่งเอกสาร) ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PutMapping(path="/info/addresses")
	@ResponseStatus(HttpStatus.OK)
	public void updateUserAddress(
		@ApiParam(required=true,type="body",value="Updated user addresses") 
		@RequestBody UserAddresses userAddresses
	) {
		return;
	}
	
	private final String updateUserSpouse = "updateUserSpouse";
	@ApiOperation(value=updateUserSpouse,
			notes="API สำหรับปรับปรุงข้อมูลคู่สมรส ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PutMapping(path="/info/spouse")
	@ResponseStatus(HttpStatus.OK)
	public void updateUserSpouse(
		@ApiParam(required=true,type="body",value="Updated user's spouse") 
		@RequestBody SpouseInfo userSpouse
	) {
		return;
	}
	
	private final String updateUserWorkInfo = "updateUserWorkInfo";
	@ApiOperation(value=updateUserWorkInfo,
			notes="API สำหรับปรับปรุงข้อมูลการทำงาน ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PutMapping(path="/info/workInfo")
	@ResponseStatus(HttpStatus.OK)
	public void updateUserWorkInfo(
		@ApiParam(required=true,type="body",value="Updated user's work") 
		@RequestBody WorkInfo workInfo
	) {
		return;
	}
}














