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

	private JLabel   lblCarteActive;
	private JLabel   lblManche;
	private JLabel[] defausse;
	private JPanel   panelDefausse;
	private int      numTour;

	private int     cptManche;
	

	public PanelPioche(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());
		this.ctrl.initierPioche();
		
		JPanel panelGauche;
		//-------------------------//
		// création des composants //
		//-------------------------//
		this.panelDefausse   = new JPanel();
		this.defausse        = new JLabel[12];
		this.numTour         = 0;
		
		panelGauche          = new JPanel ();

		panelGauche.setLayout(new GridLayout(3,1));
		
		ImageIcon iconOriginal = new ImageIcon("../images/cartes/" + this.ctrl.tirerCarte(0) + ".png");
		Image img50            = iconOriginal.getImage().getScaledInstance(50, 100, Image.SCALE_SMOOTH);
		ImageIcon icon50       = new ImageIcon(img50);
		

		this.btnPiocher      = new JButton(icon50);
		this.btnPasser        = new JButton();
		this.btnCartePiocher  = new JButton();

		
		this.btnPiocher.setOpaque           (false);
		this.btnPiocher.setContentAreaFilled(false);
		this.btnPiocher.setBorderPainted    (false);
		this.btnPiocher.setFocusPainted     (false);

		this.cptManche = 1;

		this.lblCarteActive  = new JLabel( "Carte active");
		this.lblManche       = new JLabel("Manche n°" + Integer.toString(cptManche));

		

		//-------------------------------//
		// positionnement des composants //
		//-------------------------------//



		panelGauche.add(this.btnCartePiocher                   );
		panelGauche.add(this.lblCarteActive                    );
		panelGauche.add(this.btnPiocher                        );
	
		this       .add(this.lblManche  ,BorderLayout.NORTH    );
		this       .add(panelGauche     ,BorderLayout.WEST     );

		
		panelGauche.add(this.btnCartePiocher                          );
		panelGauche.add(this.lblCarteActive                           );
		panelGauche.add(this.btnPiocher                               );
		
		this       .add(this.btnPasser       , BorderLayout.EAST      );
		this       .add(this.panelDefausse   , BorderLayout.SOUTH     );
		this       .add(this.lblManche       ,BorderLayout.NORTH      );
		this       .add(panelGauche          ,BorderLayout.WEST       );


		/* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
        
		this.btnCartePiocher.addActionListener(this);
		this.btnPiocher     .addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		

		if(e.getSource() == btnCartePiocher) {}

		if(e.getSource() == btnPiocher)
		{
			if (!this.ctrl.verifFinManche())
			{
				ImageIcon iconOriginal = new ImageIcon("../images/cartes/" + this.ctrl.tirerCarte(0) + ".png");
				Image img50 = iconOriginal.getImage().getScaledInstance(50, 100, Image.SCALE_SMOOTH);
				ImageIcon icon50 = new ImageIcon(img50);
				
				this.defausse[this.numTour] = new JLabel(icon50);
				this.panelDefausse.add(this.defausse[this.numTour++]);
				this.lblCarteActive.setIcon(icon50);
				
				if (!this.ctrl.verifFinManche()) 
				{
					ImageIcon iconOriginalPremiere = new ImageIcon("../images/cartes/" + this.ctrl.premiereCarte() + ".png");
					Image img60 = iconOriginalPremiere.getImage().getScaledInstance(50, 100, Image.SCALE_SMOOTH);
					this.btnPiocher.setIcon(new ImageIcon(img60));
				}
				else
				{
					this.btnPiocher.setIcon(null); 
				}

				this.panelDefausse.revalidate();
				this.panelDefausse.repaint();
			}
			else
			{
				System.out.println("Fin de Manche");
				this.cptManche++;
				this.lblManche.setText("Manche n°" + this.cptManche);
				
				this.panelDefausse.removeAll();
				this.panelDefausse.revalidate();
				this.panelDefausse.repaint();
				this.numTour = 0; 
				
				this.ctrl.nouvelleManche();
			}
		}
	}

}