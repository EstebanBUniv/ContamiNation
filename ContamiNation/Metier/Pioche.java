package ContamiNation.Metier;

public class Pioche
{
	private Carte[] pioche;
	private String[]    nomSymboles = {"Aeroport", "Entrepot", "Hopital", "Laboratoire", "Ville", "Epidemie"};


	public pioche ()
	{
		this.pioche = new Carte[12];
		for (int cpt = 0 ; cpt < this.pioche.length ; cpt++)
		{
			if( cpt%2 == 1)
			{
				this.pioche[cpt] = new Carte(this.nomSymboles[(cpt % this.pioche.length/2)], true);
			}
			else
			{
				this.pioche[cpt] = new Carte(this.nomSymboles[(cpt % this.pioche.length/2)], false);
			}
		}
	}


	public Carte tirerCarte(int nbCarte)
	{
		Carte carteTire;
		
		if (nbCarte <= 0 || nbCarte > this.pioche.length)
			return null;
		
		carteTire = this.pioche[nbCarte];
		this.pioche[nbCarte] = null;
		
		return carteTire;
	}


	public void melanger()
	{
		this.pioche.shuffle();
	}
}