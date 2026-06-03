package ContamiNation_Creer.IHM;

import ContamiNation_Creer.IHM.FrameParametre;
import ContamiNation_Creer.Controleur;
import javax.swing.*;
import java.io.File;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class FramePlateau extends JFrame
{
	private PanelPlateau panel;
	private Controleur   ctrl;

	public FramePlateau(Controleur ctrl)
	{
		this.setTitle   ("ContamiNation");
		this.setSize    (500,500);
		this.setLocationRelativeTo(null);
		//this.setLocation( 20,200); 
		
		this.ctrl  = ctrl;
		this.panel = new PanelPlateau(this);
		
		this.add(panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void charger(File fichier)
	{
		this.ctrl.charger(fichier);
		this.dispose();
	}

	public void creerPlateau()
	{
		new FrameParametre(ctrl);
		this.dispose();
	}
}
