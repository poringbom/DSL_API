package th.co.ktb.dsl.model.suspend;

public enum SuspendReason {
	DISABILITY (1),
	DEADLY_INFECTION (2),
	CHRONIC_DISEASE (3),
	IMPRISONMENT_LIFE (4),
	BANKRUPTCY (5);
	
	@SuppressWarnings("unused")
	private int value;  
	private SuspendReason(int value){  
		this.value=value;  
	}  
}
