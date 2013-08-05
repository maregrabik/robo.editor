package pl.horaczy.robocode.editor.pool;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import pl.horaczy.robocode.editor.provider.ProjectsLabelProvider;

/**
 * Pula obrazkow.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public final class Images {

	/** Pula obrazow zaladowanych z istniejacego zasobu. */
	private static final Map<String, Image> POOL_BY_PATH;
	/** Pula obrazow utworzonych dla koloru. */
	private static final Map<Color, Image> POOL_BY_COLOR;

	/** Ikona dla projektu. */
	public static final Image PROJECT_ICO;
	/** Ikona dla robota. */
	public static final Image ROBOT_ICO;
	/** Ikona dla kodu robota. */
	public static final Image CODE_ICO;
	/** Ikona dla siatki. */
	public static final Image GRID_ICO;
	/** Ikona dla przyciagania do siatki. */
	public static final Image SNAP_TO_GRID_ICO;
	/** Ikona dla przesuniecia warstwy jeden poziom nizej. */
	public static final Image LAYER_DOWN_ICO;
	/** Ikona dla przesuniecia warstwy jeden poziom wyzej. */
	public static final Image LAYER_UP_ICO;
	/** Ikona dla zaznaczonego checkboxa. */
	public static final Image CHECKBOX_ON_ICO;
	/** Ikona dla odznaczonego checkboxa. */
	public static final Image CHECKBOX_OFF_ICO;

	static {
		POOL_BY_PATH = new HashMap<>();
		POOL_BY_COLOR = new HashMap<>();
		PROJECT_ICO = create("project.png");
		ROBOT_ICO = create("robot.png");
		CODE_ICO = create("code.png");
		GRID_ICO = create("grid.png");
		SNAP_TO_GRID_ICO = create("snap_to_grid.png");
		LAYER_DOWN_ICO = create("layer_down.png");
		LAYER_UP_ICO = create("layer_up.png");
		CHECKBOX_ON_ICO = create("checkbox_on.png");
		CHECKBOX_OFF_ICO = create("checkbox_off.png");
	}

	private Images() {
		// nop
	}

	/**
	 * Pobiera zadany obraz z puli. Jesli obraz w puli nie istenieje wtedy jest ladowany
	 * i zapamietywany na wypadek kolejnych uzyc.
	 * 
	 * @param	pPath sciezka do obrazu
	 * 
	 * @return	obraz z puli
	 */
	public synchronized static Image create(String pPath) {
		Image image = POOL_BY_PATH.get(pPath);
		if (image == null) {
			image = loadImage(pPath);
			POOL_BY_PATH.put(pPath, image);
		}
		return image;
	}

	/**
	 * Tworzy jednokolorowy obraz o wielkosci 16x16 pikseli w czarnej ramce.
	 * 
	 * @param	pColor kolor obrazu
	 * 
	 * @return	obraz z puli
	 */
	public synchronized static Image create(Color pColor) {
		Image image = POOL_BY_COLOR.get(pColor);
		if (image == null) {
			image = new Image(Display.getDefault(), 12, 12);
			GC gc = new GC(image);
			gc.setForeground(Colors.BLACK);
			gc.setBackground(pColor);
			gc.fillRectangle(1, 1, 10, 10);
			gc.drawRectangle(1, 1, 10, 10);
			gc.dispose();
			POOL_BY_COLOR.put(pColor, image);
		}
		return image;
	}

	/**
	 * Zwalnia wszystkie obrazy z puli.
	 */
	public static void dispose() {
		for (Image image : POOL_BY_PATH.values()) {
			image.dispose();
		}
		for (Image image : POOL_BY_COLOR.values()) {
			image.dispose();
		}
		POOL_BY_PATH.clear();
		POOL_BY_COLOR.clear();
	}

	/**
	 * @param	pFile sciezka do pliku z obrazem wzgledem katalogu <i>icons</i>.
	 * 
	 * @return	zaladowany obraz
	 */
	private static Image loadImage(String pFile) {
		Bundle bundle = FrameworkUtil.getBundle(ProjectsLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + pFile), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();

	}

}
