package ContamiNation.Metier;

import java.util.ArrayList;
import ContamiNation.Controleur;

public class Plateau
{
	private final ArrayList<Sommet> SOMMETS = new ArrayList<Sommet>();
	
	private Controleur ctrl;
	private int        largeur;
	private int        hauteur;
	private int        nbCouleur;
	private String[]   couleurs;
	private int        tailleCases;
	private Case[][]   tabCases;

	private void creaCase()
	{
		for (int i = 0; i < this.hauteur; i++ )
		{
			for(int j = 0; j < this.largeur; j++)
			{
				this.tabCases[i][j] = new Case(i,j);
			}
		}
	}
	
	public static Plateau creerPlateau(int largeur, int hauteur, int nbCouleur, Controleur ctrl)
	{
		if ( largeur <= 0 || hauteur <= 0 || nbCouleur <=1)
				return null;
		return new Plateau(largeur, hauteur, nbCouleur, ctrl);
	}

	private Plateau(int largeur, int hauteur, int nbCouleur, Controleur ctrl)
	{
		this.ctrl = ctrl;
		
		this.largeur   = largeur;
		this.hauteur   = hauteur;
		this.nbCouleur = nbCouleur;

		this.couleurs    = new String[this.nbCouleur];
		this.tailleCases = 50;
		this.tabCases    = new Case[this.hauteur][this.largeur];
		this.creaCase();
	}

	public Case getCase(int x, int y)
	{
		return this.tabCases[x][y];
	}
	
	public void ajouterZone (int x, int y, int numZone)
	{
		if (this.tabCases[x][y].getZone() != 0 )
			return;
		for (int i = 0; i < this.hauteur; i++ )
		{
			for(int j = 0; j < this.largeur; j++)
			{
				if (this.tabCases[i][j].getZone() == numZone)
				{
					 if ( (x > 0 ? this.tabCases[x - 1][y].getZone() == numZone : false) ||
					     (x < this.hauteur - 1 ? this.tabCases[x + 1][y].getZone() == numZone : false) ||
					     (y > 0 ? this.tabCases[x][y - 1].getZone() == numZone : false) ||
					     (y < this.largeur - 1 ? this.tabCases[x][y + 1].getZone() == numZone : false) )
					
							this.tabCases[x][y].ajouterZone(numZone);
					else 
						this.ajouterZone(x, y, numZone + 1);
					return;
				}
			}
		}
		
		this.tabCases[x][y].ajouterZone(numZone);
	}
	
	public void relierTousLesSommets()
	{
		for (int i = 0; i < this.hauteur; i++)
		{
			for(int j = 0; j < this.largeur; j++)
			{
				Sommet sommetCourant = this.tabCases[i][j].getSommet();
				
				if (sommetCourant != null)
				{
					chercherVoisins(i, j, sommetCourant);
				}
			}
		}
	}
	
	private void chercherVoisins(int x, int y, Sommet sommetCourant)
	{
		int[][] directions = {
			{-1, 0}, {1, 0}, {0, -1}, {0, 1}, 
			{-1, -1}, {-1, 1}, {1, -1}, {1, 1}
		};
		
		for (int i = 0; i < directions.length; i++)
		{
			int dx = directions[i][0];
			int dy = directions[i][1];
			
			int xCherche = x + dx;
			int yCherche = y + dy;
			
			boolean continuer = true;
			while (xCherche >= 0 && xCherche < this.hauteur && yCherche >= 0 && yCherche < this.largeur && continuer)
			{
				Sommet sommetTrouve = this.tabCases[xCherche][yCherche].getSommet();
				
				if (sommetTrouve != null)
				{
					sommetCourant.ajouterVoisin(i, sommetTrouve);
					continuer = false; 
				}
				xCherche += dx;
				yCherche += dy;
			}
		}
	}
	
	public void initBtn()
	{
		for (int i = 0; i < this.hauteur; i++ )
		{
			for(int j = 0; j < this.largeur; j++)
			{
				this.ctrl.initBtn(this.tabCases[i][j] + "", i, j) ;
			}
		}
	}

	public String toString()
	{
		String res = "";

		if (this == null)
			return "Une erreur a été commise pendant la création du plateau";

		for (int i = 0; i < this.hauteur; i++ )
		{
			for(int j = 0; j < this.largeur; j++)
			{
				res += this.tabCases[i][j] ;
			}
			res += "\n";
		}
		
		return res;
	}
	

}