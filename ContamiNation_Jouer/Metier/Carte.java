package ContamiNation_Jouer.Metier;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Carte
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private String  symbole;
	private boolean estClair;
	
	public Carte (String symbole, boolean estClair)
	{
		this.symbole  = symbole;
		this.estClair = estClair;
	}
	
	/*------------*/
	/*   getters  */
	/*------------*/
	public boolean getEstClair () { return this.estClair; }
	public String  getSymbole  () { return this.symbole ; }
	
	public String toString()
	{
		return this.symbole + "_" + ((this.estClair) ? "clair" : "fonce");
	}
}