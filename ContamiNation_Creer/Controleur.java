package ContamiNation_Creer;

import ContamiNation_Creer.Metier.*;
import ContamiNation_Creer.IHM.*;
import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

/* SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Controleur
{
	/*----------------------------*/
	/* Attributs de la classe    */
	/*----------------------------*/
	
	public static final Color COLOR_BACKGROUND = new Color( 58, 111, 134); 
	public static final Color COLOR_FOREGROUND = new Color(230, 230, 230); 

	private FrameCreer          frame;
	private FrameSommet         frameSommet;
	private Plateau             plateau;
	private Map<Integer, Color> couleursZones = new HashMap<>();
	private int r;
	private int g;
	private int b;
	
	/*----------------------------*/
	/* Constructeur de la classe */
	/*----------------------------*/
	
	// Initialise l'application et ouvre la fenêtre de création ou de chargement.
	public Controleur()
	{
		this.frame = new FrameCreer(this);
		this.frameSommet = null;
	}
	
	/*----------------------------*/
	/* Getters                   */
	/*----------------------------*/
	
	// Retourne le nombre de lignes du plateau actuel.
	public int getLig()     { return plateau.getLig() ; }
	
	// Retourne le nombre de colonnes du plateau actuel.
	public int getCol()     { return plateau.getCol() ; }

	public Color getVirus(int id) { return Virus.getCouleur(id) ;}
	
	// Récupère l'instance du bouton graphique correspondant aux coordonnées.
	public JButton getButton(int lig, int col)
	{
		return this.frameSommet.getPanelGrille().getButton(lig, col);
	}
	
	// Retourne l'objet métier Case situé aux coordonnées spécifiées.
	public Case getCase(int lig, int col)
	{
		return this.plateau.getCase(lig, col);
	}
	
	// Renvoie le nombre total de virus définis pour le plateau.
	public int getNbVirus() { return this.plateau.getNbVirus(); }

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

	// Extrait et retourne le nom du plateau sauvegardé dans un fichier donné.
	public String getNom(File fichier)
	{
		return Enregistrement.Recuperer(fichier, this).getNom();
	}
	
	// Vérifie si la case aux coordonnées données contient un sommet placé.
	public boolean aSommet (int lig, int col)
	{
		return this.plateau.getCase(lig, col).getSommet() != null;
	}
	
	// Retourne le texte du symbole (Aéroport, Ville...) du sommet situé sur la case.
	public String getSymboleSommet(int lig, int col)
	{
		if (this.aSommet(lig, col))
			return this.plateau.getCase(lig, col).getSommet().getSymbole();
		return null;
	}
	
	// Vérifie si le sommet de la case est défini comme la base d'un virus.
	public int getEstBaseSommet(int lig, int col)
	{
		if (this.aSommet(lig, col))
			return this.plateau.getCase(lig, col).getSommet().getEstBase();
		return 0;
	}
	
	// Retourne l'identifiant numérique de la zone attribuée à la case spécifiée.
	public int getZone(int lig, int col)
	{
		return this.plateau.getCase(lig, col).getZone();
	}
	
	/*----------------------------*/
	/* Méthodes                  */
	/*----------------------------*/
	
	// Efface le numéro de zone attribué à une case donnée.
	public void reinitialiserZone(int lig, int col)
	{
		this.plateau.getCase(lig, col).supprimerZone();
	}
	
	// Instancie un nouveau plateau vierge avec les paramètres de configuration spécifiés.
	public void creerPlateau(int lig, int col, int nbCouleur, String nomPlateau)
	{
		this.resetCouleurs();
		this.plateau = Plateau.creerPlateau( lig, col, nbCouleur, nomPlateau, this);
	}

	// Bascule l'affichage de l'interface principale vers la grille d'édition des zones.
	public void changerPanel(int lig, int col)
	{
		this.frame.changerPanel(new PanelGrille(lig, col, this, true, this.frameSommet));
		this.plateau.initBtn();
	}
	
	// Met à jour l'affichage d'un bouton de la grille sur la ou les fenêtres actives.
	public void initBtn(String val, int lig, int col)
	{
		if (this.frame.getPanel() instanceof PanelGrille)
			this.frame.initBtn(val, lig, col);

		if (this.frameSommet != null)
			this.frameSommet.initBtn(val, lig, col);
	}
	
	// Tente d'affecter une case à une zone et retourne le numéro de zone validé ou corrigé.
	public int ajouterZone (int lig, int col, int numZone)
	{
		int zoneAppliquee = this.plateau.ajouterZone(lig, col, numZone);
		this.plateau.initBtn();
		return zoneAppliquee;
	}

	// Ouvre la seconde interface dédiée au placement des sommets et à la visualisation des arêtes.
	public void ouvrirSommet()
	{
		this.frameSommet = new FrameSommet(this, this.plateau.getLig(), this.plateau.getCol());
		this.plateau.initBtn();
	}
	
	// Supprime le sommet d'une case et demande la mise à jour des liaisons du graphe.
	public void supprimerSommet(int lig, int col)
	{
		this.plateau.supprimerSommet(lig, col);
		this.plateau.relierTousLesSommets();
		this.plateau.initBtn();
	}

	// Place un nouveau sommet sur une case et recalcule ses connexions directes avec les autres.
	public void ajouterSommet(int lig, int col, String symbole)
	{
		this.plateau.ajouterSommet(lig, col, symbole);
		this.plateau.relierTousLesSommets();
		this.plateau.initBtn();
	}
	
	// Déclenche la procédure de sauvegarde de l'état actuel du plateau dans un fichier.
	public void enregistrer()
	{
		this.plateau.enregistrer();
		this.OuvrirCreer();
	}

	// Réouvre la fenêtre d'accueil principale pour recommencer ou charger un autre plateau.
	public void OuvrirCreer()
	{
		this.frame = new FrameCreer(this);
	}
	
	// Charge les données d'un plateau depuis un fichier et affiche sa grille correspondante.
	public void charger(File fichier)
	{
		this.resetCouleurs();
		this.plateau = Enregistrement.Recuperer(fichier, this);
		this.frame.changerPanel(new PanelGrille(this.plateau.getLig(), this.plateau.getCol(), this, true, this.frameSommet));
		this.plateau.initBtn();
	}

	// Modifie la propriété "nom" d'un plateau sauvegardé et réenregistre le fichier.
	public void Renommer(String nom, File fichier)
	{
		this.resetCouleurs();
		this.plateau = Enregistrement.Recuperer(fichier, this);
		this.plateau.setNom(nom);
		this.plateau.enregistrer();
		this.plateau.initBtn();
	}

	// Crée une sauvegarde indépendante d'un plateau existant sous un nouveau fichier distinct.
	public void copier(File fichier)
	{
		this.resetCouleurs();
		this.plateau = Enregistrement.Recuperer(fichier, this);
		this.plateau.setNom(this.plateau.getNom() + " - Copie");
		this.plateau.setFichierSource(null);
		this.plateau.enregistrer();
		this.plateau.initBtn();
	}

	// Transmet l'ordre de suppression de ligne de fichier au panneau de sauvegarde visuel.
	public void supprimerFichier(int ligne)
	{
		((PanelSauvegarde)(this.frame.getPanel())).supprimerFichier(ligne);
	}
	
	// Détermine le prochain numéro séquentiel disponible pour nommer un nouveau fichier data.
	public int nbPlateau()
	{
		int num = 0;
		while (new File("../niveaux/carte_num_" + num + ".data").exists())
			num++;
		return num;
	}

	// Retire l'appartenance d'une case à sa zone avec vérification algorithmique de continuité.
	public void supprimerZone(int lig, int col)
	{
		this.plateau.supprimerZone(lig, col);
		this.plateau.initBtn();
	}

	// Ajoute un nouveau virus avec le nom spécifié dans les données du plateau en cours.
	public void creerVirus(String nom)
	{
		this.plateau.creerVirus(nom);
	}

	// Réinitialise le générateur pseudo-aléatoire servant à colorier les zones à l'écran.
	public void resetCouleurs()
	{
		this.couleursZones.clear();
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}
	
	// Définit le sommet de la case spécifiée comme étant le point de départ d'un virus (base).
	public void setBaseSommet(int lig, int col, int idBase)
	{
		if (this.aSommet(lig, col))
			this.plateau.getCase(lig, col).getSommet().setBase(idBase);
	}

	// Retire le statut de base virale au sommet présent sur la case.
	public void retirerBaseSommet(int lig, int col)
	{
		if (this.aSommet(lig, col))
			this.plateau.getCase(lig, col).getSommet().retirerBase();
	}
	
	// Remplace de force l'identifiant de base affecté à un sommet existant.
	public void setSommet(int lig, int col, int id)
	{
		if (this.plateau.getCase(lig, col).getSommet() != null) 
		{
			this.plateau.getCase(lig, col).getSommet().setBase(id);
		}
	}
	
	// Remplit récursivement une zone complète avec une nouvelle couleur (outil pot de peinture).
	public void full(int lig, int col, int ancienneZone, int nouvelleZone, boolean[][] visite) 
	{
		this.plateau.full(lig, col, ancienneZone, nouvelleZone, visite);
	}
	
	// Parcourt intégralement le plateau pour vérifier si un ID de zone est encore utilisé.
	public boolean zoneExiste(int numZone)
	{
		for (int i = 0; i < this.getLig(); i++)
			for (int j = 0; j < this.getCol(); j++)
				if (this.plateau.getCase(i, j).getZone() == numZone) 
					return true;
		return false;
	}

	// Vérifie si l'une des 4 cases voisines orthogonales appartient à la zone spécifiée.
	public boolean estAdjacentZone(int lig, int col, int numZone)
	{
		if (lig > 0 && this.plateau.getCase(lig - 1, col).getZone() == numZone) return true;
		if (lig < this.getLig() - 1 && this.plateau.getCase(lig + 1, col).getZone() == numZone) return true;
		if (col > 0 && this.plateau.getCase(lig, col - 1).getZone() == numZone) return true;
		if (col < this.getCol() - 1 && this.plateau.getCase(lig, col + 1).getZone() == numZone) return true;
		return false;
	}

	// Point d'entrée de démarrage global pour lancer le programme Java.
	public static void main (String[] args)
	{
		new Controleur();
	}
}