package pl.horaczy.robocode.editor.model;

import pl.horaczy.robocode.editor.model.annotation.Property;
import pl.horaczy.robocode.editor.pool.Colors;

public class InstructionRotate extends Instruction {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Kat obrotu w stopniach. */
	@Property(name = "Kat obrotu", priority = 500)
	private int angle;

	public InstructionRotate(int pX, int pY) {
		super("Obroc", pX, pY);
		setFillColor(Colors.YELLOW_L);
	}

	@Override
	public String generateJavaCode() {
		return "robot.rotate(" + this.angle + ");";
	}

	/**
	 * @return wartosc pola angle
	 */
	public int getAngle() {
		return this.angle;
	}

	/**
	 * @param pAngle nowa wartosc pola angle
	 */
	public void setAngle(int pAngle) {
		firePropertyChange("angle", this.angle, this.angle = pAngle);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Instruction#getLabel()
	 */
	@Override
	protected String getLabel() {
		return String.format("%s o %d stopni", getName(), getAngle());
	}

}
