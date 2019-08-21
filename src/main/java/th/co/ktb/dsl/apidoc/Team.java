package th.co.ktb.dsl.apidoc;

import java.text.MessageFormat;

public enum Team {
	AIM("???"),
	RMS("K' Tan"),
	DMS("K' Orn");
	
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
}
