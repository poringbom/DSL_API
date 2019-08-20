package th.co.ktb.dsl.exception;

import java.text.MessageFormat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import th.co.ktb.dsl.model.common.ApiRequestInputType;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BadRequestException extends ClientException{
	String code = "400-000";
	ApiRequestInputType type;
	String param;
	
	@Override
	public String getCode() {
		return code;
	}
	
	public BadRequestException(ApiRequestInputType type, String param) {
		this.type = type;
		this.param = param;
	}
	
	public BadRequestException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		if (message == null) {
			MessageFormat mf = new MessageFormat("Bad request for ''{0}'', name ''{1}''");
			return mf.format(new Object[]{type, param});
		} else {
			return message;
		}
	}
}
