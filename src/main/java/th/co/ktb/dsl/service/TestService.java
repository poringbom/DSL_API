package th.co.ktb.dsl.service;

import org.springframework.stereotype.Component;

@Component("TestService")
public class TestService {
	public String doHello(String name) {
		return "hello "+name;
	}
	
	public void doError() throws Exception{
		throw new Exception("Something went wrong!!!");
	}
}
