package pl.horaczy.robocode.editor.service;

import java.util.Collection;

import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;

import pl.horaczy.robocode.editor.model.Model;

/**
 * Interfejs serwisu odpowiedzialnego za zarzadzanie komponentami typu {@link MInputPart}.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public interface InputPartService {

	/**
	 * Tworzy komponent edytora na podstawie opisu o podanym identyfikatorze.
	 * 
	 * @param	pPartDescriptionId identyfikator opisu komponentu
	 * @param	pModel model dla ktorego zostanie utworzony edytor
	 * 
	 * @return	utworzony komponent edytora
	 */
	MInputPart createInputPart(String pPartDescriptionId, Model pModel);

	/**
	 * Ustawia aktywnosc na wskazanym komponencie.
	 * 
	 * @param	pPart komponent do aktywacji
	 */
	void activate(MInputPart pPart);

	/**
	 * Zwraca edytor dla podanego modelu o ile istnieje.
	 * 
	 * @param	pModel model dla ktorego zostanie zwrocony edytor
	 * 
	 * @return	otwarty edytor dla modelu - moze byc <code>null</code>
	 */
	MInputPart getInputPart(Model pModel);

	/**
	 * Zwraca kolekcje wszystkie edytorow zarzadzanych przez ten serwis.
	 * 
	 * @return	kolekcja edytorow (nigdy <code>null</code>)
	 */
	Collection<MInputPart> getInputParts();

}
