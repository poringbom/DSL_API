package th.co.ktb.dsl.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class RequestDocFormExample {
	@ApiModelProperty(position = 1, required=true)
	@NonNull
	RequestDocumentType type;
	
	@ApiModelProperty(position = 2, required=false)
	String form;
	
	@ApiModelProperty(position = 3, required=false)
	String example;
	
	public RequestDocFormExample(RequestDocumentType type,String form) {
		this(type,form,null);
	}
	
	public static RequestDocFormExample[] getExample(RequestReason reason) {
		if (RequestReason.NO_INCOME == reason) { // NO_INCOME = ไม่มีรายได้
			return new RequestDocFormExample[] {
				new RequestDocFormExample (RequestDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"), 
				new RequestDocFormExample (RequestDocumentType.DSL203_CERTIFICATE_DEBT) };
		} else if (RequestReason.LOW_INCOME == reason) { // LOW_INCOME = รายได้ไม่ถึงเกณฑ์
			return new RequestDocFormExample[] {
				new RequestDocFormExample (RequestDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"),
				new RequestDocFormExample (RequestDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1") };
		} else if (RequestReason.EXP_DISASTER == reason) { // EXP_DISASTER = ประสบภัย
			return new RequestDocFormExample[] {
				new RequestDocFormExample (RequestDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"),
				new RequestDocFormExample (RequestDocumentType.CITIZEN_CARD,"1") };
		} else if (RequestReason.REGRESS_INCOME == reason) { // REGRESS_INCOME = รายได้ถดถอย
			return new RequestDocFormExample[] {
				new RequestDocFormExample (RequestDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"),
				new RequestDocFormExample (RequestDocumentType.ACCOUNT_STATEMENT,"1"),
				new RequestDocFormExample (RequestDocumentType.SALARY_SLIP_BEFORE,"1"),
				new RequestDocFormExample (RequestDocumentType.SALARAY_SLIP_AFTER,"1") };
		} else if (RequestReason.LOOK_AFTER_FAMILY == reason) { // LOOK_AFTER_FAMILY = ดูแลบุคคลในครอบครัว
			return new RequestDocFormExample[] {
				new RequestDocFormExample (RequestDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"),
				new RequestDocFormExample (RequestDocumentType.RELATIONSHIP_EVIDENCE,"1") };
		} else if (RequestReason.OTHER == reason) { // OTHER = อื่นๆ
			return new RequestDocFormExample[] {
					new RequestDocFormExample (RequestDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1") };
		} else {
			return new RequestDocFormExample[] {
				new RequestDocFormExample (RequestDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1") };
		}
	}
}
