package pl.horaczy.robocode.editor.pool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * Pula czcionek.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public final class Fonts {

	/** Czcionka typu monotype. */
	public static final Font MONOTYPE;

	/** Pula. */
	private static final List<Font> POOL;

	static {
		POOL = new ArrayList<Font>();
		POOL.add(MONOTYPE = new Font(Display.getDefault(), "Consolas", 11, SWT.BOLD));
	}

	private Fonts() {
		// nop
	}

	/**
	 * Zwalnia wszystkie czcionki z puli.
	 */
	public static void dispose() {
		for (Font font : POOL) {
			font.dispose();
		}
		POOL.clear();
	}

}
