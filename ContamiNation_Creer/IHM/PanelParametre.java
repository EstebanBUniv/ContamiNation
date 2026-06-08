package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.IHM.PanelVirus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/* SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelParametre extends JPanel implements ActionListener
{
	private FrameCreer frameMere;
	private JButton btnValider, btnAnnuler, btnRetour;
	private JTextField txtLigne, txtColonne, txtNbVirus, txtNomPlateau;
	private JPanel panelBouton, panelTxt;
	private Image imgFond;

	//Constructeur

	public PanelParametre(FrameCreer frameMere)
	{
		this.frameMere = frameMere;
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0, this.frameMere.MARGE, 0, this.frameMere.MARGE));
		this.imgFond = getToolkit().getImage("./images/fond/fond2.png");

		initComposants();
	}

	// Initialisation et placement des éléments d'interface
	private void initComposants()
	{
		// Configuration des labels
		UIManager.put("Label.foreground", Color.BLACK);
		UIManager.put("Label.font", new Font("Arial", Font.BOLD, 14));

		// Création des boutons
		this.btnRetour = new JButton("Retour");
		this.btnAnnuler = new JButton("Annuler");
		this.btnValider = new JButton("Valider");

		JButton[] tabBtn = {this.btnRetour, this.btnAnnuler, this.btnValider};
		for (JButton btn : tabBtn)
		{
			btn.setBackground(Controleur.COLOR_BACKGROUND);
			btn.setForeground(Controleur.COLOR_FOREGROUND);
			btn.addActionListener(this);
		}

		// Champs de saisie
		this.txtColonne = new JTextField(30);
		this.txtLigne = new JTextField(30);
		this.txtNbVirus = new JTextField(30);
		this.txtNomPlateau = new JTextField(30);

		// Panneaux conteneurs
		this.panelBouton = new JPanel(new FlowLayout());
		this.panelTxt = new JPanel(new GridLayout(8, 1));
		this.panelBouton.setOpaque(false);
		this.panelTxt.setOpaque(false);

		// Remplissage des panneaux
		for (JButton btn : tabBtn) this.panelBouton.add(btn);

		this.panelTxt.add(new JLabel("Nombre de lignes :"));
		this.panelTxt.add(this.txtLigne);
		this.panelTxt.add(new JLabel("Nombre de colonnes :"));
		this.panelTxt.add(this.txtColonne);
		this.panelTxt.add(new JLabel("Nombre de virus :"));
		this.panelTxt.add(this.txtNbVirus);
		this.panelTxt.add(new JLabel("Nom du plateau :"));
		this.panelTxt.add(this.txtNomPlateau);

		this.add(this.panelTxt, BorderLayout.CENTER);
		this.add(this.panelBouton, BorderLayout.SOUTH);
	}

	// Gestion dans les activations de bouton

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnAnnuler) 
			effacerChamps();
		if (e.getSource() == this.btnValider) 
			validerParametres();
		if (e.getSource() == this.btnRetour)
			this.frameMere.changerPanel(new PanelSauvegarde(this.frameMere, this.frameMere.getCtrl()));
	}

	// Vérifie et traite la validation des données saisies
	private void validerParametres()
	{
		if ( this.txtColonne   .getText().matches("[0-9]+") && 
			 this.txtLigne     .getText().matches("[0-9]+") && 
			 this.txtNbVirus   .getText().matches("[0-9]+") && 
			!this.txtNomPlateau.getText().isBlank())
		{    
			int col = Integer.parseInt(this.txtColonne.getText());
			int lig = Integer.parseInt(this.txtLigne.getText());
			int nbCouleur = Integer.parseInt(this.txtNbVirus.getText());
			String nomPlateau = this.txtNomPlateau.getText();
			
			if (col > 0 && lig > 0 && nbCouleur > 0) 
			{
				this.frameMere.valider(lig, col, nbCouleur, nomPlateau);
				this.frameMere.changerPanel(new PanelVirus(this.frameMere, nbCouleur, lig, col));
			}
		}
	}

	// Réinitialise le contenu des champs de texte
	private void effacerChamps()
	{
		this.txtColonne   .setText("");
		this.txtLigne     .setText("");
		this.txtNbVirus   .setText("");
		this.txtNomPlateau.setText("");
	}

	//rendu graphique
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (imgFond != null)
		{
			((Graphics2D) g).drawImage(imgFond, 0, 0, getWidth(), getHeight(), this);
		}
	}
}