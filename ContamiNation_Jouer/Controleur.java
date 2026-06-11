package ContamiNation_Jouer;

import ContamiNation_Jouer.IHM.*;
import ContamiNation_Jouer.Metier.*;

import java.awt.Color;

import javax.swing.JPanel;

import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

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
	private boolean             fin         = false;
	private ServeurJeu          serveurJeu;
	private ClientJoueur        clientJoueur; 

	// Variables pour le système de sélection
	private boolean estClique        = false;
	private Case    caseSelectionnee = null;

	public Controleur() 
	{ 
		this.frame = new FrameJeu(this); 
	}
	
	/*------------*/
	/*   getters  */
	/*------------*/
	
	public Plateau getPlateau    (int idJoueur)                   { return this.plateau[idJoueur]                        ; }
	public int     getTailleCase ()                               { return this.frame.getPanelPlateau(0).getTailleCase() ; }
	public int     getLig        ()                               { return this.plateau[0].getLig()                      ; }
	public int     getCol        ()                               { return this.plateau[0].getCol()                      ; }
	public Case    getCase       (int lig, int col, int idJoueur) { return this.plateau[idJoueur].getCase(lig, col)      ; }
	public JPanel  getPanel      (int lig, int col, int idJoueur) { return this.frame.getTabPanel(idJoueur)[lig][col]    ; }
	public Virus   getVirus      (int idJoueur)                   { return this.plateau[idJoueur].getVirusActif()        ; }
	public Map<Integer, Color>   getCouleurZone  ()               { return this.couleursZones                            ; }
	public Case    getCaseSelectionnee()                          { return this.caseSelectionnee                         ; }
	public Carte   getCarte(int indice)                           { return this.pioche.getCarte(indice)                  ; }
	public int     getTaillePioche ()                             { return this.pioche.getTaillePioche()                 ; }
	public boolean getModeDebiche  ()                             { return this.modeDebiche                              ; }
	
	public boolean possedeSommet(int lig, int col, int idJoueur) 
	{
		return this.plateau[idJoueur].getCase(lig, col).getSommet() != null;
	}

	public boolean possedeVoisin(int lig, int col, int direction, int idJoueur) 
	{
		return this.plateau[idJoueur].getCase(lig, col).getSommet().getLstVoisin()[direction] != null;
	}

	public int getLigVoisin(int lig, int col, int direction, int idJoueur)
	{
		return this.plateau[idJoueur].getCase(lig, col).getSommet().getLstVoisin()[direction].getLigSommet();
	}

	public int getColVoisin(int lig, int col, int direction, int idJoueur) 
	{
		return this.plateau[idJoueur].getCase(lig, col).getSommet().getLstVoisin()[direction].getColSommet();
	}

	public int getNbVirus(int idJoueur) 
	{
		return this.plateau[idJoueur].getNbVirus();
	}

	public int getTailleCheminVirus(int idJoueur, int indexVirus) 
	{
		Virus v = this.plateau[idJoueur].getVirus(indexVirus); 
		return (v != null && v.getConquis() != null) ? v.getConquis().size() : 0;
	}

	public Color getCouleurVirus(int idJoueur, int indexVirus) 
	{
		return this.plateau[idJoueur].getVirus(indexVirus).getCouleur();
	}

	public int getLigChemin(int idJoueur, int indexVirus, int indexChemin) 
	{
		return this.plateau[idJoueur].getVirus(indexVirus).getConquis().get(indexChemin).getLigSommet();
	}

	public int getColChemin(int idJoueur, int indexVirus, int indexChemin) 
	{
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
		this.plateau[0]  = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, 0, this);
		
		this.frame.afficherPlateauMulti(1);
		
		this.initierPioche();
		this.melangerPioche();
		
		this.attribuerVirusDepart();
		
		for (int i = 0; i < nbJoueurs; i++)
		{
			int indexVirusActif = (this.plateau[i].getOffsetVirus()) % this.plateau[i].getNbVirus();
			this.frame.getPanelPlateau(i).changerCouleurManche(indexVirusActif);
		}

		this.frame.reinitierPanelPioche();
		if (this.getModeDebiche()) this.appelerChoixCarte();
	}
	
	public void chargerNiveauMulti(File fichier, int nbJoueurs) 
	{
		this.nbJoueurs   = nbJoueurs;
		this.plateau     = new Plateau[nbJoueurs];
		this.aJoueCeTour = new boolean[nbJoueurs];
		
		for (int i = 0; i < nbJoueurs; i++) 
			this.plateau[i] = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, i, this);
		
		this.initierPioche();
		this.melangerPioche();
		
		this.attribuerVirusDepart();
		
		this.frame.reinitierPanelPioche();
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
			for(int lig = 0; lig < this.plateau.length; lig++)
				System.out.println("Fin de tout le jeu. Score J" + (lig+1) + ": " + this.plateau[lig].getPointTotal());
			this.partieTerminee(true);
		}
	}

	public void changerCouleurManche(int num, int idJoueur)
	{
		this.frame.getPanelPlateau(idJoueur).changerCouleurManche(num);
	}

	public void changerImageBase(int idJoueur)
	{
		this.frame.getPanelPlateau(idJoueur).changerImageBase();
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
	public void initierPioche ()        { this.pioche = new Pioche(this.getSymbole())           ; }
	public void melangerPioche()        { this.pioche.melanger()               ; }


	public void tirerCarte(int indice) 
	{ 
		this.pioche.setCarteTiree(indice);
		if (this.frameChoixCarte != null) 
		{
			this.frameChoixCarte.dispose();
			this.frameChoixCarte = null;
			this.frameChoixCarte = new FrameChoixCarte(this);
		}
		if (this.frame != null) this.frame.carteChoisie();

	}

	public Carte getCarteTiree() { return this.pioche.getCarteTire(); }
	public Carte premiereCarte()        { return this.pioche.premiereCarte()   ; }
	public boolean verifFinManche()     { return this.pioche.verifFinManche()  ; }
	
	public void forcerPassageTourCollectif() 
	{
		if (this.aJoueCeTour == null) return;

		for (int i = 0; i < this.nbJoueurs; i++) 
		{
			this.aJoueCeTour[i] = false;
		}

		if (this.frame != null) 
		{
			this.frame.reinitierPanelPioche();
			this.frame.repaint();
		}
	}

	public static void main (String[] args) 
	{ 
		new Controleur(); 
	}
	
	public String[] getSymbole()
	{
		List<String> symboles = new ArrayList<>();
		for (int lig = 0; lig < this.getLig(); lig++ )
			for (int col = 0; col < this.getCol(); col++)
				if ( this.plateau[0].getCase(lig, col).getAUnSommet() && !symboles.contains(this.plateau[0].getCase(lig, col).getSymbole()))
						symboles.add(this.plateau[0].getCase(lig, col).getSymbole());
		
		symboles.add("Epidemie");
		return symboles.toArray(new String[0]);
	}
	
	public void attribuerVirusDepart() 
	{
		if (this.plateau == null || this.plateau.length == 0 || this.plateau[0] == null) return;

		int nbVirusSurCarte = this.plateau[0].getNbVirus();
		java.util.List<Integer> chapeauNumeros = new java.util.ArrayList<>();
		
		for (int i = 0; i < nbVirusSurCarte; i++) 
		{
			chapeauNumeros.add(i);
		}

		java.util.Collections.shuffle(chapeauNumeros);

		for (int i = 0; i < this.nbJoueurs; i++) 
		{
			int numeroTire = chapeauNumeros.get(i % chapeauNumeros.size());
			this.plateau[i].setIndexVirusActif(numeroTire);
		}
	}

	public void partieTerminee(boolean fin)
	{
		this.fin = true;
	}

	public boolean getFin()
	{
		return this.fin;
	}

	public void lancerServeur(int ip) 
	{
	new Thread(() -> {
		this.serveurJeu = new ServeurJeu(this, ip);
	}).start();
	}

	public void lancerClient(int ip)
	{
		this.clientJoueur = new ClientJoueur(this, ip);
	}

}