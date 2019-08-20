package th.co.ktb.dsl.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.model.authen.LoginRequest;
import th.co.ktb.dsl.model.authen.LoginResponse;

@Component
public class MockService {
	@Autowired ServiceSQL sql;
	public LoginResponse signIn(LoginRequest login) throws Exception{
		Object o = sql.login(login.getUsername(), login.getPassword());
		if (o == null) {
			throw new ClientException("401-001","Invalid user or password");
		}
		return new LoginResponse();
	}
}
