package pl.horaczy.robocode.editor.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import pl.horaczy.robocode.editor.model.Model;
import pl.horaczy.robocode.editor.model.annotation.ModelUtil;
import pl.horaczy.robocode.editor.model.annotation.PropertyDescription;
import pl.horaczy.robocode.editor.pool.Colors;
import pl.horaczy.robocode.editor.pool.Images;
import pl.horaczy.robocode.editor.widget.SpinnerCellEditor;

/**
 * Widok z lista wszystkich wlasciwosci zaznaczonego modelu.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class WlasciwosciView implements PropertyChangeListener {

	private class ModelEditingSupport extends EditingSupport {

		private final Class<?>[] supportedTypes = new Class<?>[] { String.class, Color.class, int.class, boolean.class };

		private TableViewer viewer;

		public ModelEditingSupport(TableViewer pViewer) {
			super(pViewer);
			this.viewer = pViewer;
		}

		@Override
		protected CellEditor getCellEditor(Object pElement) {
			PropertyDescription pd = (PropertyDescription) pElement;
			Class<?> valueType = pd.getValueType();
			if (String.class.isAssignableFrom(valueType)) {
				return new TextCellEditor(this.viewer.getTable());
			} else if (int.class.isAssignableFrom(valueType)) {
				return new SpinnerCellEditor(this.viewer.getTable());
			} else if (Color.class.isAssignableFrom(valueType)) {
				return new ColorCellEditor(this.viewer.getTable());
			} else if (boolean.class.isAssignableFrom(valueType)) {
				return new CheckboxCellEditor(this.viewer.getTable());
			}

			return null;
		}

		@Override
		protected boolean canEdit(Object pElement) {
			PropertyDescription pd = (PropertyDescription) pElement;
			Class<?> valueType = pd.getValueType();
			for (Class<?> c : supportedTypes) {
				if (c.isAssignableFrom(valueType)) {
					return true;
				}
			}
			return false;
		}

		@Override
		protected Object getValue(Object pElement) {
			PropertyDescription pd = (PropertyDescription) pElement;
			return convertValue(pd.getValue());
		}

		@Override
		protected void setValue(Object pElement, Object pValue) {
			PropertyDescription pd = (PropertyDescription) pElement;
			pd.setValue(convertValue(pValue));
		}

		/**
		 * Konwertuje wartosci w celu dopasowania do wartosci obslugiwanych przez edytor.
		 * 
		 * @param	pValue wartosc do skonwertowania (np. {@link Color})
		 * 
		 * @return	skonwertowana wartosc (np. {@link RGB})
		 */
		private Object convertValue(Object pValue) {
			if (pValue instanceof Color) {
				return ((Color) pValue).getRGB();
			} else if (pValue instanceof RGB) {
				RGB rgb = (RGB) pValue;
				return Colors.create(rgb.red, rgb.green, rgb.blue);
			}

			return pValue;
		}

	}

	private TableViewer tableViewer;
	private Model model;
	private WritableList<PropertyDescription> input;

	public WlasciwosciView() {
		this.input = new WritableList<>(new ArrayList<PropertyDescription>(), PropertyDescription.class);
	}

	/**
	 * Tworzy kontrolki widoku.
	 * 
	 * @param	pParent komponent rodzica
	 */
	@PostConstruct
	public void createComposite(Composite pParent) {
		TableColumnLayout layout = new TableColumnLayout();
		pParent.setLayout(layout);
		this.tableViewer = new TableViewer(pParent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		this.tableViewer.setContentProvider(new ObservableListContentProvider());
		this.tableViewer.setInput(this.input);
		this.tableViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer pViewer, Object pE1, Object pE2) {
				PropertyDescription pd1 = (PropertyDescription) pE1;
				PropertyDescription pd2 = (PropertyDescription) pE2;
				return pd1.compareTo(pd2);
			}
		});

		TableViewerColumn columnName = new TableViewerColumn(this.tableViewer, SWT.NONE);
		columnName.getColumn().setText("Nazwa");
		columnName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object pElement) {
				PropertyDescription p = (PropertyDescription) pElement;
				return p.getDescription();
			}

			@Override
			public String getToolTipText(Object pElement) {
				PropertyDescription p = (PropertyDescription) pElement;
				return p.getDescription();
			}
		});
		layout.setColumnData(columnName.getColumn(), new ColumnWeightData(30, 110, false));

		TableViewerColumn columnValue = new TableViewerColumn(this.tableViewer, SWT.NONE);
		columnValue.getColumn().setText("Wartosc");
		columnValue.setEditingSupport(new ModelEditingSupport(this.tableViewer));
		columnValue.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object pElement) {
				PropertyDescription p = (PropertyDescription) pElement;
				if (p.getValue() instanceof Color) {
					Color color = (Color) p.getValue();
					return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
				} else if (p.getValue() instanceof Boolean) {
					return (Boolean) p.getValue() ? "Tak" : "Nie";
				}
				return String.valueOf(p.getValue());
			}

			@Override
			public Image getImage(Object pElement) {
				PropertyDescription p = (PropertyDescription) pElement;
				if (p.getValue() instanceof Color) {
					return Images.create((Color) p.getValue());
				} else if (p.getValue() instanceof Boolean) {
					return ((Boolean) p.getValue()) ? Images.CHECKBOX_ON_ICO : Images.CHECKBOX_OFF_ICO;
				}
				return super.getImage(pElement);
			}
		});
		layout.setColumnData(columnValue.getColumn(), new ColumnWeightData(70, 100, false));

		Table table = this.tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	/**
	 * Ustawia focus na komponencie tabeli.
	 */
	@Focus
	public void setFocus() {
		this.tableViewer.getTable().setFocus();
	}

	/**
	 * Obsluga zaznaczenia modelu w IDE.
	 * 
	 * @param	pModel wybrany model
	 */
	@Inject
	public void setModel(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Model pModel) {
		clearInput();
		if (this.model != null) {
			this.model.removePropertyChangeListener(this);
			this.model = null;
		}
		this.model = pModel;
		if (this.model != null) {
			this.input.addAll(ModelUtil.scan(this.model));
			this.model.addPropertyChangeListener(this);
		}
	}

	/**
	 * @param	pEvent zdarzenie zmiany wartosci wlasciwosci
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pEvent) {
		if (this.tableViewer != null && !this.tableViewer.getTable().isDisposed()) {
			this.tableViewer.refresh();
		}
	}

	/**
	 * Czysci liste wlascowosci.
	 */
	private void clearInput() {
		for (PropertyDescription pd : this.input) {
			pd.dispose();
		}
		this.input.clear();
	}

}
