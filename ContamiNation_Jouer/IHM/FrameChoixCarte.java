package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;
import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/


public class FrameChoixCarte extends JFrame
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private Controleur           ctrl;

	private PanelChoixCarte      panelChoixCarte;

	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public FrameChoixCarte(Controleur ctrl)
	{
		this.ctrl    = ctrl;

		this.setTitle("ContamiNation - Choix de la carte");
		this.setSize(800, 200);
		this.setLocation(100, 100);

		this.panelChoixCarte = new PanelChoixCarte(this.ctrl);


		this.add(this.panelChoixCarte);

		this.setVisible(true);


	}
}