package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Plateau
{
	private Controleur ctrl;
	private int        col;
	private int        lig;
	private int        nbVirus;
	private String     nom;
	private Case[][]   tabCases; 
	private File       fichierSource = null;
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
	/* Getters                   */
	/*----------------------------*/

	public int getLig() { return this.lig; }
	public int getCol() { return this.col; }
	public int getNbVirus() { return this.nbVirus; }
	public String getNom() { return this.nom; }
	public File getFichierSource() { return this.fichierSource; }
	public Case getCase(int lig, int col) { return this.tabCases[lig][col]; }

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
	/* Setters et Méthodes       */
	/*----------------------------*/

	public void setFichierSource(File fichier) { this.fichierSource = fichier; }
	
	public void creerVirus(String nom) { this.lstVirus.add(new Virus(nom)); }

	public void ajouterZoneDirecte(int lig, int col, int zone) {
		this.tabCases[lig][col].ajouterZone(zone);
	}

	public void ajouterSommet(int lig, int col, String symbole) {
		this.tabCases[lig][col].ajouterSommet(symbole);
	}

	public void relierTousLesSommets() 
	{
		// C'est ici que nous allons recréer le graphe de sommets plus tard
		// pour que l'algorithme de jeu puisse fonctionner.

		for (int i = 0; i < this.lig; i++)
			for (int j = 0; j < this.col; j++)
				if (this.tabCases[i][j].getSommet() != null)
					this.tabCases[i][j].getSommet().resetVoisins();
			
		for (int i = 0; i < this.lig; i++)
		{
			for(int j = 0; j < this.col; j++)
			{
				Sommet sommetCourant = this.tabCases[i][j].getSommet();
				
				if (sommetCourant != null)
				{
					chercherVoisins(i, j, sommetCourant);
				}
			}
		}
	}

	//indique les 8 directions et ajoute les voisins de chaques sommets
	private void chercherVoisins(int lig, int col, Sommet sommetCourant)
	{
		int[][] directions = {
			{-1, 0}, {1, 0}, {0, -1}, {0, 1}, 
			{-1, -1}, {-1, 1}, {1, -1}, {1, 1}
		};
		
		for (int i = 0; i < directions.length; i++)
		{
			int dLig = directions[i][0];
			int dCol = directions[i][1];
			
			int ligCherche = lig + dLig;
			int colCherche = col + dCol;
			
			boolean continuer = true;
			while (ligCherche >= 0 && ligCherche < this.lig && colCherche >= 0 && colCherche < this.col && continuer)
			{
				Sommet sommetTrouve = this.tabCases[ligCherche][colCherche].getSommet();
				
				if (sommetTrouve != null)
				{
					sommetCourant.ajouterVoisin(i, sommetTrouve);
					continuer = false; 
				}
				ligCherche += dLig;
				colCherche += dCol;
			}
		}
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