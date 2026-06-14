package ContamiNation_Jouer;

import ContamiNation_Jouer.IHM.*;
import ContamiNation_Jouer.Metier.*;

import java.awt.Color;
import java.awt.Font;


import javax.swing.JPanel;

import java.io.File;
import java.io.PrintStream;
import java.io.OutputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Controleur
{
	public static final Color COLOR_BACKGROUND = new Color( 58, 111, 134);
	public static final Color COLOR_FOREGROUND = new Color(230, 230, 230);
	public static final Color COLO_EST_SELECT  = new Color( 86, 136, 158);

	public static final Font POLICE_TITRE = new Font("Arial", Font.BOLD, 16);
	public static final Font POLICE_TEXTE = new Font("Arial", Font.BOLD, 12);

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
	private boolean             modeMulti              = false;
	private boolean             estServeurReseau       = false;
	private boolean             estClientReseau        = false;
	private boolean             receptionReseauEnCours = false;
	private long                gameSeed               = 0; // La fameuse graine !

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
	public int     getNbJoueur()                                  { return this.nbJoueurs;                               }
	public FrameJeu getFrame()                                    { return this.frame;                                   }
	public void    setGameSeed(long s)                            { this.gameSeed = s;                                   }
	public boolean getModeMulti()   { return this.modeMulti; }
	
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
			return this.caseSelectionnee.getSommet().getVoisin(indice);
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
	
	public void chargerNiveau(File fichier, int nbJoueurs) 
	{
		this.nbJoueurs   = nbJoueurs;
		this.plateau     = new Plateau[nbJoueurs];
		this.aJoueCeTour = new boolean[nbJoueurs];

		if ( nbJoueurs > 1 )
			this.modeMulti = true;  
		
		for (int i = 0; i < nbJoueurs; i++) 
			this.plateau[i] = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, i, this);
		
		this.attribuerVirusDepart();
		
		this.frame.afficherPlateau(nbJoueurs);
		
		for (Plateau p : this.plateau) {
			if (p.mancheSuivante()) {
				p.preparerNouvelleManche();
			}
		}
		
		this.initierPioche();
		if (this.estServeurReseau || this.estClientReseau) 
			this.pioche.melangerReseau(this.gameSeed);
		else 
			this.melangerPioche();

		this.frame.reinitierPanelPioche();
		if (this.frame != null) this.frame.repaint();
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
			if (suite) 
			{
				initierPioche();
				if (this.estServeurReseau || this.estClientReseau) 
					this.pioche.melangerReseau(this.gameSeed);
				else 
					this.melangerPioche();

				this.frame.nouvelleManche();
				this.frame.reinitierPanelPioche();
				if (this.frame != null) this.frame.repaint();
			}
		} 
		else 
		{
			int nbMax          = 1;
			int maxManche      = 0;
			int Joueur         = 0;
			for(int lig = 0; lig < this.plateau.length; lig++)
			{
				System.out.println("Fin de tout le jeu. Score J" + (lig+1) + ": " + this.plateau[lig].getPointTotal());
				if (this.plateau[lig].getPointTotal() >= maxManche)
				{
					if (this.plateau[lig].getPointTotal() == maxManche)
						nbMax++;
					else
					{
						Joueur = lig;
						nbMax = 1;
						maxManche = this.plateau[lig].getPointTotal();
					}
				}				
			}

			if (nbMax == 1)
			{
				this.partieTerminee(true);
				System.out.println("Le joueur " + (Joueur+1) + " a gagné avec " + maxManche + " points !");
			}
			else
			{
				int maxTour         = 0;
				int joueur      = 0;
				boolean egalite = false;
				for(int lig = 0; lig < this.plateau.length; lig++)
				{
					for (int cpt = 0 ; cpt < this.plateau[lig].getnbPointManche().size() ; cpt++)
					{
						if ((int)(this.plateau[lig].getnbPointManche().get(cpt)) > maxTour)
						{
							egalite = false;
							maxTour = (int)(this.plateau[lig].getnbPointManche().get(cpt));
							joueur = lig;
						}
						else
						{
							if ((int)(this.plateau[lig].getnbPointManche().get(cpt)) == maxTour)
							{
								egalite = true;
							}
						}
					}
				}

				if( egalite == true)
				{
					this.partieTerminee(true);
					System.out.println(" Egalité parfaite avec " + maxTour + " en une manche chacun !");
				}
				else
				{
					this.partieTerminee(true);
					System.out.println("Le joueur" + (joueur+1) + " a gagné la partie avec " + maxTour + " en une manche !");
				}
			}
			
		}
	}

	public void changerLabelPropagation(int idJoueur)
	{
		this.frame.getPanelPlateau(idJoueur).changerLabelPropagation();
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

		if (caseAVerif == null || this.aJoueCeTour[idJoueur]) {
			return;
		}

		if (!this.receptionReseauEnCours)
		{
			// Appliquer le filtre réseau seulement si on est vraiment en mode réseau
			if (this.estServeurReseau || this.estClientReseau)
			{
				if (this.estServeurReseau && idJoueur != 0) {
					return;
				}
				if (this.estClientReseau  && idJoueur != 1) {
					return;
				}
			}
		}

		Plateau plateauActif = this.plateau[idJoueur];
		Virus v = plateauActif.getVirusActif();
		Sommet s = caseAVerif.getSommet();
		Carte carteActive = this.pioche.getCarteTire();


		if (!this.estClique)
		{

			if (s != null && v.getConquis().contains(s) && v.estExtremite(s))
			{
				this.estClique = true;
				this.caseSelectionnee = caseAVerif;
				this.frame.SommetClique();
			}

			this.frame.repaint();
			return;
		}


		boolean coupValide = plateauActif.estCoupValide(caseAVerif, carteActive);
		boolean voisinOk = this.estVoisinAtteignableMulti(caseAVerif, idJoueur);

		if (coupValide && voisinOk)
		{
			int choixForce = 0;
			if (s != null && v.getTailleChemin() > 1 && v.toucheTete(s) && v.toucheQueue(s))
				choixForce = this.frame.demanderChoixBoucle();

			boolean resultat = plateauActif.verifSommet(caseAVerif, carteActive, choixForce);

			if (resultat)
			{
				this.aJoueCeTour[idJoueur] = true;

				if (!this.receptionReseauEnCours)
				{
					this.transmettreCoupReseau(
						this.caseSelectionnee.getLig(), this.caseSelectionnee.getCol(),
						caseAVerif.getLig(), caseAVerif.getCol()
					);
				}

				this.verifierFinDeTourCollectif();
			}
		}

		this.estClique = false;
		this.caseSelectionnee = null;
		this.frame.SommetClique();
		this.frame.repaint();
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

	
	// Méthode de triche
	public void setModeDebiche   () { this.modeDebiche = true                          ; }
	public void appelerChoixCarte() { this.frameChoixCarte = new FrameChoixCarte(this) ; }
	
	public void melangerPioche() { this.pioche.melanger()                              ; }

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

	public Carte getCarteTiree   () { return this.pioche.getCarteTire         ()  ; }
	public Carte premiereCarte   () { return this.pioche.premiereCarte        ()  ; }
	public boolean verifFinManche() { return this.pioche.verifFinManche       ()  ; }
	public void initierPioche ()    { this.pioche = new Pioche(this.getSymbole()) ; }
	
	private void verifierFinDeTourCollectif() 
	{
		// 1. On vérifie si tout le monde a joué
		boolean tousJoues = true;
		for (boolean aJoue : this.aJoueCeTour) {
			if (!aJoue) { tousJoues = false; break; }
		}

		if (tousJoues) 
		{
			//Fin de la manche
			for (int i = 0; i < this.nbJoueurs; i++) {
				this.aJoueCeTour[i] = false;
			}
			if (this.frame != null) {
				this.frame.reinitierPanelPioche();
				// Le prochain à jouer est FORCÉMENT le Joueur 0 (Serveur)
				this.frame.afficherPlateauJoueur(0);
			}
		} 
		else 
		{
			// 2. IL RESTE DES JOUEURS : On cherche le prochain qui n'a pas joué
			int prochainJoueur = 0;
			for (int i = 0; i < this.nbJoueurs; i++) {
				if (!this.aJoueCeTour[i]) {
					prochainJoueur = i;
					break;
				}
			}
			// On bascule la caméra automatiquement sur lui !
			if (this.frame != null) {
				this.frame.afficherPlateauJoueur(prochainJoueur);
			}
		}
		
		if (this.frame != null) {
			this.frame.repaint();
		}
	}

	public void forcerPassageTourCollectif() 
	{
		if (this.aJoueCeTour == null) return;

		int joueurQuiPasse = 0;
		for (int i = 0; i < this.nbJoueurs; i++) 
		{
			if (!this.aJoueCeTour[i]) {
				joueurQuiPasse = i;
				break;
			}
		}

		if (!this.receptionReseauEnCours) {
			if (this.estServeurReseau && joueurQuiPasse != 0) return;
			if (this.estClientReseau  && joueurQuiPasse != 1) return;
			
			this.transmettrePasserReseau();
		}
		
		// Le joueur passe, on valide son tour
		this.aJoueCeTour[joueurQuiPasse] = true;

		if (this.frame != null) {
			this.frame.incrNbPasse(); 
		}

		// On lance la vérification qui va changer la caméra automatiquement
		this.verifierFinDeTourCollectif();
	}

	/**
	 * Change le plateau visible après un petit temps d'attente autonome
	 */
	private void changerPlateau(int prochainJoueur) 
	{
		// On crée un Timer Swing qui attend 1500 ms (1.5 seconde)
		javax.swing.Timer timer = new javax.swing.Timer(500, new java.awt.event.ActionListener() 
		{
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
			chapeauNumeros.add(i);

		// SYNCHRONISATION : On utilise la graine en réseau, ou le hasard pur en Solo
		if (this.estServeurReseau || this.estClientReseau) {
			java.util.Collections.shuffle(chapeauNumeros, new java.util.Random(this.gameSeed));
		} else {
			java.util.Collections.shuffle(chapeauNumeros);
		}

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

	/* ========================================================== */
	/* MÉTHODES RÉSEAU (MULTI)                                    */
	/* ========================================================== */

	public void lancerServeur(int port)
	{
		this.estServeurReseau = true;
		this.serveurJeu = new ServeurJeu(this, port);
		new Thread(this.serveurJeu).start();
	}

	public void lancerClient(String ip, int port)
	{
		this.estClientReseau = true;
		this.clientJoueur = new ClientJoueur(this, ip, port);
		new Thread(this.clientJoueur).start();
	}

	public void clientConnecte()
	{
		javax.swing.SwingUtilities.invokeLater(() -> {
			this.frame.changerPanel(new PanelNiveau(this.frame, this, true, true));
		});
	}

	public void attenteChoixNiveau()
	{
		javax.swing.SwingUtilities.invokeLater(() -> {
			javax.swing.JOptionPane.showMessageDialog(this.frame, 
				"Connecté ! \nEn attente de la sélection de la carte...", 
				"Connexion", javax.swing.JOptionPane.INFORMATION_MESSAGE);
		});
	}

	public void envoyerCarteAuClient(File fichier)
	{
		if (this.serveurJeu != null) {
			new Thread(() -> {
				try { Thread.sleep(200); } catch (InterruptedException e) {}
				this.serveurJeu.envoyerFichier(fichier); 
			}).start();
		}
	}
	
	public void recevoirCarteDuServeur(File fichierTmp)
	{
		javax.swing.SwingUtilities.invokeLater(() -> {
			this.chargerNiveau(fichierTmp, 2); 
			if (this.frame != null) {
				// FIX 1 : On laisse la caméra sur le Serveur (0) qui joue toujours en premier !
				this.frame.afficherPlateauJoueur(0); 
				this.frame.revalidate();
				this.frame.repaint();
			}
		});
	}

	public void transmettreCoupReseau(int ligDep, int colDep, int ligArr, int colArr)
	{
		String msg = "COUP:" + ligDep + ":" + colDep + ":" + ligArr + ":" + colArr;
		if (this.estServeurReseau && this.serveurJeu != null) this.serveurJeu.envoyerMessage(msg);
		else if (this.estClientReseau && this.clientJoueur != null) this.clientJoueur.envoyerMessage(msg);
	}
	
	public void recevoirCoupReseau(int ligDep, int colDep, int ligArr, int colArr)
	{
		javax.swing.SwingUtilities.invokeLater(() -> {
			this.receptionReseauEnCours = true; // On lève le bouclier
			int idAdversaire = this.estServeurReseau ? 1 : 0;

			// On sauvegarde temporairement l'état local du joueur s'il était en train de cliquer
			boolean ancienClique = this.estClique;
			Case ancienCase = this.caseSelectionnee;

			this.estClique = false; // On force le réseau
			this.verifSommet(this.getCase(ligDep, colDep, idAdversaire), idAdversaire);
			this.verifSommet(this.getCase(ligArr, colArr, idAdversaire), idAdversaire);

			// On restaure l'état du joueur local
			this.estClique = ancienClique;
			this.caseSelectionnee = ancienCase;
			this.receptionReseauEnCours = false; // On remet le bouclier
		});
	}
	
	public void transmettrePasserReseau()
	{
		if (this.estServeurReseau && this.serveurJeu != null)       this.serveurJeu.envoyerMessage("PASSER");
		else if (this.estClientReseau && this.clientJoueur != null) this.clientJoueur.envoyerMessage("PASSER");
	}

	public void recevoirPasserReseau()
	{
		javax.swing.SwingUtilities.invokeLater(() -> {
			this.receptionReseauEnCours = true;
			this.forcerPassageTourCollectif(); 
			this.receptionReseauEnCours = false;
		});
	}

	public static void main (String[] args) 
	{ 
		// On redirige toutes les sorties standards pour ne plus les afficher dans le terminal
		System.setOut(new PrintStream(OutputStream.nullOutputStream()));
		System.setErr(new PrintStream(OutputStream.nullOutputStream()));
		
		new Controleur(); 
	}

}