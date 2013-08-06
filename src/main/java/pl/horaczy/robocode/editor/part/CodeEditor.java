package pl.horaczy.robocode.editor.part;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import pl.horaczy.robocode.editor.model.Function;
import pl.horaczy.robocode.editor.model.InstructionFor;
import pl.horaczy.robocode.editor.model.InstructionRotate;
import pl.horaczy.robocode.editor.model.InstructionShoot;
import pl.horaczy.robocode.editor.model.InstructionStart;
import pl.horaczy.robocode.editor.model.InstructionStop;
import pl.horaczy.robocode.editor.model.Robot;
import pl.horaczy.robocode.editor.model.Shape;
import pl.horaczy.robocode.editor.model.ShapeGroup;
import pl.horaczy.robocode.editor.widget.InputContext;
import pl.horaczy.robocode.editor.widget.StatusMessage;
import pl.horaczy.robocode.editor.widget.VisualCodeEditor;
import pl.horaczy.robocode.editor.widget.VisualCodeEditor.EditorListener;

/**
 * Wizualny edytor kodu robota.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class CodeEditor implements EditorListener {

	private VisualCodeEditor visualEditor;
	private MInputPart inputPart;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private ESelectionService selectionService;

	/**
	 * Tworzy kontrolki edytora.
	 * 
	 * @param	pParent komponent rodzica
	 */
	@PostConstruct
	public void createComposite(Composite pParent, MInputPart pInputPart, Function pCode) {
		this.inputPart = pInputPart;
		Robot robot = pCode.getRobot();
		this.inputPart.setLabel(String.format("%s, %s", robot.getNazwa(), pCode.getNazwa()));

		ShapeGroup shapeGroup = new ShapeGroup("commands", 0, 0);
		shapeGroup.add(new InstructionStart(20, 20));
		shapeGroup.add(new InstructionFor(20, 80));
		shapeGroup.add(new InstructionRotate(60, 140));
		shapeGroup.add(new InstructionShoot(60, 200));
		shapeGroup.add(new InstructionStop(20, 260));

		pParent.setLayout(new FillLayout());
		this.visualEditor = new VisualCodeEditor(pParent, shapeGroup);
		this.visualEditor.addEditorListener(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.widget.VisualCodeEditor.EditorListener#onInputChanged(InputContext)
	 */
	@Override
	public void onInputChanged(InputContext pInput) {
		this.eventBroker.post("statusEvent", new StatusMessage(pInput.getCurMouse().x + "x" + pInput.getCurMouse().y));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.widget.VisualCodeEditor.EditorListener#onSelectionChanged(Shape)
	 */
	@Override
	public void onSelectionChanged(Shape pSelected) {
		if (pSelected != null) {
			this.selectionService.setSelection(pSelected);
		}
	}

}
