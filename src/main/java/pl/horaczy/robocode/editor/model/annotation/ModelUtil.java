package pl.horaczy.robocode.editor.model.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pl.horaczy.robocode.editor.model.Model;

/**
 * Klasa narzedziowa ulatwiajaca prace z modelem.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public final class ModelUtil {

	/**
	 * Ukryty konstruktor.
	 */
	private ModelUtil() {
		// nop
	}

	/**
	 * Skanule podany model pod katem wlasciwosci.
	 * 
	 * @param	pModel model do przeskanowania
	 * 
	 * @return	lista zawierajaca opisy wlasciwosci modelu
	 */
	public static List<PropertyDescription> scan(Model pModel) {
		List<PropertyDescription> descriptions = new ArrayList<>();

		Class<?> clazz = pModel.getClass();
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				PropertyDescription pd = processPropertyAnnotation(pModel, field);
				if (pd != null) {
					descriptions.add(pd);
				}
			}
			clazz = clazz.getSuperclass();
		}

		return descriptions;
	}

	/**
	 * Przetwarza adnotacje {@link Property} przy polu.
	 * 
	 * @param	pModel model
	 * @param	pField pole modelu
	 * 
	 * @return	opis wlasciwosci - moze byc <code>null</code> jesli pole nie zostalo zaadnotowane jako wlasciwosc modelu
	 */
	private static PropertyDescription processPropertyAnnotation(Model pModel, Field pField) {
		Property property = pField.getAnnotation(Property.class);
		return property != null ? new PropertyDescription(pModel, pField, property) : null;
	}

}
