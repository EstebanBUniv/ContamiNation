package ContamiNation_Jouer.Metier;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Sommet
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private String   symbole;
	private Sommet[] lstVoisins;
	private int      estBase;
	private Case     caseSommet;

	private int      nbChemin;
	private boolean  contamine;
	
	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public Sommet (String symbole, Case caseSommet)
	{
		this.symbole    = symbole;
		this.lstVoisins = new Sommet[8];
		this.estBase    = 0;
		this.caseSommet = caseSommet;
		this.nbChemin   = 0;
		this.contamine  = false;
	}
	
	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/
	
	public String   getSymbole  () { return this.symbole            ; }
	public Sommet[] getLstVoisin() { return this.lstVoisins         ; }
	public int      getLigSommet() { return this.caseSommet.getLig(); }
	public int      getColSommet() { return this.caseSommet.getCol(); }
	public int      getEstBase  () { return this.estBase            ; }
	public int      getNbChemin () { return this.nbChemin           ; }
	public boolean  getContamine() { return this.contamine          ; }
	
	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/

	// Ajoute un voisin selon une direction spécifique (0-7)
	public void ajouterVoisin(int direction, Sommet voisin)
	{
		this.lstVoisins[direction] = voisin;
		this.nbChemin++;
	}
	
	// Supprime un voisin spécifique de la liste
	public void retirerVoisin(Sommet s)
	{
		for (int i = 0; i < this.lstVoisins.length; i++)
		{
			if (this.lstVoisins[i] == s)
			{
				this.lstVoisins[i] = null;
				this.nbChemin--;
			}
    	}
	}
	
	// Réinitialise toutes les connexions du sommet
	public void resetVoisins()
	{
		this.lstVoisins = new Sommet[8];
		this.nbChemin   = 0;
	}
	
	// Définit ce sommet comme base pour un virus donné
	public void setBase(int estBase)
	{
		if (this.estBase == 0)
		{
			this.estBase = estBase;
		}
	}
	
	// Supprime le statut de base de ce sommet
	public void retirerBase()
	{
		this.estBase = 0;
	}
	
	public String toString ()
	{
		return this.symbole + "\t" + this.estBase;
	}
	
	public void resetContamine() { this.contamine = false; }

}