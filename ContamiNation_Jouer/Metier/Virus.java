package ContamiNation_Jouer.Metier;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import java.util.LinkedList;
import ContamiNation_Jouer.Metier.Sommet;

/* SAE 2.01 | Développement d'une application 
@author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban
Groupe   : 3
*/

public class Virus
{
	private static int compteurId = 1; 

	private static Map<Integer, Color> couleursVirus = new HashMap<>();
	private LinkedList<Sommet> cheminConquis = new LinkedList<>();

	private static int r = 0;
	private static int g = 0;
	private static int b = 0;

	private int    idVirus;
	private String nom;

	public Virus(String nom)
	{
		this.nom     = nom;
		this.idVirus = Virus.compteurId++;

		if (!couleursVirus.containsKey(this.idVirus)) 
			couleursVirus.put(this.idVirus, nextColor());
	}

	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/

	public int    getId()      { return this.idVirus; }
	public String getNom()     { return this.nom;     }

	public Color  getCouleur() { return couleursVirus.get(this.idVirus); }


	/*----------------------------*/
	/* Méthodes Utilitaires       */
	/*----------------------------*/

	private static Color nextColor()
	{
		r = (r + 89) % 256;
		g = (g + 149) % 256;
		b = (b + 211) % 256;

		return new Color(r, g, b);
	}

	public static void resetData()
	{
		compteurId = 1;
		couleursVirus.clear();
	}

	public static Color getCouleur(int id)
	{
		if (couleursVirus.containsKey(id))
			return couleursVirus.get(id);
		return Color.BLACK;
	}
	
	public void setBaseDepart(Sommet base)
	{
		this.cheminConquis.add(base);
		// base.setProprietaire(this); // (À décommenter plus tard quand on fera la logique des sommets)
	}

	public String toString()
	{
		return this.nom;
	}
}