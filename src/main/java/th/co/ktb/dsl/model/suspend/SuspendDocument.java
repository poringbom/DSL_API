package th.co.ktb.dsl.model.suspend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import th.co.ktb.dsl.model.common.AttachDocument;

@Data
@EqualsAndHashCode(callSuper=false)
public class SuspendDocument extends AttachDocument {

	@ApiModelProperty(position = 11, required=true)
	SuspendDocumentType docType;

	@ApiModelProperty(position = 12, required=true)
	SuspendDocument.AcceptFormat docFormat;

	@ApiModelProperty(position = 13, required=true)
	String refID;
	
	static enum AcceptFormat {
		PDF, JPEG, GIF, TIF, PNG;
	}
}

