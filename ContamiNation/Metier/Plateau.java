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

	public Plateau(int largeur, int hauteur, int nbCouleur)
	{
		this.largeur   = largeur;
		this.hauteur   = hauteur;
		this.nbCouleur = nbCouleur;

		this.couleurs    = new String[this.nbCouleur];
		this.tailleCases = 50;
		this.tabCases    = new Case[this.largeur][this.hauteur];
	}

	public Case getCase(int x, int y)
	{
		return this.tabCases[x][y];
	}
	

}