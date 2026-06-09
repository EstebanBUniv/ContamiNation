package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Plateau
{
	private Controleur  ctrl;
	private int         col;
	private int         lig;
	private int         nbVirus;
	private String      nom;
	private Case[][]    tabCases; 
	private File        fichierSource = null;
	private List<Virus> lstVirus; 

	public static Plateau creerPlateau(int lig, int col, int nbVirus, String nom, Controleur ctrl)
	{
		if ( col <= 0 || lig <= 0 || nbVirus <=0) return null;
		return new Plateau(lig, col, nbVirus, nom, ctrl);
	}

	private Plateau(int lig, int col, int nbVirus, String nom, Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.col  = col;
		this.lig  = lig;
		this.nom  = nom;
		this.nbVirus  = nbVirus;
		this.lstVirus = new ArrayList<>();
		this.tabCases = new Case[lig][col];

		// CRUCIAL : Initialisation de la grille de jeu
		for (int i = 0; i < lig; i++) {
			for (int j = 0; j < col; j++) {
				this.tabCases[i][j] = new Case(i, j);
			}
		}
	}

	/*----------------------------*/
	/* Getters                    */
	/*----------------------------*/

	public int    getLig()                   { return this.lig                ; }
	public int    getCol()                   { return this.col                ; }
	public int    getNbVirus()               { return this.nbVirus            ; }
	public String getNom()                   { return this.nom                ; }
	public File   getFichierSource()         { return this.fichierSource      ; }
	public Case   getCase(int lig, int col)  { return this.tabCases[lig][col] ; }

	public int getNumero() 
	{
		if (this.fichierSource != null) {
			String name = this.fichierSource.getName();
			name = name.replaceAll("[^0-9]", ""); // Garde uniquement les chiffres
			if (!name.isEmpty()) return Integer.parseInt(name);
		}
		return 0;
	}

	public String getNomVirus(int index) 
	{
		if (index >= 0 && index < this.lstVirus.size()) 
			return this.lstVirus.get(index).getNom();
		return "";
	}

	/*----------------------------*/
	/* Setters et Méthodes        */
	/*----------------------------*/

	public void setFichierSource(File fichier) { this.fichierSource = fichier; }
	
	public void creerVirus(String nom) { this.lstVirus.add(new Virus(nom)); }

	public void ajouterZoneDirecte(int lig, int col, int zone) 
	{
		this.tabCases[lig][col].ajouterZone(zone);
	}

	public void ajouterSommet(int lig, int col, String symbole) 
	{
		this.tabCases[lig][col].ajouterSommet(symbole);
	}

	public void relierTousLesSommets() 
	{
		// C'est ici que nous allons recréer le graphe de sommets plus tard
		// pour que l'algorithme de jeu puisse fonctionner.
	}
	
	// Initialise le point de départ pour une manche précise (le virus associé)
	public void initialiserBaseVirus(Sommet base, int numeroManche)
	{
		// L'index dans la liste (lstVirus) commence à 0, donc on fait numeroManche - 1
		if (numeroManche > 0 && numeroManche <= this.lstVirus.size()) 
		{
			Virus v = this.lstVirus.get(numeroManche - 1);
			v.setBaseDepart(base);
		}
	}
}