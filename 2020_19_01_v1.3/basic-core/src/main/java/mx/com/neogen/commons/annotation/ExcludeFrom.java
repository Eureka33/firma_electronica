package mx.com.neogen.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 	Indica excluir la propiedad anotada de alguna operación automatizada 
 * (toString por default)
 */
@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD)
public @interface ExcludeFrom {
	String value() default "toString";		
}