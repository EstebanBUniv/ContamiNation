package ContamiNation.Metier;

public class Case
{
	private final int posLig;
	private final int posCol;
	
	private Sommet    sommet;
	private int       zone;
	
	
	public Case (int lig, int col)
	{
		this.posLig   = lig;
		this.posCol   = col;
		this.zone     = 0;
		this.sommet   = null;
	}
	
	public void ajouterZone(int zone) { this.zone = zone; }
	
	public boolean verifZone()        { return this.zone != 0; }

	public void supprimerZone() { this.zone = 0; }
	
	public void ajouterSommet(String symbole) { this.sommet = new Sommet(symbole); }
	
	public int getZone()      { return this.zone; }
	
	public Sommet getSommet() { return this.sommet; }
	
	public String toString()
	{
		return "(" + this.posLig + "," + this.posCol + ";" + this.zone + ";" + this.sommet + ")";
	}
	
}