package core.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify a custom implementation class for UI elements
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ElementImpl {
    /**
     * The implementation class to use for this element
     */
    Class<?> value();

    /**
     * Optional: Constructor parameter types if needed for instantiation
     */
    Class<?>[] constructorParams() default {java.lang.Object.class};
}

