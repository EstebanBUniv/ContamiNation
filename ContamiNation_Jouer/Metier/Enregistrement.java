package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class Enregistrement
{

	public static Plateau Recuperer(File fichier, int idJoueur, Controleur ctrl)
	{
		Virus.resetData();

		Plateau plateau = null;
		String nomVirus;
		try (Scanner sc = new Scanner(new FileInputStream(fichier), "UTF-8"))
		{
			int lig      = sc.nextInt();
			int col      = sc.nextInt();
			int nbVirus  = sc.nextInt();
			sc.nextLine(); 
			String nom   = sc.nextLine();
			
			plateau = Plateau.creerPlateau(lig, col, nbVirus, nom, ctrl, idJoueur);
			
			for (int i = 0; i < nbVirus; i++)
			{
				nomVirus = sc.nextLine();
				plateau.creerVirus(nomVirus);
				if (nomVirus.toLowerCase().equals("debiche"))
					ctrl.setModeDebiche();
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
					int estBase = sc.nextInt();
					plateau.ajouterSommet(ligCase, colCase, sommetCase);
					
					if (estBase != 0) 
					{
						Sommet laBase = plateau.getCase(ligCase, colCase).getSommet();
						laBase.setBase(estBase); 
						
						plateau.initialiserBaseVirus(laBase, estBase); 
					}
				}
			}
			plateau.relierTousLesSommets(); 
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return plateau;
	}
}