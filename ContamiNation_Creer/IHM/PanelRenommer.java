package ContamiNation_Creer.IHM;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;

import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.IHM.FrameRenommer;

public class PanelRenommer extends JPanel implements ActionListener
{
	private FrameRenommer frameMere;
	private Controleur    ctrl;
	private File          fichier;
	private int           ligne;

	private JPanel        panelBouton;
	private PanelSauvegarde    panelSauvegarde;

	private JTextField    txtNom;
	private JButton       btnValider;
	private JButton       btnAnnuler;      

	public PanelRenommer(FrameRenommer frame, Controleur ctrl, File fichier, int ligne, PanelSauvegarde panelSauvegarde)
	{
		this.frameMere = frame;
		this.panelSauvegarde = panelSauvegarde;
		this.ctrl      = ctrl;
		this.fichier   = fichier;
		this.ligne     = ligne;

		this.setLayout(new BorderLayout());

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		this.panelBouton = new JPanel(new FlowLayout(FlowLayout.CENTER));

		this.txtNom      = new JTextField(15);
		this.btnValider  = new JButton("Valider");
		this.btnAnnuler  = new JButton("Annuler");

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//

		this.panelBouton.add(this.btnValider);
		this.panelBouton.add(this.btnAnnuler);

		this.add(new JLabel("    Nouveau nom : "), BorderLayout.NORTH );
		this.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
		this.add(this.txtNom,      BorderLayout.CENTER);
		this.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		this.add(this.panelBouton, BorderLayout.SOUTH );

		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//

		this.btnValider.addActionListener(this);
		this.btnAnnuler.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnValider )
		{
			if ( !this.txtNom.getText().isBlank() )
			{
				this.ctrl.Renommer(this.txtNom.getText(), this.fichier);
				this.panelSauvegarde.rafraichir();
				this.frameMere.dispose();
			}
		}

		if ( e.getSource() == this.btnAnnuler )
		{
			this.frameMere.dispose();
		}
	}
}