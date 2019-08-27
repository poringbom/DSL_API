package th.co.ktb.dsl.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.JwtUtil;
import th.co.ktb.dsl.SkipPathRequestMatcher;
import th.co.ktb.dsl.config.security.ApiAuthenticationFailureHandler;
import th.co.ktb.dsl.config.security.ApiAuthenticationSuccessHandler;
import th.co.ktb.dsl.config.security.DSLAuthenticationProvider;
import th.co.ktb.dsl.config.security.ApiAuthenticationProvider;
import th.co.ktb.dsl.config.security.JwtAuthenticationFailureHandler;
import th.co.ktb.dsl.config.security.JwtAuthenticationProvider;
import th.co.ktb.dsl.config.security.JwtAuthenticationSuccessHandler;
import th.co.ktb.dsl.filter.ApiAuthenticationFilter;
import th.co.ktb.dsl.filter.JwtAuthenticationFilter;
 
@Configuration
@ConditionalOnProperty(name="test.enable-security", havingValue = "true")
@EnableWebSecurity
@Slf4j

@SuppressWarnings("unused")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String SWAGGER_UI_ENTRY_POINT = "/swagger-ui.html";
	private static final String API_LOGIN_ENTRY_POINT = "/api/v1/signIn";
	private static final String API_LOGOUT_ENTRY_POINT = "/api/v1/signOut";
	private static final String TOKEN_REFRESH_ENTRY_POINT = "/api/v1/token";
	private static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/v1/**";

	private static final String[] swagger_url = {
			"/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources",
            "/configuration/security",
            SWAGGER_UI_ENTRY_POINT,
            "/webjars/**"};
	
	private static final List<String> SWAGGER_ENDPOINT = Arrays.asList(swagger_url);
	
	private static final List<String> EXCLUDE_JWT_AUTH_ENDPOINT = Arrays.asList(
			TOKEN_REFRESH_ENTRY_POINT,
			API_LOGIN_ENTRY_POINT,
			"/test",
			"/api/v1/verif/otp/req",
			"/api/v1/verif/email",
			"/api/v1/verif/otp/req",
			"/api/v1/verif/otp"
		);
	
	@Autowired AuthenticationManager authenticationManager;
	@Autowired JwtUtil jwtUtil;


	public WebSecurityConfig() {
		log.info("************* ENABLE - Web Security (API, TOKEN ) *************");
		log.info(">> new " + WebSecurityConfig.class.getSimpleName());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	  	log.info(">> configure HttpSecurity");
		http
			.csrf().disable()
//			.cors().disable()
			.requiresChannel()
			.requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
			.requiresSecure()
			.and()
			.authorizeRequests()
			.antMatchers("/api/changelog").permitAll()
			.antMatchers("/messages/**").permitAll()
			.anyRequest().authenticated()
		.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	    	.and()
			.addFilterBefore(apiLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
    			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//		.and()
			.formLogin()
			.usernameParameter("j_username")
			.passwordParameter("j_password")
			.defaultSuccessUrl(SWAGGER_UI_ENTRY_POINT,true);
		
//			.loginProcessingUrl("/login-process");
			
//		.and()
//			.logout()
//            .logoutUrl(API_LOGOUT_ENTRY_POINT)
//            .permitAll();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/resources/**"); 
    }

	private ApiAuthenticationFilter apiLoginProcessingFilter(){
		ApiAuthenticationFilter filter = new ApiAuthenticationFilter(API_LOGIN_ENTRY_POINT);
		filter.setAuthenticationSuccessHandler(new ApiAuthenticationSuccessHandler(jwtUtil));
		filter.setAuthenticationFailureHandler(new ApiAuthenticationFailureHandler());
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}
	
	private JwtAuthenticationFilter jwtAuthenticationFilter(){
    		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(EXCLUDE_JWT_AUTH_ENDPOINT, TOKEN_BASED_AUTH_ENTRY_POINT);
		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(matcher);
		filter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
		filter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}

	@Bean
	public ApiAuthenticationProvider dslAuthenticationProvider() {
		log.info(">> new {}",ApiAuthenticationProvider.class.getSimpleName());
		return new ApiAuthenticationProvider();
	}
    
	@Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
	    	log.info(">> new {}",JwtAuthenticationProvider.class.getSimpleName());
	    	return new JwtAuthenticationProvider(jwtUtil);
    }
    
	@Bean
    public DSLAuthenticationProvider apiAuthenticationProvider() {
	    	log.info(">> new {}",DSLAuthenticationProvider.class.getSimpleName());
	    	return new DSLAuthenticationProvider();
    }
	
   @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
   @Override
   public AuthenticationManager authenticationManagerBean() throws Exception {
       return super.authenticationManagerBean();
   }
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
	  	auth.authenticationProvider(jwtAuthenticationProvider());
	  	log.info(">> configure {}",JwtAuthenticationProvider.class.getSimpleName());
	  	
	  	auth.authenticationProvider(dslAuthenticationProvider());
	  	log.info(">> configure {}",ApiAuthenticationProvider.class.getSimpleName());
	  	
	  	auth.authenticationProvider(apiAuthenticationProvider());
	  	log.info(">> configure {}",DSLAuthenticationProvider.class.getSimpleName());
	}
}
