package ContamiNation_Creer;

import ContamiNation_Creer.Metier.*;
import ContamiNation_Creer.IHM.*;
import javax.swing.*;
import java.io.File;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Controleur
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private FrameMenu    frame;
	private FrameGrille  grille;
	private FrameSommet  frameSommet;
	private Plateau      plateau;
	
	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public Controleur()
	{
		this.frame = new FrameMenu(this);
	}
	
	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/
	
	public FrameGrille getGrille() { return this.grille; }
	
	public int getLig() { return plateau.getLig() ; }
	
	public int getCol() { return plateau.getCol() ; }
	
	public JButton getButton(int lig, int col)
	{
		return this.frameSommet.getPanelGrille().getButton(lig, col);
	}
	
	public Case getCase(int lig, int col)
	{
		return this.plateau.getCase(lig, col);
	}
	
	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/
	
	public void creerPlateau(int lig, int col, int nbCouleur, String nomPlateau)
	{
		this.plateau = Plateau.creerPlateau( lig, col, nbCouleur, nomPlateau, this);
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

	
	
	public void ouvrirSommet()
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
	
	
	public void enregistrer()
	{
		this.plateau.enregistrer();
	}
	
	public void charger(File fichier)
	{
		this.plateau = Enregistrement.Recuperer(fichier, this);
		
		this.grille = new FrameGrille(this, this.plateau.getLig(), this.plateau.getCol());
		this.plateau.initBtn();
	}
	
	public static void main (String[] args)
	{
		new Controleur();
	}
	
	public int nbPlateau()
	{
		int num = 0;
		while (new File("./niveaux/carte_num_" + num + ".data").exists())
			num++;
		return num;
	}
}