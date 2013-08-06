package pl.horaczy.robocode.editor.provider;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.swt.graphics.Image;

import pl.horaczy.robocode.editor.model.Function;
import pl.horaczy.robocode.editor.model.Project;
import pl.horaczy.robocode.editor.model.Robot;
import pl.horaczy.robocode.editor.pool.Images;

/**
 * Dostawca etykiet dla projektow.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class ProjectsLabelProvider extends ObservableMapLabelProvider {

	/**
	 * @param	pAttributeMaps lista obserwowalnych atrybutow
	 */
	public ProjectsLabelProvider(IObservableMap<?, ?>... pAttributeMaps) {
		super(pAttributeMaps);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object pElement) {
		if (pElement instanceof Project) {
			return ((Project) pElement).getNazwa();
		} else if (pElement instanceof Robot) {
			return String.format("Robot %s", ((Robot) pElement).getNazwa());
		} else if (pElement instanceof Function) {
			return String.format("funkcja %s", ((Function) pElement).getNazwa());
		}
		return super.getText(pElement);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object pElement) {
		if (pElement instanceof Project) {
			return Images.PROJECT_ICO;
		} else if (pElement instanceof Robot) {
			return Images.ROBOT_ICO;
		} else if (pElement instanceof Function) {
			return Images.CODE_ICO;
		}
		return super.getImage(pElement);
	}

}
