package th.co.ktb.dsl;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.model.postpone.PostponeReason;

@SpringBootApplication
@Slf4j
public class DSLApiApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(DSLApiApplication.class, args);
		log.info("Datasource: {}",ctx.getBean(DataSource.class));
		log.info("{} -> {}",PostponeReason.NO_INCOME,PostponeReason.NO_INCOME.getRequestTopicID());
		
	}

}
