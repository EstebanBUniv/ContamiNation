package ContamiNation;

import ContamiNation.Metier.*;
import ContamiNation.IHM.*;
import javax.swing.*;

public class Controleur
{
	private FrameMenu    frame;
	private FrameGrille  grille;
	private FrameSommet  frameSommet;
	private FrameJeu     frameJeu;

	private Plateau      plateau;

	public Controleur()
	{
		this.frame = new FrameMenu(this);
	}

	public static void main (String[] args)
	{
		new Controleur();
	}

	public void creerPlateau(int lig, int col, int nbCouleur)
	{
		this.plateau = Plateau.creerPlateau( lig, col, nbCouleur, this);
		this.grille  = new FrameGrille(this, lig, col);
		this.plateau.initBtn();
	}
	
	public void initBtn(String val, int lig, int col)
	{
		if (this.grille != null)
			this.grille.initBtn(val, lig, col);

		if (this.frameSommet != null)
			this.frameSommet.initBtn(val, lig, col);
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
	
	public void OuvrirSommet()
	{
		this.frameSommet = new FrameSommet(this, this.plateau.getLig(), this.plateau.getCol());
		this.plateau.initBtn();
	}
	
	public void supprimerSommet(int lig, int col)
	{
		this.plateau.supprimerSommet(lig, col);
		this.plateau.relierTousLesSommets();
		this.plateau.initBtn();
	}

	public void ajouterSommet(int lig, int col, String symbole)
	{
		this.plateau.ajouterSommet(lig, col, symbole);
		this.plateau.relierTousLesSommets();
		this.plateau.initBtn();
	}
	public FrameGrille getGrille() { return this.grille; }

	public int getLig(){return plateau.getLig();}

	public int getCol(){return plateau.getCol();}

	public JButton getButton(int lig, int col)
	{
		return this.frameSommet.getPanel().getButton(lig, col);
  }
	
	public void enregistrer()
	{
		this.plateau.enregistrer();
	}
	
	public void charger(int num)
	{
		this.plateau = Enregistrement.Recuperer(num, this);
		
		this.grille = new FrameGrille(this, this.plateau.getLig(), this.plateau.getCol());
		this.plateau.initBtn();
	}

	public void ouvrirJeu(){ this.frameJeu = new FrameJeu(this); }
}