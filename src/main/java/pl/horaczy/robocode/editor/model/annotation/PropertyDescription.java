package pl.horaczy.robocode.editor.model.annotation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import pl.horaczy.robocode.editor.model.Model;

/**
 * Opis wlasciwosci.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class PropertyDescription implements PropertyChangeListener, Comparable<PropertyDescription> {

	/** Model ktorego dotyczy ten opis wlasciwosci. */
	private Model model;
	/** Pole dla ktorego utworzone ten opis wlasciwosci. */
	private Field field;

	/** Priorytet wlasciwosci - uzywany do sortowania wlasciwosci na ekranie. */
	private int priority;
	/** Nazwa wlasciwosci. */
	private String name;
	/** Opis wlasciwosci prezentowany uzytkownikowi. */
	private String description;
	/** Wartosc wlascowosci. */
	private Object value;

	/**
	 * Tworzy opis wlasciwosci dla konkretnego pola modelu.
	 * 
	 * @param	pModel model
	 * @param	pField pole modelu
	 * @param	pProperty opis wlasciwosci
	 */
	public PropertyDescription(Model pModel, Field pField, Property pProperty) {
		this.model = pModel;
		this.field = pField;
		this.priority = pProperty.priority();
		this.name = pField.getName();
		this.description = pProperty.name();
		updateValue();
		this.model.addPropertyChangeListener(this);
	}

	/**
	 * @return	priorytet wlasciwosci - uzywany do sortowania wlasciwosci na ekranie
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * @return	nazwa wlasciwosci
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return	opis wlasciwosci
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return	wartosc wlasciwosci
	 */
	public Object getValue() {
		return this.value;
	}

	public void setValue(Object pValue) {
		setFieldValue(this.model, this.field, pValue);
	}

	/**
	 * @return	type wartosci wlasciwosci
	 */
	public Class<?> getValueType() {
		return this.field.getType();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pEvent) {
		updateValue();
	}

	/**
	 * Zwalnia wszystkie zasoby.
	 */
	public void dispose() {
		if (this.model != null) {
			this.model.removePropertyChangeListener(this);
			this.model = null;
		}
		this.field = null;
		this.name = null;
		this.description = null;
		this.value = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PropertyDescription pDesc) {
		int cmp = Integer.compare(this.priority, pDesc != null ? pDesc.getPriority() : 0);
		if (cmp == 0) {
			cmp = this.name.compareTo(pDesc != null ? pDesc.getName() : null);
		}
		return cmp;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s (%s) = %s", getName(), getDescription(), String.valueOf(getValue()));
	}

	/**
	 * Aktualizuje wartosc wlasciwosci pobierajac wartosc pola modelu przez refleksje.
	 */
	private void updateValue() {
		this.value = getFieldValue(this.model, this.field);
	}

	/**
	 * Pobiera wartosc okreslonego pola.
	 * 
	 * @param	pModel model
	 * @param	pField pole modelu
	 * 
	 * @return	wartosc pola
	 */
	private Object getFieldValue(Model pModel, Field pField) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(pField.getName(), pModel.getClass());
			Method readMethod = pd.getReadMethod();
			if (readMethod != null) {
				return readMethod.invoke(pModel);
			} else {
				throw new RuntimeException("Brak gettera dla pola [" + pField.getName() + "] w modelu ["
						+ pModel.getClass().getSimpleName() + "]");
			}
		} catch (Exception ex) {
			throw new RuntimeException("Blad pobierania wartosci pola [" + pField.getName() + "] z modelu ["
					+ pModel.getClass().getSimpleName() + "]", ex);
		}
	}

	/**
	 * Ustawia wartosc okreslonego pola.
	 * 
	 * @param	pModel model
	 * @param	pField pole modelu
	 * @param	pValue wartosc pola
	 * 
	 * @return	wartosc pola
	 */
	private void setFieldValue(Model pModel, Field pField, Object pValue) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(pField.getName(), pModel.getClass());
			Method writeMethod = pd.getWriteMethod();
			if (writeMethod != null) {
				writeMethod.invoke(pModel, pValue);
			} else {
				throw new RuntimeException("Brak settera dla pola [" + pField.getName() + "] w modelu ["
						+ pModel.getClass().getSimpleName() + "]");
			}
		} catch (Exception ex) {
			throw new RuntimeException("Blad ustawiania wartosci [" + pValue + "] dla pola [" + pField.getName() + "] w modelu ["
					+ pModel.getClass().getSimpleName() + "]", ex);
		}
	}

}
