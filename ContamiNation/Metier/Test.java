import iut.algo.*;

public class Test
{
	public static void main (String[] args)
	{
		int largeur, hauteur, nbCouleur;
		System.out.println("Donnez la largeur puis hauteur puis le nombre de couleur");
		largeur = Clavier.lire_int();
		hauteur = Clavier.lire_int();
		nbCouleur = Clavier.lire_int();
		
		Plateau plateau = new Plateau(largeur, hauteur, nbCouleur);
		System.out.println(plateau);
		
		boolean continuer = true;
		while (continuer)
		{
			System.out.println("Voulez-vous continuer ?");
			continuer = Clavier.lireString().equals("oui");
			
			int x, y;
			System.out.println("Coordonnées de la case : ");
			x = Clavier.lire_int() - 1;
			y = Clavier.lire_int() - 1;
			
			plateau.getCase(x,y).ajouterZone(1);
			System.out.println(plateau);
		}
		
		continuer = true;
		while (continuer)
		{
			System.out.println("Voulez-vous continuer ?");
			continuer = Clavier.lireString().equals("oui");
			
			int x, y;
			System.out.println("Coordonnées de la case : ");
			x = Clavier.lire_int() - 1;
			y = Clavier.lire_int() - 1;
			
			plateau.getCase(x,y).ajouterSommet("Test");
			System.out.println(plateau);
		}
	}
}