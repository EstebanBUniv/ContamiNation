package ContamiNation_Creer.Metier;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Case
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private int       posLig;
	private int       posCol;
	
	private Sommet    sommet;
	private int       zone;
	
	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public Case (int lig, int col)
	{
		this.posLig   = lig;
		this.posCol   = col;
		this.zone     = 0;
		this.sommet   = null;
	}
	
	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/
	
	public int    getZone  () { return this.zone   ; }
	public int    getLig   () { return this.posLig ; }
	public int    getCol   () { return this.posCol ; }
	public Sommet getSommet() { return this.sommet ; }
	
	/*----------------------------*/
	/*  Setter                    */
	/*----------------------------*/
	
	public void ajouterZone  (int    zone   ) { this.zone   = zone; }
	public void ajouterSommet(String symbole) { this.sommet = new Sommet(symbole, this); }
	
	public void supprimerZone  () { this.zone   = 0    ; }
	public void supprimerSommet() { this.sommet = null ;}
	
	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/
	
	public boolean verifZone()        { return this.zone != 0; }
	
	public String toString()
	{
		return this.posLig + "\t" + this.posCol + "\t" + this.zone + "\t" + this.sommet;
	}
	
}