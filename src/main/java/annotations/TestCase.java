package annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestCase {
    String id();
    String module();
    String description() default "";
    String[] tags() default {};
}
