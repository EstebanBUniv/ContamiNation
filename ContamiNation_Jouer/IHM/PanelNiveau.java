package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import ContamiNation_Jouer.IHM.FrameJeu;
import ContamiNation_Jouer.IHM.PanelMenu;

public class PanelNiveau extends JPanel implements ActionListener
{
	private JTable      tabNiveau;
	private JScrollPane scroll;

	private JPanel      panelBoutons;

	private JButton     btnJouer;
	private JButton     btnRetour;

	private ArrayList<File> fichiersDossier;

	private FrameJeu   frameMere;
	private Controleur ctrl;
	
	private Image      imgFond;

	public PanelNiveau(FrameJeu frameMere, Controleur ctrl)
	{
		this.frameMere = frameMere;
		this.ctrl      = ctrl;

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 100, 5, 100));

		this.imgFond = getToolkit().getImage("../images/fond/fond2.png");

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		this.panelBoutons = new JPanel(new FlowLayout());

		this.btnJouer  = new JButton("Jouer" );
		this.btnRetour = new JButton("Retour");

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

		this.add(this.scroll , BorderLayout.CENTER);
		this.add(panelBoutons, BorderLayout.SOUTH );

		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//

		this.btnJouer.addActionListener(this);
		this.btnRetour.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		int[] lignes = this.tabNiveau.getSelectedRows();

		if ( e.getSource() == this.btnJouer )
		{
			if (lignes.length == 1)
			{
				this.ctrl.chargerNiveau(this.fichiersDossier.get(lignes[0]));
				this.frameMere.afficherPlateau();
			}
				
		}

		if ( e.getSource() == this.btnRetour )
		{
			this.frameMere.changerPanel(new PanelMenu(this.ctrl, this.frameMere));
		}
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

	private DefaultTableCellRenderer creerRenderer()
	{
		return new DefaultTableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable t, Object value,
					boolean isSelected, boolean hasFocus, int row, int col)
			{
				super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
				
				if (isSelected)
					setBackground(new Color (127,87,67));
				else
					setBackground(Controleur.COLOR_BACKGROUND);
				
				setForeground(Controleur.COLOR_FOREGROUND);
				
				
				return this;
			}
		};
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics g2 = (Graphics2D) g;
		
		// Ajout de l'image du fond
		if ( imgFond != null )
			g2.drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
	}
}