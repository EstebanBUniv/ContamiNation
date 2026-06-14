package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;
import java.net.*;
import java.io.*;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class ClientJoueur implements Runnable
{
	private Controleur  ctrl;
	private String      ip;
	private int         portSecret;
	private PrintWriter out;
	private boolean     enLigne;
	private java.util.List<String> lignesCarte = new java.util.ArrayList<>();

	public ClientJoueur(Controleur ctrl, String ip, int portSecret)
	{
		this.ctrl       = ctrl;
		this.ip         = ip;
		this.portSecret = portSecret;
		this.enLigne    = true;
	}

	@Override
	public void run()
	{
		try 
		{
			System.out.println(">>> Tentative de connexion à " + ip + ":" + portSecret);
			Socket toServer = new Socket(this.ip, this.portSecret);
			
			this.out = new PrintWriter(toServer.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(toServer.getInputStream()));

			this.out.println("AUTH:" + this.portSecret);
			
			String reponse = in.readLine();
			if ("AUTH_OK".equals(reponse)) 
			{
				System.out.println(">>> Connexion acceptée ! En attente du niveau...");
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this.ctrl.getFrame(), 
						"Connecté avec succès ! \nEn attente que l'hôte choisisse la carte...", 
						"Connexion Réussie", JOptionPane.INFORMATION_MESSAGE);
				});
			} 
			else 
			{
				System.out.println(">>> Connexion refusée par l'hôte.");
				toServer.close();
				return;
			}

			String msg;
			while (this.enLigne && (msg = in.readLine()) != null) 
			{
				System.out.println(">>> Reçu du Serveur : " + msg);
				traiterMessage(msg);
			}

			toServer.close();
		} 
		catch (IOException e) 
		{
			System.err.println(">>> Serveur introuvable ou injoignable.");
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this.ctrl.getFrame(), 
					"Impossible de trouver la partie.\nVérifiez l'IP et le Code secret.", 
					"Erreur de Connexion", JOptionPane.ERROR_MESSAGE);
			});
		}
	}

	private void traiterMessage(String msg) 
	{
		if (msg.startsWith("SEED:")) 
		{
			// LE CLIENT RÉCUPÈRE LA GRAINE DU SERVEUR
			long seed = Long.parseLong(msg.split(":")[1]);
			this.ctrl.setGameSeed(seed);
			System.out.println(">>> Graine reçue, synchronisation parfaite prête !");
		}
		else if (msg.startsWith("CARTE_DATA:")) 
		{
			this.lignesCarte.add(msg.substring(11));
		}
		else if (msg.equals("CARTE_FIN"))
		{
			System.out.println(">>> Réception de la carte terminée.");
			try 
			{
				File tmp = File.createTempFile("carte_reseau_", ".data");
				tmp.deleteOnExit(); 

				try (PrintWriter pw = new PrintWriter(new java.io.FileWriter(tmp))) 
				{
					for (String ligne : this.lignesCarte) pw.println(ligne);
				}
				
				SwingUtilities.invokeLater(() -> {
					java.awt.Window[] windows = java.awt.Window.getWindows();
					for (java.awt.Window window : windows) {
						if (window instanceof javax.swing.JDialog) window.dispose();
					}
					this.ctrl.recevoirCarteDuServeur(tmp);
				});
			} 
			catch (IOException e) { e.printStackTrace(); }
		}
		else if (msg.startsWith("COUP:"))
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
}