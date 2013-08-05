package pl.horaczy.robocode.editor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class Model implements Serializable {

	/** UID. */
	private static final long serialVersionUID = 1L;
	/** Prefiks URL. */
	private static final String URI_PREFIX = "robo://pl.horaczy.robocode.editor";

	/** Zarzadzanie zmianami. */
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	/**
	 * Rejestruje nowy obiekt nasluchujacy na zmiany.
	 * 
	 * @param	pListener obiekt nasluchujacy na zmiany
	 */
	public void addPropertyChangeListener(PropertyChangeListener pListener) {
		this.propertyChangeSupport.addPropertyChangeListener(pListener);
	}

	/**
	 * Usuwa obiekt nasluchujacy na zmiany.
	 * 
	 * @param	pListener obiekt nasluchujacy na zmiany
	 */
	public void removePropertyChangeListener(PropertyChangeListener pListener) {
		this.propertyChangeSupport.removePropertyChangeListener(pListener);
	}

	/**
	 * Rejestruje nowy obiekt nasluchujacy na zmiany konkretnego pola.
	 * 
	 * @param	pPropertyName nazwa wlasciwosci
	 * @param	pListener obiekt nasluchujacy na zmiany
	 */
	public void addPropertyChangeListener(String pPropertyName, PropertyChangeListener pListener) {
		this.propertyChangeSupport.addPropertyChangeListener(pPropertyName, pListener);
	}

	/**
	 * Usuwa obiekt nasluchujacy na zmiany konkretnego pola.
	 * 
	 * @param	pPropertyName nazwa wlasciwosci
	 * @param	pListener obiekt nasluchujacy na zmiany
	 */
	public void removePropertyChangeListener(String pPropertyName, PropertyChangeListener pListener) {
		this.propertyChangeSupport.removePropertyChangeListener(pPropertyName, pListener);
	}

	/**
	 * @return	adres URL do konkretne instancji
	 */
	public String getInputURI() {
		try {
			return URLEncoder.encode(String.format("%s%s", URI_PREFIX, getInputURIPart()), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("UTF-8 not supported!", ex);
		}
	}

	/**
	 * Powiadamia obiektu nasluchujace o zmianie wartosci konkretnej wlasciwosci i zwraca nowa wartosc wlasciwosci.
	 * 
	 * @param	pName nazwa wlascowosci
	 * @param	pOldValue aktualna wartosc wlasciwosci
	 * @param	pNewValue nowa wartosc wlasciwosci
	 * 
	 * @return	nowa wartosc wlasciwosci
	 */
	protected <T> T firePropertyChange(String pName, T pOldValue, T pNewValue) {
		this.propertyChangeSupport.firePropertyChange(pName, pOldValue, pNewValue);
		return pNewValue;
	}

	/**
	 * @return	czesc URL (bez prefiksu) dla konkretnej instancji
	 */
	protected abstract String getInputURIPart();

}
