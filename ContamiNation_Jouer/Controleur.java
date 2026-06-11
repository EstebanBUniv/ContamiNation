package ContamiNation_Jouer;

import ContamiNation_Jouer.IHM.*;
import ContamiNation_Jouer.Metier.*;

import java.awt.Color;

import javax.swing.JPanel;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

public class Controleur
{
	public static final Color COLOR_BACKGROUND = new Color( 58, 111, 134);
	public static final Color COLOR_FOREGROUND = new Color(230, 230, 230);
	public static final Color COLO_EST_SELECT  = new Color( 86, 136, 158);

	private JPanel[][]          tabPanel;
	private FrameJeu            frame;
	private Plateau[]           plateau;
	private Pioche              pioche;
	private FrameChoixCarte     frameChoixCarte;
	private Map<Integer, Color> couleursZones = new HashMap<>();
	private int                 r, g, b;
	private int                 nbJoueurs;
	private boolean             modeDebiche = false;
	private boolean[]           aJoueCeTour;

	// Variables pour le système de sélection
	private boolean estClique = false;
	private Case caseSelectionnee = null;

	public Controleur() 
	{ 
		this.frame = new FrameJeu(this); 
	}
	
	/*------------*/
	/*   getters  */
	/*------------*/
	
	public Plateau getPlateau    (int idJoueur)                   { return this.plateau[idJoueur]                       ; }
	public int     getTailleCase ()                               { return this.frame.getPanelPlateau().getTailleCase() ; }
	public int     getLig        ()                               { return plateau[0].getLig()                          ; }
	public int     getCol        ()                               { return plateau[0].getCol()                          ; }
	public Case    getCase       (int lig, int col, int idJoueur) { return this.plateau[idJoueur].getCase(lig, col)     ; }
	public JPanel  getPanel      (int lig, int col)               { return this.frame.getTabPanel()[lig][col]           ; }
	public Virus   getVirus      (int idJoueur)                   { return this.plateau[idJoueur].getVirusActif()       ; }
	public Map<Integer, Color>   getCouleurZone  ()               { return this.couleursZones                           ; }
	public Case    getCaseSelectionnee()                          { return this.caseSelectionnee                        ; }
	public Carte   getCarte(int indice)                           { return this.pioche.getCarte(indice)                 ; }
	public int     getTaillePioche ()                             { return this.pioche.getTaillePioche()                ; }
	public boolean getModeDebiche  ()                             { return this.modeDebiche                             ; }
	
	public boolean possedeSommet(int lig, int col, int idJoueur) {
		return this.plateau[idJoueur].getCase(lig, col).getSommet() != null;
	}

	public boolean possedeVoisin(int lig, int col, int direction, int idJoueur) {
		return this.plateau[idJoueur].getCase(lig, col).getSommet().getLstVoisin()[direction] != null;
	}

	public int getLigVoisin(int lig, int col, int direction, int idJoueur) {
		return this.plateau[idJoueur].getCase(lig, col).getSommet().getLstVoisin()[direction].getLigSommet();
	}

	public int getColVoisin(int lig, int col, int direction, int idJoueur) {
		return this.plateau[idJoueur].getCase(lig, col).getSommet().getLstVoisin()[direction].getColSommet();
	}

	public int getNbVirus(int idJoueur) {
		return this.plateau[idJoueur].getNbVirus();
	}

	public int getTailleCheminVirus(int idJoueur, int indexVirus) {
		Virus v = this.plateau[idJoueur].getVirus(indexVirus); 
		return (v != null && v.getConquis() != null) ? v.getConquis().size() : 0;
	}

	public Color getCouleurVirus(int idJoueur, int indexVirus) {
		return this.plateau[idJoueur].getVirus(indexVirus).getCouleur();
	}

	public int getLigChemin(int idJoueur, int indexVirus, int indexChemin) {
		return this.plateau[idJoueur].getVirus(indexVirus).getConquis().get(indexChemin).getLigSommet();
	}

	public int getColChemin(int idJoueur, int indexVirus, int indexChemin) {
		return this.plateau[idJoueur].getVirus(indexVirus).getConquis().get(indexChemin).getColSommet();
	}

	public Sommet  getVoisin       (int indice) 
	{
		if (this.caseSelectionnee != null && this.caseSelectionnee.getSommet() != null) 
			{
			return this.caseSelectionnee.getSommet().getVoisin(indice);
			}
		return null;
	}
	
	public Color getCouleurZone(int numZone) 
	{
		if (numZone == 0) return Color.WHITE;
		if (!this.couleursZones.containsKey(numZone)) 
		{
			this.r = (this.r + 67) % 256; this.g = (this.g + 113) % 256; this.b = (this.b + 193) % 256;
			this.couleursZones.put(numZone, new Color(this.r, this.g, this.b));
		}
		return this.couleursZones.get(numZone);
	}

	public void chargerNiveau(File fichier) 
	{
		this.nbJoueurs   = 1;
		this.plateau     = new Plateau[1];
		this.aJoueCeTour = new boolean[1];
		this.plateau[0] = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, this);
		if (this.getModeDebiche()) this.appelerChoixCarte();
	}
	
	public void chargerNiveauMulti(File fichier, int nbJoueurs) 
	{
		this.nbJoueurs   = nbJoueurs;
		this.plateau     = new Plateau[nbJoueurs];
		this.aJoueCeTour = new boolean[nbJoueurs];
		
		for (int i = 0; i < nbJoueurs; i++) 
			this.plateau[i] = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, this);
	}

	public void resetCouleurs() 
	{ 
		this.couleursZones.clear(); 
		this.r = 0; 
		this.g = 0; 
		this.b = 0; 
	}
	
	
	
	public void nouvelleManche() 
	{
		boolean suite = false;
		for (Plateau p : this.plateau) 
		{
			if (p.mancheSuivante()) 
			{
				p.preparerNouvelleManche();
				suite = true;
			}
		}

		if (suite) 
		{
			initierPioche();
			melangerPioche();
			this.frame.nouvelleManche();
			this.frame.reinitierPanelPioche();
			if (this.frame != null) this.frame.repaint();
		} 
		else 
		{
			System.out.println("Fin de tout le jeu. Score J1 : " + this.plateau[0].getPointTotal());
		}
	}

	public void changerCouleurManche(int num)
	{
		this.frame.getPanelPlateau().changerCouleurManche(num);
	}

	public void changerImageBase()
	{
		this.frame.getPanelPlateau().changerImageBase();
	}
	
	public void verifSommet(Case caseAVerif, int idJoueur)
	{
		if (caseAVerif == null || this.aJoueCeTour[idJoueur]) return;
		
		Plateau plateauActif      = this.plateau[idJoueur];
		Virus  v          = plateauActif.getVirusActif();
		Sommet s          = caseAVerif.getSommet();
		Carte carteActive = this.pioche.getCarteTire();

		if (!this.estClique)
		{
			if (s != null && v.getConquis().contains(s) && v.estExtremite(s))
			{
				this.estClique        = true;
				this.caseSelectionnee = caseAVerif;
				this.frame.SommetClique();
			}
			this.frame.repaint();
			return;
		}

		if (plateauActif.estCoupValide(caseAVerif, carteActive) && this.estVoisinAtteignableMulti(caseAVerif, idJoueur))
		{
			int choixForce = 0;
			if (s != null && v.getTailleChemin() > 1 && v.toucheTete(s) && v.toucheQueue(s))
				choixForce = this.frame.demanderChoixBoucle();

			if (plateauActif.verifSommet(caseAVerif, carteActive, choixForce))
			{
				this.aJoueCeTour[idJoueur] = true;
				this.verifierFinDeTourCollectif();
			}
		}

		this.estClique        = false;
		this.caseSelectionnee = null;
		this.frame.SommetClique();
		this.frame.repaint     ();
	}
	
	public boolean estVoisinAtteignableMulti(Case caseAVerif, int idJoueur) 
	{
		if (this.caseSelectionnee == null || caseAVerif == null || caseAVerif.getSommet() == null) return false;
		if (!this.plateau[idJoueur].estCoupValide(caseAVerif, this.pioche.getCarteTire())) return false;

		for (Sommet voisin : this.caseSelectionnee.getSommet().getLstVoisin()) {
			if (voisin == caseAVerif.getSommet()) return true;
		}
		return false;
	}

private void verifierFinDeTourCollectif() 
	{
		for (boolean aJoue : aJoueCeTour) 
			if (!aJoue) return; 
		
		for (int i = 0; i < nbJoueurs; i++) 
			aJoueCeTour[i] = false;
		this.frame.reinitierPanelPioche();
	}
	
	// Méthode de triche
	public void setModeDebiche()      { this.modeDebiche = true; }
	public void appelerChoixCarte()   { this.frameChoixCarte = new FrameChoixCarte(this); }
	
	// Méthode de Pioche
	public void initierPioche ()        { this.pioche = new Pioche()           ; }
	public void melangerPioche()        { this.pioche.melanger()               ; }
	public Carte tirerCarte(int indice) { return this.pioche.tirerCarte(indice); }
	public Carte premiereCarte()        { return this.pioche.premiereCarte()   ; }
	public boolean verifFinManche()     { return this.pioche.verifFinManche()  ; }

	public static void main (String[] args) 
	{ 
		new Controleur(); 
	}
}