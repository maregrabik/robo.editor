package pl.horaczy.robocode.editor.model;

import pl.horaczy.robocode.editor.model.annotation.Property;
import pl.horaczy.robocode.editor.pool.Colors;

public class InstructionFor extends Instruction {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Wartosc poczatkowa. */
	@Property(name = "Poczatek", priority = 200)
	private int start;
	/** Krok. */
	@Property(name = "Krok", priority = 201)
	private int step = 1;
	/** Wartosc koncowa */
	@Property(name = "Koniec", priority = 202)
	private int stop;

	public InstructionFor(int pX, int pY) {
		super("Powtarzaj", pX, pY);
		setFillColor(Colors.BLUE_L);
	}

	@Override
	public String generateJavaCode() {
		return "for (int i = " + this.start + "; i <= " + this.stop + "; i += " + this.step + ")";
	}

	/**
	 * @return wartosc pola start
	 */
	public int getStart() {
		return this.start;
	}

	/**
	 * @param pStart nowa wartosc pola start
	 */
	public void setStart(int pStart) {
		firePropertyChange("start", this.start, this.start = pStart);
	}

	/**
	 * @return wartosc pola step
	 */
	public int getStep() {
		return this.step;
	}

	/**
	 * @param pStep nowa wartosc pola step
	 */
	public void setStep(int pStep) {
		firePropertyChange("step", this.step, this.step = pStep);
	}

	/**
	 * @return wartosc pola stop
	 */
	public int getStop() {
		return this.stop;
	}

	/**
	 * @param pStop nowa wartosc pola stop
	 */
	public void setStop(int pStop) {
		firePropertyChange("stop", this.stop, this.stop = pStop);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Instruction#getLabel()
	 */
	@Override
	protected String getLabel() {
		return String.format("%s od %d co %d az do %d", getName(), getStart(), getStep(), getStop());
	}

}
