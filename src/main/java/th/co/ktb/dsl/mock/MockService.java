package th.co.ktb.dsl.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.model.authen.SignInRq;
import th.co.ktb.dsl.model.authen.SignInRs;

@Component
public class MockService {
	@Autowired ServiceSQL sql;
	public SignInRs signIn(SignInRq login) throws Exception{
		Object o = sql.login(login.getUsername(), login.getPassword());
		if (o == null) {
			throw new ClientException("401-001","Invalid user or password");
		}
		return new SignInRs();
	}
}
