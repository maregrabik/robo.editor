package pl.horaczy.robocode.editor.widget;

import org.eclipse.swt.graphics.Point;

/**
 * Stan urzadzen wejsciowych dla edytora.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public interface InputContext {

	/**
	 * @return	aktualna pozycja kursora w obrebie edytora
	 */
	public Point getCurMouse();

	/**
	 * @return	przesuniecie wzgledem poprzedniej pozycji kursora
	 */
	public Point getCurOffset();

	/**
	 * @return	<code>true</code> jesli pierwszy (zazwyczaj lewy) przycisk myszy jest wcisniety
	 */
	public boolean isBtnMouse();

	/**
	 * @return	<code>true</code> jesli przycisk <i>Ctrl</i> jest wcisniety
	 */
	public boolean isKeyCtrl();

	/**
	 * @return	<code>true</code> jesli przycisk <i>Shift</i> jest wcisniety
	 */
	public boolean isKeyShift();

	/**
	 * @return	<code>true</code> jesli przycisk <i>Alt</i> jest wcisniety
	 */
	public boolean isKeyAlt();
}