package th.co.ktb.dsl.model.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiMetadataRequest {
	String src;
	String des;
	String service;
}
