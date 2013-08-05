package pl.horaczy.robocode.editor.model;

import pl.horaczy.robocode.editor.model.annotation.Property;
import pl.horaczy.robocode.editor.pool.Colors;

public class InstructionShoot extends Instruction {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Typ strzalu. */
	@Property(name = "Typ strzalu", priority = 300)
	private int type;

	public InstructionShoot(int pX, int pY) {
		super("Strzelaj", pX, pY);
		setFillColor(Colors.YELLOW_L);
	}

	@Override
	public String generateJavaCode() {
		return "robot.shoot(" + this.type + ");";
	}

	/**
	 * @return wartosc pola type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * @param pType nowa wartosc pola type
	 */
	public void setType(int pType) {
		firePropertyChange("type", this.type, this.type = pType);
	}

}
