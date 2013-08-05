package pl.horaczy.robocode.editor.service.impl;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

/**
 * Tworzy instancje serwisu {@link InputPartService} na potrzeby DI.
 * 
 * @author MHoraczy
 * @since 0.1.0
 */
public class InputPartServiceCreationFunction extends ContextFunction {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.e4.core.contexts.ContextFunction#compute(IEclipseContext, String)
	 */
	@Override
	public InputPartService compute(IEclipseContext pContext, String pContextKey) {
		return ContextInjectionFactory.make(InputPartService.class, pContext);
	}

}
