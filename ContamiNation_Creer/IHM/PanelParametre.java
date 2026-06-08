package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
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
	private JButton btnValider;
	private JButton btnAnnuler;
	private JButton btnRetour;
	
	private JTextField txtLigne;
	private JTextField txtColonne;
	private JTextField txtNbVirus;
	private JTextField txtNomPlateau;

	private JPanel panelBouton, panelTxt;
	private Image imgFond;

	private JPanel panelNomsVirus;
	private JTextField[] txtNomsVirus;

	public PanelParametre(FrameCreer frameMere)
	{
		this.frameMere = frameMere;
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0, this.frameMere.MARGE, 0, this.frameMere.MARGE));
		this.imgFond = getToolkit().getImage("./images/fond/fond2.png");

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		UIManager.put("Label.foreground", Color.BLACK);
		UIManager.put("Label.font", new Font("Arial", Font.BOLD, 14));

		JButton[] tabBtn = new JButton[3];
		tabBtn[0] = this.btnRetour  = new JButton("Retour");
		tabBtn[1] = this.btnAnnuler = new JButton("Annuler");
		tabBtn[2] = this.btnValider = new JButton("Valider");

		for (JButton btn : tabBtn)
		{
			btn.setBackground(Controleur.COLOR_BACKGROUND);
			btn.setForeground(Controleur.COLOR_FOREGROUND);
		}

		this.txtColonne    = new JTextField(10);
		this.txtLigne      = new JTextField(10);
		this.txtNbVirus    = new JTextField(10);
		this.txtNomPlateau = new JTextField(30);

		this.panelBouton = new JPanel(new FlowLayout());
		this.panelTxt    = new JPanel(new GridLayout(8, 1));

		this.panelBouton.setOpaque(false);
		this.panelTxt.setOpaque(false);

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//

		for (JButton btn : tabBtn)
		{
			this.panelBouton.add(btn);
		}

		this.panelTxt.add(new JLabel("Nombre de lignes :"));
		this.panelTxt.add(this.txtLigne);
		this.panelTxt.add(new JLabel("Nombre de colonnes :"));
		this.panelTxt.add(this.txtColonne);
		this.panelTxt.add(new JLabel("Nom du plateau :"));
		this.panelTxt.add(this.txtNomPlateau);
		this.panelTxt.add(new JLabel("Nombre de virus (Appuyez sur Entrée pour valider) :"));
		this.panelTxt.add(this.txtNbVirus);

		this.panelNomsVirus = new JPanel();
		this.panelNomsVirus.setOpaque(false);

		JPanel tmpVirus = new JPanel(new BorderLayout());
		tmpVirus.setOpaque(false);
		tmpVirus.add(this.panelNomsVirus, BorderLayout.NORTH);

		JScrollPane scrollVirus = new JScrollPane(tmpVirus);
		scrollVirus.setOpaque(false);
		scrollVirus.getViewport().setOpaque(false);
		scrollVirus.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		JPanel panelCentre = new JPanel(new BorderLayout());
		panelCentre.setOpaque(false);
		panelCentre.add(this.panelTxt, BorderLayout.NORTH);
		panelCentre.add(scrollVirus, BorderLayout.CENTER);

		this.add(panelCentre, BorderLayout.CENTER);
		this.add(this.panelBouton, BorderLayout.SOUTH);
		
		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//
		
		for (JButton btn : tabBtn)
		{
			btn.addActionListener(this);
		}

		this.txtNbVirus.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.txtNbVirus)
		{
			majChampsVirus();
		}

		if (e.getSource() == this.btnAnnuler)
		{
			this.txtColonne    .setText("");
			this.txtLigne      .setText("");
			this.txtNbVirus    .setText("");
			this.txtNomPlateau .setText("");
			this.panelNomsVirus.removeAll();
			this.panelNomsVirus.revalidate();
			this.panelNomsVirus.repaint();
			this.txtNomsVirus = null;
		}

		if (e.getSource() == this.btnValider)
		{
			validerParametres();
		}

		if (e.getSource() == this.btnRetour)
		{
			this.frameMere.changerPanel(new PanelSauvegarde(this.frameMere, this.frameMere.getCtrl()));
		}
	}

	private void majChampsVirus()
	{
		this.panelNomsVirus.removeAll();

		if (this.txtNbVirus.getText().matches("[0-9]+"))
		{
			int nb = Integer.parseInt(this.txtNbVirus.getText());
			if (nb > 0 && nb <= 30) 
			{
				this.panelNomsVirus.setLayout(new GridLayout(nb, 2, 5, 5));
				this.txtNomsVirus = new JTextField[nb];

				for (int i = 0; i < nb; i++)
				{
					this.panelNomsVirus.add(new JLabel("Nom du Virus n°" + (i + 1) + " :"));
					this.txtNomsVirus[i] = new JTextField(15);
					this.panelNomsVirus.add(this.txtNomsVirus[i]);
				}
			}
		}
		this.panelNomsVirus.revalidate();
		this.panelNomsVirus.repaint();
	}

	private void validerParametres()
	{
		if (this.txtColonne    .getText().matches("[0-9]+") && 
			this.txtLigne      .getText().matches("[0-9]+") && 
			this.txtNbVirus    .getText().matches("[0-9]+") && 
			!this.txtNomPlateau.getText().isBlank())
		{	
			int col = Integer.parseInt(this.txtColonne.getText());
			int lig = Integer.parseInt(this.txtLigne.getText());
			int nbCouleur = Integer.parseInt(this.txtNbVirus.getText());
			String nomPlateau = this.txtNomPlateau.getText();
			
			boolean nomsValides = (this.txtNomsVirus != null && this.txtNomsVirus.length == nbCouleur);
			if (nomsValides)
			{
				for (JTextField txtNom : this.txtNomsVirus)
				{
					if (txtNom.getText().isBlank()) 
					{
						nomsValides = false;
					}
				}
			}

			if (col > 0 && lig > 0 && nbCouleur > 0 && nomsValides) 
			{
				// 1. Initialisation du plateau d'origine
				this.frameMere.valider(lig, col, nbCouleur, nomPlateau);

				// 2. Enregistrement des virus (via les méthodes exactes de ta FrameCreer)
				for (int i = 0; i < nbCouleur; i++)
				{
					this.frameMere.creerVirus(this.txtNomsVirus[i].getText());
				}
				
				// 3. Transition d'origine exacte récupérée de ton PanelVirus
				this.frameMere.setEstNouveau(true);
				this.frameMere.changerGrille(lig, col);
				this.frameMere.setEstNouveau(true);
				this.frameMere.setResizable(true);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Veuillez spécifier le nombre de virus, valider avec la touche 'Entrée', puis remplir tous leurs noms.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Veuillez vérifier la validité des dimensions saisies.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (imgFond != null)
		{
			((Graphics2D) g).drawImage(imgFond, 0, 0, getWidth(), getHeight(), this);
		}
	}
}