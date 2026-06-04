package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Metier.Case;
import ContamiNation_Creer.Controleur;

import javax.swing.*;

import java.awt.BorderLayout;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class FrameGrille extends JFrame
{
	private PanelGrille panel;
	private Controleur  ctrl;
	private JTextField  txtNumZone;

	public FrameGrille(Controleur ctrl, int lig, int col)
	{
		this.setTitle   ("ContamiNation");
		this.setSize    (500,500);
		this.setLocation( 20,200); 
		
		/* ------------------------------ */
		/* Création des composants        */
		/* ------------------------------ */
		
		this.ctrl       = ctrl;
		this.panel      = new PanelGrille(lig, col, this.ctrl, true);
		this.txtNumZone = new JTextField(10);
		
		/* ------------------------------ */
		/* Positionnement des Composants  */
		/* ------------------------------ */
		
		this.add(new JLabel("Initialisez les zones : "), BorderLayout.NORTH );
		this.add(panel                                 , BorderLayout.CENTER);
		this.add(this.txtNumZone                       , BorderLayout.WEST  );

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void initBtn (String valeur, int lig, int col)
	{
		this.panel.initBtn(valeur, lig, col);
	}
	
	public void ajouterZone (int lig, int col)
	{
		try
		{
			if ( this.txtNumZone.getText().matches( "[0-9]+" ) && Integer.parseInt(this.txtNumZone.getText()) >= 1)
				this.ctrl.ajouterZone(lig, col, Integer.parseInt(this.txtNumZone.getText()));
		}
		catch (NumberFormatException e)
		{
			System.out.println("Rentrez une valeur valide");
		}
	}

	public Case getCase(int lig, int col)
	{
		return ctrl.getCase(lig, col);
	}

	public void fermer()
	{
		this.dispose();
		this.ctrl.ouvrirSommet();
	}
	
	public PanelGrille getPanel() { return this.panel; }
}
