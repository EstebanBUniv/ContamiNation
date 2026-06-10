package ContamiNation_Jouer;

import ContamiNation_Jouer.IHM.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ContamiNation_Jouer.Metier.*;


import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Controleur
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/

	public static final Color COLOR_BACKGROUND = new Color( 58, 111, 134); // couleur de fond
	public static final Color COLOR_FOREGROUND = new Color(230, 230, 230); // couleur de texte

	private JPanel[][]          tabPanel;
	private FrameJeu            frame;
	private Plateau             plateau;
	private Pioche              pioche;
  private FrameChoixCarte     frameChoixCarte;
  private Map<Integer, Color> couleursZones = new HashMap<>();
	private int                 r;
	private int                 g;
	private int                 b;
  
  private boolean             modeDebiche = false;


	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/

	public Controleur()
	{
		this.frame = new FrameJeu(this);
	}

	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/

	public Plateau getPlateau()
	{
		return this.plateau;
	}

	public int getTailleCase()
	{
		return this.frame.getPanelPlateau().getTailleCase();
	}

	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/

	// Retourne le nombre de lignes du plateau actuel.
	public int getLig() { return plateau.getLig() ; }
	
	// Retourne le nombre de colonnes du plateau actuel.
	public int getCol() { return plateau.getCol() ; }

	// Retourne l'objet métier Case situé aux coordonnées spécifiées.
	public Case getCase(int lig, int col)
	{
		return this.plateau.getCase(lig, col);
	}

	public void chargerNiveau(File fichier)
	{
		this.plateau = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, this);

		if (this.getModeDebiche())
			this.appelerChoixCarte();
	}

	public JPanel getPanel(int lig, int col)
	{
		this.tabPanel = this.frame.getTabPanel();

		return this.tabPanel[lig][col];
	}

	public Virus getVirus(int index)
	{
		return this.plateau.getVirus(index);
	}
  
  // Retourne la structure de données associant chaque identifiant de zone à sa couleur.
	public Map<Integer, Color> getCouleurZone() { return this.couleursZones; }
	
	// Génère ou récupère la couleur unique associée à un numéro de zone spécifique.
	public Color getCouleurZone(int numZone)
	{
		if (numZone == 0) return Color.WHITE;

		if (!this.couleursZones.containsKey(numZone))
		{
			this.r = (this.r + 67) % 256;
			this.g = (this.g + 113) % 256;
			this.b = (this.b + 193) % 256;
			this.couleursZones.put(numZone, new Color(this.r, this.g, this.b));
		}
		return this.couleursZones.get(numZone);
	}

	// Réinitialise le générateur pseudo-aléatoire servant à colorier les zones à l'écran.
	public void resetCouleurs()
	{
		this.couleursZones.clear();
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}

	public void initierPioche ()
	{
		this.pioche = new Pioche();
	}

	public void melangerPioche()
	{
		this.pioche.melanger();
	}


	public Carte tirerCarte(int indice)
	{
		return this.pioche.tirerCarte(indice);
	}

	public Carte premiereCarte()
	{
		return this.pioche.premiereCarte();
	}


	public boolean verifFinManche()
	{
		return this.pioche.verifFinManche();
	}

	public void nouvelleManche()
	{
		if (this.plateau.mancheSuivante())
		{
			this.plateau.preparerNouvelleManche();
			initierPioche();
			melangerPioche();
			if (this.frame != null) 
			{
				this.frame.repaint();
			}
		}
		
		else
			System.out.println("Fin de tout le jeu" + this.plateau.getPointTotal());
	}
	
	public void verifSommet(Case caseAVerif)
	{
		this.plateau.verifSommet(caseAVerif, this.pioche.getCarteTire());
		//croise pas un autre chemin
		//commence par une extremité
		//pas déjà relié a un sommet contaminé
		//avoir la bonne carte
		this.frame.repaint();
	}


	public void setModeDebiche()
	{
		this.modeDebiche = true;
	}

	public void appelerChoixCarte()
	{
		this.frameChoixCarte = new FrameChoixCarte(this);
	}

	public Carte getCarte(int indice)
	{
		return this.pioche.getCarte(indice);
	}

	public int getTaillePioche()
	{
		return this.pioche.getTaillePioche();
	}

	public boolean getModeDebiche()
	{
		return this.modeDebiche;
	}

  public static void main (String[] args)
	{
		new Controleur();
	}
 }