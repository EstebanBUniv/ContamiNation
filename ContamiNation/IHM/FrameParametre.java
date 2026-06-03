package ContamiNation.IHM;

import ContamiNation.Controleur;
import ContamiNation.IHM.*; 
import ContamiNation.Metier.*;

import javax.swing.JFrame;

public class FrameParametre extends JFrame
{
	private Controleur ctrl;
	public FrameParametre(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setTitle("ContamiNation : Paramètres du plateau");
		this.setSize(450, 200);
		this.setLocationRelativeTo(null);

		this.add(new PanelParametre(this));

		this.setVisible(true);
	}
}