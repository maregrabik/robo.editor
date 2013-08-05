package pl.horaczy.robocode.editor.model;

import pl.horaczy.robocode.editor.pool.Colors;

public class InstructionStop extends Instruction {

	/** UID. */
	private static final long serialVersionUID = 1L;

	public InstructionStop(int pX, int pY) {
		super("Zakoncz", pX, pY);
		setFillColor(Colors.RED_L);
	}

	@Override
	public String generateJavaCode() {
		return "}";
	}
}
