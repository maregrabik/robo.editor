package pl.horaczy.robocode.editor.handler;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import pl.horaczy.robocode.editor.model.Function;
import pl.horaczy.robocode.editor.service.InputPartService;

/**
 * Obsluga komendy edycji kodu robota.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class EditCodeHandler {

	/**
	 * @param	pActive zaznaczony element
	 * 
	 * @return	<code>true</code> jesli wybrano kod robota
	 */
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) Object pActive) {
		return pActive instanceof Function;
	}

	/**
	 * @param	pApp model aplikacji
	 * @param	pModelService serwis modelu
	 * @param	pPartService serwis widokow
	 * @param	pCode aktywna kod robota
	 */
	@Execute
	public void execute(MApplication pApp, EModelService pModelService, InputPartService pPartService,
			@Named(IServiceConstants.ACTIVE_SELECTION) Function pCode) {

		pApp.getContext().set(Function.class, pCode);
		MInputPart inputPart = pPartService.getInputPart(pCode);
		if (inputPart == null) {
			inputPart = pPartService.createInputPart("pl.horaczy.robocode.editor.part.code.editor", pCode);
			MPartStack partStack = (MPartStack) pModelService.find("pl.horaczy.robocode.editor.dataStack", pApp);
			partStack.getChildren().add(inputPart);
		}
		pPartService.activate(inputPart);
	}
}
