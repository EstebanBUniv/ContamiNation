package ContamiNation_Jouer.Metier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pioche
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private List<Carte> pioche;
	private Carte       carteTire;
	private String[]    nomSymboles = {"Aeroport", "Entrepot", "Hopital", "Laboratoire", "Ville", "Epidemie"};
	
	public Pioche ()
	{
		int nbCarte = 12;
		this.pioche = new ArrayList<>();
		this.carteTire = null;
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
	
	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/
	
	public Carte getCarteTire()       { return this.carteTire          ; }
	public Carte premiereCarte()      { return this.pioche.get(0)      ; }
	public Carte getCarte(int indice) { return this.pioche.get(indice) ; }
	public int   getTaillePioche()    { return this.pioche.size()      ; }
	
	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/
	
	public void melanger()
	{
		Collections.shuffle(this.pioche);
	}
	
	public Carte tirerCarte(int indiceCarte)
	{
		Carte carteTire;
		
		if (indiceCarte < 0 || indiceCarte > this.pioche.size())
			return null;
		
		this.carteTire = this.pioche.get(indiceCarte);
		
		this.pioche.remove(indiceCarte);
		
		return this.carteTire;
	}
	
	public boolean verifFinManche ()
	{
		for ( Carte c : pioche)
		{
			if (!c.getEstClair())
				return false;
		}
		return true;
	}
	
	public void setCarteTiree(int indice) 
	{ 
		this.carteTire = this.pioche.get(indice); 
		this.pioche.remove(indice);
	}

	public String toString()
	{
		String sRep = "";
		for ( Carte c : pioche)
			sRep += c + " , ";
		return sRep;
	}
}