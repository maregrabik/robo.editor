package pl.horaczy.robocode.editor.widget.css;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.ControlElement;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import pl.horaczy.robocode.editor.widget.EditorCanvas;

@SuppressWarnings("restriction")
public class EditorCanvasElement extends ControlElement {

	private boolean dynamicEnabled = Boolean.getBoolean("org.eclipse.e4.ui.css.dynamic");

	private ModifyListener modifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			doApplyStyles();
		}
	};

	public EditorCanvasElement(EditorCanvas canvas, CSSEngine engine) {
		super(canvas, engine);
	}

	public void initialize() {
		super.initialize();

		if (!this.dynamicEnabled) {
			return;
		}

		EditorCanvas editorCanvas = getEditorCanvas();
		editorCanvas.addModifyListener(this.modifyListener);
	}

	protected EditorCanvas getEditorCanvas() {
		return (EditorCanvas) getNativeWidget();
	}

}
