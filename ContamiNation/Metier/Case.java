public class Case
{
	private final int posX;
	private final int posY;
	
	private Sommet    sommet;
	private int       zone;
	
	
	public Case (int x, int y)
	{
		this.posX   = x;
		this.posY   = y;
		this.zone   = 0;
		this.sommet = null;
	}
	
	public void ajouterZone(int zone)
	{
		this.zone = zone;
	}
	
	public boolean verifZone()
	{
		return this.zone != 0;
	}
	
	public void ajouterSommet(String symbole)
	{
		this.sommet = new Sommet(symbole);
	}
	
	public String toString()
	{
		return "(" + this.posX + "," + this.posY + ";" + this.zone + ";" + this.sommet + ")";
	}
	
}