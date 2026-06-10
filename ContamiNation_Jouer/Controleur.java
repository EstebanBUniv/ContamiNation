package ContamiNation_Jouer;

import ContamiNation_Jouer.IHM.*;
import ContamiNation_Jouer.Metier.*;

import javax.swing.JPanel;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Controleur
{
	/*----------------------------*/
	/* Attributs de la classe    */
	/*----------------------------*/

	public static final Color COLOR_BACKGROUND = new Color( 58, 111, 134);
	public static final Color COLOR_FOREGROUND = new Color(230, 230, 230);

	private JPanel[][]          tabPanel;
	private FrameJeu            frame;
	private Plateau             plateau;
	private Pioche              pioche;
	private FrameChoixCarte     frameChoixCarte;
	private Map<Integer, Color> couleursZones    = new HashMap<>();
	private int                 r;
	private int                 g;
	private int                 b;
	private boolean             estClique        = false;
	private boolean             modeDebiche      = false;
	private Case                caseSelectionnee = null;


	/*----------------------------*/
	/* Constructeur de la classe */
	/*----------------------------*/

	public Controleur()
	{
		this.frame = new FrameJeu(this);
	}


	/*----------------------------*/
	/* Getters                   */
	/*----------------------------*/

	public Plateau getPlateau()          { return this.plateau;          }
	public boolean getModeDebiche()      { return this.modeDebiche;      }
	public Case    getCaseSelectionnee() { return this.caseSelectionnee; }

	public int getTailleCase()
	{
		return this.frame.getPanelPlateau().getTailleCase();
	}

	public int getLig() { return this.plateau.getLig(); }
	public int getCol() { return this.plateau.getCol(); }

	public Case getCase(int lig, int col)
	{
		return this.plateau.getCase(lig, col);
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

	public Map<Integer, Color> getCouleurZone() { return this.couleursZones; }

	public Color getCouleurZone(int numZone)
	{
		if (numZone == 0) return Color.WHITE;

		if (!this.couleursZones.containsKey(numZone))
		{
			this.r = (this.r + 67)  % 256;
			this.g = (this.g + 113) % 256;
			this.b = (this.b + 193) % 256;
			this.couleursZones.put(numZone, new Color(this.r, this.g, this.b));
		}
		return this.couleursZones.get(numZone);
	}

	public Carte   tirerCarte(int indice) { return this.pioche.tirerCarte(indice); }
	public Carte   premiereCarte()        { return this.pioche.premiereCarte();     }
	public boolean verifFinManche()       { return this.pioche.verifFinManche();    }
	public Carte   getCarte(int indice)   { return this.pioche.getCarte(indice);    }
	public int     getTaillePioche()      { return this.pioche.getTaillePioche();   }


	/*----------------------------*/
	/* Méthodes                  */
	/*----------------------------*/

	public void chargerNiveau(File fichier)
	{
		this.plateau = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, this);

		if (this.getModeDebiche())
			this.appelerChoixCarte();
	}

	public void resetCouleurs()
	{
		this.couleursZones.clear();
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}

	public void initierPioche()
	{
		this.pioche = new Pioche();
	}

	public void melangerPioche()
	{
		this.pioche.melanger();
	}

	public void nouvelleManche()
	{
		if (this.plateau.mancheSuivante())
		{
			this.plateau.preparerNouvelleManche();
			initierPioche();
			melangerPioche();
			this.frame.reinitierPanelPioche();

			if (this.frame != null)
				this.frame.repaint();
		}
		else
		{
			System.out.println("Fin de tout le jeu " + this.plateau.getPointTotal());
		}
	}

	public void verifSommet(Case caseAVerif)
	{
		// 1. On récupère dynamiquement le virus de la manche actuelle
		int indexManche = this.plateau.getNumManche() - 1;
		Virus v = this.getVirus(indexManche);
		
		Sommet s = caseAVerif.getSommet();
		Carte carteActive = this.pioche.getCarteTire();

		// Deuxième clic : tentative de propagation depuis l'extrémité sélectionnée
		if (this.estClique)
		{
			// On s'assure d'abord que le coup respecte les règles générales ET qu'il est bien voisin de notre sélection
			if (this.plateau.estCoupValide(caseAVerif, carteActive) && this.estVoisinAtteignable(caseAVerif))
			{
				int choixForce = 0;

				// Détection de boucle fermée : pop-up IHM uniquement si le coup est légal
				if (s != null && v.getTailleChemin() > 1 && v.toucheTete(s) && v.toucheQueue(s))
				{
					choixForce = this.frame.demanderChoixBoucle();
				}

				// Validation finale et ajout au chemin
				if (this.plateau.verifSommet(caseAVerif, carteActive, choixForce))
				{
					this.frame.reinitierPanelPioche();
				}
			}
			
			// Qu'il y ait eu contamination ou erreur, on libère la sélection après le 2e clic
			this.estClique = false;
			this.caseSelectionnee = null;
			this.frame.SommetClique(this.estClique, null);
		}
		// Premier clic : sélection d'une extrémité libre du virus
		else
		{
			if (s != null && s.getContamine() && v.estExtremite(s))
			{
				this.estClique = true;
				this.caseSelectionnee = caseAVerif;
				this.frame.SommetClique(this.estClique, caseAVerif); // Met en valeur visuellement
			}
		}

		this.frame.repaint();
	}

	// Vérifie si une case est un voisin atteignable selon les règles métier
	public boolean estVoisinAtteignable(Case caseAVerif)
	{
		if (this.caseSelectionnee == null || caseAVerif.getSommet() == null)
			return false;

		// Le sommet est déjà dans le chemin du virus : interdit
		if (this.plateau.getVirusActif().getConquis().contains(caseAVerif.getSommet()))
			return false;

		// La carte tirée ne correspond pas au symbole : interdit
		Carte carteTiree = this.pioche.getCarteTire();
		if (carteTiree == null)
			return false;

		if (!carteTiree.getSymbole().equals(caseAVerif.getSommet().getSymbole()) &&
			!carteTiree.getSymbole().equals("Epidemie"))
			return false;

		// On vérifie uniquement les voisins du sommet sélectionné
		Sommet sommetSelectionne = this.caseSelectionnee.getSommet();
		for (Sommet voisin : sommetSelectionne.getLstVoisin())
		{
			if (voisin == caseAVerif.getSommet())
				return true;
		}
		return false;
	}

	public void setModeDebiche()
	{
		this.modeDebiche = true;
	}

	public void appelerChoixCarte()
	{
		this.frameChoixCarte = new FrameChoixCarte(this);
	}

	public static void main(String[] args)
	{
		new Controleur();
	}
}