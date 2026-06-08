package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Plateau
{
	private Controleur ctrl;
	private int        col;
	private int        lig;
	private int        nbVirus;
	private String     nom;
	private String[]   couleurs;
	//private Case[][]   tabCases;
	private File       fichierSource = null;

	public static Plateau creerPlateau(int lig, int col, int nbVirus, String nom, Controleur ctrl)
	{
		if ( col <= 0 || lig <= 0 || nbVirus <=0)
				return null;
		return new Plateau(lig, col, nbVirus, nom, ctrl);
	}

	private Plateau(int lig, int col, int nbVirus, String nom, Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.col  = col;
		this.lig  = lig;
		this.nom  = nom;

		this.nbVirus   = nbVirus;
		this.couleurs  = new String[this.nbVirus];
	}

	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/

	public int getLig() { return this.lig; }
	public int getCol() { return this.col; }

	/*----------------------------*/
	/*  Setters                   */
	/*----------------------------*/

	public void setFichierSource(File fichier)
	{
		this.fichierSource = fichier;
	}
	
	public void enregistrer()
	{
		Enregistrement save = new Enregistrement(this);
		save.enregistrer();
	}

	public String toString()
	{
		String res = "";

		for (int i = 0; i < this.lig; i++ )
		{
			for(int j = 0; j < this.col; j++)
			{
				res += this.tabCases[i][j] ;
			}
			res += "\n";
		}
		
		return res;
	}

	public void creerVirus(String nom)
	{
		this.lstVirus.add(new Virus(nom));
	}


	public void supprimerZone(int lig, int col)
	{
		int zoneCible = this.tabCases[lig][col].getZone();
		
		if (zoneCible == 0)
			return;
			
		int totalCasesZone = 0;
		int ligDepart      = -1;
		int colDepart      = -1;
		
		for (int i = 0; i < this.lig; i++)
			for (int j = 0; j < this.col; j++)
				if (this.tabCases[i][j].getZone() == zoneCible)
				{
					totalCasesZone++;
					if (i != lig || j != col)
					{
						ligDepart = i;
						colDepart = j;
					}
				}
				
		if (totalCasesZone <= 1)
		{
			this.tabCases[lig][col].supprimerZone();
			return;
		}
		
		this.tabCases[lig][col].supprimerZone();
		
		boolean[][] visite          = new boolean[this.lig][this.col];
		int         casesConnectees = this.compterCasesConnectees(ligDepart, colDepart, zoneCible, visite);
		
		if (casesConnectees < totalCasesZone - 1)
			this.tabCases[lig][col].ajouterZone(zoneCible);
	}

	private int compterCasesConnectees(int l, int c, int zoneCible, boolean[][] visite)
	{
		if (l < 0 || l >= this.lig || c < 0 || c >= this.col)
			return 0;
			
		if (visite[l][c] || this.tabCases[l][c].getZone() != zoneCible)
			return 0;
			
		visite[l][c] = true;
		int nb = 1;
		
		nb += this.compterCasesConnectees(l - 1, c, zoneCible, visite);
		nb += this.compterCasesConnectees(l + 1, c, zoneCible, visite);
		nb += this.compterCasesConnectees(l, c - 1, zoneCible, visite);
		nb += this.compterCasesConnectees(l, c + 1, zoneCible, visite);
		
		return nb;
	}


	public boolean VerifChemin( Case caseAVerif)
	{
		if (! case.getAUnSommet() && case.getEstTraverse())
			return false;
		if (case.getNbChemin() == 0)
			return false;
		return true;
	}
	

}
