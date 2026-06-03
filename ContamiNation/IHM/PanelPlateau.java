package ContamiNation.IHM;

import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class PanelPlateau extends JPanel implements ActionListener
{
	private JTextField        txtCol;
	private JTextField        txtLig;
	private JTextField        txtCouleur;

	private JButton           btnCreer;
	private JButton           btnAnnuler;

	private FramePlateau      frameMere;

	public PanelPlateau(FramePlateau frameMere)
	{
		JPanel panelBtn;
		
		this.frameMere = frameMere;

		this.setLayout(new GridLayout(7, 1));
		
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		panelBtn = new JPanel();

		this.txtCol     = new JTextField();
		this.txtLig     = new JTextField();
		this.txtCouleur = new JTextField();

		this.btnAnnuler = new JButton("Annuler");
		this.btnCreer   = new JButton("Creer");

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(new JLabel("Entrez le nombres de colonnes : "));
		this.add(this.txtCol);
		this.add(new JLabel("Entrez le nombres de lignes : "));
		this.add(this.txtLig);
		this.add(new JLabel("Combien de couleurs voulez-vous : "));
		this.add(this.txtCouleur);

		panelBtn.add(this.btnAnnuler);
		panelBtn.add(this.btnCreer);
		this.add(panelBtn);

		 /* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
		this.btnAnnuler.addActionListener(this);
		this.btnCreer  .addActionListener(this);
		this.txtCol    .addActionListener(this);
		this.txtLig    .addActionListener(this);
		this.txtCouleur.addActionListener(this);

		
	}

	public void actionPerformed(ActionEvent e)
	{
		Integer col       = null;
		Integer lig       = null;
		Integer nbCouleur = null;
		
		
		if (e.getSource() == this.btnAnnuler)
		{
			this.txtCol.setText("");
			this.txtLig.setText("");
			this.txtCouleur.setText("");
		}

		if (e.getSource() == this.btnCreer)
		{
			if ( this.txtCol.getText().matches ( "[0-9]+" ) && this.txtLig.getText().matches ( "[0-9]+" ) && this.txtCouleur.getText().matches ( "[0-9]+" ))
			{	
				col       = Integer.parseInt(this.txtCol.getText());
				lig       = Integer.parseInt(this.txtLig.getText());
				nbCouleur = Integer.parseInt(this.txtCouleur.getText());
				System.out.println(col + "," + lig + "," + nbCouleur);
				
				this.frameMere.creerPlateau( lig, col, nbCouleur);
			}
		}
	}
}
