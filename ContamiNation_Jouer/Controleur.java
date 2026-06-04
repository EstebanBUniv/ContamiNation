package ContamiNation_Jouer;

import ContamiNation_Jouer.IHM.*;
import javax.swing.*;

import ContamiNation_Jouer.IHM.FrameJeu;

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

	private FrameJeu frame;

	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/

	public Controleur()
	{
		this.frame = new FrameJeu(this);
	}

	public static void main (String[] args)
	{
		new Controleur();
	}

}