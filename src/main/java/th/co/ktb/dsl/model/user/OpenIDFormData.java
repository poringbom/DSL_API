package th.co.ktb.dsl.model.user;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import th.co.ktb.dsl.DateUtil;

@Data
@JsonInclude(value=Include.NON_EMPTY)
public class OpenIDFormData {
	PersonTitle title = PersonTitle.BOY;
	String firstName = "พงศ์เชฐ";
	String lastName = "อมรจารุสนธิกุล";
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date dob = DateUtil.dateStrToDate("1990-04-18", "yyyy-MM-dd");
	
	String citizenID = "1234567890123";
	
	@Data
	public static class TempUser {
		public static final String TEMP_OPEN_ID = "OpenID";
		public static final String TEMP_VALIDATED = "Validated";
		Integer refID;
		String data;
		String type = TEMP_OPEN_ID;
	}
}