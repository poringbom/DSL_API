package th.co.ktb.dsl.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ApiMetadataArgumentResolver());
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    		log.info("MvcConfig.addCorsMappings()");
        registry.addMapping("/**")
        		.allowedMethods("*")
        		.allowedOrigins("*")
        		.allowedHeaders("*")
        		.allowCredentials(true);
    }
    
    @Bean
    public RestTemplate restTemplate() {
 	    return new RestTemplate();
    }
}
