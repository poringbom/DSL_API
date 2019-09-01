package th.co.ktb.dsl.mock;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.mapstruct.Mapper;

@Mapper
public interface MockResponseSQL {
	
	@Select("SELECT getDate()")
	public Date getDBTime();
	
	
	public final String GET_MOCK_RESPONSE = "" +
			"SELECT TOP 1 *" + 
			"FROM (" + 
			"    SELECT 1 + " + 
			"        CASE WHEN (CHARINDEX(#{channel}, Channel) > 0) THEN 10 WHEN (#{channel} is null) THEN 10 ELSE 0 END +" + 
			"        CASE WHEN (Scenario = #{scenario}) THEN 100 WHEN (#{scenario} is null) THEN 100 ELSE 0 END " + 
			"        AS SEQ, " + 
			"        MockID, Service, Channel, Scenario, ResponseStatus, ResponseHeader, ResponseBodyID " + 
			"    FROM ApiMockResponse " + 
			"    WHERE Service = #{service} AND " + 
			"    ( (CHARINDEX(#{channel}, Channel) > 0 AND Scenario = #{scenario}) OR " + 
			"      (CHARINDEX(#{channel}, Channel) > 0 AND Scenario IS NULL) OR " + 
			"      (Channel IS NULL AND Scenario = #{scenario}) OR " + 
			"      (Channel IS NULL AND Scenario IS NULL) " + 
			"    ) " + 
			") a " + 
			"ORDER BY SEQ DESC, ResponseStatus ASC, Channel, Scenario ";
	@Select(GET_MOCK_RESPONSE)
	public MockResponse getMockResponse(@Param("service") String svc, @Param("channel") String ch, @Param("scenario") String scn);

	public final String GET_MOCK_RESPONSE_BODY = "" +
			"SELECT ResponseBody FROM ApiMockResponseBody WHERE ResponseBodyID = #{responseBodyID} "; 
	@Select(GET_MOCK_RESPONSE_BODY)
	public String getMockResponseBody(@Param("responseBodyID") Integer id);
	
	public final String INSERT_MOCK_REQUEST = "" +
			"INSERT INTO LogApiTesting ( " + 
			"        ResponseID,RemoteAddr, RemoteHost, RequestData, RequestUrl, ReturnMockID " + 
			") VALUES ( " +
			"		#{responseID},#{remoteAddr}, #{remoteHost}, #{requestData}, #{requestUrl}, #{returnMockID} " +
			") ";
	@Insert(INSERT_MOCK_REQUEST)
	@SelectKey(statement="SELECT @@identity", keyProperty="logID", before=false, resultType=Long.class)
	public void addMockRequest(MockRequestLog log);
	
	public final String DELETE_LOG = "" +
			" DELETE TOP (50) l " + 
			" FROM LogApiTesting l " + 
			" WHERE DATEDIFF(HOUR,l.RequestDTM,#{current}) >= #{numHr} ";
	@Delete(DELETE_LOG)
	public Integer deleteLogResponse(@Param("numHr") Integer numHr, @Param("current") Date current);
	
	public final String GET_TEST_SCENARIO = "SELECT TestScenario FROM TestProxyConfig WHERE TestDev = #{dev}";
	@Select(GET_TEST_SCENARIO)
	public String getTestScenario(@Param("dev") String dev);
	
	public final String DELETE_UPLOAD = "" +
			" DELETE TOP (50) l " + 
			" FROM UploadFile l " + 
			" WHERE DATEDIFF(HOUR,l.UploadDTM,#{current}) >= #{numHr} ";
	@Delete(DELETE_UPLOAD)
	public Integer deleteUpload(@Param("numHr") Integer numHr, @Param("current") Date current);
	
	public final String DELETE_TOKEN = "" +
			" DELETE TOP (50) l " + 
			" FROM MockToken l " + 
			" WHERE DATEDIFF(HOUR,l.ExpireDTM,#{current}) >= #{numHr} ";
	@Delete(DELETE_TOKEN)
	public Integer deleteToken(@Param("numHr") Integer numHr, @Param("current") Date current);

	public final String DELETE_TEMP_USER = "" +
			" DELETE TOP (50) l " + 
			" FROM TempUser l " + 
			" WHERE DATEDIFF(HOUR,l.CreateDTM,#{current}) >= #{numHr} ";
	@Delete(DELETE_TEMP_USER)
	public Integer deleteTempUser(@Param("numHr") Integer numHr, @Param("current") Date current);
}
