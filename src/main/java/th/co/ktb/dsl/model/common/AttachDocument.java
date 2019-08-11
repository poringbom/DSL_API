package th.co.ktb.dsl.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttachDocument {
	@ApiModelProperty(position = 1, required=true)
	String docID;

	@ApiModelProperty(position = 2, required=false)
	int size = 0;
	
	@ApiModelProperty(position = 3, required=true)
	String docName;
}
