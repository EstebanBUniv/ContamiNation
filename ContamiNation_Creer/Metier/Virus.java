package ContamiNation_Creer.Metier;

public class Virus
{

	private String     nom;
	private static int nbVirus = 0;
	private int        idVirus;

	public Virus(String nom)
	{
		this.nom = nom;
		this.nbVirus = this.idVirus++;
	}

	public String getNom()
	{
		return this.nom;
	}
}