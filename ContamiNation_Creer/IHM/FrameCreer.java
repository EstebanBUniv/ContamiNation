package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class FrameCreer extends JFrame
{
	// Attribut de classe
	public static final Color COLOR_SELECT     = new Color(70, 150, 160);

	// Attribut d'instance
	public final int MARGE;

	private JPanel       panel;

	private Controleur   ctrl;

	public FrameCreer(Controleur ctrl) 
	{
		this.setTitle   ("ContamiNation");
		this.setMinimumSize(new Dimension(900, 600));
		this.setLocationRelativeTo(null);              //Pour faire appraitre la frame au millieu de l'écran.

		this.MARGE           = (int)(this.getWidth()*0.25);
		
		this.ctrl            = ctrl;
		this.panel           = new PanelSauvegarde(this, this.ctrl);

		this.setLayout(new BorderLayout());

		this.add(this.panel, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	// Création des getters

	public JPanel getPanel()
	{
		return this.panel;
	}

	public Controleur getCtrl()
	{
		return this.ctrl;
    }

	// Méthode permettant de changer de panel pour afficher la grille
	public void changerGrille(int lig, int col)
	{
		this.ctrl.changerPanel(lig, col);
		this.setSize(800,600);
		this.setLocationRelativeTo(null);
	}
	
	// Méthode permettant de charger un plateau à l'aide d'un fichier .data
	public void charger(File fichier)
	{
		this.ctrl.charger(fichier);
		this.changerPanel(panel);
	}

	// Méthode qui change de panel pour permettre de rentrer les paramètres du nouveau plateau
	public void creerPlateau()
	{
		this.changerPanel(new PanelParametre(this));
	}

	// Méthode permettant de créer un nouveau plateau
	public void valider (int lig, int col, int nbVirus, String nomPlateau)
	{
		this.ctrl.creerPlateau(lig, col, nbVirus, nomPlateau);
		this.setMinimumSize(new Dimension(1000,500));
		this.setSize(1000, 500);
		this.setLocationRelativeTo(null);
	}

	// Méthode permettant créer les boutons correspondant aux cases du plateau
	public void initBtn (String valeur, int lig, int col)
	{
		if (this.panel instanceof PanelGrille)	
			((PanelGrille)(this.panel)).initBtn(valeur, lig, col);
	}

	// Méthode qui ferme la frame et ouvre une nouvelle frame FrameSommet
	public void fermer()
	{
		this.ctrl.ouvrirSommet();
		this.dispose();
	}

	// Méthode permettant de changer le panel de la frame avec celui rentré en paramètre
	public void changerPanel(JPanel panel)
	{
		this.remove(this.panel);
		this.panel = panel;
		this.add(this.panel);
		this.revalidate();
		this.repaint();
	}

	// Méthode permettant de définir si l'on crée un nouveau plateau
	public void setEstNouveau(boolean val)
	{
		if ( this.panel instanceof PanelGrille )
		{
			((PanelGrille)(this.panel)).setEstNouveau(val);
		}
			
	}

	// Méthode qui créer un nouveau virus avec le nom rentré par l'utilisateur
	public void creerVirus(String nom)
	{
		this.ctrl.creerVirus(nom);
	}
}