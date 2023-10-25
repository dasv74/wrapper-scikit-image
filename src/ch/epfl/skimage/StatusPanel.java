package ch.epfl.skimage;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class StatusPanel extends JToolBar {

	public JButton bnClose = new JButton("Close");
	public JButton bnHelp = new JButton("Help");
	public JLabel status = new JLabel("(c) 2023. Biomedical Image Group");
	
	public StatusPanel() {
		super();
		setFloatable(false);
		status.setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BorderLayout());
		add(bnHelp, BorderLayout.WEST);
		add(status, BorderLayout.CENTER);
		add(bnClose, BorderLayout.EAST);
	}
}
