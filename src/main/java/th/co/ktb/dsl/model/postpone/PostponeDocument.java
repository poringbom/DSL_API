package th.co.ktb.dsl.model.postpone;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.ktb.dsl.model.common.AttachDocument;

@Data
@EqualsAndHashCode(callSuper=false)
public class PostponeDocument extends AttachDocument {

	@ApiModelProperty(position = 11, required=true)
	PostponeDocumentType docType;

	@ApiModelProperty(position = 12, required=true)
	PostponeDocument.AcceptFormat docFormat;

	@ApiModelProperty(position = 13, required=true)
	String refID;

//	@ApiModelProperty(position = 14, required=false)
//	String example;
//
//	@ApiModelProperty(position = 15, required=false)
//	String formTemplate;
	
	static enum AcceptFormat {
		PDF, JPEG, GIF, TIF, PNG;
	}
}

