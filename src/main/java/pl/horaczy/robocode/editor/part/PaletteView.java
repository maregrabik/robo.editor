package pl.horaczy.robocode.editor.part;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import pl.horaczy.robocode.editor.model.InstructionTemplate;

/**
 * Widok z lista wszystkich szablonow instrukcji.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class PaletteView {

	private TableViewer tableViewer;
	private WritableList<InstructionTemplate> input;
	@Inject
	private ESelectionService selectionService;

	/**
	 * Tworzy niezainicjowany widok projektow.
	 */
	public PaletteView() {
		this.input = new WritableList<>(new ArrayList<InstructionTemplate>(), InstructionTemplate.class);
	}

	/**
	 * Tworzy kontrolki widoku.
	 * 
	 * @param	pParent komponent rodzica
	 */
	@PostConstruct
	public void createComposite(Composite pParent) {
		pParent.setLayout(new FillLayout());
		this.tableViewer = new TableViewer(pParent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		this.tableViewer.setSorter(new ViewerSorter());
		this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent pEvent) {
				IStructuredSelection selection = (IStructuredSelection) PaletteView.this.tableViewer.getSelection();
				PaletteView.this.selectionService.setSelection(selection.getFirstElement());
			}
		});
		ViewerSupport.bind(this.tableViewer, this.input, BeanProperties.value("nazwa"));

		refreshData();
	}

	/**
	 * Ustawia focus na komponencie tabeli.
	 */
	@Focus
	public void setFocus() {
		this.tableViewer.getTable().setFocus();
	}

	/**
	 * Odswieza liste szablonow w widoku.
	 */
	public void refreshData() {
		// TODO: lista instrukcji
		this.input.clear();
		this.input.add(new InstructionTemplate("Start"));
		this.input.add(new InstructionTemplate("For"));
		this.input.add(new InstructionTemplate("Rotate"));
		this.input.add(new InstructionTemplate("Move"));
		this.input.add(new InstructionTemplate("Shoot"));
		this.input.add(new InstructionTemplate("Stop"));
	}

}
