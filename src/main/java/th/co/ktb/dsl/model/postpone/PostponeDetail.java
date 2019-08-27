package th.co.ktb.dsl.model.postpone;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PostponeDetail extends PostponeInfo{

	@ApiModelProperty(position = 12, required=false)
	AdditionalRequestInfo additionInfo;
	
	@ApiModelProperty(position = 20, notes="List of uploaded postpone request document")
	List<PostponeDocument> postponseRequestDoc;
}

