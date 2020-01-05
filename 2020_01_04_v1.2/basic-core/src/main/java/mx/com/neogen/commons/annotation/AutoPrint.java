package mx.com.neogen.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 	Indica incluir la clase anotada en una operación automatizada de 
 * impresión de contenido.
 */
@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.TYPE)
public @interface AutoPrint {
	
}