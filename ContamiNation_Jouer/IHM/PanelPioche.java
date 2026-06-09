package ContamiNation_Jouer.IHM;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.ImageIcon;
import java.awt.Image;

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
	
	//private Pioche pioche;

	public PanelPioche(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());
		this.ctrl.initierPioche();
		this.ctrl.melangerPioche();
		
		JPanel panelGauche;
		//-------------------------//
		// création des composants //
		//-------------------------//
		panelGauche          = new JPanel ();

		panelGauche.setLayout(new GridLayout(3,1));
		
		ImageIcon iconOriginal = new ImageIcon("../images/cartes/" + this.ctrl.tirerCarte(0) + ".png");
		Image img50 = iconOriginal.getImage().getScaledInstance(50, 100, Image.SCALE_SMOOTH);
		ImageIcon icon50 = new ImageIcon(img50);
		
		this.btnPasser       = new JButton();
		this.btnCartePiocher = new JButton();
		this.btnPiocher      = new JButton(icon50);
		
		this.btnPiocher.setOpaque(false);
		this.btnPiocher.setContentAreaFilled(false);
		this.btnPiocher.setBorderPainted(false);
		this.btnPiocher.setFocusPainted(false);


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

		if(e.getSource() == btnCartePiocher) {}

		if(e.getSource() == btnPiocher)
		{
			if (!this.ctrl.verifFinManche())
			{
				ImageIcon iconOriginal = new ImageIcon("../images/cartes/" + this.ctrl.tirerCarte(0) + ".png");
				Image img50 = iconOriginal.getImage().getScaledInstance(50, 100, Image.SCALE_SMOOTH);
				ImageIcon icon50 = new ImageIcon(img50);
				ImageIcon iconOriginalPremiere = new ImageIcon("../images/cartes/" + this.ctrl.premiereCarte() + ".png");
				Image img60 = iconOriginalPremiere.getImage().getScaledInstance(50, 100, Image.SCALE_SMOOTH);
				ImageIcon icon60 = new ImageIcon(img60);
				this.btnPiocher.setIcon(icon60);
				this.lblCarteActive.setIcon(icon50);
			}
			else
				System.out.println("Fin de la partie");
		}
	}

}
