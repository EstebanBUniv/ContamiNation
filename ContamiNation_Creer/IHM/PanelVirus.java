package ContamiNation_Creer.IHM;

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

import ContamiNation_Creer.IHM.FrameCreer;
import ContamiNation_Creer.IHM.FrameRenommer;

public class PanelVirus extends JPanel implements ActionListener
{
	private FrameCreer    frameMere;

	private JPanel        panelBouton;

	private JTextField    txtNomVirus;
	private JButton       btnValider;
	private JButton       btnAnnuler;
	private JLabel        lbNumVirus; 
	
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

		this.setLayout(new BorderLayout());

		this.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

		//-------------------------------//
		// Création des composants       //
		//-------------------------------//

		this.panelBouton = new JPanel(new FlowLayout(FlowLayout.CENTER));

		this.btnValider  = new JButton("Valider");
		this.btnAnnuler  = new JButton("Annuler");

		this.txtNomVirus = new JTextField(15);

		this.lbNumVirus  = new JLabel("Nom du Virus n°" + (this.cptVirus+1));

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//

		this.panelBouton.add(this.btnValider);
		this.panelBouton.add(this.btnAnnuler);

		this.add(this.lbNumVirus, BorderLayout.NORTH  );
		this.add(this.txtNomVirus, BorderLayout.CENTER);
		this.add(this.panelBouton, BorderLayout.SOUTH );

		//-------------------------------//
		// Activation des composants     //
		//-------------------------------//

		this.txtNomVirus.addActionListener(e -> this.valider());
		this.btnValider .addActionListener(e -> this.valider());
		this.btnAnnuler .addActionListener(this);

		
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnAnnuler )
			this.txtNomVirus.setText("");
	}

	private void valider()
	{
		if ( !this.txtNomVirus.getText().isBlank() )
		{
			this.frameMere.creerVirus(this.txtNomVirus.getText());
			this.cptVirus++;
			if (this.cptVirus < this.nbVirus )
				this.creerVirus();
			else
			{
				this.frameMere.setEstNouveau(true);
				this.frameMere.changerGrille(this.lig, this.col);
			}
		}
	}

	public void creerVirus()
	{
		this.txtNomVirus.setText("");
		this.lbNumVirus.setText("    Nom du Virus n°" + (this.cptVirus+1));
		this.revalidate();
		this.repaint();
	}
}