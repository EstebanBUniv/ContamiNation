package ContamiNation_Creer.Metier;

import ContamiNation_Creer.Controleur;

import java.util.ArrayList;
import java.util.List;
import java.io.File;


/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Plateau
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private Controleur ctrl;
	private int        col;
	private int        lig;
	private int        nbVirus;
	private int        tailleCases;
	private String     nom;
	private String[]   couleurs;
	private Case[][]   tabCases;
	private File fichierSource = null;

	private List<Virus> lstVirus = new ArrayList<Virus>();
	
	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public static Plateau creerPlateau(int lig, int col, int nbVirus, String nom, Controleur ctrl)
	{
		if ( col <= 0 || lig <= 0 || nbVirus <=0)
				return null;
		return new Plateau(lig, col, nbVirus, nom, ctrl);
	}

	private Plateau(int lig, int col, int nbVirus, String nom, Controleur ctrl)
	{
		Virus.resetData();
		this.ctrl        = ctrl;
		
		this.col         = col;
		this.lig         = lig;
		
		this.nom = nom;
		
		this.nbVirus   = nbVirus;
		this.couleurs    = new String[this.nbVirus];
		
		this.tailleCases = 50;
		this.tabCases    = new Case[this.lig][this.col];
		this.creaCase();
	}
	
	private void creaCase()
	{
		for (int i = 0; i < this.lig; i++ )
		{
			for(int j = 0; j < this.col; j++)
			{
				this.tabCases[i][j] = new Case(i,j);
			}
		}
	}
	
	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/
	
	public int    getLig         () { return this.lig              ; }
	public int    getCol         () { return this.col              ; }
	public int    getNbVirus     () { return this.nbVirus          ; }
	public int    getNumero      () { return this.ctrl.nbPlateau() ; }
	public String getNom         () { return this.nom              ; }
	public File getFichierSource () { return this.fichierSource    ; }
	
	public Case getCase(int lig, int col)
	{
		return this.tabCases[lig][col];
	}

	public Virus getVirus (int index)
	{
		return this.lstVirus.get(index);
	}
	
	public String getNomVirus(int nomVirus)
	{
		return this.lstVirus.get(nomVirus).getNom();
	}

	/*----------------------------*/
	/*  Setters                   */
	/*----------------------------*/

	public void setNom           (String nom)  { this.nom           = nom ; }
	public void setFichierSource (File   file) { this.fichierSource = file; }

	
	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/
	
	public void ajouterSommet(int lig, int col, String symbole)
	{
		this.tabCases[lig][col].ajouterSommet(symbole);
	}
	
	public void supprimerSommet(int lig, int col)
	{
		Sommet aSupprimer = this.tabCases[lig][col].getSommet();

		if (aSupprimer == null) return;

		// Parcourt toutes les cases et retire aSupprimer des voisins
		for (int i = 0; i < this.lig; i++)
		{
			for (int j = 0; j < this.col; j++)
			{
				Sommet s = this.tabCases[i][j].getSommet();
				if (s != null)
					s.retirerVoisin(aSupprimer);
			}
		}

		// Supprime le sommet de la case
		this.tabCases[lig][col].supprimerSommet();
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
	
	public void ajouterZone (int lig, int col, int numZone)
	{
		if (this.tabCases[lig][col].getZone() != 0 )
			return;
		for (int i = 0; i < this.lig; i++ )
		{
			for(int j = 0; j < this.col; j++)
			{
				if (this.tabCases[i][j].getZone() == numZone)
				{
					 if ( (lig > 0 ? this.tabCases[lig - 1][col].getZone() == numZone : false) ||
					      (lig < this.lig - 1 ? this.tabCases[lig + 1][col].getZone() == numZone : false) ||
					      (col > 0 ? this.tabCases[lig][col - 1].getZone() == numZone : false) ||
					      (col < this.col - 1 ? this.tabCases[lig][col + 1].getZone() == numZone : false) )
					
							this.tabCases[lig][col].ajouterZone(numZone);
					else 
						this.ajouterZone(lig, col, numZone + 1);
					return;
				}
			}
		}
		
		this.tabCases[lig][col].ajouterZone(numZone);
	}
	
	public void ajouterZoneDirecte(int lig, int col, int zone)
	{
		this.tabCases[lig][col].ajouterZone(zone);
	}
	
	public void initBtn()
	{
		for (int lig = 0; lig < this.lig; lig++ )
		{
			for(int col = 0; col < this.col; col++)
			{
				this.ctrl.initBtn(this.tabCases[lig][col] + "", lig, col) ;
			}
		}
	}
	
	public void enregistrer()
	{
		Enregistrement save = new Enregistrement(this);
		save.enregistrer();
	}

	public String toString()
	{
		String res = "";

		for (int i = 0; i < this.lig; i++ )
		{
			for(int j = 0; j < this.col; j++)
			{
				res += this.tabCases[i][j] ;
			}
			res += "\n";
		}
		
		return res;
	}

	public void creerVirus(String nom)
	{
		this.lstVirus.add(new Virus(nom));
	}


	public void supprimerZone(int lig, int col)
	{
		int zoneCible = this.tabCases[lig][col].getZone();
		
		if (zoneCible == 0)
			return;
			
		int totalCasesZone = 0;
		int ligDepart      = -1;
		int colDepart      = -1;
		
		for (int i = 0; i < this.lig; i++)
			for (int j = 0; j < this.col; j++)
				if (this.tabCases[i][j].getZone() == zoneCible)
				{
					totalCasesZone++;
					if (i != lig || j != col)
					{
						ligDepart = i;
						colDepart = j;
					}
				}
				
		if (totalCasesZone <= 1)
		{
			this.tabCases[lig][col].supprimerZone();
			return;
		}
		
		this.tabCases[lig][col].supprimerZone();
		
		boolean[][] visite          = new boolean[this.lig][this.col];
		int         casesConnectees = this.compterCasesConnectees(ligDepart, colDepart, zoneCible, visite);
		
		if (casesConnectees < totalCasesZone - 1)
			this.tabCases[lig][col].ajouterZone(zoneCible);
	}

	private int compterCasesConnectees(int l, int c, int zoneCible, boolean[][] visite)
	{
		if (l < 0 || l >= this.lig || c < 0 || c >= this.col)
			return 0;
			
		if (visite[l][c] || this.tabCases[l][c].getZone() != zoneCible)
			return 0;
			
		visite[l][c] = true;
		int nb = 1;
		
		nb += this.compterCasesConnectees(l - 1, c, zoneCible, visite);
		nb += this.compterCasesConnectees(l + 1, c, zoneCible, visite);
		nb += this.compterCasesConnectees(l, c - 1, zoneCible, visite);
		nb += this.compterCasesConnectees(l, c + 1, zoneCible, visite);
		
		return nb;
	}
	

}