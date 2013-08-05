package pl.horaczy.robocode.editor.model;

import pl.horaczy.robocode.editor.pool.Colors;

public class InstructionStart extends Instruction {

	/** UID. */
	private static final long serialVersionUID = 1L;

	public InstructionStart(int pX, int pY) {
		super("Rozpocznij", pX, pY);
		setFillColor(Colors.GREEN_L);
	}

	@Override
	public String generateJavaCode() {
		return "public static void main(String[] args) {";
	}
}
