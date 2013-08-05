package pl.horaczy.robocode.editor.widget;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StatusBar {

	private Label statusLabel;

	/**
	 * Tworzy kontrolki.
	 * 
	 * @param	pParent komponent rodzica
	 */
	@PostConstruct
	public void createComposite(Composite pParent) {
		FillLayout layout = new FillLayout();
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		pParent.setLayout(layout);

		this.statusLabel = new Label(pParent, SWT.NONE);
	}

	/**
	 * Obsluga otrzymanego komunikatu statusu.
	 * 
	 * @param	pMessage komunikat statusu
	 */
	@Inject
	@Optional
	void onEventReceived(@UIEventTopic("statusEvent") StatusMessage pMessage) {
		this.statusLabel.setText(pMessage.getMessage());
	}

}
