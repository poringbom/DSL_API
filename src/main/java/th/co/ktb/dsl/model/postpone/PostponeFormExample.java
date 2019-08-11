package th.co.ktb.dsl.model.postpone;

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
public class PostponeFormExample {
	@ApiModelProperty(position = 1, required=true)
	@NonNull
	PostponeDocumentType type;
	
	@ApiModelProperty(position = 2, required=false)
	String form;
	
	@ApiModelProperty(position = 3, required=false)
	String example;
	
	public PostponeFormExample(PostponeDocumentType type,String form) {
		this(type,form,null);
	}
	
	public static PostponeFormExample[] getExample(PostponeReason reason) {
		if (PostponeReason.NO_INCOME == reason) { // NO_INCOME = ไม่มีรายได้
			return new PostponeFormExample[] {
				new PostponeFormExample (PostponeDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"), 
				new PostponeFormExample (PostponeDocumentType.DSL203_CERTIFICATE_DEBT) };
		} else if (PostponeReason.LOW_INCOME == reason) { // LOW_INCOME = รายได้ไม่ถึงเกณฑ์
			return new PostponeFormExample[] {
				new PostponeFormExample (PostponeDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"),
				new PostponeFormExample (PostponeDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1") };
		} else if (PostponeReason.EXP_DISASTER == reason) { // EXP_DISASTER = ประสบภัย
			return new PostponeFormExample[] {
				new PostponeFormExample (PostponeDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"),
				new PostponeFormExample (PostponeDocumentType.CITIZEN_CARD,"1") };
		} else if (PostponeReason.REGRESS_INCOME == reason) { // REGRESS_INCOME = รายได้ถดถอย
			return new PostponeFormExample[] {
				new PostponeFormExample (PostponeDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"),
				new PostponeFormExample (PostponeDocumentType.ACCOUNT_STATEMENT,"1"),
				new PostponeFormExample (PostponeDocumentType.SALARY_SLIP_BEFORE,"1"),
				new PostponeFormExample (PostponeDocumentType.SALARAY_SLIP_AFTER,"1") };
		} else if (PostponeReason.LOOK_AFTER_FAMILY == reason) { // LOOK_AFTER_FAMILY = ดูแลบุคคลในครอบครัว
			return new PostponeFormExample[] {
				new PostponeFormExample (PostponeDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1"),
				new PostponeFormExample (PostponeDocumentType.RELATIONSHIP_EVIDENCE,"1") };
		} else if (PostponeReason.OTHER == reason) { // OTHER = อื่นๆ
			return new PostponeFormExample[] {
					new PostponeFormExample (PostponeDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1") };
		} else {
			return new PostponeFormExample[] {
				new PostponeFormExample (PostponeDocumentType.DSL202_POSTPONE_REQUEST_FORM,"1","1") };
		}
	}
}
