package pl.horaczy.robocode.editor.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Metadane wlasciwosci modelu.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {

	/**
	 * @return	nazwa wlasciwosci
	 */
	String name();

	/**
	 * @return	priorytet uzywany do posortowania przed prezentacja na ekranie
	 */
	int priority() default 0;

}
