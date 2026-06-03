package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.IHM.*; 
import ContamiNation_Creer.Metier.*;

import javax.swing.JFrame;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

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

		this.add(new PanelMenu(this.ctrl, this));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}