package pl.horaczy.robocode.editor.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import pl.horaczy.robocode.editor.model.Model;

/**
 * Implementacja serwisu odpowiedzialnego za zarzadzanie komponentami typu {@link MInputPart}.
 * 
 * @author	MHoraczy
 * @since	0.1.0
 */
public class InputPartService implements pl.horaczy.robocode.editor.service.InputPartService {

	@Inject
	private EPartService partService;

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.service.InputPartService#createInputPart(String, Model)
	 */
	@Override
	public MInputPart createInputPart(String pId, Model pModel) {
		MInputPart inputPart = MBasicFactory.INSTANCE.createInputPart();
		MPart part = this.partService.createPart(pId);
		inputPart.setCloseable(part.isCloseable());
		inputPart.setDirty(part.isDirty());
		inputPart.setOnTop(part.isOnTop());
		inputPart.setToBeRendered(part.isToBeRendered());
		inputPart.setVisible(part.isVisible());
		inputPart.setElementId(part.getElementId());
		inputPart.setLabel(part.getLabel());
		inputPart.setIconURI(part.getIconURI());
		inputPart.setDescription(part.getDescription());
		inputPart.setContainerData(part.getContainerData());
		inputPart.setContributionURI(part.getContributionURI());
		inputPart.setContributorURI(part.getContributorURI());
		inputPart.setInputURI(pModel.getInputURI());
		return inputPart;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.service.InputPartService#activate(MInputPart)
	 */
	@Override
	public void activate(MInputPart pPart) {
		this.partService.activate(pPart);
	}

	@Override
	public MInputPart getInputPart(Model pModel) {
		if (pModel == null) {
			throw new IllegalArgumentException("Model jest wymagany");
		}
		Collection<MInputPart> inputParts = this.partService.getInputParts(pModel.getInputURI());
		return !inputParts.isEmpty() ? inputParts.iterator().next() : null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see		pl.horaczy.robocode.editor.service.InputPartService#getInputParts()
	 */
	@Override
	public Collection<MInputPart> getInputParts() {
		List<MInputPart> inputParts = new ArrayList<>();
		for (MPart part : this.partService.getParts()) {
			if (part instanceof MInputPart) {
				inputParts.add((MInputPart) part);
			}
		}
		return inputParts;
	}

}
