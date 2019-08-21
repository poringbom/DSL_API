package th.co.ktb.dsl.apidoc;

import java.text.MessageFormat;

public enum Team {
	RMS("K' Tan"),
	GATEWAY(""),
	DMS("K' Orn"),
	DSL_SECURITY("K' Pond + Pibule");
	;
	
	private String leader;
	private Team(String leader) {
		this.leader = leader;
	}
	private static String TEAM_DEVELOP = "ทีมพัฒนาโดย {0} ติดต่อ {1}";
	public String leader() {
		return leader;
	}
	public static String implementationBy (Team team) {
		MessageFormat mf = new MessageFormat(TEAM_DEVELOP);
		String implementor = mf.format(new Object[]{team,team.leader});
		return implementor;
	}
	
	public static final String RMS_TEAM = " ***     ทีมพัฒนา RMS ผู้ประสานงาน 'คุณแทน'     ***";
	public static final String GATEWAY_TEAM = "          ***     ทีมพัฒนา Gateway ผู้ประสานงาน 'คุณหนึ่ง'     ***";
	public static final String DMS_TEAM = "          ***     ทีมพัฒนา DMS ผู้ประสานงาน 'คุณอร (คุณมะนอย)'     ***";
	public static final String DMS_PAYMENT_TEAM = "          ***     ทีมพัฒนา DMS ผู้ประสานงาน 'คุณอร (คุณนะ)'     ***";
	public static final String DSL_SECURITY_TEAM = "          ***     ทีมพัฒนา DSL-Security ผู้ประสานงาน 'คุณปอนด์/บูน'     ***";
}
