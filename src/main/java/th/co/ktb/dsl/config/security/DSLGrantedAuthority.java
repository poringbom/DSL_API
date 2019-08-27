package th.co.ktb.dsl.config.security;

import org.springframework.security.core.GrantedAuthority;

public class DSLGrantedAuthority implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	private static final Integer DEFAULT_GROUP_ID = 0;
	public static final String PREFIX = "G:";
	public static final String DUMMY_ICAS_GRANTED_AUTHORITY = "G:0";
	public static final DSLGrantedAuthority DUMMY_ICASGrantedAuthority = new DSLGrantedAuthority(DEFAULT_GROUP_ID);

	private Integer groupID;
	public DSLGrantedAuthority(Integer groupID) {
		this.groupID = groupID;
	}
	
	@Override
	public String getAuthority() {
		return PREFIX+groupID;
	}

}
