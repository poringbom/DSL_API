package th.co.ktb.dsl.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadableDocument {

	@ApiModelProperty(position = 1, required=true)
	String documentID;

	@ApiModelProperty(position = 2, required=true)
    String name;

	@ApiModelProperty(position = 4, required=true)
    String format;

	@ApiModelProperty(position = 5, required=true)
    long size;
}