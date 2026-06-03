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
	
	public void valider (int lig, int col, int nbVirus, String nomPlateau)
	{
		this.ctrl.creerPlateau(lig, col, nbVirus, nomPlateau);
		this.dispose();
	}
}