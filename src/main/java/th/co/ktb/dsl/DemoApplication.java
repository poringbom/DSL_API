package th.co.ktb.dsl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		String api_meta = "src=andriod(spec...); dest=dsl-dms; service=PaymentInfo";
		Pattern p = Pattern.compile("(src=(.*))(dest=(.*))(service=(.*))");
		Matcher m = p.matcher(api_meta);
		boolean b = m.matches();
		if (b) {
			log.info(m.group(2));
			log.info(m.group(4));
			log.info(m.group(6));
		}
	}

}
