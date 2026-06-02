import java.util.ArrayList;

public class Plateau
{
	private final ArrayList<Sommet> SOMMETS = new ArrayList<Sommet>();
   
	private int      largeur;
	private int      hauteur;
	private int      nbCouleur;
	private String[] couleurs;
	private int      tailleCases;
	private Case[][] tabCases;

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
	
	public Plateau(int largeur, int hauteur, int nbCouleur)
	{
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

	public String toString()
	{
		String res = "";

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