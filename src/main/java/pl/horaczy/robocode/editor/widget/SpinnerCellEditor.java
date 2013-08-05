package pl.horaczy.robocode.editor.widget;


import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

/**
 * Edytor komorek tabelki z wykorzystaniem komponentu {@link Spinner}.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class SpinnerCellEditor extends CellEditor {

	/** Kontrolka edycji wartosci liczbowych. */
	private Spinner spinner;

	/**
	 * @param	pParent kontrolka rodzica
	 */
	public SpinnerCellEditor(Composite pParent) {
		this(pParent, SWT.NONE);
	}

	/**
	 * @param	pParent kontrolka rodzica
	 * @param	pStyle styl
	 */
	public SpinnerCellEditor(Composite pParent, int pStyle) {
		super(pParent, pStyle);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.jface.viewers.CellEditor#createControl(Composite)
	 */
	@Override
	protected Control createControl(Composite pParent) {
		this.spinner = new Spinner(pParent, getStyle());
		this.spinner.setValues(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, 10);
		this.spinner.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent pE) {
				keyReleaseOccured(pE);
			}
		});
		return this.spinner;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		return this.spinner.getSelection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		 org.eclipse.jface.viewers.CellEditor#doSetValue(Object)
	 */
	@Override
	protected void doSetValue(Object pValue) {
		Assert.isTrue(this.spinner != null);
		this.spinner.setSelection((int) pValue);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		 org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		if (this.spinner != null) {
			this.spinner.setFocus();
		}
	}

}
