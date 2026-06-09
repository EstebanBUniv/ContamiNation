package ContamiNation_Jouer.IHM;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ContamiNation_Jouer.Controleur;

public class PanelPioche extends JPanel implements ActionListener
{
	private Controleur ctrl;

	private JButton btnPasser;
	private JButton btnCartePiocher; // afficher les cartes déjà piocher
	private JButton btnPiocher;

	private JLabel  lblCarteActive;
	private JLabel  lblManche;

	private int     cptManche;

	public PanelPioche(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());
		
		JPanel panelGauche;
		//-------------------------//
		// création des composants //
		//-------------------------//
		panelGauche          = new JPanel ();

		panelGauche.setLayout(new GridLayout(3,1));

		this.btnPasser       = new JButton();
		this.btnCartePiocher = new JButton();
		this.btnPiocher      = new JButton();

		this.lblCarteActive  = new JLabel( "Carte active");
		this.lblManche       = new JLabel("Manche n°");

		this.cptManche = 0;

		//-------------------------------//
		// positionnement des composants //
		//-------------------------------//

		this       .add(this.btnPasser, BorderLayout.EAST      );
		panelGauche.add(this.btnCartePiocher                   );
		panelGauche.add(this.lblCarteActive                    );
		panelGauche.add(this.btnPiocher                        );
	
		this       .add(this.lblManche  ,BorderLayout.NORTH    );
		this       .add(panelGauche     ,BorderLayout.WEST     );

		/* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
        this.btnPasser      .addActionListener(this);
		this.btnCartePiocher.addActionListener(this);
		this.btnPiocher     .addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btnPasser){}

		if(e.getSource() == btnCartePiocher){}

		if(e.getSource() == btnPiocher){}
	}

}
