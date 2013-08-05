package pl.horaczy.robocode.editor.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TypedListener;

import pl.horaczy.robocode.editor.model.Shape;
import pl.horaczy.robocode.editor.model.ShapeGroup;
import pl.horaczy.robocode.editor.model.Shape.State;
import pl.horaczy.robocode.editor.pool.Colors;

public class EditorCanvas extends Canvas implements PaintListener, PropertyChangeListener, MouseListener, MouseMoveListener, KeyListener {

	public interface CanvasListener {
		/**
		 * Zdarzenie zmiany zaznaczenia.
		 * 
		 * @param	pSelected zaznaczony ksztalt (moze byc <code>null</code>)
		 */
		void onSelectionChanged(Shape pSelected);
	}

	private static final Cursor CURSOR_NORMAL = new Cursor(Display.getDefault(), SWT.CURSOR_ARROW);
	private static final Cursor CURSOR_HOVER = CURSOR_NORMAL;
	private static final Cursor CURSOR_MOVE = new Cursor(Display.getDefault(), SWT.CURSOR_SIZEALL);

	private final ShapeGroup rootGroup;

	private Color gridLinesColor;
	private boolean gridShow;
	private boolean gridSnap;
	private int gridSpacing;

	private Image bgImage;

	private final WritableInputContext inputContext;

	/** Czy edytor dziala w trybie debug. */
	private boolean debug;

	/** Obiekt nasluchujacy na zmiany w edytorze. */
	private CanvasListener canvasListeners;

	public EditorCanvas(Composite pParent, ShapeGroup pShapes, int pStyle, CanvasListener pListener) {
		super(pParent, SWT.NO_BACKGROUND | pStyle);
		this.debug = System.getProperty("debug") != null;

		this.canvasListeners = pListener;

		this.inputContext = new WritableInputContext();
		if (pShapes == null) {
			throw new IllegalArgumentException("Glowna grupa ksztaltow jest wymagana");
		}
		this.rootGroup = pShapes;
		this.rootGroup.addPropertyChangeListener(this);

		setGridLinesColor(Colors.create(0xF0, 0xF0, 0xF0));
		setGridShow(true);
		setGridSpacing(10);

		addMouseListener(this);
		addMouseMoveListener(this);
		addKeyListener(this);
		addPaintListener(this);
	}

	@Override
	public void paintControl(PaintEvent pE) {
		GC gc = pE.gc;
		Rectangle bounds = getBounds();
		drawBackground(gc, bounds);
		this.rootGroup.draw(gc);
		if (this.debug) {
			this.rootGroup.drawBounds(gc);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pEvt) {
		asyncRedraw();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param	pEvent zdarzenie podwojnego klikniecia
	 * 
	 * @see		org.eclipse.swt.events.MouseListener#mouseDoubleClick(MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent pEvent) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param	pEvent zdarzenie wcisniecia przycisku myszy
	 * 
	 * @see		org.eclipse.swt.events.MouseListener#mouseDown(MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent pEvent) {
		if (pEvent.button == 1) {
			this.inputContext.setBtnMouse(true);
		}
		updateEditorState(true);
		this.canvasListeners.onSelectionChanged(this.rootGroup.getFirstShape(State.SELECTED));

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param	pEvent zdarzenie puszczenia przycisku myszy
	 * 
	 * @see		org.eclipse.swt.events.MouseListener#mouseUp(MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent pEvent) {
		if (pEvent.button == 1) {
			this.inputContext.setBtnMouse(false);
		}
		updateEditorState(false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param	pEvent zdarzenie ruchu myszy
	 * 
	 * @see		org.eclipse.swt.events.MouseMoveListener#mouseMove(MouseEvent)
	 */
	@Override
	public void mouseMove(MouseEvent pEvent) {
		this.inputContext.setCurMouse(pEvent.x, pEvent.y);
		Shape selected = this.inputContext.isBtnMouse() ? this.rootGroup.getFirstShape(State.SELECTED) : null;
		if (selected != null) {
			if (isGridSnap()) {
				Rectangle shapeBounds = selected.getBounds();
				if (shapeBounds != null) {
					int newX = snapToGrid(this.inputContext.getCurMouse().x - selected.getClickPosOffset().x, shapeBounds.width);
					int newY = snapToGrid(this.inputContext.getCurMouse().y - selected.getClickPosOffset().y, shapeBounds.height);
					selected.moveTo(newX, newY);
				}
			} else {
				selected.moveBy(this.inputContext.getCurOffset().x, this.inputContext.getCurOffset().y);
			}
		} else {
			updateEditorState(false);
		}
	}

	@Override
	public void keyPressed(KeyEvent pEvent) {
		if (pEvent.keyCode == SWT.ALT) {
			this.inputContext.setKeyAlt(true);
		}
		if (pEvent.keyCode == SWT.SHIFT) {
			this.inputContext.setKeyShift(true);
		}
		if (pEvent.keyCode == SWT.CTRL) {
			this.inputContext.setKeyCtrl(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent pEvent) {
		if (pEvent.keyCode == SWT.ALT) {
			this.inputContext.setKeyAlt(false);
		}
		if (pEvent.keyCode == SWT.SHIFT) {
			this.inputContext.setKeyShift(false);
		}
		if (pEvent.keyCode == SWT.CTRL) {
			this.inputContext.setKeyCtrl(false);
		}
	}

	/*
	 * ----------------------------------------------------------------
	 */

	/**
	 * @return	wartosc pola gridLinesColor
	 */
	public Color getGridLinesColor() {
		return this.gridLinesColor;
	}

	/**
	 * @param	pGridLinesColor nowa wartosc pola gridLinesColor
	 */
	public void setGridLinesColor(Color pGridLinesColor) {
		this.gridLinesColor = pGridLinesColor;
	}

	/**
	 * @param	pListener obiekt nasluchujacy na zmiany
	 */
	public void addModifyListener(ModifyListener pListener) {
		addListener(SWT.Modify, new TypedListener(pListener));
	}

	/**
	 * @return	wartosc pola gridShow
	 */
	public boolean isGridShow() {
		return this.gridShow;
	}

	/**
	 * @param	pGridShow nowa wartosc pola gridShow
	 */
	public void setGridShow(boolean pGridShow) {
		if (pGridShow != this.gridShow) {
			this.bgImage = null;
		}
		this.gridShow = pGridShow;
		if (this.bgImage == null) {
			asyncRedraw();
		}
	}

	/**
	 * @return	wartosc pola gridSnap
	 */
	public boolean isGridSnap() {
		return this.gridSnap;
	}

	/**
	 * @param	pGridSnap nowa wartosc pola gridSnap
	 */
	public void setGridSnap(boolean pGridSnap) {
		this.gridSnap = pGridSnap;
	}

	/**
	 * @return	wartosc pola gridSpacing
	 */
	public int getGridSpacing() {
		return this.gridSpacing;
	}

	/**
	 * @param	pGridSpacing nowa wartosc pola gridSpacing
	 */
	public void setGridSpacing(int pGridSpacing) {
		this.gridSpacing = pGridSpacing;
	}

	/**
	 * @return	grupa wszystkich ksztaltow
	 */
	public ShapeGroup getRootGroup() {
		return this.rootGroup;
	}

	/**
	 * @return	stan wejscia (mysz, klawiatura) dla edytora
	 */
	public InputContext getInputContext() {
		return this.inputContext;
	}

	/*
	 * ----------------------------------------------------------------
	 */

	/**
	 * Planuje asynchroniczne wykonanie odrysowania ekranu.
	 */
	private void asyncRedraw() {
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				redraw();
			}
		});
	}

	/**
	 * @param	pGc kontekst rysowania
	 * @param	pBounds rozmiar obszaru rysowania
	 */
	private void drawBackground(GC pGc, Rectangle pBounds) {
		if (this.bgImage != null && !this.bgImage.getBounds().equals(pBounds)) {
			this.bgImage.dispose();
			this.bgImage = null;
		}

		if (this.bgImage == null) {
			this.bgImage = new Image(getDisplay(), pBounds.width, pBounds.height);
			GC gc = new GC(this.bgImage);
			gc.setBackground(getBackground());
			gc.fillRectangle(pBounds);

			if (this.gridShow) {
				gc.setForeground(getGridLinesColor());
				// vertical lines
				for (int x = this.gridSpacing; x < pBounds.width; x += this.gridSpacing) {
					gc.drawLine(x, 0, x, pBounds.height);
				}
				// horizontal lines
				for (int y = this.gridSpacing; y < pBounds.height; y += this.gridSpacing) {
					gc.drawLine(pBounds.x, y, pBounds.width, y);
				}
			}

			gc.dispose();
		}

		pGc.drawImage(this.bgImage, 0, 0);
	}

	/**
	 * @param	pSelect <code>true</code> jesli nalezy ustawic ksztalt znajdujacy sie pod kursorem jako zaznaczony
	 */
	private void updateEditorState(boolean pSelect) {
		Shape hoveredShape = this.rootGroup.getFirstShape(this.inputContext.getCurMouse());
		if (pSelect && this.inputContext.isBtnMouse()) {
			hoveredShape = setStateOfHovered(hoveredShape, State.SELECTED);
			if (hoveredShape != null && hoveredShape.getState() == State.SELECTED) {
				hoveredShape.setClickPos(this.inputContext.getCurMouse());
			}
		}
		if (hoveredShape == null || (hoveredShape != null && hoveredShape.getState() != State.SELECTED)) {
			hoveredShape = setStateOfHovered(hoveredShape, State.HOVERED);
		}
		setCursor(CURSOR_NORMAL);
		if (hoveredShape != null) {
			if (hoveredShape.getState() == State.HOVERED) {
				setCursor(CURSOR_HOVER);
			} else if (hoveredShape.getState() == State.SELECTED && this.inputContext.isBtnMouse()) {
				setCursor(CURSOR_MOVE);
			}
		}
	}

	/**
	 * Ustawia nowy stan figure znajdujacej sie bezposrednio pod kursorem myszy. Jesli
	 * pod kursorem myszy nie znajduej sie zadna figura, wtedy stan figury ktora aktualnie
	 * znajduje sie w zadanem stanie jest ustawiany jako {@link State.#NORMAL}.
	 * 
	 * @param	pShape figura znajdujaca sie bezposrednia pod kursorem
	 * @param	pNewState nowy stan ksztalut znajdujacego sie pod kursorem
	 * 
	 * @return	figura ktorej ustawiono nowy stan (moze byc <code>null</code>)
	 */
	private Shape setStateOfHovered(Shape pShape, State pNewState) {
		Shape oldShape = this.rootGroup.getFirstShape(pNewState);
		if (pShape == null && oldShape != null) {
			oldShape.setState(State.NORMAL);
		} else if (pShape != null && pShape.getState() != pNewState) {
			if (oldShape != null) {
				oldShape.setState(State.NORMAL);
			}
			pShape.setState(pNewState);
		}
		return pShape;
	}

	/**
	 * Wylicza nowa pozycja krawedzi tak aby wybrana krawedz figury zostala przyciagnieta do siatki.
	 * 
	 * @param	pEdge pozycja X lewej krawedzi lub Y gornej krawedzi figury
	 * @param	pSize szerokosc lub wysokosc figury
	 * 
	 * @return	nowa pozycja X lewej lub Y gornej krawedzi figury
	 */
	private int snapToGrid(int pEdge, int pSize) {
		int g1 = snapToGrid(pEdge);
		int g2 = snapToGrid(pEdge + pSize);
		return snapToGrid(g1, g2, pEdge, pSize);
	}

	/**
	 * Wylicza najblizsza pozycje do ktorej nalezy przyciagnac wybrana krawedz figury.
	 * 
	 * @param	pPos pozycja jednej z krawedzi (lewej, prawej, dolnej lub gornej) figury
	 * 
	 * @return	pozycja krawedzi po przyciagnieciu do siatki
	 */
	private int snapToGrid(int pPos) {
		int p1 = pPos - pPos % this.gridSpacing;
		int p2 = p1 + this.gridSpacing;
		return Math.abs(pPos - p1) < Math.abs(pPos - p2) ? p1 : p2;
	}

	/**
	 * Wybiera korzystniejsza (blizsza jednej z krawedzi figury) pozycje siatki do przyciagniecia.
	 * 
	 * @param	pGrid1 pozycja do ktorej nalezy przyciagnac lewa lub gorna krawedz figury
	 * @param	pGrid2 pozycja do ktorej nalezy przyciagnac prawa lub dolna krawedz figury
	 * @param	pEdge pEdge pozycja X lewej krawedzi lub Y gornej krawedzi figury
	 * @param	pSize Size szerokosc lub wysokosc figury
	 * 
	 * @return	nowa pozycja lewej lub gornej krawedzi figury po przyciagnieciu do siatki
	 */
	private int snapToGrid(int pGrid1, int pGrid2, int pEdge, int pSize) {
		return Math.abs(pGrid1 - pEdge) < Math.abs(pGrid2 - pEdge - pSize) ? pGrid1 : pGrid2 - pSize;
	}

}
