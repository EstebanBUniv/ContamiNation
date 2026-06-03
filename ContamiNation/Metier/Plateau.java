package ContamiNation.Metier;

import java.util.ArrayList;
import ContamiNation.Controleur;

public class Plateau
{
	private final ArrayList<Sommet> SOMMETS = new ArrayList<Sommet>();
	
	private Controleur ctrl;
	private int        col;
	private int        lig;
	private int        nbCouleur;
	private String[]   couleurs;
	private int        tailleCases;
	private Case[][]   tabCases;

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
	
	public static Plateau creerPlateau(int lig, int col, int nbCouleur, Controleur ctrl)
	{
		if ( col <= 0 || lig <= 0 || nbCouleur <=1)
				return null;
		return new Plateau(lig, col, nbCouleur, ctrl);
	}

	private Plateau(int lig, int col, int nbCouleur, Controleur ctrl)
	{
		this.ctrl = ctrl;
		
		this.col       = col;
		this.lig       = lig;
		this.nbCouleur = nbCouleur;

		this.couleurs    = new String[this.nbCouleur];
		this.tailleCases = 50;
		this.tabCases    = new Case[this.lig][this.col];
		this.creaCase();
	}
	
	public int getLig() { return this.lig; }
	public int getCol() { return this.col; }
	
	public Case getCase(int lig, int col)
	{
		return this.tabCases[lig][col];
	}
	
	public void ajouterSommet(int lig, int col, String symbole)
	{
		this.tabCases[lig][col].ajouterSommet(symbole);
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
	
	public void relierTousLesSommets()
	{
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
	

}