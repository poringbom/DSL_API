package th.co.ktb.dsl.model.user;

import java.util.List;

import lombok.Data;

@Data
public class UserInfo {
	String userID;
	String role;
	PersonalInfo personalInfo;
	ContactInfo contactInfo;
	List<LoanInfo> loans;
	Boolean gracePeriod;
}



