package ContamiNation_Creer.Metier;

import ContamiNation_Creer.Controleur;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.File;

import java.util.Scanner;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Enregistrement
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private Plateau plateau;
	
	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public Enregistrement(Plateau plateau)
	{
		this.plateau = plateau;
	}
	
	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/
	
	public void enregistrer()
	{
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("../niveaux/carte_num_" + this.plateau.getNumero() + ".data"), "UTF8"));
			
			pw.println(this.plateau.getLig      ());
			pw.println(this.plateau.getCol      ());
			pw.println(this.plateau.getNbCouleur());
			pw.println(this.plateau.getNom      ());
			
			for (int lig = 0; lig < this.plateau.getLig(); lig++)
			{
				for (int col = 0; col < this.plateau.getCol(); col++)
				{
					pw.print(this.plateau.getCase(lig, col) + "\t");
				}
				pw.println();
			}
			pw.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public static Plateau Recuperer(File fichier, Controleur ctrl)
	{
		Plateau plateau = null;

		try
		{
			 Scanner sc = new Scanner(new FileInputStream(fichier));
			
			int    lig       = sc.nextInt();
			int    col       = sc.nextInt();
			int    nbCouleur = sc.nextInt();
			String nom       = sc.next   ();
			
			plateau = Plateau.creerPlateau(lig, col, nbCouleur, nom, ctrl);
			
			while (sc.hasNextInt())
			{
				int ligCase  = sc.nextInt();
				int colCase  = sc.nextInt();
				int zoneCase = sc.nextInt();
				
				plateau.ajouterZone(ligCase, colCase, zoneCase);
				
				String sommetCase = sc.next();
				if (!sommetCase.equals("null"))
					plateau.ajouterSommet(ligCase, colCase, sommetCase);
			}
			sc.close();
		}
		catch (Exception e) { e.printStackTrace(); }
		return plateau;
	}
}