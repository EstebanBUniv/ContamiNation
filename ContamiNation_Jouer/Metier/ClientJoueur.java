package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;

import iut.algo.*;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientJoueur
{

	private Controleur ctrl;

	public ClientJoueur(Controleur ctrl, int ip)
	{
		this.ctrl = ctrl;

		int port = ip;

		
		

		try
		{
			Socket toServer = new Socket("test", port); //Connection au serveur

			PrintWriter out = new PrintWriter(toServer.getOutputStream(), true); //out pour envoyer
			BufferedReader in = new BufferedReader(new InputStreamReader(toServer.getInputStream())); //in pour recevoir

			String message = "";

			String banniere = in.readLine(); //Lecture du message serveur
			System.out.println("Message reçu du serveur: " + banniere);

			do //Boucle Do While pour lire le clavier et envoyer le message le temp qu'il est différent d'une chaine vide
			{
				message = Clavier.lireString();
				out.println(message);
			} while (!message.isEmpty());

			//Fermeture de la connexion
			
			in.close();
			toServer.close();
		} catch (UnknownHostException e) {
			System.err.println("Serveur introuvable");
		} catch (IOException e) {
			System.err.println("Erreur de connexion");
		}
	}
}
