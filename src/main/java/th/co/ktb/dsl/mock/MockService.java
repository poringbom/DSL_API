package th.co.ktb.dsl.mock;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.DateUtil;
import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.model.authen.MockOTP;
import th.co.ktb.dsl.model.authen.RequestOTPVerifyRs;
import th.co.ktb.dsl.model.authen.SignInRq;
import th.co.ktb.dsl.model.authen.SignInRs;
import th.co.ktb.dsl.model.authen.VerifyOTPChannel;

@Component
@Slf4j
public class MockService {
	@Autowired ServiceSQL sql;
	public SignInRs signIn(SignInRq login) throws Exception{
		Object o = sql.login(login.getUsername(), login.getPassword());
		if (o == null) {
			throw new ClientException("401-001","Invalid user or password");
		}
		return new SignInRs();
	}
	
	

	public RequestOTPVerifyRs generateOTP(String objective, VerifyOTPChannel channel, String channelInfo){
		String refID = new String(generateRef(4));
		String otp = new String(generateOTP(6));
		
		Integer validSec = 180;
		Date expireTime = DateUtil.currDatePlusSec(validSec);

		MockOTP mockOTP = new MockOTP();
		mockOTP.setRefID(refID);
		mockOTP.setOtp(otp);
		mockOTP.setUserID(0);
		mockOTP.setAction(objective);
		mockOTP.setChannel(channel.toString());
		mockOTP.setChannelInfo(channelInfo);
		mockOTP.setExpireDTM(expireTime);
		log.info("Generated OTP: {} ",mockOTP);
		sql.addNewOTP(mockOTP);
		
		RequestOTPVerifyRs otpVerif = new RequestOTPVerifyRs();
		String validPeriod = DateUtil.dateToDateStr(
				DateUtil.datePlusSec(DateUtil.currDate(), validSec), "mm:ss");
		otpVerif.setChannel(channel);
		otpVerif.setChannelInfo(channelInfo);
		otpVerif.setRefID(refID);
		otpVerif.setObjective(objective);
		otpVerif.setValidPeriod(validPeriod);
		return otpVerif;
	}

	private char[] generateRef(int len) {
		Random rndm_method = new Random();
        char[] ref = new char[len]; 
		for (int i = 0; i < len; i++) {
			ref[i] = (char)(65 + rndm_method.nextInt(26));
		}
		return ref;
	}
	
	private char[] generateOTP(int len) 
    { 
        // Using numeric values 
        String numbers = "0123456789"; 
  
        // Using random method 
        Random rndm_method = new Random(); 
  
        char[] otp = new char[len]; 
  
        for (int i = 0; i < len; i++) 
        { 
            // Use of charAt() method : to get character value 
            // Use of nextInt() as it is scanning the value as int 
            otp[i] = 
             numbers.charAt(rndm_method.nextInt(numbers.length())); 
        } 
        return otp; 
    } 
}


