package pl.horaczy.robocode.editor.widget.css;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.properties.AbstractCSSPropertySWTHandler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.css.CSSValue;

import pl.horaczy.robocode.editor.widget.EditorCanvas;

@SuppressWarnings("restriction")
public class CSSGridLinesPropertyHandler extends AbstractCSSPropertySWTHandler {

	@Override
	protected void applyCSSProperty(Control pControl, String pProperty, CSSValue pValue, String pPseudo, CSSEngine pEngine)
			throws Exception {
		Color color = (Color) pEngine.convert(pValue, Color.class, pControl.getDisplay());
		if (pControl instanceof EditorCanvas) {
			EditorCanvas editorCanvas = (EditorCanvas) pControl;
			editorCanvas.setGridLinesColor(color);
		}
	}

	@Override
	protected String retrieveCSSProperty(Control pControl, String pProperty, String pPseudo, CSSEngine pEngine) throws Exception {
		if (pControl instanceof EditorCanvas) {
			EditorCanvas editorCanvas = (EditorCanvas) pControl;
			return pEngine.convert(editorCanvas.getGridLinesColor(), String.class, pControl.getDisplay());
		}
		return null;
	}

}
