public class Sommet
{
	private String symbole;
	private String couleur;
	private Sommet[] lstVoisins;
	private boolean  estBase;
	
	public Sommet (String symbole)
	{
		this.symbole = symbole;
		this.couleur = null;
		this.lstVoisins = new Sommet[8];
		this.estBase = false;
	}
	
	public String toString ()
	{
		return this.symbole;
	}
}