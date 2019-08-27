package th.co.ktb.dsl.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity 
@Configuration
@ConditionalOnProperty(name="test.enable-security", havingValue = "false", matchIfMissing=true)
@Slf4j
public class WebNoSecurityConfig extends WebSecurityConfigurerAdapter {

	String[] swagger_url = {
			"/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"};

	public WebNoSecurityConfig() {
		log.info("************* DISABLE - Web Security *************");
		log.info(">> new " + WebSecurityConfig.class.getSimpleName());
	}
	
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
//				.loginProcessingUrl("/api/v1/signIn");
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/resources/**"); 
    }
	
}
