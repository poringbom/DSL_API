package th.co.ktb.dsl.model.suspend;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SuspendDetail extends SuspendInfo{
	@ApiModelProperty(position = 20, notes="List of uploaded suspend request document")
	List<SuspendDocument> suspendRequestDoc;
}

