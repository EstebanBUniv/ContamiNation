package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelMulti extends JPanel implements ActionListener
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/
	
	private Controleur ctrl;
	
	private FrameJeu   frameMere;

	private JPanel     panelCentre;

	private JTextField txtServeurClient;
	private JTextField txtIpServeur;
	private JButton    btnLocal;
	private JButton    btnReseauClient;
	private JButton    btnReseauServeur;
	private JButton    btnRetour;

	private Image      imgFond;
	private Graphics2D g2;
	
	/*----------------------------*/
	/*  Constructeur de la classe */
	/*----------------------------*/
	
	public PanelMulti(FrameJeu frameMere, Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.frameMere = frameMere;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.imgFond = getToolkit().getImage("../images/fond/fond2.png");

		this.setOpaque(false);
		
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/

		this.panelCentre = new JPanel(new GridLayout(8, 1, 0, 15));
		this.panelCentre.setBorder(BorderFactory.createEmptyBorder(0, (int)(this.frameMere.getWidth()*0.2),
		                                                           0, (int)(this.frameMere.getWidth()*0.2)
															      ));
		this.panelCentre.setOpaque(false);

		this.txtServeurClient = new JTextField(10);
		this.txtIpServeur     = new JTextField("localhost", 10);
		
		this.btnLocal         = new JButton("Jouer sur un PC");
		this.btnReseauClient  = new JButton("Etre Client"    );
		this.btnReseauServeur = new JButton("Etre serveur"   );
		this.btnRetour        = new JButton("Retour"         );

		JButton[] tabBtn = {this.btnLocal, this.btnReseauClient, this.btnReseauServeur, this.btnRetour};
		for ( JButton btn : tabBtn )
		{
			btn.setBackground(Controleur.COLOR_BACKGROUND);
			btn.setForeground(Controleur.COLOR_FOREGROUND);
		}

		/*-------------------------------*/
		/* positionnement des composants */
		/*-------------------------------*/
		
		this.panelCentre.add(this.btnLocal        );
		this.panelCentre.add(this.btnReseauServeur);
		this.panelCentre.add(this.btnReseauClient );
		this.panelCentre.add(new JLabel("Entrez un port de connexion :"));
		this.panelCentre.add(this.txtServeurClient);
		this.panelCentre.add(new JLabel("Adresse IP de l'hôte : "));
		this.panelCentre.add(this.txtIpServeur    );
		this.panelCentre.add(this.btnRetour       );

		this.add(this.frameMere.creerTitre(2));
		this.add(Box.createVerticalGlue());
		this.add(this.panelCentre);
		this.add(Box.createVerticalGlue());

		/* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
		
		for ( JButton btn : tabBtn )
			btn.addActionListener(this);
	}


	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnLocal) // Demande la sélection de niveau en mettant true au modeMulti
		{
			this.frameMere.changerPanel(new PanelNiveau(this.frameMere, this.ctrl, true));
		}

		if (e.getSource() == this.btnReseauClient) // Créer le client
		{
			String portTxt = this.txtServeurClient.getText().trim();
			String ipTxt   = this.txtIpServeur.getText().trim();

			if (portTxt.matches("[0-9]+") && !ipTxt.isEmpty())
			{
				this.ctrl.lancerClient(ipTxt, Integer.parseInt(portTxt));
			}
			else
			{
				javax.swing.JOptionPane.showMessageDialog(
					this,
					"Entre une adresse IP et un port valides.",
					"Connexion impossible",
					javax.swing.JOptionPane.WARNING_MESSAGE
				);
			}
		}

		if (e.getSource() == this.btnReseauServeur) // Créer le serveur
		{
			String portTxt = this.txtServeurClient.getText().trim();

			if (portTxt.matches("[0-9]+"))
			{
				int port = Integer.parseInt(portTxt);
				this.ctrl.lancerServeur(port);

				String ipLocale;
				try
				{
					ipLocale = java.net.InetAddress.getLocalHost().getHostAddress();
				}
				catch (java.net.UnknownHostException ex)
				{
					ipLocale = "IP introuvable";
				}

				javax.swing.JOptionPane.showMessageDialog(
					this.frameMere,
					"Serveur lancé !\n\nAdresse IP : " + ipLocale + "\nPort : " + port,
					"Informations de connexion",
					javax.swing.JOptionPane.INFORMATION_MESSAGE
				);
			}
			else
			{
				javax.swing.JOptionPane.showMessageDialog(
					this,
					"Entre un numéro de port valide (ex: 12345).",
					"Port invalide",
					javax.swing.JOptionPane.WARNING_MESSAGE
				);
			}
		}

		if (e.getSource() == this.btnRetour)
		{
			this.frameMere.changerPanel(new PanelMenu(this.frameMere, this.ctrl));
		}
	}
	
	//Change l'image de fond
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		this.g2 = (Graphics2D) g;
		
		// Ajout de l'image du fond
		if ( imgFond != null )
			this.g2.drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
	}
}

