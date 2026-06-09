package ContamiNation_Jouer;

import ContamiNation_Jouer.IHM.*;
import javax.swing.JFrame;

import ContamiNation_Jouer.Metier.Pioche;
import ContamiNation_Jouer.Metier.Carte;
import ContamiNation_Jouer.Metier.Plateau;

import java.awt.Color;

import ContamiNation_Jouer.IHM.PanelPlateau;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

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

	public static final Color COLOR_BACKGROUND = new Color( 58, 111, 134); // couleur de fond
	public static final Color COLOR_FOREGROUND = new Color(230, 230, 230); // couleur de texte

	private FrameJeu frame;
	private Plateau  plateau;
	private Pioche   pioche;

	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/

	public Controleur()
	{
		this.frame = new FrameJeu(this);
	}

	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/

	public Plateau getPlateau()
	{
		return this.plateau;
	}

	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/

	public void chargerNiveau(File fichier)
	{
		this.plateau = ContamiNation_Jouer.Metier.Enregistrement.Recuperer(fichier, this);
	}

	public static void main (String[] args)
	{
		new Controleur();
	}

	public void initierPioche ()
	{
		this.pioche = new Pioche();
	}

	public void melangerPioche()
	{
		this.pioche.melanger();
	}


	public Carte tirerCarte(int indice)
	{
		return this.pioche.tirerCarte(indice);
	}

	public Carte premiereCarte()
	{
		return this.pioche.premiereCarte();
	}


	public boolean verifFinManche()
	{
		return this.pioche.verifFinManche();
	}
}