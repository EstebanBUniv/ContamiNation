package ContamiNation_Creer.IHM;

import ContamiNation_Creer.IHM.PanelVirus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	public PanelParametre(FrameCreer frameMere)
	{
		this.frameMere = frameMere;

		this.setLayout(new BorderLayout());

		this.setBorder(BorderFactory.createEmptyBorder(0, this.frameMere.MARGE, 0, this.frameMere.MARGE));

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		this.btnAnnuler = new JButton("Annuler");
		this.btnValider = new JButton("Valider");
		this.btnRetour  = new JButton("Retour" );

		this.txtColonne    = new JTextField(30);
		this.txtLigne      = new JTextField(30);
		this.txtNbVirus    = new JTextField(30);
		this.txtNomPlateau = new JTextField(30);

		this.panelBouton = new JPanel();
		this.panelBouton.setLayout(new FlowLayout());
		this.panelTxt    = new JPanel();
		this.panelTxt.setLayout(new GridLayout(8, 1));

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//

		this.panelBouton.add(this.btnValider);
		this.panelBouton.add(this.btnAnnuler);
		this.panelBouton.add(this.btnRetour );

		this.panelTxt.add( new JLabel("Nombre de lignes :"  ));
		this.panelTxt.add( this.txtLigne      );
		this.panelTxt.add( new JLabel("Nombre de colonnes :"));
		this.panelTxt.add( this.txtColonne    );
		this.panelTxt.add( new JLabel("Nombre de virus :"   ));
		this.panelTxt.add( this.txtNbVirus    );
		this.panelTxt.add( new JLabel("Nom du plateau :"   ));
		this.panelTxt.add( this.txtNomPlateau );

		this.add(this.panelTxt);
		this.add(this.panelBouton, BorderLayout.SOUTH);
		
		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//
		
		this.btnAnnuler.addActionListener(this);
		this.btnValider.addActionListener(this);
		this.btnRetour .addActionListener(this);
	}

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
}