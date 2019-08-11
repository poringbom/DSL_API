package th.co.ktb.dsl.model.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class EmbedResource {
	@ApiModelProperty(position = 1, required=true)
	byte[] content;
	@ApiModelProperty(position = 2, required=true)
	String saveAsName;
	@ApiModelProperty(position = 3, required=true)
	String format;
	
}