package th.co.ktb.dsl.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.PARAMETER})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ApiMetadata {
	public static final String HEADER_NAME = "Api-MetaData";
	String value() default HEADER_NAME;
}
