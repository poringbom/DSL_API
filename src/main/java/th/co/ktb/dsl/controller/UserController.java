package th.co.ktb.dsl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized;
import th.co.ktb.dsl.apidoc.ApiDocHeaderAuthorized2Authen;
import th.co.ktb.dsl.apidoc.ApiDocResponseAuthorized;
import th.co.ktb.dsl.apidoc.Team;
import th.co.ktb.dsl.config.security.ApiAuthenticationToken;
import th.co.ktb.dsl.config.security.UserToken;
import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.mock.ServiceSQL;
import th.co.ktb.dsl.mock.ServiceSQL.LoginUser;
import th.co.ktb.dsl.mock.Testable;
import th.co.ktb.dsl.model.user.ContactInfo;
import th.co.ktb.dsl.model.user.LoanInfo;
import th.co.ktb.dsl.model.user.PersonTitle;
import th.co.ktb.dsl.model.user.PersonalInfo;
import th.co.ktb.dsl.model.user.SpouseInfo;
import th.co.ktb.dsl.model.user.UserAddresses;
import th.co.ktb.dsl.model.user.UserInfo;
import th.co.ktb.dsl.model.user.WorkInfo;

@Api(tags="0.1. DSL : User API", description="API หมวดเกี่ยวกับข้อมูลผู้ใช้งานระบบ")
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
	
	@Autowired ServiceSQL sql;
	@Autowired JwtUtil jwtUtil;

	private final String getUserInfo = "getUserInfo";
	@Testable(alwaysMock=false)
	@ApiOperation(value=getUserInfo+Team.GATEWAY_TEAM,
			notes="API สำหรับดึงข้อมูล profile, contact ของผู้ใช้ และข้อมูลสิทธิ์ทีจำเป็นในการแสดงผลในบางเมนู " + 
			"/ ใช้เรียกหลังจากผู้ใช้ login สำเร็จเรียบร้อย")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@GetMapping(path="/info" , produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public UserInfo getUserInfo(
		@ApiParam(required=false,type="query",value="Filter list information (all, address, spouse, work)") 
		@RequestParam(name="filter", required=false, defaultValue="ALL") 
		List<UserInfoFilter> filter,
		@ApiIgnore Authentication auth
	) throws Exception {
		log.info("do {}()",getUserInfo);
		UserToken userToken = ((ApiAuthenticationToken) auth).getUserInfo();
		LoginUser user = sql.getUserByID(userToken.getUserID());
		if (user == null) {
			throw new ClientException("401-000", "Not found user information");
		}
		UserInfo ret = new UserInfo();
		ret.setTitle(PersonTitle.valueOf(user.getTitle()));
		ret.setFirstName(user.getFirstName());
		ret.setLastName(user.getLastName());
		ret.setCitizenID(user.getCitizenID());
		ret.setDob(user.getDob());
		
		ContactInfo contact = new ContactInfo();
		contact.setEmail(user.getLogin());
		contact.setMobileNo(user.getMobileNo());
		contact.setTelNo("1234567890");
		ret.setContact(contact);
		if ((filter.contains(UserInfoFilter.ALL) || filter.contains(UserInfoFilter.ADDRESS_INFO)) &&
			user.getAddresses() != null
		) {
			UserAddresses addr = Utilities.getObjectMapper().readValue(user.getAddresses(), UserAddresses.class);
			ret.setAddresses(addr);
		}
		if ((filter.contains(UserInfoFilter.ALL) || filter.contains(UserInfoFilter.WORKING_INFO)) &&
			user.getWorkInfo() != null
		) {
			WorkInfo work = Utilities.getObjectMapper().readValue(user.getWorkInfo(), WorkInfo.class);
			ret.setWorkInfo(work);
		}
		if ((filter.contains(UserInfoFilter.ALL) || filter.contains(UserInfoFilter.SPOUSE_INFO)) &&
				user.getSpouse() != null
		) {
			SpouseInfo spouse = Utilities.getObjectMapper().readValue(user.getSpouse(), SpouseInfo.class);
			ret.setSpouse(spouse);
		}
		return ret;
	}
	
	private final String listUserAccount = "listUserAccount";
	@Testable
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
	@Testable(alwaysMock=false)
	@ApiOperation(value=updateUserInfo+Team.GATEWAY_TEAM,
			notes="API สำหรับปรับปรุงข้อมูลพื้นฐานของผู้ใช้ (รหัสบัตรประชาชน, ชื่อ, สกุล, วันเกิด, ข้อมูลติดต่อ) ")
	@ApiDocHeaderAuthorized2Authen
	@ApiDocResponseAuthorized
	@PutMapping(path="/info")
	@ResponseStatus(HttpStatus.OK)
	public void updateUserInfo(
		@ApiParam(required=true,type="body",value="Updated user information") 
		@RequestBody PersonalInfo userInfo,
		@ApiIgnore @RequestHeader("Verify-Token-Action") String verifyTokenAction
	) throws Exception {
		log.info("do {}()",updateUserInfo);
		UserToken verifyToken = null;
		if (verifyTokenAction != null && verifyTokenAction.startsWith(JwtUtil.JWT_HEADER_OTT_PREFIX)) {
	    		log.info("token come along with request via header (VerifyAction).");
	        String authToken = verifyTokenAction.substring(6);
	        verifyToken = jwtUtil.parseToken(authToken);
	    } else {
	    		throw new ClientException("400-001","Invalid verify-token");
	    } 
		
		LoginUser updateUser = new LoginUser();
		updateUser.setUserID(verifyToken.getUserID());
		updateUser.setTitle(userInfo.getTitle().toString());
		updateUser.setFirstName(userInfo.getFirstName());
		updateUser.setLastName(userInfo.getLastName());
		updateUser.setDob(userInfo.getDob());
		updateUser.setCitizenID(userInfo.getCitizenID());
		updateUser.setMobileNo(userInfo.getMobileNo());
		updateUser.setLogin(userInfo.getEmail());
		log.info("Update MockUser: {}",updateUser);
		sql.updateUser(updateUser);
		return;
	}
	
	private final String updateUserAddress = "updateUserAddress";
	@Testable(alwaysMock=false)
	@ApiOperation(value=updateUserAddress+Team.GATEWAY_TEAM,
			notes="API สำหรับปรับปรุงข้อมูลที่อยู่ (ที่อยู่ตามทะเบียนบ้าน, ที่อยู่ปัจจุบัน, ที่อยู่จัดส่งเอกสาร) ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PutMapping(path="/info/addresses")
	@ResponseStatus(HttpStatus.OK)
	public void updateUserAddress(
		@ApiParam(required=true,type="body",value="Updated user addresses") 
		@RequestBody UserAddresses userAddresses,
		@ApiIgnore Authentication auth
	) throws Exception {
		log.info("do {}()",updateUserAddress);
		UserToken userToken = ((ApiAuthenticationToken) auth).getUserInfo();
		LoginUser user = new LoginUser(userToken.getUserID()); 
		user.setAddresses(Utilities.getObjectMapper().writeValueAsString(userAddresses));
		sql.updateUserInfoByID(user);
		return;
	}
	
	private final String updateUserSpouse = "updateUserSpouse";
	@Testable(alwaysMock=false)
	@ApiOperation(value=updateUserSpouse+Team.GATEWAY_TEAM,
			notes="API สำหรับปรับปรุงข้อมูลคู่สมรส ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PutMapping(path="/info/spouse")
	@ResponseStatus(HttpStatus.OK)
	public void updateUserSpouse(
		@ApiParam(required=true,type="body",value="Updated user's spouse") 
		@RequestBody SpouseInfo userSpouse,
		@ApiIgnore Authentication auth
	) throws Exception {
		log.info("do {}()",updateUserSpouse);
		UserToken userToken = ((ApiAuthenticationToken) auth).getUserInfo();
		LoginUser user = new LoginUser(userToken.getUserID()); 
		user.setSpouse(Utilities.getObjectMapper().writeValueAsString(userSpouse));
		sql.updateUserInfoByID(user);
		return;
	}
	
	private final String updateUserWorkInfo = "updateUserWorkInfo";
	@Testable(alwaysMock=false)
	@ApiOperation(value=updateUserWorkInfo+Team.GATEWAY_TEAM,
			notes="API สำหรับปรับปรุงข้อมูลการทำงาน ")
	@ApiDocHeaderAuthorized
	@ApiDocResponseAuthorized
	@PutMapping(path="/info/workInfo")
	@ResponseStatus(HttpStatus.OK)
	public void updateUserWorkInfo(
		@ApiParam(required=true,type="body",value="Updated user's work") 
		@RequestBody WorkInfo workInfo,
		@ApiIgnore Authentication auth
	) throws Exception {
		log.info("do {}()",updateUserWorkInfo);
		UserToken userToken = ((ApiAuthenticationToken) auth).getUserInfo();
		LoginUser user = new LoginUser(userToken.getUserID()); 
		user.setWorkInfo(Utilities.getObjectMapper().writeValueAsString(workInfo));
		sql.updateUserInfoByID(user);
		return;
	}
}














