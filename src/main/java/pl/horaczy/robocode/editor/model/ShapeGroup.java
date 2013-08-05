package pl.horaczy.robocode.editor.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ShapeGroup extends Shape implements PropertyChangeListener {

	/** UID. */
	private static final long serialVersionUID = 1L;

	/** Kolekcja ksztaltow w grupie, posortowana wg. z-indeksu. */
	private LinkedList<Shape> shapes;
	/** Obszar obejmujacy wszystkie ksztaltu w grupie - <code>null</code> w przypadku braku ksztaltow. */
	private Rectangle bounds;

	/**
	 * Tworzy pusta grupe.
	 * 
	 * @param	pName nazwa grupy
	 * @param	pX pozycja na osi OX
	 * @param	pY pozycja na osi OY
	 */
	public ShapeGroup(String pName, int pX, int pY) {
		super(pName, pX, pY);
		this.shapes = new LinkedList<>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Shape#getBounds()
	 */
	@Override
	public Rectangle getBounds() {
		return this.bounds;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Shape#draw(org.eclipse.swt.graphics.GC)
	 */
	@Override
	public void draw(GC pGc) {
		for (Shape shape : this.shapes) {
			shape.draw(pGc);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Shape#contains(org.eclipse.swt.graphics.Point)
	 */
	@Override
	public boolean contains(Point pPoint) {
		for (Shape shape : this.shapes) {
			if (shape.contains(pPoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Dodaje ksztalt do grupy. Po kazdorazowym dodaniu nowego elementu
	 * kolekcja jest sortowana wg z-indeksu.
	 * 
	 * @param	pShape ksztalt ktory zostanie dodany do grupy
	 */
	public void add(Shape pShape) {
		if (pShape != null && this.shapes.add(pShape)) {
			pShape.addPropertyChangeListener(this);
			onShapesChanged();
		}
	}

	/**
	 * Usuwa ksztalt z grupy.
	 * 
	 * @param	pShape ksztalt ktory zostanie usuniety z grupy
	 */
	public void remove(Shape pShape) {
		if (pShape != null && this.shapes.remove(pShape)) {
			pShape.removePropertyChangeListener(this);
			onShapesChanged();
		}
	}

	/**
	 * @return	kolekcja wszystkich ksztaltow znajdujacych sie w grupie
	 */
	public List<Shape> getShapes() {
		return this.shapes;
	}

	/**
	 * Znajduje pierwsza figure do ktorej nalezy okreslony punkt.
	 * 
	 * @param	pPoint wspolrzedne punktu
	 * 
	 * @return	figura do ktorej nalezy punkt <code>pPoint</code> (moze byc <code>null</code>)
	 */
	public Shape getFirstShape(Point pPoint) {
		for (Iterator<Shape> iter = this.shapes.descendingIterator(); iter.hasNext();) {
			Shape shape = iter.next();
			if (shape.contains(pPoint)) {
				return shape;
			}
		}
		return null;
	}

	/**
	 * Znajduje pierwsza figure znajdujaca sie w okreslonym stanie.
	 * 
	 * @param	pState stan figury
	 * 
	 * @return	figura w stanie <code>pState</code> (moze byc <code>null</code>)
	 */
	public Shape getFirstShape(State pState) {
		for (Iterator<Shape> iter = this.shapes.descendingIterator(); iter.hasNext();) {
			Shape shape = iter.next();
			if (shape.getState() == pState) {
				return shape;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pEvent) {
		onShapesChanged();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.model.Model#getInputURIPart()
	 */
	@Override
	protected String getInputURIPart() {
		return String.format("/group-%s_%dx%d", getName(), getX(), getY());
	}

	/**
	 * Obsluga zdarzenia zmiany kszaltow na liscie {@link #shapes}.
	 */
	private void onShapesChanged() {
		Collections.sort(this.shapes);
		recomputeBounds();
		firePropertyChange("shapes", 0, 1);
	}

	/**
	 * Przelicza granice grupy ksztaltow.
	 */
	private synchronized void recomputeBounds() {
		this.bounds = null;
		for (Shape shape : this.shapes) {
			Rectangle shapeBounds = shape.getBounds();
			if (this.bounds == null) {
				this.bounds = new Rectangle(shapeBounds.x, shapeBounds.y, shapeBounds.width, shapeBounds.height);
			} else {
				this.bounds.add(shapeBounds);
			}
		}
	}

}
