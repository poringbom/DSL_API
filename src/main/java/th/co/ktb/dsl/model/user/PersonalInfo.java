package th.co.ktb.dsl.model.user;

import java.util.Date;

import lombok.Data;

@Data
public class PersonalInfo {
	String firstName;
	String lastName;
	Date dob;
	String citizenID;
}