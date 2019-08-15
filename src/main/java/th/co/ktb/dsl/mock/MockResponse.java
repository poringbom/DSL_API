package th.co.ktb.dsl.mock;

import lombok.Data;

@Data
public class MockResponse {
	Integer mockID;
	String service;
	String channel;
	String scenario;
	Integer responseStatus;
	String responseHeader;
	Integer responseBodyID;
}
