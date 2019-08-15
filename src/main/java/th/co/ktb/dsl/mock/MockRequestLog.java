package th.co.ktb.dsl.mock;

import lombok.Data;

@Data
public class MockRequestLog {
	Long logID;
	String remoteAddr;
	String remoteHost;
	String requestUrl;
	String requestData;
	String responseID;
	Integer returnMockID;
	public MockRequestLog(
			String responseID, String remoteHost, String remoteAddr, 
			String requestUrl, String requestData, Integer returnMockID) {
		this.responseID = responseID;
		this.remoteHost = remoteHost;
		this.remoteAddr = remoteAddr;
		this.requestUrl = requestUrl;
		this.requestData = requestData;
		this.returnMockID = returnMockID;
	}
}
