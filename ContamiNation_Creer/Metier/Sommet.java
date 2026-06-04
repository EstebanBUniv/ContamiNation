package ContamiNation_Creer.Metier;

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
	
	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public Sommet (String symbole, Case caseSommet)
	{
		this.symbole    = symbole;
		this.lstVoisins = new Sommet[8];
		this.estBase    = 0;
		this.caseSommet = caseSommet;
	}
	
	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/
	
	public String   getSymbole  () { return this.symbole            ; }
	public Sommet[] getLstVoisin() { return this.lstVoisins         ; }
	public int      getLigSommet() { return this.caseSommet.getLig(); }
	public int      getColSommet() { return this.caseSommet.getCol(); }
	public int      getEstBase()   { return this.estBase            ; }
	
	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/
	
	public void ajouterVoisin(int direction, Sommet voisin)
	{
		this.lstVoisins[direction] = voisin;
	}

	public void retirerVoisin(Sommet s)
	{
		for (int i = 0; i < this.lstVoisins.length; i++)
		{
			if (this.lstVoisins[i] == s)
				this.lstVoisins[i] = null;
    	}
	}
	
	public void resetVoisins()
	{
		this.lstVoisins = new Sommet[8];
	}

	public void setBase(int estBase)
	{
		if (this.estBase == 0)
		{
			this.estBase = estBase;
		}
	}
	
	public String toString ()
	{
		return this.symbole + "\t" + this.estBase;
	}
}