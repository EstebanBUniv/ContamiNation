package ContamiNation_Creer.Metier;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/* SAE 2.01 | Développement d'une application 
@author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban
Groupe   : 3
*/

public class Virus
{
	private static int compteurId = 1; 

	private static Map<Integer, Color> couleursVirus = new HashMap<>();

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
		{
			couleursVirus.put(this.idVirus, nextColor());
		}
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
	
	// Génère une couleur via une progression arithmétique modulo 256.
    // Les nombres 89, 149 et 211 sont des nombres premiers, 
    // ce qui assure une bonne distribution des couleurs.
	private static Color nextColor()
	{
		r = (r + 89) % 256;
		g = (g + 149) % 256;
		b = (b + 211) % 256;

		return new Color(r, g, b);
	}

	// Réinitialise les compteurs lors de la création d'une nouvelle partie.
	public static void resetData()
	{
		compteurId = 1;
		couleursVirus.clear();
	}
	
	// Récupère la couleur associée à un ID virus.
    // Retourne noir par défaut si l'ID n'est pas trouvé.
	public static Color getCouleur(int id)
	{
		if (couleursVirus.containsKey(id))
			return couleursVirus.get(id);
		return Color.BLACK;
	}

	public String toString()
	{
		return this.nom;
	}
}