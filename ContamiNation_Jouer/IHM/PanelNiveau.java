package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Scanner;

public class PanelNiveau extends JPanel implements ActionListener
{
	// Attribut d'instance
	private JTable      tabNiveau;
	private JScrollPane scroll;

	private JPanel      panelBoutons;
	private JPanel      panelCentre;

	private JButton     btnJouer;
	private JButton     btnRetour;

	private ArrayList<File> fichiersDossier;

	private FrameJeu   frameMere;
	private Controleur ctrl;
	
	private Image      imgFond;
	
	private boolean    estMulti;
	private boolean estServeurReseau;
	
	public PanelNiveau(FrameJeu frameMere, Controleur ctrl, boolean estMulti)
	{
		this(frameMere, ctrl, estMulti, false);
	}
	
	public PanelNiveau(FrameJeu frameMere, Controleur ctrl, boolean estMulti, boolean estServeurReseau)
	{
		this.frameMere = frameMere;
		this.ctrl      = ctrl;
		this.estMulti  = estMulti;
		this.estServeurReseau = estServeurReseau;

		this.setLayout(new BorderLayout());

		this.imgFond = getToolkit().getImage("../images/fond/fond2.png");

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		this.panelBoutons = new JPanel(new FlowLayout  ());
		this.panelBoutons.setOpaque(false);
		
		this.panelCentre  = new JPanel(new BorderLayout());
		this.panelCentre.setBorder(BorderFactory.createEmptyBorder(5, 200, 5, 200));
		this.panelCentre.setOpaque(false);

		this.btnJouer  = new JButton("Jouer" );
		this.btnRetour = new JButton("Retour");

		JButton[] tabBtn = {this.btnJouer, this.btnRetour};
		for ( JButton btn : tabBtn )
		{
			btn.setBackground(Controleur.COLOR_BACKGROUND);
			btn.setForeground(Controleur.COLOR_FOREGROUND);
			btn.setFont      (Controleur.POLICE_TEXTE    );
		}

		this.tabNiveau = new JTable(this.getFichier("../niveaux/"), new String[]{"nom"});

		this.tabNiveau.setRowHeight      (50);
		this.tabNiveau.setTableHeader    (null);                               // retire l'entête de la table
		this.tabNiveau.setDefaultEditor  (Object.class, null);                 // Empêche l'edition des cellules
		this.tabNiveau.setDefaultRenderer(Object.class, this.creerRenderer()); // modifie l'aspect des cellules

		this.scroll = new JScrollPane(this.tabNiveau);

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//

		this.panelBoutons.add(this.btnJouer );
		this.panelBoutons.add(this.btnRetour);
		this.panelCentre.add(this.scroll);

		this.add(this.frameMere.creerTitre(0), BorderLayout.NORTH);
		this.add(this.panelCentre            , BorderLayout.CENTER);
		this.add(this.panelBoutons           , BorderLayout.SOUTH );

		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//

		this.btnJouer.addActionListener(this);
		this.btnRetour.addActionListener(this);
	}

	//-------------//
	//   getters   //
	//-------------//
	public String[][] getFichier(String chemin)
	{
		File   dossier  = new File(chemin);
		File[] fichiers = dossier.listFiles();

		if (fichiers == null || fichiers.length == 0)
			return new String[0][1];

		this.fichiersDossier = new ArrayList<>();
		for (File f : fichiers)
			if (f.isFile() && f.getName().endsWith(".data"))
				this.fichiersDossier.add(f);

		String[][] tabPlateau = new String[this.fichiersDossier.size()][2];

		for (int i = 0; i < this.fichiersDossier.size(); i++)
		{
			try
			{
				Scanner sc = new Scanner(new FileInputStream(this.fichiersDossier.get(i)));
				sc.nextLine(); // lig
				sc.nextLine(); // col
				sc.nextLine(); // nbCouleur
				tabPlateau[i][0] = sc.nextLine(); // nom
				sc.close();
			}
			catch (Exception ex) { ex.printStackTrace(); }
		}

		return tabPlateau;
	}

	public void actionPerformed(ActionEvent e)
	{
		int[] lignes = this.tabNiveau.getSelectedRows();

		if ( e.getSource() == this.btnJouer )
		{
			if (lignes.length == 1)
			{
				File fichierLvl = this.fichiersDossier.get(lignes[0]);
				
				if (this.estServeurReseau) 
				{
					System.out.println(">>> Démarrage de la partie réseau !");
					this.ctrl.envoyerCarteAuClient(fichierLvl); // Envoie le .data
					this.ctrl.chargerNiveau(fichierLvl, 2);     // Charge pour 2 joueurs
				}
				if (!this.estMulti) 
				{
					this.ctrl.chargerNiveau(fichierLvl, 1);
				} 
				else if (!this.estServeurReseau)
				{
					Object[] options = {"2 Joueurs", "3 Joueurs", "4 Joueurs"};
					int choix = javax.swing.JOptionPane.showOptionDialog
					(
						this, 
						"Combien de joueurs vont s'affronter ?", 
						"Configuration Multijoueur", 
						javax.swing.JOptionPane.DEFAULT_OPTION, 
						javax.swing.JOptionPane.QUESTION_MESSAGE, 
						null, options, options[0]
					);
						if (choix != javax.swing.JOptionPane.CLOSED_OPTION) 
						{
							int nbJoueurs = choix + 2;
							this.ctrl.chargerNiveau(fichierLvl, nbJoueurs);
						}

				}
			}
		}

		if ( e.getSource() == this.btnRetour )
		{
			this.frameMere.changerPanel(new PanelMenu(this.frameMere, this.ctrl));
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics g2 = (Graphics2D) g;
		
		if ( imgFond != null )
			g2.drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
	}

	private DefaultTableCellRenderer creerRenderer()
	{
		return new DefaultTableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable t, Object value,
					boolean isSelected, boolean hasFocus, int row, int col)
			{
				super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
				
				if (isSelected)
					setBackground(Controleur.COLO_EST_SELECT);
				else
					setBackground(Controleur.COLOR_BACKGROUND);
				
				setForeground(Controleur.COLOR_FOREGROUND);
				setFont      (Controleur.POLICE_TEXTE    );
				
				
				return this;
			}
		};
	}
}