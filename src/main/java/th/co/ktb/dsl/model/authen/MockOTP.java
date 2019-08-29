package th.co.ktb.dsl.model.authen;

import java.util.Date;

import lombok.Data;

@Data
public class MockOTP {
	String refID;
	Integer userID;
	String otp;
	String action;
	Date expireDTM;
	String channel;
	String channelInfo;
	String status;
}
