package th.co.ktb.dsl.config;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {    
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
        		.useDefaultResponseMessages(false)
//            .operationsSorter(OperationsSorter.METHOD)
//        		.operationOrdering(operationOrdering)
        		.select()                                  
        		.apis(RequestHandlerSelectors.any())  
        		.paths(PathSelectors.any())
        		.build()
        		.apiInfo(apiEndPointsInfo());
    }
    

    private ApiInfo apiEndPointsInfo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		String d = "2019-08-09";
    		Date lastUpdate = null;
		try { lastUpdate = sdf.parse(d); } catch (ParseException e) {}
    		
        return new ApiInfoBuilder().title("Digital Student Loan (DSL) RESTful API")
            .description("Currently support only Debt Management System (DMS)")
//            .contact(new Contact("Ramesh Fadatare", "www.javaguides.net", "ramesh24fadatare@gmail.com"))
            .license( (new MessageFormat("Last updated: {0,date} ")).format(new Object [] {lastUpdate}) )
//            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .version("1.0.0 - Draft")
            .build();
    }
}

