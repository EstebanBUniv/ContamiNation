package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelRenommer extends JPanel implements ActionListener
{
	private FrameRenommer frameMere;
	private Controleur    ctrl;
	private File          fichier;
	private int           ligne;

	private JPanel        panelBouton;

	private JTextField    txtNom;
	private JButton       btnValider;
	private JButton       btnAnnuler;      

	private PanelSauvegarde panelSauvegarde;

	public PanelRenommer(FrameRenommer frame, Controleur ctrl, File fichier, int ligne, PanelSauvegarde panelSauvegarde)
	{
		this.frameMere = frame;
		this.panelSauvegarde = panelSauvegarde;
		this.ctrl      = ctrl;
		this.fichier   = fichier;
		this.ligne     = ligne;

		this.setLayout(new BorderLayout());

		System.out.println(this.frameMere.MARGE);
		this.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		this.panelBouton = new JPanel(new FlowLayout(FlowLayout.CENTER));

		this.txtNom      = new JTextField(this.ctrl.getNom(fichier), 15);
		this.btnValider  = new JButton("Valider");
		this.btnAnnuler  = new JButton("Annuler");

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//

		this.panelBouton.add(this.btnValider);
		this.panelBouton.add(this.btnAnnuler);

		this.add(new JLabel("Nouveau nom : "), BorderLayout.NORTH );
		this.add(this.txtNom,      BorderLayout.CENTER);
		this.add(this.panelBouton, BorderLayout.SOUTH );

		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//

		this.txtNom    .addActionListener(e -> this.valider());
		this.btnValider.addActionListener(e -> this.valider());
		this.btnAnnuler.addActionListener(this);

		
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnAnnuler )
			this.frameMere.dispose();
	}

	private void valider()
	{
		if ( !this.txtNom.getText().isBlank() )
		{
			this.ctrl.Renommer(this.txtNom.getText(), this.fichier);
			this.panelSauvegarde.rafraichir();
			this.frameMere.dispose();
		}
	}
}