package th.co.ktb.dsl.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.PARAMETER})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ApiMetadata {
	public static final String HEADER_NAME = "Api-Metadata";
	String value() default HEADER_NAME;
	String desName() default "";
	String serviceName() default "";
}
