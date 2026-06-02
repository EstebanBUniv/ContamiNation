package ContamiNation.IHM;

import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class PanelPlateau extends JPanel implements ActionListener
{
	private JTextField        txtLargeur;
	private JTextField        txtHauteur;
	private JTextField        txtCouleur;

	private JButton btnCreer;
	private JButton btnAnnuler;

	public PanelPlateau()
	{
		JPanel panelBtn;

		this.setLayout(new GridLayout(7, 1));
		
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		panelBtn = new JPanel();

		this.txtLargeur = new JTextField();
		this.txtHauteur = new JTextField();
		this.txtCouleur = new JTextField();

		this.btnAnnuler = new JButton("Annuler");
		this.btnCreer   = new JButton("Creer");

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(new JLabel("Entrez la largeur : "));
		this.add(this.txtLargeur);
		this.add(new JLabel("Entrez la hauteur : "));
		this.add(this.txtHauteur);
		this.add(new JLabel("Combien de couleurs voulez-vous : "));
		this.add(this.txtCouleur);

		panelBtn.add(this.btnAnnuler);
		panelBtn.add(this.btnCreer);
		this.add(panelBtn);

		 /* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
        this.btnAnnuler.addActionListener(this);
		this.btnCreer.addActionListener(this);
		this.txtLargeur.addActionListener(this);
		this.txtHauteur.addActionListener(this);
		this.txtCouleur.addActionListener(this);

		
	}

	public void actionPerformed(ActionEvent e)
	{
		Integer largeur   = null;
		Integer hauteur   = null;
		Integer nbCouleur = null;
		
		
		if (e.getSource() == this.btnAnnuler)
		{
			this.txtLargeur.setText("");
			this.txtHauteur.setText("");
			this.txtCouleur.setText("");
		}

		if (e.getSource() == this.btnCreer)
		{
			try
			{
				largeur = Integer.parseInt(this.txtLargeur.getText());
				hauteur = Integer.parseInt(this.txtHauteur.getText());
				nbCouleur = Integer.parseInt(this.txtHauteur.getText());

				System.out.println(largeur + "," + hauteur + "," + nbCouleur);
			}
			catch(Exception exc){System.out.println("Erreur");}
		}
	}
}
