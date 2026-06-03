package ContamiNation.IHM;

import ContamiNation.Controleur;
import ContamiNation.IHM.*; 
import ContamiNation.Metier.*;

import javax.swing.JFrame;

public class FrameMenu extends JFrame
{
	private Controleur ctrl;

	public FrameMenu(Controleur ctrl)
	{
		this.setTitle("ContamiNation");
		this.setSize(600, 300);
		//this.setLocation(20, 200);
		this.setLocationRelativeTo(null);

		this.ctrl = ctrl;

		this.add(new PanelMenu(ctrl, this));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}