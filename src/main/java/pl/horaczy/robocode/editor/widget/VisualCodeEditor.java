package pl.horaczy.robocode.editor.widget;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import pl.horaczy.robocode.editor.model.Shape;
import pl.horaczy.robocode.editor.model.ShapeGroup;
import pl.horaczy.robocode.editor.pool.Images;

public class VisualCodeEditor extends Composite implements MouseListener, MouseMoveListener, MouseWheelListener, KeyListener,
		EditorCanvas.CanvasListener {

	/**
	 * Definiuje interfejs obiektow nasluchujacych na zmiany w edytorze.
	 * 
	 * @author	MHoraczy
	 * @since	0.1.0
	 */
	public interface EditorListener {
		/**
		 * Zdarzenie zmiany stanu wejscia.
		 * 
		 * @param	pInput stan urzadzen wejsciowych (mysz, klawiatura)
		 */
		void onInputChanged(InputContext pInput);

		/**
		 * Zdarzenie zmiany zaznaczenia.
		 * 
		 * @param	pSelected zaznaczony ksztalt (moze byc <code>null</code>)
		 */
		void onSelectionChanged(Shape pSelected);
	}

	/** Panel paska narzedziowego. */
	private Composite toolbar;
	private Button btnShowGrid;
	private Button btnSnapToGrid;

	/** Obszar rysowania. */
	private EditorCanvas editorCanvas;
	/** Kolekcja obiektow nasluchujacych na zmiany w edytorze. */
	private Set<EditorListener> editorListeners;

	public VisualCodeEditor(Composite pParent, ShapeGroup pShapes) {
		super(pParent, SWT.NONE);
		setLayout();
		setBackground(getBackground());

		createEditorToolbar();
		createEditorCanvas(pShapes);
		createEditorToolbarWidgets();

		this.editorListeners = new HashSet<>();
	}

	/**
	 * @param	pListener nowy obiekt nasluchujacy na zmiany
	 */
	public void addEditorListener(EditorListener pListener) {
		if (pListener != null) {
			this.editorListeners.add(pListener);
		}
	}

	/**
	 * @param	pListener obiekt nasluchujacy do wyrejestrowania
	 */
	public void removeEditorListener(EditorListener pListener) {
		if (pListener != null) {
			this.editorListeners.remove(pListener);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.swt.events.KeyListener#keyPressed(KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent pE) {
		fireOnInputChangedEvent();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.swt.events.KeyListener#keyReleased(KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent pE) {
		fireOnInputChangedEvent();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.swt.events.MouseMoveListener#mouseMove(MouseEvent)
	 */
	@Override
	public void mouseMove(MouseEvent pE) {
		fireOnInputChangedEvent();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.swt.events.MouseListener#mouseDoubleClick(MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent pE) {
		fireOnInputChangedEvent();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.swt.events.MouseListener#mouseDown(MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent pE) {
		fireOnInputChangedEvent();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.swt.events.MouseListener#mouseUp(MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent pE) {
		fireOnInputChangedEvent();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.swt.events.MouseWheelListener#mouseScrolled(MouseEvent)
	 */
	@Override
	public void mouseScrolled(MouseEvent pE) {
		fireOnInputChangedEvent();
	}

	/**
	 * {@inheritDoc}
	 * @see		pl.horaczy.robocode.editor.widget.EditorCanvas.CanvasListener#onSelectionChanged(Shape)
	 */
	@Override
	public void onSelectionChanged(Shape pSelected) {
		fireOnSelectionChangedEvent(pSelected);
	}

	/**
	 * Tworzy pasek narzedziowy edytora.
	 */
	private void createEditorToolbar() {
		RowLayout layout = new RowLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;

		this.toolbar = new Composite(this, SWT.NONE);
		this.toolbar.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		this.toolbar.setLayout(layout);
		this.toolbar.setBackground(getBackground());
	}

	/**
	 * Tworzy obszar roboczy edytora.
	 * 
	 * @param	pShapes kolekcja ksztaltow do edycji
	 */
	private void createEditorCanvas(ShapeGroup pShapes) {
		this.editorCanvas = new EditorCanvas(this, pShapes, SWT.BORDER, this);
		this.editorCanvas.setGridSpacing(20);
		this.editorCanvas.setGridSnap(true);
		this.editorCanvas.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		this.editorCanvas.addMouseListener(this);
		this.editorCanvas.addMouseMoveListener(this);
		this.editorCanvas.addMouseWheelListener(this);
		this.editorCanvas.addKeyListener(this);
	}

	/**
	 * Tworzy komponenty na pasku narzedziowym edytora.
	 */
	private void createEditorToolbarWidgets() {
		this.btnShowGrid = new Button(this.toolbar, SWT.TOGGLE);
		this.btnShowGrid.setToolTipText("Pokaż siatkę");
		this.btnShowGrid.setBackground(getBackground());
		this.btnShowGrid.setImage(Images.GRID_ICO);
		this.btnShowGrid.setSelection(this.editorCanvas.isGridShow());
		this.btnShowGrid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pE) {
				VisualCodeEditor.this.editorCanvas.setGridShow(btnShowGrid.getSelection());
			}
		});

		this.btnSnapToGrid = new Button(this.toolbar, SWT.TOGGLE);
		this.btnSnapToGrid.setToolTipText("Przyciągaj do siatki");
		this.btnSnapToGrid.setBackground(getBackground());
		this.btnSnapToGrid.setImage(Images.SNAP_TO_GRID_ICO);
		this.btnSnapToGrid.setSelection(this.editorCanvas.isGridSnap());
		this.btnSnapToGrid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent pE) {
				VisualCodeEditor.this.editorCanvas.setGridSnap(btnSnapToGrid.getSelection());
			}
		});
	}

	/**
	 * Ustawia manadzer rozkladu kontrolek na panelu.
	 */
	private void setLayout() {
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
	}

	/**
	 * Informuje wszystkie obiektu nasluchujace na zmiany edytora o zmianie wejscia (mysz, klawiatura).
	 */
	private void fireOnInputChangedEvent() {
		for (EditorListener listener : this.editorListeners) {
			listener.onInputChanged(this.editorCanvas.getInputContext());
		}
	}

	/**
	 * Informuje wszystkie obiektu nasluchujace na zmiany edytora o zmianie zaznaczenia.
	 * 
	 * @param	pSelected zaznaczony ksztalt (<code>null</code> jesli nic nie zaznaczono</code>)
	 */
	private void fireOnSelectionChangedEvent(Shape pSelected) {
		for (EditorListener listener : this.editorListeners) {
			listener.onSelectionChanged(pSelected);
		}
	}

}
