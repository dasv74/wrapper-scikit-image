package ch.epfl.skimage.process;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ch.epfl.skimage.SetupPanel;
import ch.epfl.skimage.StatusPanel;
import ch.epfl.skimage.VirtualEnvironmentRunner;
import ch.epfl.skimage.components.HTMLPane;
import ch.epfl.skimage.tools.Log;
import ij.gui.GUI;

@SuppressWarnings("serial")
public abstract class WrapperSkimageAbstract extends JDialog implements ActionListener, Runnable {
	
	protected SetupPanel setup;
	protected StatusPanel status; 
	protected JButton bnRun;
	protected Thread thread;
	protected HTMLPane info = new HTMLPane(500, 50);


	public WrapperSkimageAbstract(String name) {
		super(new JFrame(), "Wrapper skimage");
		this.setup = new SetupPanel(name);
		this.status = new StatusPanel();
		
		bnRun = new JButton("Run");
		
		info.append("h2", this.getTitle());
		info.append("p", getSyntax());
		JScrollPane scroll = new JScrollPane(info);	
		
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.PAGE_AXIS));
		pn.add(scroll, BorderLayout.NORTH);
		pn.add(getParametersPanel(), BorderLayout.CENTER);
		pn.add(setup, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(pn, BorderLayout.NORTH);
		panel.add(Log.getPane(), BorderLayout.CENTER);
		panel.add(status, BorderLayout.SOUTH);
		
		bnRun.addActionListener(this);
		status.bnHelp.addActionListener(this);
		status.bnClose.addActionListener(this);
		this.add(panel);
		this.pack();
		this.setModal(false);
		GUI.center(this);
		this.setVisible(true);
	}
	
	public abstract String getSyntax();
	public abstract JPanel getParametersPanel();
	public abstract void process(VirtualEnvironmentRunner venv);

	public void run() {
        String venvPath = setup.txtVenv.getText();
        VirtualEnvironmentRunner venv = new VirtualEnvironmentRunner(venvPath);

		double chrono = System.nanoTime();
		process(venv);
        String s = String.format("%3.3f ms", ((System.nanoTime() - chrono) * 1e-6));
        status.status.setText(s);
		thread = null;
	}
	
	public JButton getRunButton() {
		return bnRun;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bnRun) {
			if (thread == null) {
				thread = new Thread(this);
				thread.setPriority(Thread.MIN_PRIORITY);
				thread.start();
			}
		}
		
		if (e.getSource() == status.bnClose) {
			dispose();
		}
		
	}
}
