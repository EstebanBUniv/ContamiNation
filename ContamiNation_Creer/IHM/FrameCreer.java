package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import java.io.File;
import java.awt.event.*;
import java.awt.Color;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class FrameCreer extends JFrame
{
	public static final Color COLOR_SELECT     = new Color(70, 150, 160);

	public final int MARGE;

	private JPanel       panel;

	private Controleur   ctrl;

	public FrameCreer(Controleur ctrl) 
	{
		this.setTitle   ("ContamiNation");
		this.setMinimumSize(new Dimension(900, 600));
		this.setLocationRelativeTo(null);

		this.MARGE = (int)(this.getWidth()*0.25);
		
		this.ctrl            = ctrl;
		this.panel           = new PanelSauvegarde(this, this.ctrl);

		this.setLayout(new BorderLayout());

		this.add(this.panel, BorderLayout.CENTER);

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
		this.setLocationRelativeTo(null);
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
		this.setMinimumSize(new Dimension(300,120));
		this.setSize(300, 120);
		this.setLocationRelativeTo(null);
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
}