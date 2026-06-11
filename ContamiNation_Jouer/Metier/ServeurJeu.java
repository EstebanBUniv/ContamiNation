package ContamiNation_Jouer.Metier;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import iut.algo.*;
import ContamiNation_Jouer.Controleur;


public class ServeurJeu 
{

	private Controleur ctrl;
	
	public ServeurJeu(Controleur ctrl)
	{
		this.ctrl = ctrl;

		System.out.println("Sur quel port voulez-vous être ?");
		int port = Clavier.lire_int(); //Création du serveur
		try (ServerSocket ss = new ServerSocket(port)) 
		{
			while (! this.ctrl.getFin()) //Boucle While pour laisser le serveur allumé
			{
				System.out.println("En attente d'un autre joueur...");
				Socket toClient = ss.accept(); //Le temp qu'un client ne vient pas on attent
				System.out.println("Joueur connecté !");


				PrintWriter out = new PrintWriter(toClient.getOutputStream(), true); //out pour envoyer
				BufferedReader in = new BufferedReader(new InputStreamReader(toClient.getInputStream())); //in pour recevoir

				out.println("Bienvenue sur le Serveur Echo. Tapez quelque chose...");

				String inputLine;

				while ((inputLine = in.readLine()) != null && !inputLine.isEmpty())
				//Boucle While pour lire le message du client et s'arrete si il envoie une chiane vide ou se déconnecte'
				{
					System.out.println("Reçu du client : " + inputLine);
					out.println("Echo : " + inputLine);
				}

				System.out.println("Client déconnecté.");
				toClient.close(); //Fermeture de la connexion
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
