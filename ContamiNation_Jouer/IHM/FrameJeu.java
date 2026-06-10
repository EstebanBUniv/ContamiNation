package ContamiNation_Jouer.IHM;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ContamiNation_Jouer.Controleur;
import ContamiNation_Jouer.Metier.Case;
import ContamiNation_Jouer.Controleur;

public class FrameJeu extends JFrame
{
	// Attribut d'instance
	private Controleur   ctrl;
	private JPanel       panel;

	private PanelPlateau panelPlateau;
	private PanelArrete  panelArrete;
	private PanelPioche  panelPioche;


	/*----------------------------*/
	/*  Constructeur              */
	/*----------------------------*/

	public FrameJeu(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("ContamiNation");
		this.setSize(900, 600);
		this.setMinimumSize(new Dimension(600, 300));
		this.setLocationRelativeTo(null);

		this.panelPioche = new PanelPioche(this.ctrl);
		this.panel       = new PanelMenu(this.ctrl, this);

		this.add(this.panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.ctrl.melangerPioche();
		this.panelPioche.passerTour();
	}


	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/


	//----------------//
	//    Getters     //
	//----------------//
	public PanelPlateau getPanelPlateau()
	{
		return this.panelPlateau;
	}

	public JPanel[][] getTabPanel()
	{
		return this.panelPlateau.getTabPanel();
	}


	/*----------------------------*/
	/*  Méthodes                  */
	/*----------------------------*/

	//---------------//
	//    Méthodes   //
	//---------------//

	// Méthode permettant de changer le panel de la frame avec celui rentré en paramètre
	public void changerPanel(JPanel panel)
	{
		this.remove(this.panel);
		this.panel = panel;
		this.add(this.panel);
		this.revalidate();
		this.repaint();
	}

	// Méthode qui permet l'affichage correcte du plateau de jeu
	public void afficherPlateau()
	{
		this.setLayout(new BorderLayout());
		this.panelPlateau = new PanelPlateau(this, this.ctrl);
		this.panelArrete  = new PanelArrete(this.ctrl);

		JPanel centerPanel = new JPanel(null)
		{
			public boolean isOptimizedDrawingEnabled() { return false; }
		};

		centerPanel.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				int w = centerPanel.getWidth();
				int h = centerPanel.getHeight();
				panelPlateau.setBounds(0, 0, w, h);
				panelArrete.setBounds(0, 0, w, h);
				panelPlateau.revalidate();
				panelArrete.repaint();
			}
		});

		centerPanel.add(this.panelArrete);
		centerPanel.add(this.panelPlateau);

		this.changerPanel(centerPanel);
		this.add(this.panelPioche, BorderLayout.WEST);
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

	public void nouvelleManche()
	{
		javax.swing.JOptionPane.showOptionDialog(this, 
				"Une nouvelle manche vient de se lancer !", 
				"Nouvelle Manche", 
				javax.swing.JOptionPane.DEFAULT_OPTION, 
				javax.swing.JOptionPane.PLAIN_MESSAGE, 
				null, null, null);
	}

}