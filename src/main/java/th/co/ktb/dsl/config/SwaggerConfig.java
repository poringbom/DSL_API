package th.co.ktb.dsl.config;

//import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import th.co.ktb.dsl.model.common.ApiMetadataRequest;

@Configuration
@EnableSwagger2
public class SwaggerConfig {    

	private final String lastUpdateString = "2019-08-16";
	
	@Bean
	UiConfiguration uiConfig() {
	    return UiConfigurationBuilder
	            .builder()
	            .operationsSorter(OperationsSorter.METHOD)
	            .build();
	}
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
        		.ignoredParameterTypes(new Class[] {ApiMetadataRequest.class})
        		.useDefaultResponseMessages(false)
//            .operationsSorter(OperationsSorter.METHOD)
//        		.operationOrdering(operationOrdering)
        		.select()                                  
        		.apis(RequestHandlerSelectors.basePackage("th.co.ktb.dsl.controller"))  
        		.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        		.paths(PathSelectors.any())
        		.build()
        		.apiInfo(apiEndPointsInfo());
    }

    @Bean
	@SuppressWarnings("unused")
    public ApiInfo apiEndPointsInfo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date lastUpdate = null;
		try { lastUpdate = sdf.parse(lastUpdateString); } catch (ParseException e) {}
    		
//		String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
//				.path("/api")
//				.path("/changelog")
//				.toUriString();
		
        return new ApiInfoBuilder().title("Digital Student Loan (DSL) RESTful API")
            .description("Currently support only Debt Management System (DMS)")
            .contact(new Contact("A.Pongchet", "https://github.com/pongchetgithub/DSL_API", "pongchet@orcsoft.co.th"))
//            .license( (new MessageFormat("Last updated: {0,date} ")).format(new Object [] {lastUpdate}) )
            .license( "Change log ..." )
            .licenseUrl("/api/v1/changelog")
            .version("1.0.0 - Draft")
            .build();
    }
}

