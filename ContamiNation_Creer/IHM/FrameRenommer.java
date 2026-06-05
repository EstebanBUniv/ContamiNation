package ContamiNation_Creer.IHM;

import javax.swing.JFrame;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.IHM.PanelRenommer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.io.File;


import javax.swing.*;

public class FrameRenommer extends JFrame
{
	private File       fichier;
	private Controleur ctrl;
	private int        ligne;
	
	public FrameRenommer(File fichier, Controleur ctrl, int ligne, PanelSauvegarde panelSauvegarde)
	{
		this.fichier    = fichier;
		this.ctrl       = ctrl;
		this.ligne      = ligne;

		this.setSize(300, 120);
		this.setLocationRelativeTo(null);
		this.add(new PanelRenommer(this, this.ctrl, this.fichier, this.ligne, panelSauvegarde ));

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setVisible(true);
	}
}