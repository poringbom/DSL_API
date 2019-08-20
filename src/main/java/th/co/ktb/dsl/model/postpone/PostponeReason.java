package th.co.ktb.dsl.model.postpone;

public enum PostponeReason {
	NO_INCOME (1),
	LOW_INCOME (2),
	EXP_DISASTER (3),
	REGRESS_INCOME (4),
	LOOK_AFTER_FAMILY (5),
	OTHER (6);
	
	private int value;  
	private PostponeReason(int value){  
		this.value=value;  
	}  
	
	public Integer getRequestTopicID() {
		return value;
	}
	
}
