package ContamiNation.IHM;

import ContamiNation.Controleur;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.concurrent.Flow;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.Border;

public class PanelPlateau extends JPanel implements ActionListener
{
	private JTable      tabSauvegarde;
	private JScrollPane scroll;

	private JButton btnNouveau;
	private JButton btnModifier;
	private JButton btnSupprimer;
	private JButton btnRetour;
	private JButton btnRenommer;
	private JButton btnJouer;

	private FramePlateau frameMere;

	private JPanel panelTitre;

	private JPanel panelSauvegarde;

	private JPanel panelBoutons;
	private JPanel panelBtnHaut;
	private JPanel panelBtnBas;

	

	public PanelPlateau(FramePlateau frameMere)
	{
		this.frameMere = frameMere;
		
		this.setLayout(new BorderLayout());

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//
		this.panelTitre      = new JPanel(new FlowLayout  ());
		
		this.panelSauvegarde = new JPanel(new BorderLayout());

		this.panelBoutons    = new JPanel(new GridLayout(2, 1));
		this.panelBtnHaut    = new JPanel(new FlowLayout  ());
		this.panelBtnBas     = new JPanel(new FlowLayout  ());		
		
	this.tabSauvegarde = new JTable(this.getFichier("./niveaux/"), new String[]{"nom"});
		this.tabSauvegarde.setRowHeight(50);
		this.tabSauvegarde.setTableHeader(null);
		this.scroll        = new JScrollPane(this.tabSauvegarde);

		this.btnNouveau   = new JButton("Nouveau plateau");
		this.btnModifier  = new JButton("Modifier");
		this.btnSupprimer = new JButton("Supprimer");
		this.btnRenommer  = new JButton("Renommer");
		this.btnRetour    = new JButton("Retour");
		this.btnJouer     = new JButton("Jouer");

		this.btnNouveau  .setPreferredSize(new Dimension(205, 30));
		this.btnJouer    .setPreferredSize(new Dimension(205, 30));
		this.btnRenommer .setPreferredSize(new Dimension(100, 30));
		this.btnSupprimer.setPreferredSize(new Dimension(100, 30));
		this.btnModifier .setPreferredSize(new Dimension(100, 30));
		this.btnRetour   .setPreferredSize(new Dimension(100, 30));

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//
		this.panelTitre.add(new JLabel("Selectionner un plateau"));
		
		this.panelSauvegarde.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
		this.panelSauvegarde.add(this.scroll, BorderLayout.CENTER);
		this.panelSauvegarde.add(Box.createHorizontalStrut(100), BorderLayout.EAST);
		
		this.panelBtnHaut.add(this.btnJouer   );
		this.panelBtnHaut.add(this.btnNouveau );

		this.panelBtnBas.add(this.btnRenommer );
		this.panelBtnBas.add(this.btnSupprimer);
		this.panelBtnBas.add(this.btnModifier );
		this.panelBtnBas.add(this.btnRetour   );

		this.panelBoutons.add(this.panelBtnHaut);
		this.panelBoutons.add(this.panelBtnBas);

		this.add(this.panelTitre     , BorderLayout.NORTH );
		this.add(this.panelSauvegarde, BorderLayout.CENTER);
		this.add(this.panelBoutons   , BorderLayout.SOUTH );

		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//
		this.btnNouveau  .addActionListener(this);
		this.btnJouer    .addActionListener(this);
		this.btnRenommer .addActionListener(this);
		this.btnSupprimer.addActionListener(this);
		this.btnModifier .addActionListener(this);
		this.btnRetour   .addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnJouer )
		{
			if ( this.tabSauvegarde.getSelectedRow() != -1 )
				this.frameMere.charger(this.tabSauvegarde.getSelectedRow());
		}

		if ( e.getSource() == this.btnNouveau )
		{
			this.frameMere.creerPlateau();
		}
	}

	public String[][] getFichier(String chemin)
	{
		File dossier = new File(chemin);

		int cpt = 0;

		for (File f : dossier.listFiles())
			if (f.isFile() && f.getName().endsWith(".data"))
				cpt++;

	String[][] tabPlateau = new String[1][cpt];

		try
		{
			for ( cpt = 0; cpt < tabPlateau.length; cpt++ )
			{
				Scanner sc = new Scanner(new FileInputStream(chemin + "carte_num_" + cpt + ".data"));
				
				sc.nextLine();
				sc.nextLine();
				sc.nextLine();

				tabPlateau[0][cpt] = sc.nextLine();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
			

		return tabPlateau;
	}
}
