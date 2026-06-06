package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.IHM.FrameRenommer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.Component;
import java.awt.BasicStroke;

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelSauvegarde extends JPanel implements ActionListener
{
	//--------------------------------//
	// Constantes                     //
	//--------------------------------//

	private static final Color COLOR_BACKGROUND = new Color( 37,  37,  37);
	private static final Color COLOR_FOREGROUND = new Color(210, 210, 210);
	private static final Color COLOR_SELECT     = new Color( 70,  70,  70);

	//--------------------------------//
	// Variables                      //
	//--------------------------------//

	// Table et barre de défilement
	private JTable      tabSauvegarde;
	private JScrollPane scroll;

	// Boutons
	private JButton btnModifier;
	private JButton btnNouveau;
	private JButton btnSupprimer;
	private JButton btnRenommer;
	private JButton btnQuitter;
	private JButton btnCopier;

	// Panels
	private JPanel panelSauvegarde;
	private JPanel panelBoutons;
	private JPanel panelBtnHaut;
	private JPanel panelBtnBas;
	private JPanel panelMain;

	private ArrayList<File> fichiersDossier;

	private FrameCreer frameMere;
	private Controleur ctrl;
	
	private Image      imgFond;

	public PanelSauvegarde(FrameCreer frameMere, Controleur ctrl)
	{
		this.frameMere = frameMere;
		this.ctrl      = ctrl;

		this.setLayout(new BorderLayout());

		this.imgFond = getToolkit().getImage("./images/fond/fond.png");

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//
		
		this.panelSauvegarde = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.panelBoutons    = new JPanel(new GridLayout(2, 1, 5, 5));
		this.panelBtnHaut    = new JPanel(new GridLayout(1, 2, 5, 5));
		this.panelBtnBas     = new JPanel(new GridLayout(1, 4, 5, 5));

		this.panelMain       = new JPanel(new BorderLayout());
		
		// ajoute des marges a gauche et a droite de panelMain (table + boutons)
		this.panelMain.setBorder(BorderFactory.createEmptyBorder(5, this.frameMere.MARGE, 5, this.frameMere.MARGE));

		// tout les panel transparent pour voir l'image de fond
		this.panelMain      .setOpaque(false);
		this.panelBoutons   .setOpaque(false);
		this.panelBtnHaut   .setOpaque(false);
		this.panelBtnBas    .setOpaque(false);
		this.panelSauvegarde.setOpaque(false);

		this.tabSauvegarde = new JTable(this.getFichier("../niveaux/"), new String[]{"nom"});
		this.tabSauvegarde.setRowHeight      (50);
		this.tabSauvegarde.setTableHeader    (null);                                     // retire l'entête de la table
		this.tabSauvegarde.setDefaultEditor  (Object.class, null);                       // Empêche l'edition des cellules
		this.tabSauvegarde.setDefaultRenderer(Object.class, this.creerRenderer());       // modifie l'aspect des cellules

		this.scroll = new JScrollPane(this.tabSauvegarde);

		JButton[] tabBtn = new JButton[6];

		tabBtn[0] = this.btnNouveau    = new JButton("Nouveau plateau");
		tabBtn[1] = this.btnModifier   = new JButton("Modifier"       );
		tabBtn[2] = this.btnSupprimer  = new JButton("Supprimer"      );
		tabBtn[3] = this.btnRenommer   = new JButton("Renommer"       );
		tabBtn[4] = this.btnCopier     = new JButton("Copier"         );
		tabBtn[5] = this.btnQuitter    = new JButton("Quitter"        );

		for ( JButton btn : tabBtn )
		{
			btn.setBackground(PanelSauvegarde.COLOR_BACKGROUND); // change la couleur des boutons en gris foncé
			btn.setForeground(PanelSauvegarde.COLOR_FOREGROUND); // change la couleur du texte des boutons en gris clair
		}

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//
		
		this.panelSauvegarde.add(this.scroll);

		this.panelBtnHaut.add(this.btnModifier );
		this.panelBtnHaut.add(this.btnNouveau  );

		this.panelBtnBas.add(this.btnSupprimer );
		this.panelBtnBas.add(this.btnRenommer  );
		this.panelBtnBas.add(this.btnCopier    );
		this.panelBtnBas.add(this.btnQuitter   );

		this.panelBoutons.add(this.panelBtnHaut);
		this.panelBoutons.add(this.panelBtnBas );

		// ajoute une image en entête du Panel principal
		this.add(this.creerTitre()             , BorderLayout.NORTH);

		this.panelMain.add(this.scroll         , BorderLayout.CENTER);
		this.panelMain.add(this.panelBoutons   , BorderLayout.SOUTH );

		this.add(this.panelMain);

		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//

		for ( JButton btn : tabBtn )
			btn.addActionListener(this);
	}


	public void actionPerformed(ActionEvent e)
	{
		int[] lignes = this.tabSauvegarde.getSelectedRows(); // tab des cellules sélectionnées

		if (e.getSource() == this.btnQuitter)
		{
			this.frameMere.dispose();
		}

		if (e.getSource() == this.btnSupprimer)
		{
			if (lignes.length > 0)
			{
				for (int i = lignes.length - 1; i >= 0; i--)
				{
					this.fichiersDossier.get(lignes[i]).delete();
				}

				this.rafraichir();
			}
		}			
		
		if (e.getSource() == this.btnModifier)
			 if (lignes.length == 1)
                this.frameMere.charger(this.fichiersDossier.get(lignes[0]));

		if (e.getSource() == this.btnNouveau)
			this.frameMere.creerPlateau();

		if ( e.getSource() == this.btnRenommer )
			 if (lignes.length == 1)
				new FrameRenommer(this.fichiersDossier.get(lignes[0]), this.ctrl, lignes[0], this );
		
		if ( e.getSource() == this.btnCopier )
		{
			if (lignes.length == 1)
			{
				this.ctrl.copier(this.fichiersDossier.get(lignes[0]));
				this.rafraichir();
			}
		}		
	}

	// actualise l'affichage des cellules de tabSauvegarde
	public void rafraichir()
	{
		this.tabSauvegarde.setModel(
			new DefaultTableModel(this.getFichier("../niveaux/"), new String[]{"nom"})
		);
	}

	public void supprimerFichier(int ligne)
	{
		this.fichiersDossier.get(ligne).delete();
		this.rafraichir();
	}

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

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// Ajout de l'image du fond
		if ( imgFond != null )
		{
			((Graphics2D) g).drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
		}
	}

	// retourne un JLabel avec l'image Titre.png
	private JLabel creerTitre()
	{
		ImageIcon icon    = new ImageIcon("./images/Titre.png");
		int       largeur = (int)(this.frameMere.getWidth() * 0.60);
		int       hauteur = icon.getIconHeight() * largeur / icon.getIconWidth();
		Image     img     = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);
		return new JLabel(new ImageIcon(img));
	}

	// change l'aspect des cellules de tabSauvegarde
	private DefaultTableCellRenderer creerRenderer()
	{
		return new DefaultTableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable t, Object value,
					boolean isSelected, boolean hasFocus, int row, int col)
			{
				super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
				
				if (isSelected)
					setBackground(PanelSauvegarde.COLOR_SELECT);
				else
					setBackground(PanelSauvegarde.COLOR_BACKGROUND);
				
				setForeground(PanelSauvegarde.COLOR_FOREGROUND);
				
				
				return this;
			}
		};
	}
}