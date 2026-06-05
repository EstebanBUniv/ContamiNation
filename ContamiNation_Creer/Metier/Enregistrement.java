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
		File dossier = new File("../niveaux/");
		if (!dossier.exists()) 
			dossier.mkdirs(); // Crée le dossier et ses parents si besoin
		
		File fichierCible;
		if (this.plateau.getFichierSource() != null) 
			fichierCible = this.plateau.getFichierSource();
		else
			fichierCible = new File("../niveaux/carte_num_" + this.plateau.getNumero() + ".data");
		
		try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fichierCible), "UTF8")))
		{
			pw.println(this.plateau.getLig      ());
			pw.println(this.plateau.getCol      ());
			pw.println(this.plateau.getNbVirus  ());
			pw.println(this.plateau.getNom      ());
			
			for (int cptVirus = 0; cptVirus < this.plateau.getNbVirus(); cptVirus++)
				pw.println(this.plateau.getNomVirus(cptVirus));
			
			for (int lig = 0; lig < this.plateau.getLig(); lig++)
			{
				for (int col = 0; col < this.plateau.getCol(); col++)
				{
					pw.print(this.plateau.getCase(lig, col) + "\t");
				}
				pw.println();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public static Plateau Recuperer(File fichier, Controleur ctrl)
	{
		Plateau plateau = null;

		try
		{
			 Scanner sc = new Scanner(new FileInputStream(fichier));
			

			int    lig       = sc.nextInt ();
			int    col       = sc.nextInt ();
			int    nbVirus   = sc.nextInt ();
			                   sc.nextLine();
			String nom       = sc.nextLine();
			
			plateau = Plateau.creerPlateau(lig, col, nbVirus, nom, ctrl);
			
			for (int i = 0; i < nbVirus; i++)
			{
				String nomVirus = sc.nextLine();
				plateau.creerVirus(nomVirus);
			}
			
			while (sc.hasNextInt())
			{
				int ligCase  = sc.nextInt();
				int colCase  = sc.nextInt();
				int zoneCase = sc.nextInt();
				
				plateau.ajouterZoneDirecte(ligCase, colCase, zoneCase);
				
				String sommetCase = sc.next();
				if (!sommetCase.equals("null"))
				{
					plateau.ajouterSommet(ligCase, colCase, sommetCase);
					int estBase = sc.nextInt();
					if (estBase != 0)
						plateau.getCase(ligCase, colCase).getSommet().setBase(estBase);
				}
			}
			sc.close();
			
			plateau.setFichierSource(fichier);
			plateau.relierTousLesSommets();
		}
		catch (Exception e) { e.printStackTrace(); }
		return plateau;
	}
}