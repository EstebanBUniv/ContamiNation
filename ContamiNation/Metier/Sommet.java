package ContamiNation.Metier;

public class Sommet
{
	private String symbole;
	private String couleur;
	private Sommet[] lstVoisins;
	private boolean  estBase;
	private Case     case;
	
	
	public Sommet (String symbole)
	{
		this.symbole    = symbole;
		this.couleur    = null;
		this.lstVoisins = new Sommet[8];
		this.estBase    = false;
	}
	public String getSymbole() { return this.symbole; }
	
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
	
	public String toString ()
	{
		String rep = "";
		for (int i = 0; i < this.lstVoisins.length; i++)
		{
			if (this.lstVoisins[i] != null)
				rep += "" + i;
		}
		return rep + this.symbole;
	}
}