package th.co.ktb.dsl.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity 
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	String[] swagger_url = {
			"/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
//			.cors().disable()
			.requiresChannel()
			.requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
			.requiresSecure()
			.and()
			.authorizeRequests()
			.antMatchers("/api/v1/**").permitAll()
			.antMatchers("/messages/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/resources/**"); 
    }
}
