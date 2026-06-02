package ContamiNation;

import ContamiNation.Metier.*;
import ContamiNation.IHM.*;

public class Controleur
{
	private FramePlateau frame;
	private FrameGrille  grille;

	private Plateau      plateau;

	public Controleur()
	{
		this.frame   = new FramePlateau(this);
	}

	public static void main (String[] args)
	{
		new Controleur();
	}

	public void creerPlateau(int lig, int col, int nbCouleur)
	{
		this.plateau = Plateau.creerPlateau( lig, col, nbCouleur, this);
		this.grille = new FrameGrille(this, lig, col);
		this.plateau.initBtn();
	}
	
	public void initBtn (String val, int lig, int col)
	{
		this.grille.initBtn(val, lig, col);
	}
	
	public void ajouterZone (int lig, int col, int numZone)
	{
		this.plateau.ajouterZone(lig, col, numZone);
		this.plateau.initBtn();
	}

	public Case getCase(int lig, int col)
	{
		return this.plateau.getCase(lig, col);
	}

}