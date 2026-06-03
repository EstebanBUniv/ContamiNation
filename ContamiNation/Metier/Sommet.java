package ContamiNation.Metier;

public class Sommet
{
	private String   symbole;
	private String   couleur;
	private Sommet[] lstVoisins;
	private boolean  estBase;
	private Case     caseSommet;
	
	public Sommet (String symbole, Case caseSommet)
	{
		this.symbole    = symbole;
		this.couleur    = null;
		this.lstVoisins = new Sommet[8];
		this.estBase    = false;
		this.caseSommet      = caseSommet;
	}
	public String getSymbole() { return this.symbole; }

	public Sommet[] getLstVoisin(){return this.lstVoisins;}
	
	public void ajouterVoisin(int direction, Sommet voisin)
	{
		this.lstVoisins[direction] = voisin;
	}

	public int getLigSommet()
	{
		return this.caseSommet.getLig();
	}

	public int getColSommet()
	{
		return this.caseSommet.getCol();
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
		return this.symbole;
	}
}