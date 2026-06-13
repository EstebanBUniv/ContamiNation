package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class FrameJeu extends JFrame
{
	// Attribut d'instance
	private Controleur   ctrl;
	private JPanel       panel;

	private PanelPlateau[] panelPlateau;
	private PanelPioche    panelPioche;

	private CardLayout cardLayout;
	private JPanel     conteneurPlateaux;  


	/*----------------------------*/
	/*  Constructeur              */
	/*----------------------------*/

	public FrameJeu(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("ContamiNation");
		this.setSize (900, 600);
		this.setMinimumSize(new Dimension(600, 300));
		this.setLocationRelativeTo(null);

		this.panel = new PanelMenu  (this, this.ctrl);

		this.add(this.panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/

	public PanelPlateau getPanelPlateau(int idJoueur)
	{
		return this.panelPlateau[idJoueur];
	}

	public JPanel[][] getTabPanel(int idJoueur)
	{
		return this.panelPlateau[idJoueur].getTabPanel();
	}

	/*----------------------------*/
	/*  Setters                   */
	/*----------------------------*/

	public void setPanelPioche(PanelPioche panel)
	{
		if ( this.panelPioche != null )
			this.remove(this.panelPioche);
		this.panelPioche = new PanelPioche(this, ctrl);
	}

	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/

	// Méthode permettant de changer le panel de la frame avec celui rentré en paramètre
	public void changerPanel(JPanel panel)
	{
		this.remove(this.panel);
		this.panel = panel;
		this.add(this.panel);
		this.revalidate();
		this.repaint();
	}

	// Méthode qui renvoi un JLabel contenant le titre sous forme d'image
	public JLabel creerTitre(int num)
	{
		String chemin = "../images/entête/Titre" + num + ".png";
		
		ImageIcon icon    = new ImageIcon(chemin);
		int       largeur = (int)(this.getWidth() * 0.60);
		int       hauteur = icon.getIconHeight() * largeur / icon.getIconWidth();
		Image     img     = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);

		JLabel label = new JLabel(new ImageIcon(img));
   		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

		return label;
	}

	public void afficherPlateau(int nbJoueurs)
	{
		this.setLayout(new BorderLayout());

		this.cardLayout = new CardLayout();
		this.conteneurPlateaux = new JPanel(this.cardLayout);


		this.panelPlateau = new PanelPlateau[nbJoueurs];
		
		for (int i = 0; i < nbJoueurs; i++) 
		{
			this.panelPlateau[i] = new PanelPlateau(this, this.ctrl, i);
			PanelArrete  panelArrete   = new PanelArrete(this.ctrl, i, panelPlateau[i]);

			// Superposition du plateau et des arrêtes pour CHAQUE joueur
			JPanel splitPanel = new JPanel(null) 
			{
				public boolean isOptimizedDrawingEnabled() { return false; }
			};

			// Gestion des redimensionnements par plateau individuel
			int index = i;
			splitPanel.addComponentListener(new ComponentAdapter() 
			{
				public void componentResized(ComponentEvent e) 
				{
					int w = splitPanel.getWidth();
					int h = splitPanel.getHeight();
					panelPlateau[index].setBounds(0, 0, w, h);
					panelArrete.setBounds(0, 0, w, h);
					panelPlateau[index].revalidate();
					panelArrete.repaint();
				}
			});

			splitPanel.add(panelArrete);
			splitPanel.add(panelPlateau[i]);
			
			conteneurPlateaux.add(splitPanel, "joueur" + i);
		}

		this.changerPanel(conteneurPlateaux);
		this.add(this.panelPioche, BorderLayout.WEST); // La pioche reste partagée sur le côté
		
	}

	public void afficherPlateauJoueur(int idJoueurActuel)
	{
		if (this.cardLayout != null && this.conteneurPlateaux != null) 
		{
			// On demande au CardLayout d'afficher le panneau correspondant à l'identifiant
			this.cardLayout.show(this.conteneurPlateaux, "joueur" + idJoueurActuel);
			
			this.conteneurPlateaux.revalidate();
			this.conteneurPlateaux.repaint();
		}
	}

	// Permet de mettre a jour l'affichage de la pioche
	public void reinitierPanelPioche()
	{
		this.panelPioche.passerTour();
	}
	
	public int demanderChoixBoucle()
	{
		Object[] options = {"Côté Tête", "Côté Queue"};
		int reponse = javax.swing.JOptionPane.showOptionDialog(this, 
				"Votre virus forme une boucle ! De quel côté voulez-vous vous brancher ?", 
				"Choix de connexion", 
				javax.swing.JOptionPane.YES_NO_OPTION, 
				javax.swing.JOptionPane.QUESTION_MESSAGE, 
				null, options, options[1]);
				
		if (reponse == 0) return 1; // 1 = Tête
		if (reponse == 1) return 2; // 2 = Queue
		return 0; // Au cas où on ferme la fenêtre sans répondre
	}

	/*
	 * Appelée par le Controleur quand un sommet-extrémité est cliqué.
	 * estClique   : true = on entre en mode sélection, false = on en sort
	 * caseCliquee : la case dont le sommet vient d'être sélectionné
	 * Le repaint() global suffit : chaque PanelCase interroge
	 * ctrl.getCaseSelectionnee() dans son paintComponent.
	 */
	public void SommetClique()
	{
		this.repaint();
	}

	public void carteChoisie()
	{
		this.panelPioche.carteChoisie();
	}

	public void nouvelleManche()
	{
		javax.swing.JOptionPane.showOptionDialog(this, 
				"Une nouvelle manche vient de se lancer !", 
				"Nouvelle Manche", 
				javax.swing.JOptionPane.DEFAULT_OPTION, 
				javax.swing.JOptionPane.PLAIN_MESSAGE, 
				null, null, null);
	}

	public void incrNbPasse()
	{
		this.panelPioche.incrNbPasse();
	}

}