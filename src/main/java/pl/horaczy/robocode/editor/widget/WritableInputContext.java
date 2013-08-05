package pl.horaczy.robocode.editor.widget;

import org.eclipse.swt.graphics.Point;

/**
 * Domyslna implementacja kontekstu wejscia (mysz, klawiatura).
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class WritableInputContext implements InputContext {
	/** Pozycja kursora myszy. */
	private Point curMouse;
	/** Przesuniecie kursorwa myszy wzgledem poprzedniej pozycji. */
	private Point curOffset;
	/** Czy pierwszy (zazwyczaj lewy) przycisk myszy jest wcisniety. */
	private boolean btnMouse;
	/** Czy klawisz <i>Ctrl</i> jest wcisniety. */
	private boolean keyCtrl;
	/** Czy klawisz <i>Shift</i> jest wcisniety. */
	private boolean keyShift;
	/** Czy klawisz <i>Alt</i> jest wcisniety. */
	private boolean keyAlt;

	public WritableInputContext() {
		this.curMouse = new Point(0, 0);
		this.curOffset = new Point(0, 0);
	}

	/**
	 * @return	wartosc pola curMouse
	 */
	public Point getCurMouse() {
		return this.curMouse;
	}

	/**
	 * @param	pX pozycja kursora myszy na osi OX
	 * @param	pY pozycja kursora myszy na osi OY
	 */
	public void setCurMouse(int pX, int pY) {
		this.curOffset.x = pX - this.curMouse.x;
		this.curOffset.y = pY - this.curMouse.y;
		this.curMouse.x = pX;
		this.curMouse.y = pY;
	}

	/**
	 * @return	przesuniecie wzgledem poprzedniej pozycji kursora
	 */
	public Point getCurOffset() {
		return this.curOffset;
	}

	/**
	 * @return	wartosc pola btnMouse
	 */
	public boolean isBtnMouse() {
		return this.btnMouse;
	}

	/**
	 * @param	pBtnMouse nowa wartosc pola btnMouse
	 */
	public void setBtnMouse(boolean pBtnMouse) {
		this.btnMouse = pBtnMouse;
	}

	/**
	 * @return	wartosc pola keyCtrl
	 */
	public boolean isKeyCtrl() {
		return this.keyCtrl;
	}

	/**
	 * @param	pKeyCtrl nowa wartosc pola keyCtrl
	 */
	public void setKeyCtrl(boolean pKeyCtrl) {
		this.keyCtrl = pKeyCtrl;
	}

	/**
	 * @return	wartosc pola keyShift
	 */
	public boolean isKeyShift() {
		return this.keyShift;
	}

	/**
	 * @param	pKeyShift nowa wartosc pola keyShift
	 */
	public void setKeyShift(boolean pKeyShift) {
		this.keyShift = pKeyShift;
	}

	/**
	 * @return	wartosc pola keyAlt
	 */
	public boolean isKeyAlt() {
		return this.keyAlt;
	}

	/**
	 * @param	pKeyAlt nowa wartosc pola keyAlt
	 */
	public void setKeyAlt(boolean pKeyAlt) {
		this.keyAlt = pKeyAlt;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Input { mouse: %dx%d, %b; alt: %b; ctrl: %b; shift: %b }", getCurMouse().x, getCurMouse().y, isBtnMouse(),
				isKeyAlt(), isKeyCtrl(), isKeyShift());
	}
}