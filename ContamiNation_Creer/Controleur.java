package ContamiNation_Creer;

import ContamiNation_Creer.Metier.*;
import ContamiNation_Creer.IHM.*;
import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

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
	
	private FrameCreer          frame;
	private FrameSommet         frameSommet;
	private Plateau             plateau;
	private Map<Integer, Color> couleursZones = new HashMap<>();
	
	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public Controleur()
	{
		this.frame = new FrameCreer(this);
		this.frameSommet = null;
	}
	
	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/
	
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
	
	public int getNbVirus() { return this.plateau.getNbVirus(); }

	public Map<Integer, Color> getCouleurZone() { return this.couleursZones; }
	
	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/
	
	public void creerPlateau(int lig, int col, int nbCouleur, String nomPlateau)
	{
		this.plateau = Plateau.creerPlateau( lig, col, nbCouleur, nomPlateau, this);
	}

	public void changerPanel(int lig, int col)
	{
		this.frame.changerPanel(new PanelGrille(lig, col, this, true, this.frameSommet));
		this.frame.ajouterPanel();
		this.plateau.initBtn();
	}
	
	public void initBtn(String val, int lig, int col)
	{
		if (this.frame.getPanel() instanceof PanelGrille)
			this.frame.initBtn(val, lig, col);

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
		this.OuvrirCreer();
	}

	public void OuvrirCreer()
	{
		this.frame = new FrameCreer(this);
	}
	
	public void charger(File fichier)
	{
		this.plateau = Enregistrement.Recuperer(fichier, this);
		this.frame.changerPanel(new PanelGrille(this.plateau.getLig(), this.plateau.getCol(), this, true, this.frameSommet));
		this.frame.ajouterPanel();
		this.plateau.initBtn();
	}
	
	public static void main (String[] args)
	{
		new Controleur();
	}
	
	public int nbPlateau()
	{
		int num = 0;
		while (new File("../niveaux/carte_num_" + num + ".data").exists())
			num++;
		return num;
	}

	public void supprimerZone(int lig, int col)
	{
		this.plateau.supprimerZone(lig, col);
		this.plateau.initBtn();
	}

	public void creerVirus(String nom)
	{
		this.plateau.creerVirus(nom);
	}
}