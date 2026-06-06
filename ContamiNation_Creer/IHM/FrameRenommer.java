package ContamiNation_Creer.IHM;

import javax.swing.JFrame;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.IHM.PanelRenommer;
import ContamiNation_Creer.IHM.PanelSauvegarde;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.io.File;


import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class FrameRenommer extends JFrame
{
	private File       fichier;
	private Controleur ctrl;
	
	public FrameRenommer(File fichier, Controleur ctrl, PanelSauvegarde panelSauvegarde)
	{
		this.fichier    = fichier;
		this.ctrl       = ctrl;

		this.setTitle("Renommer");
		this.setSize(300, 120);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		this.add(new PanelRenommer(this, this.ctrl, this.fichier, panelSauvegarde ));

		

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setVisible(true);
	}
}