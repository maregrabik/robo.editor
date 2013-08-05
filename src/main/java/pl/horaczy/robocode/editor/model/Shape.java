package pl.horaczy.robocode.editor.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import pl.horaczy.robocode.editor.pool.Colors;

/**
 * Bazowa klasa dla ksztaltow dostepnych w edytorze kodu.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public abstract class Shape extends Model implements Comparable<Shape> {

	public enum State {
		/** Figura w stanie normalnym. */
		NORMAL,
		/** Kursor znajduje sie bezposrednio nad figura. */
		HOVERED,
		/** Figura zaznaczona / wybrana. */
		SELECTED;
	}

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Pozycja X ksztaltu (moze to byc zarowno srodek okregu jak i lewy gorny rog prostokata). */
	private int x;
	/** Pozycja Y ksztaltu (moze to byc zarowno srodek okregu jak i lewy gorny rog prostokata). */
	private int y;
	/** Nazwa ksztaltu. */
	private String name;
	/** Indeks okreslajacy glebie. */
	private int zIndex;
	/** Stan w ktorym znajduje sie figura. */
	private State state;
	/** Kolor granic kszaltu. */
	private Color boundsColor;
	/** Przesuniecie poczatku figury wzgledem pozycji kursora myszy w momencie zaznaczenia. */
	private Point clickPosOffset;

	/**
	 * @param	pName nazwa ksztaltu
	 * @param	pX pozycja X
	 * @param	pY pozycja Y
	 */
	public Shape(String pName, int pX, int pY) {
		setState(State.NORMAL);
		setName(pName);
		setX(pX);
		setY(pY);
	}

	/**
	 * @return	wartosc pola x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * @param	pX nowa wartosc pola x
	 */
	public void setX(int pX) {
		firePropertyChange("x", this.x, this.x = pX);
	}

	/**
	 * @return	wartosc pola y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * @param	pY nowa wartosc pola y
	 */
	public void setY(int pY) {
		firePropertyChange("y", this.y, this.y = pY);
	}

	/**
	 * @return	wartosc pola name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param	pName nowa wartosc pola name
	 */
	public void setName(String pName) {
		firePropertyChange("name", this.name, this.name = pName);
	}

	/**
	 * @return	wartosc pola zIndex
	 */
	public int getZIndex() {
		return this.zIndex;
	}

	/**
	 * @param	pZIndex nowa wartosc pola zIndex
	 */
	public void setZIndex(int pZIndex) {
		firePropertyChange("zIndex", this.zIndex, this.zIndex = pZIndex);
	}

	/**
	 * Zwieksza wartosc pola zIndex o <code>1</code>.
	 */
	public void incZIndex() {
		setZIndex(getZIndex() + 1);
	}

	/**
	 * Zmniejsza wartosc pola zIndex o <code>1</code>.
	 */
	public void decZIndex() {
		setZIndex(getZIndex() - 1);
	}

	/**
	 * @return	wartosc pola state
	 */
	public State getState() {
		return this.state;
	}

	/**
	 * @return	kolor granic kszaltu
	 */
	public Color getBoundsColor() {
		if (this.boundsColor == null) {
			this.boundsColor = Colors.random();
		}
		return this.boundsColor;
	}

	/**
	 * @param	pState nowa wartosc pola state
	 */
	public void setState(State pState) {
		if (pState == null) {
			throw new IllegalArgumentException("Stan jest wymagany");
		}
		firePropertyChange("state", this.state, this.state = pState);
	}

	/**
	 * @return	wartosc pola clickPosOffset
	 */
	public Point getClickPosOffset() {
		return this.clickPosOffset;
	}

	/**
	 * @param	pClickPos pozycja kursora myszy
	 */
	public void setClickPos(Point pClickPos) {
		if (pClickPos == null) {
			this.clickPosOffset = null;
		} else {
			this.clickPosOffset = new Point(pClickPos.x - this.x, pClickPos.y - this.y);
		}
	}

	/**
	 * @return	obszar w ktorym znajduje sie kszalt
	 */
	public abstract Rectangle getBounds();

	/**
	 * Przesuwa figure.
	 * 
	 * @param	pX przesuniecie na osi OX
	 * @param	pY przesuniecie na osi OY
	 */
	public void moveBy(int pX, int pY) {
		if (pX != 0 || pY != 0) {
			this.x += pX;
			this.y += pY;
			firePropertyChange("position", 0, 1);
		}
	}

	/**
	 * Przesuwa figure.
	 * 
	 * @param	pX pozycja na osi OX
	 * @param	pY pozycja na osi OY
	 */
	public void moveTo(int pX, int pY) {
		if (pX != this.x || pY != this.y) {
			this.x = pX;
			this.y = pY;
			firePropertyChange("position", 0, 1);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Shape pShape) {
		return Integer.compare(this.zIndex, pShape != null ? pShape.getZIndex() : 0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s: %dx%d", getName(), getX(), getY());
	}

	/**
	 * Rysuje figure na podanym obszarze rysowania.
	 * 
	 * @param	pGc obszar rysowania
	 */
	public abstract void draw(GC pGc);

	/**
	 * Rysuje granice figury.
	 * 
	 * @param	pGc obszar rysowania
	 */
	public void drawBounds(GC pGc) {
		Rectangle bounds = getBounds();
		if (bounds != null) {
			pGc.setLineWidth(2);
			pGc.setLineStyle(SWT.LINE_DOT);
			pGc.setForeground(getBoundsColor());
			pGc.drawRectangle(bounds);
		}
	}

	/**
	 * Sprawdza czy punkt o podanych wspolrzednych znajduje sie w obrebie tej figury.
	 * 
	 * @param	pPoint wspolrzedne punktu do sprawdzanie
	 * 
	 * @return	<code>true</code> jesli punkt znajduje sie w obrebie figury
	 */
	public abstract boolean contains(Point pPoint);

	/**
	 * Ustawia styl rysowanych linii.
	 * 
	 * @param	pGc obszar rysowania
	 */
	protected void setLineStyle(GC pGc) {
		switch (getState()) {
		case NORMAL:
			pGc.setLineWidth(1);
			pGc.setLineStyle(SWT.LINE_SOLID);
			break;

		case HOVERED:
			pGc.setLineWidth(1);
			pGc.setLineStyle(SWT.LINE_DASH);
			break;

		case SELECTED:
			pGc.setLineWidth(2);
			pGc.setLineStyle(SWT.LINE_DASH);
		}
	}

}
