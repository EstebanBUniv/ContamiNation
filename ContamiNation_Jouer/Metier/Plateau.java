package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Plateau
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private Controleur  ctrl;
	private int         col;
	private int         lig;
	private int         nbVirus;
	private int         numManche;
	private int         pointTotal;
	private String      nom;
	private Case[][]    tabCases; 
	private File        fichierSource = null;
	private List<Virus> lstVirus;

	private boolean     modeDebiche = false;

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
		this.numManche  = 1;
		this.pointTotal = 0;

		// CRUCIAL : Initialisation de la grille de jeu
		for (int i = 0; i < lig; i++) {
			for (int j = 0; j < col; j++) {
				this.tabCases[i][j] = new Case(i, j);
			}
		}
		
		if (this.modeDebiche)
			this.ctrl.appelerChoixCarte();

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
	public Virus  getVirus(int index)        { return this.lstVirus.get(index); }
	public int    getPointTotal()            { return this.pointTotal         ; }
	public int    getNumManche ()            { return this.numManche          ; }

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
	
	public boolean mancheSuivante()
	{
		this.numManche++;
		this.pointTotal += this.calculManche();
		return this.numManche <= this.lstVirus.size();
	}

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
	
	public void initialiserBaseVirus(Sommet base, int numeroManche)
	{
		if (numeroManche > 0 && numeroManche <= this.lstVirus.size()) 
		{
			Virus v = this.lstVirus.get(numeroManche - 1);
			v.setBaseDepart(base);
		}
	}

	public int calculManche()
	{
		int nbSommetParZone = 0;
		
		ArrayList<Integer> zonesVisitees = new ArrayList<>();
		for (int lig = 0; lig < this.lig; lig++)
		{
			for (int col = 0; col < this.col; col++) 
			{
				Case caseActuelle = this.tabCases[lig][col];
				if (caseActuelle.getAUnSommet() && this.tabCases[lig][col].getSommet().getContamine())
				{
					int zoneDeLaCase = caseActuelle.getZone();
					if (zoneDeLaCase != 0 && !zonesVisitees.contains(zoneDeLaCase))
					{
						zonesVisitees.add(zoneDeLaCase); 
						int tmpNbSommet = 1;
						for (int tmpLig = 0; tmpLig < this.lig; tmpLig++)
						{
							for (int tmpCol = 0; tmpCol < this.col; tmpCol++) 
							{
								if (zoneDeLaCase == this.tabCases[tmpLig][tmpCol].getZone()   && 
								    caseActuelle != this.tabCases[tmpLig][tmpCol]             && 
									this.tabCases[tmpLig][tmpCol].getAUnSommet()              && 
									this.tabCases[tmpLig][tmpCol].getSommet().getContamine())
								{
									tmpNbSommet++;
								}
							}
						}
						if (tmpNbSommet > nbSommetParZone)
							nbSommetParZone = tmpNbSommet;
					}
				}
			}
		}
		int scoreFinal = nbSommetParZone * zonesVisitees.size(); 
		return scoreFinal;
	}
	
	public boolean verifSommet(Case caseAVerif, Carte carteTire, int choixForce)
	{
		Virus virusActuel = this.lstVirus.get(this.numManche - 1);
		
		if ( carteTire != null &&
		     caseAVerif .getAUnSommet()                                            && // Vérification sommet présent (Correction : getAUnSommet())
			 virusActuel.estVoisinDeLExtremite(caseAVerif.getSommet())             && // a un voisin à une extrémité du virus
			 !caseAVerif.getSommet().getContamine()                                && // Le sommet n'est pas encore contaminé
			 (carteTire  .getSymbole().equals(caseAVerif.getSommet().getSymbole()) ||
			  carteTire.getSymbole().equals("Epidemie")))                          // La carte est correcte (Correction : ajout des parenthèses à getSymbole())
		{
			virusActuel.ajouterSommetContamine(caseAVerif.getSommet(), choixForce);
			caseAVerif.getSommet().setContamine(true);
			return true;
		}
		
		return false;
	}
	
	public void preparerNouvelleManche()
	{
		for (int l = 0; l < this.lig; l++)
		{
			for (int c = 0; c < this.col; c++)
			{
				if (this.tabCases[l][c].getAUnSommet())
				{
					this.tabCases[l][c].getSommet().setContamine(false);
				}
			}
		}
	}
	
	public boolean estCoupValide(Case caseAVerif, Carte carteTire)
	{
		Virus virusActuel = this.lstVirus.get(this.numManche - 1);
		
		if (carteTire == null || !caseAVerif.getAUnSommet()) return false;
		
		Sommet s = caseAVerif.getSommet();
		
		return virusActuel.estVoisinDeLExtremite(s) && 
			   !s.getContamine()                    && 
			   (carteTire.getSymbole().equals(s.getSymbole()) ||
			  carteTire.getSymbole().equals("Epidemie"));
	}

}