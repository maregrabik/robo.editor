package pl.horaczy.robocode.editor.model;

public class Function extends Model implements Comparable<Function> {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Definicja robota. */
	private Robot robot;

	/** Nazwa funkcji. */
	private String nazwa;

	/**
	 * Tworzy funkcje <i>main</i>.
	 */
	public Function() {
		this("main");
	}

	/**
	 * Tworzy funkcje o podanej nazwie.
	 * 
	 * @param pNazwa nazwa funkcji
	 */
	public Function(String pNazwa) {
		setNazwa(pNazwa);
	}

	/**
	 * @param	pRobot frobot dla ktorego tworzony jest kod robota
	 */
	public void setRobot(Robot pRobot) {
		this.robot = pRobot;
	}

	/**
	 * @return	robot dla ktorego tworzony jest kod
	 */
	public Robot getRobot() {
		return this.robot;
	}

	public String getNazwa() {
		return this.nazwa;
	}

	public void setNazwa(String pNazwa) {
		firePropertyChange("nazwa", this.nazwa, this.nazwa = pNazwa);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Function pFunction) {
		return this.nazwa.compareTo(pFunction != null ? pFunction.nazwa : "");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s, Funkcja %s", getRobot(), getNazwa());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Model#getInputURIPart()
	 */
	@Override
	protected String getInputURIPart() {
		return String.format("%s/funkcja-%s", getRobot().getInputURIPart(), getNazwa());
	}

}
