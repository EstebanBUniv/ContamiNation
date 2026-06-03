package ContamiNation.Metier;

import ContamiNation.Controleur;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.util.Scanner;
import java.io.FileInputStream;


public class Enregistrement
{
	private Plateau plateau;
	
	public Enregistrement(Plateau plateau)
	{
		this.plateau = plateau;
	}
	
	public void enregistrer()
	{
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("./niveaux/carte_num_" + this.plateau.getNumero() + ".data"), "UTF8"));
			
			pw.println(this.plateau.getLig());
			pw.println(this.plateau.getCol());
			pw.println(this.plateau.getNbCouleur());
			pw.println(this.plateau.getNom());
			
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
	
	public static Plateau Recuperer(int numPlateau, Controleur ctrl)
	{
		Plateau plateau = null;

		try
		{
			Scanner sc = new Scanner(new FileInputStream("./niveaux/carte_num_" + numPlateau + ".data"));

			int    lig       = Integer.parseInt(sc.nextLine().trim());
			int    col       = Integer.parseInt(sc.nextLine().trim());
			int    nbCouleur = Integer.parseInt(sc.nextLine().trim());
			String nom       = sc.nextLine().trim();


			plateau = Plateau.creerPlateau(lig, col, nbCouleur, ctrl);

			plateau.setNom(nom);

			while (sc.hasNextLine())
			{
				String ligne = sc.nextLine().trim();
				if (ligne.isEmpty()) continue; // ignore les lignes vides

				String[] parties = ligne.split("\t");

				for (String partie : parties)
				{
					partie = partie.trim();
					if (partie.isEmpty()) continue;

					// Format attendu depuis toString() de Case : "(lig,col;zone;sommet)"
					partie = partie.replace("(", "").replace(")", "");
					String[] vals = partie.split("[;,]");

					int ligCase  = Integer.parseInt(vals[0].trim());
					int colCase  = Integer.parseInt(vals[1].trim());
					int zoneCase = Integer.parseInt(vals[2].trim());

					plateau.ajouterZone(ligCase, colCase, zoneCase);

					String sommetCase = vals[3].trim();
					if (!sommetCase.equals("null") && !sommetCase.isEmpty())
						plateau.ajouterSommet(ligCase, colCase, sommetCase);
				}
			}

			sc.close();
		}
		catch (Exception e) { e.printStackTrace(); }
		return plateau;
	}
}