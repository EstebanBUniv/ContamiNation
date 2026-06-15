package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Random;

import javax.swing.SwingUtilities;

public class ServeurJeu implements Runnable
{
	private Controleur   ctrl;
	private int          portSecret; 
	private PrintWriter  out;
	private boolean      enLigne;
	private ServerSocket ss;

	public ServeurJeu(Controleur ctrl, int portSecret)
	{
		this.ctrl       = ctrl;
		this.portSecret = portSecret;
		this.enLigne    = true;
	}

	public void run()
	{
		try
		{
			this.ss = new ServerSocket(this.portSecret);
			System.out.println(">>> Serveur en écoute. Code PIN / Port : " + this.portSecret);
			Socket toClient = ss.accept(); 
			System.out.println(">>> Un joueur tente de se connecter...");

			this.out = new PrintWriter(toClient.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));

			String messageAuth = in.readLine();
			if (messageAuth != null && messageAuth.equals("AUTH:" + this.portSecret)) 
			{
				this.out.println("AUTH_OK");
				
				//Le serveur génère la graine et l'envoie
				long seed = new Random().nextLong();
				this.ctrl.setGameSeed(seed);
				this.out.println("SEED:" + seed);
				
				System.out.println(">>> Authentification validée !");
				SwingUtilities.invokeLater(() -> this.ctrl.clientConnecte());
			} 
			else 
			{
				this.out.println("AUTH_REFUSE");
				toClient.close();
				return;
			}

			String msg;
			while (this.enLigne && (msg = in.readLine()) != null) 
			{
				System.out.println(">>> Reçu du Client : " + msg);
				traiterMessage(msg);
			}

			toClient.close();
		} 
		catch (IOException e) 
		{
			System.err.println(">>> Erreur Serveur : " + e.getMessage());
		}
	}

	private void traiterMessage(String msg) 
	{
		if (msg.startsWith("COUP:"))
		{
			String[] parts = msg.split(":");
			int ligDep = Integer.parseInt(parts[1]);
			int colDep = Integer.parseInt(parts[2]);
			int ligArr = Integer.parseInt(parts[3]);
			int colArr = Integer.parseInt(parts[4]);

			this.ctrl.recevoirCoupReseau(ligDep, colDep, ligArr, colArr);
		}
		else if (msg.equals("PASSER")) 
		{
			System.out.println(">>> L'adversaire a passé son tour !");
			this.ctrl.recevoirPasserReseau();
		}
	}

	public void envoyerMessage(String msg) 
	{
		if (this.out != null) this.out.println(msg);
	}
	
	public void envoyerFichier(File fichier)
	{
		System.out.println(">>> Début de l'envoi de la carte : " + fichier.getName());
		try (BufferedReader br = new BufferedReader(new FileReader(fichier)))
		{
			String ligne;
			while ((ligne = br.readLine()) != null)
			{
				this.out.println("CARTE_DATA:" + ligne);
			}
			this.out.println("CARTE_FIN");
		}
		catch (IOException e) { System.err.println(">>> Erreur : " + e.getMessage()); }
	}
	
	public void forcerArret() 
	{
		this.enLigne = false;
		try { if (this.ss != null) this.ss.close(); } catch (Exception e) {}
	}
}