package pl.horaczy.robocode.editor.widget;

/**
 * Informacja wyswiatlana na pasku statusu.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class StatusMessage {

	/** Komunikat tekstowy. */
	private String message;

	/**
	 * @param	pMessage komunikat
	 */
	public StatusMessage(String pMessage) {
		this.message = pMessage;
	}

	/**
	 * @return	wartosc pola message
	 */
	public String getMessage() {
		return this.message;
	}

}
