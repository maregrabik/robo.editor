package pl.horaczy.robocode.editor.model;

import pl.horaczy.robocode.editor.model.annotation.Property;

public class Code extends Model implements Comparable<Code> {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Definicja robota. */
	private Robot robot;
	
	/** Numer wersji kodu. */
	@Property(name = "Numer wersji")
	private int numer;

	private String nazwa;

	/**
	 * Tworzy kod robota o numerze <i>1</i>.
	 */
	public Code() {
		this(1);
	}

	/**
	 * Tworzy kod o podanym numerze werzji.
	 * 
	 * @param	pNumer numer wersji
	 */
	public Code(int pNumer) {
		setNumer(pNumer);
	}

	public Code(String pNazwa) {
		setNazwa(pNazwa);
	numer = 1;
	}

	/**
	 * @return	wartosc pola numer
	 */
	public int getNumer() {
		return this.numer;
	}

	/**
	 * @param	pNumer numer wersji kodu
	 */
	public void setNumer(int pNumer) {
		firePropertyChange("numer", this.numer, this.numer = pNumer);
	}

	/**
	 * @param	pRobot frobot dla ktorego tworzony jest kod robota
	 */
	public void setRobot(Robot pRobot) {
		this.robot = pRobot;
	}

	/**
	 * @return	robotdla ktorego tworzony jest kod
	 */
	public Robot getRobot() {
		return this.robot;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Code pCode) {
		return Integer.compare(this.numer, pCode != null ? pCode.getNumer() : 0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s, kod %d", getRobot(), getNumer());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Model#getInputURIPart()
	 */
	@Override
	protected String getInputURIPart() {
		return String.format("%s/kod-%d", getRobot().getInputURIPart(),getNumer());
	}

	public String getNazwa() {
		return this.nazwa;
	}

	public void setNazwa(String pNazwa) {
		this.nazwa = pNazwa;
	}

}
