package ContamiNation_Creer.IHM;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;

import java.io.File;

import javax.swing.*;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.IHM.FrameCreer;
import ContamiNation_Creer.IHM.FrameRenommer;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelVirus extends JPanel implements ActionListener
{
	private final int     MARGE = 20;

	private FrameCreer    frameMere;

	private JPanel        panelBouton;

	private JButton       btnValider;
	private JButton       btnAnnuler;
	private JTextField[]  txtNomVirus;
	private JLabel[]      lbNumVirus; 
	
	private int           cptVirus = 0;
	private int           nbVirus;
	private int           lig;
	private int           col;


	public PanelVirus(FrameCreer frame, int nbVirus, int lig, int col)
	{
		this.frameMere = frame;
		this.nbVirus = nbVirus;

		this.lig = lig;
		this.col = col;

		this.setLayout(new GridLayout((this.nbVirus+1),2));

		this.setBorder(BorderFactory.createEmptyBorder(0, this.MARGE, 0, this.MARGE));

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		this.btnValider  = new JButton("Valider");
		this.btnAnnuler  = new JButton("Annuler");

		this.btnValider.setBackground(Controleur.COLOR_BACKGROUND);
		this.btnValider.setForeground(Controleur.COLOR_FOREGROUND);

		this.btnAnnuler.setBackground(Controleur.COLOR_BACKGROUND);
		this.btnAnnuler.setForeground(Controleur.COLOR_FOREGROUND);


		
		this.txtNomVirus = new JTextField[nbVirus];
		this.lbNumVirus  = new JLabel    [nbVirus];

		
		
		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//
	

		
		for (int cpt = 0 ; cpt < this.nbVirus ; cpt++)
			{
				this.lbNumVirus[cpt]  = new JLabel("Nom du Virus n°" + (cpt+1));
				this.txtNomVirus[cpt] = new JTextField(15);
				this.add(this.lbNumVirus[cpt]);
				this.add(this.txtNomVirus[cpt]);
				this.txtNomVirus[cpt].addActionListener( e -> this.valider());
			}

		
		this.add(this.btnValider);
		this.add(this.btnAnnuler);


		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//
		
				
		
		this.btnValider .addActionListener(e -> this.valider());
		this.btnAnnuler .addActionListener(this);

		
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnAnnuler )
			for (int cpt = 0 ; cpt < this.txtNomVirus.length ; cpt++)
				this.txtNomVirus[cpt].setText("");
	}

	private void valider()
	{

		for (int cpt = 0 ; cpt < this.txtNomVirus.length ; cpt++)
		{
			if (this.txtNomVirus[cpt].getText().isBlank())
			{
				JOptionPane.showMessageDialog(this, "Il reste des virus à nommer !", "Attention", JOptionPane.WARNING_MESSAGE);
				return;	
			}
		}

		for (int cpt = 0 ; cpt < this.txtNomVirus.length ; cpt++)
		{
			this.frameMere.creerVirus(this.txtNomVirus[cpt].getText());
		}
		
		this.frameMere.setEstNouveau(true);
		this.frameMere.changerGrille(this.lig, this.col);
		this.frameMere.setEstNouveau(true);
		
		this.frameMere.setResizable(true);


	}
}