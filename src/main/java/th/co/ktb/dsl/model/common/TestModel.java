package th.co.ktb.dsl.model.common;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TestModel {
	@ApiModelProperty(position = 1)
	BigDecimal amount;
	
	@ApiModelProperty(position = 2, required=true)
	Date dtm;
}
