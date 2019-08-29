package th.co.ktb.dsl.mock;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import lombok.Data;
import th.co.ktb.dsl.config.security.UserToken;
import th.co.ktb.dsl.model.authen.MockOTP;
import th.co.ktb.dsl.model.user.OpenIDFormData;
import th.co.ktb.dsl.model.user.OpenIDFormData.TempUser;

@Mapper
public interface ServiceSQL {
	@Select("SELECT UserID FROM MockUser WHERE LOGIN=#{login} AND PASSWORD = #{pwd}")
	public Integer login(@Param("login")String login, @Param("pwd")String pwd);
	
	@Insert("INSERT INTO UploadFile (Content,FileName,AliasName,DocType,RefID) "
			+ "VALUES ( #{content},#{fileName},#{alias},#{docType},#{refID} ) ")
	@SelectKey(statement="SELECT @@identity", keyProperty="fileID", before=false, resultType=Integer.class)
	public Integer addUplaodFile(UploadFile f);
	
	@Delete("DELETE UploadFile WHERE FileID = #{fileID}")
	public Integer removeUplaodFile(Integer fileID);

	@Select("SELECT FileID, Content, FileName, AliasName, DocType, RefID FROM UploadFile WHERE FileID = #{fileID}")
	public UploadFile getUplaodFile(Integer fileID);

	@Select("SELECT UserID, Login, Pin FROM MockUser "
			+ "WHERE Login = #{login} AND Password = #{password}")
	public LoginUser getUserByLogin(LoginUser user);

	@Insert("INSERT INTO MockToken (TokenValue, UserID, Action, ExpireDTM) "
			+ "VALUES (#{tokenValue}, #{userID}, #{action}, #{expiredTime}) ")
	@SelectKey(statement="SELECT @@identity", keyProperty="tokenID", before=false, resultType=Integer.class)
	public Integer addNewToken(UserToken token);
	
	@Select("SELECT TokenID, TokenValue, UserID, Action, ExpireDTM AS expiredTime FROM MockToken WHERE TokenID = #{tokenID}")
	public UserToken checkToken(Integer tokenID);
	
	@Delete("DELETE FROM MockToken WHERE TokenID = #{tokenID}")
	public Integer removeToken(Integer tokenID);

	@Insert("INSERT INTO MockOTP (RefID, UserID, OTP, Action, ExpireDTM, Channel, ChannelInfo) "
			+ "VALUES (#{refID}, #{userID}, #{otp}, #{action}, #{expireDTM}, #{channel}, #{channelInfo}) ")
	public Integer addNewOTP(MockOTP otp);
	
	@Select("SELECT RefID, OTP, ExpireDTM FROM MockOTP WHERE RefID = #{refID}")
	public MockOTP checkOTP(@Param("refID") String refID);
	
	@Delete("DELETE FROM MockOTP WHERE RefID = #{refID}")
	public Integer removeOTP(@Param("refID") String refID);

	@Select("SELECT ConfigValue FROM Config WHERE ConfigName = #{config}")
	public String getConfig(String refID);
	
	@Insert("INSERT INTO TempUser (TempUserInfo) VALUES (#{data}) ")
	@SelectKey(statement="SELECT @@identity", keyProperty="refID", before=false, resultType=Integer.class)
	public Integer addTempUser(TempUser data);
	
	@Select("SELECT TempUserInfo FROM TempUser WHERE RefID = #{refID}")
	public String getTempUser(String refID);
	
	@Data
	public static class LoginUser {
		Integer userID;
		String pin;
		String login;
		String password;
		public LoginUser(String u, String p) {
			login=u; password=p;
		}
	}
	
	@Data
	public static class UploadFile {
		Integer fileID;
		byte[] content;
		String fileName;
		String alias;
		String docType;
		String refID;
	}
}

