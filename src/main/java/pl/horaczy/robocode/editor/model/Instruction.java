package pl.horaczy.robocode.editor.model;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import pl.horaczy.robocode.editor.pool.Colors;
import pl.horaczy.robocode.editor.pool.Fonts;

public abstract class Instruction extends Rectangle {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * @param	pName nazwa instrukcji
	 * @param	pX pozycja X na ekranie
	 * @param	pY pozycja Y na ekranie
	 */
	public Instruction(String pName, int pX, int pY) {
		super(pName, pX, pY, 400, 40);
		setFrameColor(Colors.BLACK);
	}

	@Override
	public void draw(GC pGc) {
		super.draw(pGc);
		
		pGc.setForeground(Colors.WHITE);
		pGc.setFont(Fonts.MONOTYPE);
		String text = getLabel();
		Point textSize = pGc.textExtent(text);
		pGc.drawText(text, getX() + 10, getY() + (getHeight() - textSize.y) / 2);
	}

	/**
	 * @return	kod w jezyku Java dla instrukcji
	 */
	public abstract String generateJavaCode();
	
	protected String getLabel() {
		return getName();
	}

}
