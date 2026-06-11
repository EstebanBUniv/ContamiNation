package ContamiNation_Jouer;

import ContamiNation_Jouer.IHM.*;
import ContamiNation_Jouer.Metier.*;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.Timer;

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
	private boolean             modeMulti;

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
	
	public Plateau getPlateau    (int idJoueur)                   { return this.plateau[idJoueur]                       ; }
	public int     getTailleCase ()                               { return this.frame.getPanelPlateau().getTailleCase() ; }
	public int     getLig        ()                               { return this.plateau[0].getLig()                     ; }
	public int     getCol        ()                               { return this.plateau[0].getCol()                     ; }
	public Case    getCase       (int lig, int col, int idJoueur) { return this.plateau[idJoueur].getCase(lig, col)     ; }
	public JPanel  getPanel      (int lig, int col)               { return this.frame.getTabPanel()[lig][col]           ; }
	public Virus   getVirus      (int idJoueur)                   { return this.plateau[idJoueur].getVirusActif()       ; }
	public Map<Integer, Color>   getCouleurZone  ()               { return this.couleursZones                           ; }
	public Case    getCaseSelectionnee()                          { return this.caseSelectionnee                        ; }
	public Carte   getCarte(int indice)                           { return this.pioche.getCarte(indice)                 ; }
	public int     getTaillePioche ()                             { return this.pioche.getTaillePioche()                ; }
	public boolean getModeDebiche  ()                             { return this.modeDebiche                             ; }
	public boolean getModeMulti    ()                             {return  this.modeMulti                               ; }
	public int     getNbJoueur     ()                             {return  this.nbJoueurs                               ; }
	
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
		this.plateau[0] = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, this);
		
		this.initierPioche();
		this.melangerPioche();
		
		this.attribuerVirusDepart();
		
		this.frame.reinitierPanelPioche();
		if (this.getModeDebiche()) this.appelerChoixCarte();
	}
	
	public void chargerNiveauMulti(File fichier, int nbJoueurs) 
	{
		this.nbJoueurs   = nbJoueurs;
		this.plateau     = new Plateau[nbJoueurs];
		this.aJoueCeTour = new boolean[nbJoueurs];
		this.modeMulti   = true;
		
		for (int i = 0; i < nbJoueurs; i++) 
			this.plateau[i] = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, this);
		
		this.initierPioche();
		this.melangerPioche();
		this.tirerCarte(0);
		
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
				this.changerPlateau((idJoueur + 1) % this.nbJoueurs);
				this.frame.incrNbPasse();
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
		// On vérifie si au moins un joueur n'a pas encore agi
		for (boolean aJoue : aJoueCeTour) 
			if (!aJoue) return; 
		
		// Si tout le monde a fini son action (coup valide ou passe) :
		for (int i = 0; i < nbJoueurs; i++) 
			aJoueCeTour[i] = false; // Réinitialisation pour le nouveau tour de table
		
		// pour qu'il puisse jouer avec la nouvelle carte qui va être piochée
		if (this.frame != null)
		{
			this.changerPlateau(0);
		}

		// On met à jour la pioche et on tire la nouvelle carte
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

		// 1. Trouver quel joueur est actuellement en train de regarder son écran (le joueur actif)
		int joueurQuiPasse = 0;
		for (int i = 0; i < this.nbJoueurs; i++) 
		{
			// Le joueur actif est celui qui n'a pas encore joué et dont le plateau devrait être affiché
			if (!this.aJoueCeTour[i]) 
			{
				joueurQuiPasse = i;
				break;
			}
		}

		// 2. On marque ce joueur comme ayant terminé son action pour ce tour
		this.aJoueCeTour[joueurQuiPasse] = true;

		// 3. Trouver le joueur suivant qui doit encore jouer ce tour-ci
		int prochainJoueur = (joueurQuiPasse + 1) % this.nbJoueurs;
		while (this.aJoueCeTour[prochainJoueur] && prochainJoueur != joueurQuiPasse) 
		{
			prochainJoueur = (prochainJoueur + 1) % this.nbJoueurs;
		}

		// 4. On change de plateau visuellement vers le joueur suivant
		if (this.frame != null) 
		{
			this.frame.afficherPlateauJoueur(prochainJoueur);
			this.frame.incrNbPasse(); // Incrémente le compteur de passes nécessaires à la pioche
		}

		// 5. On vérifie si tout le monde a fini (si oui, piochera une nouvelle carte)
		this.verifierFinDeTourCollectif();
		
		if (this.frame != null) 
		{
			this.frame.repaint();
		}
	}

	/**
	 * Change le plateau visible après un petit temps d'attente autonome
	 */
	private void changerPlateau(int prochainJoueur) 
	{
		// On crée un Timer Swing qui attend 1500 ms (1.5 seconde)
		javax.swing.Timer timer = new javax.swing.Timer(500, new java.awt.event.ActionListener() 
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) 
			{
				if (frame != null) 
				{
					// L'action s'exécute après le délai
					frame.afficherPlateauJoueur(prochainJoueur);
				}
			}
		});
		
		timer.setRepeats(false); // TRÈS IMPORTANT : Le timer ne doit s'exécuter qu'une seule fois !
		timer.start();           // On lance le compte à rebours
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
}