/**
 * 
 */
package gov.nasa.generator.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Generate {
    String min();
    String max();
    String step() default "1";
}
