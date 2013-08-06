package pl.horaczy.robocode.editor.part;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pl.horaczy.robocode.editor.model.Function;
import pl.horaczy.robocode.editor.model.FunctionOnHit;
import pl.horaczy.robocode.editor.model.Project;
import pl.horaczy.robocode.editor.model.Robot;
import pl.horaczy.robocode.editor.provider.ProjectsLabelProvider;

/**
 * Widok z lista wszystkich projektow z kodami robotow.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
@SuppressWarnings("restriction")
public class ProjectsView implements ISelectionChangedListener, IDoubleClickListener {

	private TreeViewer treeViewer;
	private List<Project> projects;
	private WritableList<Project> input;
	@Inject
	private ESelectionService selectionService;
	@Inject
	private ECommandService commandService;
	@Inject
	private EHandlerService handlerService;
	private Robot robot1;

	/**
	 * Tworzy niezainicjowany widok projektow.
	 */
	public ProjectsView() {
		this.projects = new ArrayList<Project>();
		this.input = new WritableList<>(projects, Project.class);
	}

	/**
	 * Tworzy kontrolki widoku.
	 * 
	 * @param	pParent komponent rodzica
	 */
	@PostConstruct
	public void createComposite(Composite pParent) {

		pParent.setLayout(new FillLayout());
		addGlobalMouseListener(pParent);
		ObservableListTreeContentProvider contentProvider = createContentProvider();
		ProjectsLabelProvider labelProvider = createLabelProvider(contentProvider);

		this.treeViewer = new TreeViewer(pParent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		this.treeViewer.setSorter(new ViewerSorter());
		this.treeViewer.addSelectionChangedListener(this);
		this.treeViewer.addDoubleClickListener(this);
		this.treeViewer.setContentProvider(contentProvider);
		this.treeViewer.setLabelProvider(labelProvider);
		this.treeViewer.setInput(this.input);

		refreshData();

	}

	private void addGlobalMouseListener(final Composite pParent) {

		pParent.getDisplay().addFilter(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(Event pEvent) {

				if (pEvent.widget.toString().equals("Tree {}") && pEvent.button == 3) {

					Menu popupMenu = new Menu((Control) pEvent.widget);

					MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
					newItem.setText("Nowa instrukcja");

					MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
					refreshItem.setText("Refresh");

					MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
					deleteItem.setText("Delete");

					Menu menuInstrukcji = new Menu(popupMenu);
					newItem.setMenu(menuInstrukcji);

					MenuItem oberwanieItem = new MenuItem(menuInstrukcji, SWT.NONE);
					oberwanieItem.setText("Akcja przy oberwaniu pociskiem");
					oberwanieItem.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent pE) {
							robot1.add(new FunctionOnHit());
							treeViewer.refresh();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent pE) {
							
							
						}
					});

					MenuItem trafienieItem = new MenuItem(menuInstrukcji, SWT.NONE);
					trafienieItem.setText("Akcja przy trafieniu");

					MenuItem skanItem = new MenuItem(menuInstrukcji, SWT.NONE);
					skanItem.setText("Zeskanowanie robota");

					popupMenu.setVisible(true);

				}

			}
		});
	}

	/**
	 * Obsluga zdarzenia wybrania elementu drzewa projektow.
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent pEvent) {
		IStructuredSelection selection = (IStructuredSelection) pEvent.getSelection();
		Object selectedElement = selection.getFirstElement();
		this.selectionService.setSelection(selectedElement);
	}

	/**
	 * Obsluga podwojnego klikniecia na elemencie drzewa projektow.
	 * 
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(DoubleClickEvent)
	 */
	@Override
	public void doubleClick(DoubleClickEvent pEvent) {
		IStructuredSelection selection = (IStructuredSelection) pEvent.getSelection();
		Object selectedElement = selection.getFirstElement();

		if (selectedElement instanceof Function) {
			ParameterizedCommand cmd = this.commandService.createCommand("pl.horaczy.robocode.editor.handler.editCodeCommand", null);
			this.handlerService.executeHandler(cmd);
		}
	}

	/**
	 * Ustawia focus na komponencie drzewa.
	 */
	@Focus
	public void setFocus() {
		this.treeViewer.getTree().setFocus();
	}

	/**
	 * Odswieza liste projektow w widoku.
	 */
	public void refreshData() {
		robot1 = new Robot("Shooter");
		robot1.add(new Function("Pxxx"));
		// Robot robot2 = new Robot("Pathfinder");
		// robot2.add(new Code(1));
		// Robot robot3 = new Robot("Killer");
		// robot3.add(new Code(1));
		// robot3.add(new Code(2));
		// robot3.add(new Code(3));

		Project project1 = new Project("Testowe");
		project1.add(robot1);
		// project1.add(robot2);

		// Project project2 = new Project("Na konkurs");
		// project2.add(robot3);

		this.input.clear();
		this.input.add(project1);
		// this.input.add(project2);
		this.treeViewer.expandAll();
	}

	/**
	 * @return	dostawca tresci
	 */
	private ObservableListTreeContentProvider createContentProvider() {
		ObservableListTreeContentProvider contentProvider = new ObservableListTreeContentProvider(
				new IObservableFactory<Object, IObservable>() {
					@Override
					public IObservable createObservable(Object target) {
						if (target instanceof IObservable) {
							return (IObservable) target;
						} else if (target instanceof Project) {
							return BeansObservables.observeList(target, "robots");
						} else if (target instanceof Robot) {
							return BeansObservables.observeList(target, "codes");
						}
						return null;
					}
				}, new TreeStructureAdvisor() {
					@Override
					public Boolean hasChildren(Object pElement) {
						return pElement instanceof Project || pElement instanceof Robot;
					}
				});
		return contentProvider;
	}

	/**
	 * @param	contentProvider dostawca tresci
	 * 
	 * @return	dostawca etykiet
	 */
	@SuppressWarnings("unchecked")
	private ProjectsLabelProvider createLabelProvider(ObservableListTreeContentProvider contentProvider) {
		IObservableSet<?> knownElements = contentProvider.getKnownElements();
		IObservableMap<?, ?>[] observable = Properties.observeEach(knownElements,
				BeanProperties.values(new String[] { "nazwa", "nazwa", "numer" }));
		ProjectsLabelProvider labelProvider = new ProjectsLabelProvider(observable);
		return labelProvider;
	}

}
