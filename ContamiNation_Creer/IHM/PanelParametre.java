package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.IHM.PanelVirus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelParametre extends JPanel implements ActionListener
{
	private FrameCreer frameMere;

	private JButton btnValider;
	private JButton btnAnnuler;
	private JButton btnRetour;

	private JTextField txtLigne;
	private JTextField txtColonne;
	private JTextField txtNbVirus;
	private JTextField txtNomPlateau;

	private JPanel panelBouton;
	private JPanel panelTxt;

	private Image  imgFond;

	public PanelParametre(FrameCreer frameMere)
	{
		this.frameMere = frameMere;

		this.setLayout(new BorderLayout());

		this.setBorder(BorderFactory.createEmptyBorder(0, this.frameMere.MARGE, 0, this.frameMere.MARGE));

		this.imgFond = getToolkit().getImage("./images/fond/fond2.png");

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		UIManager.put("Label.foreground", Color.BLACK );
		UIManager.put("Label.font", new Font("Arial", Font.BOLD, 14));

		JButton[] tabBtn = new JButton[3];

		tabBtn[0] = this.btnRetour  = new JButton("Retour" );
		tabBtn[1] = this.btnAnnuler = new JButton("Annuler");
		tabBtn[2] = this.btnValider = new JButton("Valider");

		for ( JButton btn : tabBtn )
		{
			btn.setBackground(Controleur.COLOR_BACKGROUND);
			btn.setForeground(Controleur.COLOR_FOREGROUND);
		}

		this.txtColonne    = new JTextField(10);
		this.txtLigne      = new JTextField(10);
		this.txtNbVirus    = new JTextField(10);
		this.txtNomPlateau = new JTextField(10);

		JPanel panelColonne    = new JPanel(new FlowLayout());
		JPanel panelLigne      = new JPanel(new FlowLayout());
		JPanel panelNbVirus    = new JPanel(new FlowLayout());
		JPanel panelNomPlateau = new JPanel(new FlowLayout());


		this.panelBouton = new JPanel();
		this.panelBouton.setLayout(new FlowLayout());
		this.panelTxt    = new JPanel();
		this.panelTxt.setLayout(new GridLayout(4, 2));

		this.panelTxt.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

		this.panelBouton.setOpaque(false);
		this.panelTxt.setOpaque(false);
		panelColonne.setOpaque(false);
		panelLigne.setOpaque(false);
		panelNbVirus.setOpaque(false);
		panelNomPlateau.setOpaque(false);

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//

		for ( JButton btn : tabBtn )
			this.panelBouton.add(btn);

		panelColonne.add( new JLabel("Nombre de lignes :"  ));
		panelColonne.add( this.txtLigne      );
		panelLigne.add( new JLabel("Nombre de colonnes :"));
		panelLigne.add( this.txtColonne    );
		panelNbVirus.add( new JLabel("Nombre de virus :"   ));
		panelNbVirus.add( this.txtNbVirus    );
		panelNomPlateau.add( new JLabel("Nom du plateau :"   ));
		panelNomPlateau.add( this.txtNomPlateau );

		this.panelTxt.add(panelColonne);
		this.panelTxt.add(panelLigne);
		this.panelTxt.add(panelNbVirus);
		this.panelTxt.add(panelNomPlateau);

		this.add(this.panelTxt);
		this.add(this.panelBouton, BorderLayout.SOUTH);
		
		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//
		
		for ( JButton btn : tabBtn )
			btn.addActionListener(this);
	}
		// METHODE PERMETTANT DE METTRE LE PANELVIRUS
	public void actionPerformed(ActionEvent e)
	{
		Integer col        = null;
		Integer lig        = null;
		Integer nbCouleur  = null;
		String  nomPlateau = null;
		
		
		if (e.getSource() == this.btnAnnuler)
		{
			this.txtColonne   .setText("");
			this.txtLigne     .setText("");
			this.txtNbVirus   .setText("");
			this.txtNomPlateau.setText("");
		}

		if (e.getSource() == this.btnValider)
		{
			if ( this.txtColonne.getText().matches ( "[0-9]+" ) && 
			     this.txtLigne  .getText().matches ( "[0-9]+" ) && 
				 this.txtNbVirus.getText().matches ( "[0-9]+" ) && 
				 !this.txtNomPlateau.getText().isBlank())
			{	
				col        = Integer.parseInt(this.txtColonne.getText());
				lig        = Integer.parseInt(this.txtLigne.getText());
				nbCouleur  = Integer.parseInt(this.txtNbVirus.getText());
				nomPlateau = this.txtNomPlateau.getText();
				
				if (col > 0 && lig > 0 && nbCouleur > 0) 
				{
					this.frameMere.valider( lig, col, nbCouleur, nomPlateau);
					this.frameMere.changerPanel(new PanelVirus(this.frameMere, nbCouleur, lig, col));
				}
			}
		}

		if ( e.getSource() == this.btnRetour )
		{
			this.frameMere.changerPanel(new PanelSauvegarde(this.frameMere, this.frameMere.getCtrl()));
		}
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
}