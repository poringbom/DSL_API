package th.co.ktb.dsl.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.net.MediaType;

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
	@Autowired JavaMailSender javaMailSender;
	@Autowired RestTemplate restTemplate;
	@Value("${sms.username}") String smsUser;
	@Value("${sms.password}") String smsPwd;
	
	public SignInRs signIn(SignInRq login) throws Exception{
		Object o = sql.login(login.getUsername(), login.getPassword());
		if (o == null) {
			throw new ClientException("401-001","Invalid user or password");
		}
		return new SignInRs();
	}
	
	private static String SMS_URL="https://secure.thaibulksms.com/sms_api.php?%s";
//	private static String SMS_URL="username=ktbdslapi&password=abc1234-&msisdn=0819248388&message=testมีภาษาไทยด้วย"

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
		
		RequestOTPVerifyRs otpVerif = new RequestOTPVerifyRs();
		String validPeriod = DateUtil.dateToDateStr(
				DateUtil.datePlusSec(DateUtil.currDate(), validSec), "mm:ss");
		otpVerif.setChannel(channel);
		otpVerif.setChannelInfo(channelInfo);
		otpVerif.setRefID(refID);
		otpVerif.setObjective(objective);
		otpVerif.setValidPeriod(validPeriod);
		

		String text = "OTP: "+otp+" เลขอ้างอิง: "+refID+" หมดอายุภายใน: "+validPeriod+" นาที";
		String status = null;
		try {
			if (VerifyOTPChannel.MOBILE == channel) {
//--- SMS -------
//				String encodeText = URLEncoder.encode(text, "UTF-8");
				String encodeText = text;
				status = sendOTP(channelInfo,encodeText);
			} else if (VerifyOTPChannel.EMAIL == channel) {
//--- EMAIL -------
				sendEmail(channelInfo,"OTP Verification",text);
				status = "Send email success";
			}
		} catch(Exception ex) {
			status = ex.getMessage();
		}
		mockOTP.setStatus(status);
		sql.addNewOTP(mockOTP);
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
	
	private String sendOTP(String mobileNo, String text) {
		Map<String, Object> params = new HashMap<>();
		params.put("username", smsUser);
		params.put("password", smsPwd);
		params.put("msisdn", mobileNo);
		params.put("message", text);
		String parametrizedArgs = params.keySet().stream().map(k ->
		    String.format("%s={%s}", k, k)
		).collect(Collectors.joining("&"));
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_XML_UTF_8.toString());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		log.info("SMS URL >> {}",String.format(SMS_URL, parametrizedArgs));
		ResponseEntity<String> ret = restTemplate.exchange(
				String.format(SMS_URL, parametrizedArgs), HttpMethod.GET, entity, String.class, params);
		return ret.getBody();
//		return "Send sms success";
	}
	
	public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject(title); //"Testing OTP"
        msg.setText(text); //"Your OTP Code: , Ref"
        javaMailSender.send(msg);
    }
}


