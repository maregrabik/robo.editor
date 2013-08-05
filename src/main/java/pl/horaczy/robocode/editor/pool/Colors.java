package pl.horaczy.robocode.editor.pool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Pula kolorow.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public final class Colors {

	/** Kolor bialy. */
	public static final Color WHITE;
	/** Kolor carny. */
	public static final Color BLACK;
	/** Kolor zolty - jasny. */
	public static final Color YELLOW_L;
	/** Kolor zolty - ciemny. */
	public static final Color YELLOW_D;
	/** Kolor fioletowy - jasny. */
	public static final Color PURPLE_L;
	/** Kolor fioletowy - ciemny. */
	public static final Color PURPLE_D;
	/** Kolor czerwony - jasny. */
	public static final Color RED_L;
	/** Kolor czerwony - ciemny. */
	public static final Color RED_D;
	/** Kolor zielony - jasny. */
	public static final Color GREEN_L;
	/** Kolor zielony - ciemny. */
	public static final Color GREEN_D;
	/** Kolor niebieski - jasny. */
	public static final Color BLUE_L;
	/** Kolor niebieski - ciemny. */
	public static final Color BLUE_D;

	/** Domyslna paleta kolorow. */
	public static final Color[] PALETTE;

	/** Pula. */
	private static final List<Color> POOL;

	static {
		POOL = new ArrayList<Color>();
		WHITE = create(0xFF, 0xFF, 0xFF);
		BLACK = create(0x00, 0x00, 0x00);
		YELLOW_L = create(0xFF, 0xBB, 0x33);
		YELLOW_D = create(0xFF, 0x88, 0x00);
		PURPLE_L = create(0xAA, 0x66, 0xCC);
		PURPLE_D = create(0x99, 0x33, 0xCC);
		RED_L = create(0xFF, 0x44, 0x44);
		RED_D = create(0xCC, 0x00, 0x00);
		GREEN_L = create(0x99, 0xCC, 0x00);
		GREEN_D = create(0x66, 0x99, 0x00);
		BLUE_L = create(0x33, 0xB5, 0xE5);
		BLUE_D = create(0x00, 0x99, 0xCC);
		PALETTE = new Color[] { YELLOW_L, YELLOW_D, PURPLE_L, PURPLE_D, RED_L, RED_D, GREEN_L, GREEN_D, BLUE_L, BLUE_D };
	}

	private Colors() {
		// nop
	}

	/**
	 * Pobiera zadany kolor z puli. Jesli kolor w puli nie istenieje wtedy jest tworzony
	 * i zapamietywany na wypadek kolejnych uzyc.
	 * 
	 * @param	pRed czerwona skladowa koloru
	 * @param	pGreen zielona skladowa koloru
	 * @param	pBlue niebieska skladowa koloru
	 * 
	 * @return	kolor z puli
	 */
	public synchronized static Color create(int pRed, int pGreen, int pBlue) {
		for (Color color : POOL) {
			if (color.getRed() == pRed && color.getGreen() == pGreen && color.getBlue() == pBlue) {
				return color;
			}
		}

		Color color = new Color(Display.getDefault(), pRed, pGreen, pBlue);
		POOL.add(color);
		return color;
	}

	/**
	 * @return	losowy kolor z palety {@link #PALETTE}
	 */
	public static Color random() {
		return PALETTE[(int) (Math.random() * PALETTE.length)];
	}

	/**
	 * Zwalnia wszystkie kolory z puli.
	 */
	public static void dispose() {
		for (Color color : POOL) {
			color.dispose();
		}
		POOL.clear();
	}

}
