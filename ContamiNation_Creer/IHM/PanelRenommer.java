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

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelRenommer extends JPanel implements ActionListener
{
	private final int MARGE = 20;

	private FrameRenommer frameMere;
	private Controleur    ctrl;
	private File          fichier;

	private JPanel        panelBouton;

	private JTextField    txtNom;
	private JButton       btnValider;
	private JButton       btnAnnuler;      

	private PanelSauvegarde panelSauvegarde;

	public PanelRenommer(FrameRenommer frame, Controleur ctrl, File fichier, PanelSauvegarde panelSauvegarde)
	{
		this.frameMere = frame;
		this.panelSauvegarde = panelSauvegarde;
		this.ctrl      = ctrl;
		this.fichier   = fichier;

		this.setLayout(new BorderLayout());

		this.setBorder(BorderFactory.createEmptyBorder(0, this.MARGE, 0, this.MARGE));

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		this.panelBouton = new JPanel(new FlowLayout(FlowLayout.CENTER));

		this.txtNom      = new JTextField(this.ctrl.getNom(fichier), 15);

		this.btnValider  = new JButton("Valider");
		this.btnAnnuler  = new JButton("Annuler");

		this.btnValider.setBackground(Controleur.COLOR_BACKGROUND);
		this.btnValider.setForeground(Controleur.COLOR_FOREGROUND);

		this.btnAnnuler.setBackground(Controleur.COLOR_BACKGROUND);
		this.btnAnnuler.setForeground(Controleur.COLOR_FOREGROUND);

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