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
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("carte_num_" + this.plateau.getNumero() + ".data"), "UTF8"));
			
			pw.println(this.plateau.getLig());
			pw.println(this.plateau.getCol());
			pw.println(this.plateau.getNbCouleur());
			
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
			Scanner sc = new Scanner(new FileInputStream("carte_num_" + numPlateau + ".data"));

			int lig       = Integer.parseInt(sc.next());
			int col       = Integer.parseInt(sc.next());
			int nbCouleur = Integer.parseInt(sc.next());

			plateau = Plateau.creerPlateau(lig, col, nbCouleur, ctrl);

			while (sc.hasNext())
			{
				int ligCase  = Integer.parseInt(sc.next());
				int colCase  = Integer.parseInt(sc.next());
				int zoneCase = Integer.parseInt(sc.next());

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