import java.util.ArrayList;

public class Plateau
{
	private final ArrayList<Sommet> SOMMETS = new ArrayList<Sommet>();
   
	private int largeur;
	private int hauteur;
	private int nbCouleur;
	private String[] couleurs;
	private int tailleCases;
	private Case[][] tabCases;

	private void creaCase()
    {
        for (int i = 0; i<this.hauteur; i++ )
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

    public String toString()
    {
        String res = "";

        for (int i = 0; i<this.hauteur; i++ )
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