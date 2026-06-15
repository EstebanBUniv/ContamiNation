package ContamiNation_Jouer.Metier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Pioche
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private List<Carte> pioche;
	private Carte       carteTire;
	private String[]    nomSymboles;
	
	public Pioche (String [] nomSymboles)
	{
		
		this.nomSymboles = nomSymboles;
		
		int nbCarte = this.nomSymboles.length * 2;
		this.pioche = new ArrayList<>();
		this.carteTire = null;
		
		for (int cpt = 0 ; cpt < nbCarte ; cpt++)
		{
			int indexSymbole = (cpt / 2) % this.nomSymboles.length;

			if (cpt % 2 == 1)
			{
				this.pioche.add(new Carte(this.nomSymboles[indexSymbole], true));
			}
			else
			{
				this.pioche.add(new Carte(this.nomSymboles[indexSymbole], false));
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
	
	//pour la synchronisation réseau
	public void melangerReseau(long seed)
	{
		Collections.shuffle(this.pioche, new Random(seed));
	}
	
	//permet de retirer la carte tirer de la pioche 
	public Carte tirerCarte(int indiceCarte)
	{
	
		if (indiceCarte < 0 || indiceCarte >= this.pioche.size())
			return null;
		
		this.carteTire = this.pioche.get(indiceCarte);
		
		this.pioche.remove(indiceCarte);
		
		return this.carteTire;
	}
	
	//vérifie s'il reste des cartes foncé dans la pioche
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