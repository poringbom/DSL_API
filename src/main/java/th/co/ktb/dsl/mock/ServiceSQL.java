package th.co.ktb.dsl.mock;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import lombok.Data;

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

