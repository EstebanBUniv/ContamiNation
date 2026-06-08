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
}