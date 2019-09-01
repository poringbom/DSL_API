package th.co.ktb.dsl.mock;


import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.ktb.dsl.config.security.UserToken;
import th.co.ktb.dsl.model.authen.MockOTP;
import th.co.ktb.dsl.model.user.OpenIDFormData.TempUser;

@Mapper
public interface ServiceSQL {

//UploadFile-------------------------
	@Insert("INSERT INTO UploadFile (Content,FileName,AliasName,DocType,RefID) "
			+ "VALUES ( #{content},#{fileName},#{alias},#{docType},#{refID} ) ")
	@SelectKey(statement="SELECT @@identity", keyProperty="fileID", before=false, resultType=Integer.class)
	public Integer addUplaodFile(UploadFile f);
	
	@Delete("DELETE UploadFile WHERE FileID = #{fileID}")
	public Integer removeUplaodFile(Integer fileID);

	@Select("SELECT FileID, Content, FileName, AliasName, DocType, RefID FROM UploadFile WHERE FileID = #{fileID}")
	public UploadFile getUplaodFile(Integer fileID);
//----------------------------------
	
	

//MockUser-------------------------
	@Select("SELECT UserID FROM MockUser WHERE LOGIN=#{login} AND PASSWORD = #{pwd}")
	public Integer login(@Param("login")String login, @Param("pwd")String pwd);
	
	@Select("SELECT UserID, Login, Pin FROM MockUser "
			+ "WHERE Login = #{login} AND Password = #{password}")
	public LoginUser getUserByLogin(LoginUser user);
	
	@Insert("INSERT INTO MockUser (Login, Password, PIN, CitizenID, Title, FirstName, LastName, MobileNo, DOB) "
			+ "VALUES (#{login}, #{password}, #{pin}, #{citizenID}, #{title}, #{firstName}, #{lastName}, #{mobileNo}, #{dob}) ")
	@SelectKey(statement="SELECT @@identity", keyProperty="userID", before=false, resultType=Integer.class)
	public Integer addNewUser(LoginUser user);
	
	@Insert("INSERT INTO MockUserInfo (UserID) VALUES (#{userID})")
	public Integer addNewUserInfo(Integer userID);
	
	@Update("UPDATE MockUser SET Password = #{password} WHERE UserID = #{userID} AND (Password = #{oldPassword} OR #{oldPassword} IS NULL) ")
	public int resetPassword(@Param("userID") Integer userID,
			@Param("password") String password, @Param("oldPassword") String oldPassword);
	
	@Update("UPDATE MockUser SET PIN = #{pin} WHERE UserID = #{userID} AND (PIN = #{oldPin} OR #{oldPin} IS NULL) ")
	public int resetPIN(@Param("userID") Integer userID,@Param("pin") String pin, @Param("oldPin") String oldPin);
	
	@Select("SELECT u.UserID, u.Login, u.CitizenID, u.Title, u.FirstName, u.LastName, u.MobileNo, u.DOB, "
			+ "	i.WorkInfo, i.Addresses, i.Spouse "
			+ "FROM MockUser u LEFT JOIN MockUserInfo i ON u.UserID = i.UserID "
			+ "WHERE u.UserID = #{userID} ")
	public LoginUser getUserByID(Integer userID);
	
	@SuppressWarnings("el-syntax")
	@Update("UPDATE MockUserInfo SET"
			+ " WorkInfo = ISNULL(#{workInfo,jdbcType=VARCHAR}, WorkInfo), "
			+ " Addresses = ISNULL(#{addresses,jdbcType=VARCHAR}, Addresses), "
			+ " Spouse = ISNULL(#{spouse,jdbcType=VARCHAR}, Spouse) "
			+ "WHERE UserID = #{userID}")
	public int updateUserInfoByID(LoginUser user);
	
	@Update("UPDATE MockUser SET"
			+ " Login = #{login}, "
			+ " CitizenID = #{citizenID}, "
			+ " Title = #{title}, "
			+ " FirstName = #{firstName}, "
			+ " LastName = #{lastName}, "
			+ " MobileNo = #{mobileNo}, "
			+ " DOB = #{dob} "
			+ "WHERE UserID = #{userID}")
	public int updateUser(LoginUser user);
//----------------------------------
	
	

//MockToken-------------------------
	@Insert("INSERT INTO MockToken (TokenValue, UserID, Action, ExpireDTM) "
			+ "VALUES (#{tokenValue}, #{userID}, #{action}, #{expiredTime}) ")
	@SelectKey(statement="SELECT @@identity", keyProperty="tokenID", before=false, resultType=Integer.class)
	public Integer addNewToken(UserToken token);
	
	@Select("SELECT TokenID, TokenValue, UserID, Action, ExpireDTM AS expiredTime FROM MockToken WHERE TokenID = #{tokenID}")
	public UserToken checkToken(Integer tokenID);
	
	@Delete("DELETE FROM MockToken WHERE TokenID = #{tokenID}")
	public Integer removeToken(Integer tokenID);
//----------------------------------
	
	

//MockOTP-------------------------
	@Insert("INSERT INTO MockOTP (RefID, UserID, OTP, Action, ExpireDTM, Channel, ChannelInfo, Status) "
			+ "VALUES (#{refID}, #{userID}, #{otp}, #{action}, #{expireDTM}, #{channel}, #{channelInfo}, #{status}) ")
	public Integer addNewOTP(MockOTP otp);
	
	@Select("SELECT RefID, OTP, ExpireDTM FROM MockOTP WHERE RefID = #{refID}")
	public MockOTP checkOTP(@Param("refID") String refID);
	
	@Delete("DELETE FROM MockOTP WHERE RefID = #{refID}")
	public Integer removeOTP(@Param("refID") String refID);
//----------------------------------
	
	

//TempUser-------------------------
	@Insert("INSERT INTO TempUser (TempUserInfo,Type) VALUES (#{data}, #{type}) ")
	@SelectKey(statement="SELECT @@identity", keyProperty="refID", before=false, resultType=Integer.class)
	public Integer addTempUser(TempUser data);
	
	@Delete("DELETE FROM TempUser WHERE RefID = #{refID}")
	public Integer removeTempUser(@Param("refID") String refID);
	
	@Select("SELECT TempUserInfo FROM TempUser WHERE RefID = #{refID} AND [Type] = #{type}")
	public String getTempUser(@Param("refID") String refID, @Param("type") String type);

//----------------------------------

	
	
	@Select("SELECT ConfigValue FROM Config WHERE ConfigName = #{config}")
	public String getConfig(String refID);
	
	@Data
	@NoArgsConstructor
	public static class LoginUser {
		Integer userID;
		String login;
		String password;
		String pin;
		String citizenID;
		String title;
		String firstName;
		String lastName;
		String mobileNo;
		String spouse;
		String addresses;
		String workInfo;
		Date dob;
		public LoginUser(Integer u) {
			userID=u;
		}
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

