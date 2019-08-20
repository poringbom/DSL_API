package th.co.ktb.dsl.mock;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ServiceSQL {
	@Select("SELECT UserID FROM MockUser WHERE LOGIN=#{login} AND PASSWORD = #{pwd}")
	public Integer login(@Param("login")String login, @Param("pwd")String pwd);
}
