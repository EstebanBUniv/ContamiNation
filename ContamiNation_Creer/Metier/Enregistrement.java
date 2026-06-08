package ContamiNation_Creer.Metier;

import ContamiNation_Creer.Controleur;
import java.io.*;
import java.util.Scanner;

/* SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class Enregistrement
{
	private Plateau plateau;
	
	// Constructeur
	public Enregistrement(Plateau plateau)
	{
		this.plateau = plateau;
	}
	
	//Méthode

	/**
	 * Sauvegarde l'état du plateau dans un fichier texte au format .data.
	 * Utilise PrintWriter pour une écrire en UTF-8.
	 */
	public void enregistrer()
	{
		File dossier = new File("../niveaux/");
		if (!dossier.exists()) dossier.mkdirs();
		
		File fichierCible = (this.plateau.getFichierSource() != null) 
						   ? this.plateau.getFichierSource() 
						   : new File("../niveaux/carte_num_" + this.plateau.getNumero() + ".data");
		
		try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fichierCible), "UTF-8")))
		{
			// Entête du fichier : dimensions, nombre de virus et nom
			pw.println(this.plateau.getLig());
			pw.println(this.plateau.getCol());
			pw.println(this.plateau.getNbVirus());
			pw.println(this.plateau.getNom());
			
			// Liste des virus
			for (int cptVirus = 0; cptVirus < this.plateau.getNbVirus(); cptVirus++)
				pw.println(this.plateau.getNomVirus(cptVirus));
			
			// État de chaque case du plateau
			for (int lig = 0; lig < this.plateau.getLig(); lig++)
			{
				for (int col = 0; col < this.plateau.getCol(); col++)
					pw.print(this.plateau.getCase(lig, col) + "\t");
				pw.println();
			}
		}
		catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Charge un plateau depuis un fichier .data.
	 * Reconstruit la structure des sommets et des zones.
	 */
	public static Plateau Recuperer(File fichier, Controleur ctrl)
	{
		Plateau plateau = null;

		try (Scanner sc = new Scanner(new FileInputStream(fichier), "UTF-8"))
		{
			// Lecture des paramètres globaux
			int lig      = sc.nextInt();
			int col      = sc.nextInt();
			int nbVirus  = sc.nextInt();
			sc.nextLine(); // retourne a la ligne pour avoir le nom
			String nom   = sc.nextLine();
			
			plateau = Plateau.creerPlateau(lig, col, nbVirus, nom, ctrl);
			
			// Récupération des virus
			for (int i = 0; i < nbVirus; i++)
				plateau.creerVirus(sc.nextLine());
			
			// Récupération des cases (zone + sommet + base)
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
			
			plateau.setFichierSource(fichier);
			plateau.relierTousLesSommets();
		}
		catch (IOException e) { e.printStackTrace(); }
		
		return plateau;
	}
}