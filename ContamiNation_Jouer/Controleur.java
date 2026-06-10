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
	public static final Color COLOR_BACKGROUND = new Color( 58, 111, 134);
	public static final Color COLOR_FOREGROUND = new Color(230, 230, 230);

	private JPanel[][]          tabPanel;
	private FrameJeu            frame;
	private Plateau             plateau;
	private Pioche              pioche;
	private FrameChoixCarte     frameChoixCarte;
	private Map<Integer, Color> couleursZones = new HashMap<>();
	private int                 r, g, b;
	private boolean             modeDebiche = false;

	// Variables pour le système de sélection
	private boolean estClique = false;
	private Case caseSelectionnee = null;

	public Controleur() { this.frame = new FrameJeu(this); }

	public Plateau getPlateau() { return this.plateau; }
	public int getTailleCase() { return this.frame.getPanelPlateau().getTailleCase(); }
	public int getLig() { return plateau.getLig(); }
	public int getCol() { return plateau.getCol(); }
	public Case getCase(int lig, int col) { return this.plateau.getCase(lig, col); }
	public JPanel getPanel(int lig, int col) { return this.frame.getTabPanel()[lig][col]; }
	public Virus getVirus(int index) { return this.plateau.getVirus(index); }
	public Map<Integer, Color> getCouleurZone() { return this.couleursZones; }

	public void chargerNiveau(File fichier) {
		this.plateau = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, this);
		if (this.getModeDebiche()) this.appelerChoixCarte();
	}

	public Color getCouleurZone(int numZone) {
		if (numZone == 0) return Color.WHITE;
		if (!this.couleursZones.containsKey(numZone)) {
			this.r = (this.r + 67) % 256; this.g = (this.g + 113) % 256; this.b = (this.b + 193) % 256;
			this.couleursZones.put(numZone, new Color(this.r, this.g, this.b));
		}
		return this.couleursZones.get(numZone);
	}

	public void resetCouleurs() { this.couleursZones.clear(); this.r = 0; this.g = 0; this.b = 0; }
	public void initierPioche () { this.pioche = new Pioche(); }
	public void melangerPioche() { this.pioche.melanger(); }
	public Carte tirerCarte(int indice) { return this.pioche.tirerCarte(indice); }
	public Carte premiereCarte() { return this.pioche.premiereCarte(); }
	public boolean verifFinManche() { return this.pioche.verifFinManche(); }
	
	public void nouvelleManche() {
		if (this.plateau.mancheSuivante()) {
			this.plateau.preparerNouvelleManche();
			initierPioche();
			melangerPioche();
			this.frame.nouvelleManche();
			this.frame.reinitierPanelPioche();
			if (this.frame != null) this.frame.repaint();
		} else {
			System.out.println("Fin de tout le jeu " + this.plateau.getPointTotal());
		}
	}

	// --- MÉTHODES POUR L'AFFICHAGE DES COULEURS DE SÉLECTION ---
	public Case getCaseSelectionnee() { return this.caseSelectionnee; }

	public boolean estVoisinAtteignable(Case caseAVerif) {
		if (this.caseSelectionnee == null || caseAVerif.getSommet() == null) return false;
		Virus v = this.plateau.getVirusActif();
		if (v.getConquis().contains(caseAVerif.getSommet())) return false;
		
		Carte carteTiree = this.pioche.getCarteTire();
		if (carteTiree == null) return false;
		if (!carteTiree.getSymbole().equals(caseAVerif.getSommet().getSymbole()) &&
			!carteTiree.getSymbole().equals("Epidemie")) return false;

		for (Sommet voisin : this.caseSelectionnee.getSommet().getLstVoisin()) {
			if (voisin == caseAVerif.getSommet()) return true;
		}
		return false;
	}

	// --- LE SYSTÈME DE JEU À 2 CLICS ---
	public void verifSommet(Case caseAVerif) {
		int indexManche = this.plateau.getNumManche() - 1;
		Virus v = this.getVirus(indexManche);
		Sommet s = caseAVerif.getSommet();
		Carte carteActive = this.pioche.getCarteTire();

		if (this.estClique) {
			// DEUXIÈME CLIC : Tentative de propagation
			if (this.plateau.estCoupValide(caseAVerif, carteActive) && this.estVoisinAtteignable(caseAVerif)) {
				int choixForce = 0;
				if (s != null && v.getTailleChemin() > 1 && v.toucheTete(s) && v.toucheQueue(s)) {
					choixForce = this.frame.demanderChoixBoucle();
				}
				if (this.plateau.verifSommet(caseAVerif, carteActive, choixForce)) {
					this.frame.reinitierPanelPioche();
				}
			}
			this.estClique = false;
			this.caseSelectionnee = null;
			this.frame.SommetClique();
		} else {
			// PREMIER CLIC : Sélection
			if (s != null && v.getConquis().contains(s) && v.estExtremite(s)) {
				this.estClique = true;
				this.caseSelectionnee = caseAVerif;
				this.frame.SommetClique();
			}
		}
		this.frame.repaint();
	}

	public void setModeDebiche() { this.modeDebiche = true; }
	public void appelerChoixCarte() { this.frameChoixCarte = new FrameChoixCarte(this); }
	public Carte getCarte(int indice) { return this.pioche.getCarte(indice); }
	public int getTaillePioche() { return this.pioche.getTaillePioche(); }
	public boolean getModeDebiche() { return this.modeDebiche; }

	public static void main (String[] args) { new Controleur(); }
}