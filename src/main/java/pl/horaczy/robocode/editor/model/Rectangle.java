package pl.horaczy.robocode.editor.model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import pl.horaczy.robocode.editor.pool.Colors;

/**
 * Ksztalt prostokata.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class Rectangle extends Shape {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Szerokosc prostokata. */
	private int width;
	/** Wysokosc prostokata. */
	private int height;
	/** Kolor ramki prostokata. */
	private Color frameColor;
	/** Kolor wypelniania prostokata. */
	private Color fillColor;

	/**
	 * @param	pName nazwa prostokata
	 * @param	pX pozycja lewej krawedzi
	 * @param	pY pozycja gornej krawedzi
	 * @param	pWidth szerokosc prostokata
	 * @param	pHeight wysokosc prostokata
	 */
	public Rectangle(String pName, int pX, int pY, int pWidth, int pHeight) {
		super(pName, pX, pY);
		setWidth(pWidth);
		setHeight(pHeight);
		setFrameColor(Colors.BLACK);
		setFillColor(Colors.WHITE);
	}

	/**
	 * @return	wartosc pola width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @param	pWidth nowa wartosc pola width
	 */
	public void setWidth(int pWidth) {
		firePropertyChange("width", this.width, this.width = pWidth);
	}

	/**
	 * @return	wartosc pola height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * @param	pHeight nowa wartosc pola height
	 */
	public void setHeight(int pHeight) {
		firePropertyChange("height", this.height, this.height = pHeight);
	}

	/**
	 * @return	wartosc pola frameColor
	 */
	public Color getFrameColor() {
		return this.frameColor;
	}

	/**
	 * @param	pFrameColor nowa wartosc pola frameColor
	 */
	public void setFrameColor(Color pFrameColor) {
		firePropertyChange("frameColor", this.frameColor, this.frameColor = pFrameColor);
	}

	/**
	 * @return	wartosc pola fillColor
	 */
	public Color getFillColor() {
		return this.fillColor;
	}

	/**
	 * @param	pFillColor nowa wartosc pola fillColor
	 */
	public void setFillColor(Color pFillColor) {
		firePropertyChange("fillColor", this.fillColor, this.fillColor = pFillColor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Shape#getBounds()
	 */
	@Override
	public org.eclipse.swt.graphics.Rectangle getBounds() {
		return new org.eclipse.swt.graphics.Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Shape#draw(GC)
	 */
	@Override
	public void draw(GC pGc) {
		setLineStyle(pGc);
		pGc.setForeground(getFrameColor());
		pGc.drawRectangle(getX(), getY(), getWidth(), getHeight());
		pGc.setBackground(getFillColor());
		pGc.fillRectangle(getX() + 1, getY() + 1, getWidth() - pGc.getLineWidth(), getHeight() - pGc.getLineWidth());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Shape#contains(org.eclipse.swt.graphics.Point)
	 */
	@Override
	public boolean contains(Point pPoint) {
		return pPoint != null && pPoint.x >= getX() && pPoint.x <= getX() + getWidth() && pPoint.y >= getY()
				&& pPoint.y <= getY() + getHeight();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Model#getInputURIPart()
	 */
	@Override
	protected String getInputURIPart() {
		return String.format("/rectangle-%s_%dx%d", getName(), getX(), getY());
	}

}
