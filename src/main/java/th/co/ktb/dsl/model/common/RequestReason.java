package th.co.ktb.dsl.model.common;

public enum RequestReason {
	NO_INCOME (1),
	LOW_INCOME (2),
	EXP_DISASTER (3),
	REGRESS_INCOME (4),
	LOOK_AFTER_FAMILY (5),
	OTHER (6),
	DISABILITY (7),
	DEADLY_INFECTION (8),
	CHRONIC_DISEASE (9),
	IMPRISONMENT_LIFE (10),
	BANKRUPTCY(11);
	
	private int value;  
	private RequestReason(int value){  
		this.value=value;  
	}  
	
	public Integer getRequestTopicID() {
		return value;
	}
	
}
