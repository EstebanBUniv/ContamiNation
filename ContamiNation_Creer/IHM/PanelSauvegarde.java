package ContamiNation_Creer.IHM;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelSauvegarde extends JPanel implements ActionListener
{
	private JTable      tabSauvegarde;
	private JScrollPane scroll;

	private JButton btnNouveau;
	private JButton btnModifier;
	private JButton btnSupprimer;
	private JButton btnRenommer;
	private JButton btnQuitter;	

	private JPanel panelTitre;
	private JPanel panelSauvegarde;
	private JPanel panelBoutons;
	private JPanel panelBtnHaut;
	private JPanel panelBtnBas;

	private ArrayList<File> fichiersDossier;

	private FrameCreer frameMere;

	public PanelSauvegarde(FrameCreer frameMere)
	{
		this.frameMere = frameMere;

		this.setLayout(new BorderLayout());

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//
		
		this.panelTitre      = new JPanel(new FlowLayout  ()    );
		this.panelSauvegarde = new JPanel(new BorderLayout()    );
		this.panelBoutons    = new JPanel(new GridLayout  (2, 1));
		this.panelBtnHaut    = new JPanel(new FlowLayout  ()    );
		this.panelBtnBas     = new JPanel(new FlowLayout  ()    );

		this.tabSauvegarde = new JTable(this.getFichier("../niveaux/"), new String[]{"nom"});
		this.tabSauvegarde.setRowHeight(50);
		this.tabSauvegarde.setTableHeader(null);
		this.scroll        = new JScrollPane(this.tabSauvegarde);

		this.btnNouveau    = new JButton("Nouveau plateau");
		this.btnModifier   = new JButton("Modifier"       );
		this.btnSupprimer  = new JButton("Supprimer"      );
		this.btnRenommer   = new JButton("Renommer"       );
		this.btnQuitter    = new JButton("Quitter"        );

		this.btnNouveau  .setPreferredSize(new Dimension(205, 30));
		this.btnModifier .setPreferredSize(new Dimension(205, 30));
		this.btnQuitter  .setPreferredSize(new Dimension(135, 30));
		this.btnRenommer .setPreferredSize(new Dimension(135, 30));
		this.btnSupprimer.setPreferredSize(new Dimension(135, 30));

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//
		
		this.panelTitre.add(new JLabel("Selectionner un plateau"));

		this.panelSauvegarde.add(Box.createHorizontalStrut(100), BorderLayout.WEST  );
		this.panelSauvegarde.add(this.scroll,                    BorderLayout.CENTER);
		this.panelSauvegarde.add(Box.createHorizontalStrut(100), BorderLayout.EAST  );

		this.panelBtnHaut.add(this.btnModifier );
		this.panelBtnHaut.add(this.btnNouveau  );

		this.panelBtnBas.add(this.btnSupprimer );
		this.panelBtnBas.add(this.btnRenommer  );
		this.panelBtnBas.add(this.btnQuitter   );

		this.panelBoutons.add(this.panelBtnHaut);
		this.panelBoutons.add(this.panelBtnBas );

		this.add(this.panelTitre     , BorderLayout.NORTH );
		this.add(this.panelSauvegarde, BorderLayout.CENTER);
		this.add(this.panelBoutons   , BorderLayout.SOUTH );

		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//
		this.btnNouveau  .addActionListener(this);
		this.btnQuitter  .addActionListener(this);
		this.btnRenommer .addActionListener(this);
		this.btnSupprimer.addActionListener(this);
		this.btnModifier .addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		int ligne = this.tabSauvegarde.getSelectedRow();

		if (e.getSource() == this.btnQuitter)
		{
			this.frameMere.dispose();
		}

		if (e.getSource() == this.btnSupprimer)
		{
			if (ligne != -1)
			{
				this.fichiersDossier.get(ligne).delete();
				this.rafraichir();
			}
		}
		
		if (e.getSource() == this.btnModifier)
			this.frameMere.charger(this.fichiersDossier.get(ligne));

		if (e.getSource() == this.btnNouveau)
			this.frameMere.creerPlateau();
	}

	public void rafraichir()
	{
		this.tabSauvegarde.setModel(
			new DefaultTableModel(this.getFichier("../niveaux/"), new String[]{"nom"})
		);
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

		String[][] tabPlateau = new String[this.fichiersDossier.size()][1];

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
	
}