package pl.horaczy.robocode.editor.widget.css;

import org.eclipse.e4.ui.css.core.dom.IElementProvider;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.w3c.dom.Element;

import pl.horaczy.robocode.editor.widget.EditorCanvas;

@SuppressWarnings("restriction")
public class EditorCanvasSWTElementProvider implements IElementProvider {

	@Override
	public Element getElement(Object pElement, CSSEngine pEngine) {
		if (pElement instanceof EditorCanvas) {
			return new EditorCanvasElement((EditorCanvas) pElement, pEngine);
		}
		return null;
	}

}
