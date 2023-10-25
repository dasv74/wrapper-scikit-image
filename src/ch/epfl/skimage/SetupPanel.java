package ch.epfl.skimage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.epfl.skimage.components.GridPanel;
import ch.epfl.skimage.tools.Files;
import ch.epfl.skimage.tools.Log;

@SuppressWarnings("serial")
public class SetupPanel extends JPanel implements ActionListener {

	private JButton bnBrowse = new JButton("Browse");
	private JButton bnSelect = new JButton("Browse");	
	public JTextField txtDirectory = new JTextField("",30);
	public JTextField txtVenv = new JTextField("/Users/dsage/mambaforge/envs/napari-tutorial", 30);
	
	public SetupPanel(String name) {

		GridPanel pn = new GridPanel(true);
		pn.place(0, 0, "Virtual environnement");
		pn.place(0, 1, txtVenv);
		pn.place(0, 2, bnBrowse);
		pn.place(1, 0, "Exchange directory");
		pn.place(1, 1, txtDirectory);
		pn.place(1, 2, bnSelect);

		bnBrowse.addActionListener(this);
		bnSelect.addActionListener(this);
		add(pn);
		
		String dir = ExchangeDocuments.getTemporaryDirectory(name);
		ExchangeDocuments.setExchangeDirectory(dir);
		ArrayList<String> log = ExchangeDocuments.cleanExchangeDirectory();
		Log.write(log);
		txtDirectory.setText(dir);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bnSelect) {
			File path = Files.browseDirectory(System.getProperty("user.home"), "Select an exchange folder");
			if (path == null)
				return;
			ExchangeDocuments.setExchangeDirectory(path.getAbsolutePath());
			ArrayList<String> log = ExchangeDocuments.cleanExchangeDirectory();
			Log.write(log);
			txtDirectory.setText(path.getAbsolutePath());
		} 
		
		if (e.getSource() == bnBrowse) {
			File dir = Files.browseDirectory(System.getProperty("user.home"), "Select a virtual environment folder");
			if (dir == null)
				return;
			txtVenv.setText(dir.getAbsolutePath());
		} 
	}
}
