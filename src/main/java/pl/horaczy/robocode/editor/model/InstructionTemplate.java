package pl.horaczy.robocode.editor.model;


public class InstructionTemplate extends Model implements Comparable<InstructionTemplate> {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Nazwa instrukcji. */
	private String nazwa;

	/**
	 * Tworzy nienazwany szablon instrukcji.
	 */
	public InstructionTemplate() {
		this("");
	}

	/**
	 * Tworzy szablon instrukcji o podanej nazwie.
	 * 
	 * @param	pNazwa nazwa instrukcji
	 */
	public InstructionTemplate(String pNazwa) {
		setNazwa(pNazwa);
	}

	/**
	 * @return	wartosc pola nazwa
	 */
	public String getNazwa() {
		return this.nazwa;
	}

	/**
	 * @param	pNazwa nowa wartosc pola nazwa
	 */
	public void setNazwa(String pNazwa) {
		if (pNazwa == null) {
			throw new IllegalArgumentException("Nazwa szablonu instrukcji jest wymagana");
		}
		firePropertyChange("nazwa", this.nazwa, this.nazwa = pNazwa);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(InstructionTemplate pSzablon) {
		return this.nazwa.compareTo(pSzablon != null ? pSzablon.nazwa : "");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Szablon instrukcji: %s", getNazwa());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Model#getInputURIPart()
	 */
	@Override
	protected String getInputURIPart() {
		return String.format("/szablonInstrukcji-%s", getNazwa());
	}

}
