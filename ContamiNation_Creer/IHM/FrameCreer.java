package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import java.awt.BorderLayout;
import javax.swing.*;
import java.io.File;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class FrameCreer extends JFrame
{
	private JPanel       panel;

	private JTextField  txtNumZone;

	private Controleur   ctrl;

	public FrameCreer(Controleur ctrl)
	{
		this.setTitle   ("ContamiNation");
		this.setSize    (800,600);
		this.setLocationRelativeTo(null);
		
		this.ctrl       = ctrl;
		this.panel      = new PanelSauvegarde(this, this.ctrl);
		this.txtNumZone = new JTextField(10);
		
		this.add(panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public JPanel getPanel()
	{
		return this.panel;
	}

	public Controleur getCtrl()
	{
		return this.ctrl;
  }
	public void changerGrille(int lig, int col)
	{
		this.ctrl.changerPanel(lig, col);
		this.setSize(800,600);
	}
	
	public void charger(File fichier)
	{
		this.ctrl.charger(fichier);
		this.changerPanel(panel);
	}

	public void creerPlateau()
	{
		this.changerPanel(new PanelParametre(this));
	}

	public void valider (int lig, int col, int nbVirus, String nomPlateau)
	{
		this.ctrl.creerPlateau(lig, col, nbVirus, nomPlateau);
		this.setSize    (300,300);
	}



	public void ajouterZone (int lig, int col)
	{
		try
		{
			if ( this.txtNumZone.getText().matches( "[0-9]+" ))
				if (Integer.parseInt(this.txtNumZone.getText()) > 0)
					this.ctrl.ajouterZone(lig, col, Integer.parseInt(this.txtNumZone.getText()));
		}
		catch (NumberFormatException e)
		{
			System.out.println("Erreur nombre trop grand");
		}
	}

	public void initBtn (String valeur, int lig, int col)
	{
		if (this.panel instanceof PanelGrille)	
			((PanelGrille)(this.panel)).initBtn(valeur, lig, col);
	}

	public void fermer()
	{
		this.ctrl.ouvrirSommet();
		this.dispose();
	}

	public void changerPanel(JPanel panel)
	{
		this.remove(this.panel);
		this.panel = panel;
		this.add(this.panel);
		this.revalidate();
		this.repaint();
	}

	public void setEstNouveau(boolean val)
	{
		if ( this.panel instanceof PanelGrille )
		{
			((PanelGrille)(this.panel)).setEstNouveau(val);
		}
			
	}

	public void creerVirus(String nom)
	{
		this.ctrl.creerVirus(nom);
	}
	
	public void ajouterPanel()
	{
		this.add(this.txtNumZone, BorderLayout.WEST  );
		this.revalidate();
	}
	
	public void retirerPanel()
	{
		this.remove(this.txtNumZone);
		this.revalidate();
	}
}