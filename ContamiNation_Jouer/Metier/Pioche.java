package ContamiNation_Jouer.Metier;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Pioche
{
	private List<Carte> pioche;
	private String[]    nomSymboles = {"Aeroport", "Entrepot", "Hopital", "Laboratoire", "Ville", "Epidemie"};


	public Pioche ()
	{
		int nbCarte = 12;
		this.pioche = new ArrayList<>();
		for (int cpt = 0 ; cpt < nbCarte ; cpt++)
		{
			if( cpt%2 == 1)
			{
				this.pioche.add(new Carte(this.nomSymboles[(cpt % nbCarte/2)], true));
			}
			else
			{
				this.pioche.add(new Carte(this.nomSymboles[(cpt % nbCarte/2)], false));
			}
		}
	}


	public Carte tirerCarte(int indiceCarte)
	{
		Carte carteTire;
		
		if (indiceCarte <= 0 || indiceCarte > this.pioche.size())
			return null;
		
		carteTire = this.pioche.get(indiceCarte);
		this.pioche.remove(indiceCarte);
		
		return carteTire;
	}


	public void melanger()
	{
		Collections.shuffle(this.pioche);
	}
}