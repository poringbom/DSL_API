package th.co.ktb.dsl.model.suspend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SuspendFormExample {
	@ApiModelProperty(position = 1, required=true)
	SuspendDocumentType type;
	
	@ApiModelProperty(position = 2, required=true)
	String form;
	
	@ApiModelProperty(position = 3, required=true)
	String example;
}
