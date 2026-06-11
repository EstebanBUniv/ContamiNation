package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PanelPioche extends JPanel implements ActionListener
{
	private Controleur ctrl;


	private JButton btnPasser;
	private JLabel lblPioche;

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
		
		JScrollPane scrollDefausse = new JScrollPane(this.panelDefausse);
		scrollDefausse.setPreferredSize(new Dimension(0, 100)); 
		scrollDefausse.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollDefausse.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// On enlève les bordures moches du scroll par défaut
		scrollDefausse.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		panelGauche          = new JPanel ();

		panelGauche.setLayout(new GridLayout(3,1));
		
		ImageIcon iconOriginal = new ImageIcon("../images/cartes/" + this.ctrl.premiereCarte() + ".png");
		Image img50            = iconOriginal.getImage().getScaledInstance(55, 80, Image.SCALE_SMOOTH);
		ImageIcon icon50       = new ImageIcon(img50);
		

		this.lblPioche      = new JLabel(icon50);
		this.btnPasser        = new JButton( "Passer");

		/*
		this.lblPioche.setOpaque           (false);
		this.lblPioche.setContentAreaFilled(false);
		this.lblPioche.setBorderPainted    (false);
		this.lblPioche.setFocusPainted     (false);
		*/
		this.cptManche = 1;

		this.lblCarteActive  = new JLabel( "Carte active");
		this.lblManche       = new JLabel("Manche n°" + Integer.toString(cptManche));

		

		//-------------------------------//
		// positionnement des composants //
		//-------------------------------//
		
		panelGauche.add(this.lblCarteActive                           );
		panelGauche.add(this.lblPioche                               );
		panelGauche.add(this.btnPasser);
		
		this       .add(scrollDefausse   , BorderLayout.SOUTH     );
		this       .add(this.lblManche       ,BorderLayout.NORTH      );
		this       .add(panelGauche          ,BorderLayout.WEST       );


		/* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
        
		this.btnPasser.addActionListener(this);
	}

	// Méthode qui s'occupe du déroulement de la pioche
	public void passerTour()
	{
		if (!this.ctrl.verifFinManche())
		{
			if (!this.ctrl.getModeDebiche())
			{
				this.ctrl.tirerCarte(0);
				this.afficherCarteActive();
			}
			else
			{
				// La carte a déjà été choisie via FrameChoixCarte
				// On prépare le tour suivant
				this.ctrl.appelerChoixCarte();
			}
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

	public void carteChoisie()
	{
		this.afficherCarteActive();
	}

	private void afficherCarteActive()
	{
		if (this.ctrl.getCarteTiree() == null) return;

		ImageIcon iconOriginal = new ImageIcon("../images/cartes/" + this.ctrl.getCarteTiree() + ".png");
		Image img50 = iconOriginal.getImage().getScaledInstance(55, 80, Image.SCALE_SMOOTH);
		ImageIcon icon50 = new ImageIcon(img50);

		this.defausse[this.numTour] = new JLabel(icon50);
		this.panelDefausse.add(this.defausse[this.numTour++]);
		this.lblCarteActive.setIcon(icon50);

		if (!this.ctrl.verifFinManche())
		{
			ImageIcon iconPremiere = new ImageIcon("../images/cartes/" + this.ctrl.premiereCarte() + ".png");
			Image img60 = iconPremiere.getImage().getScaledInstance(55, 80, Image.SCALE_SMOOTH);
			this.lblPioche.setIcon(new ImageIcon(img60));
		}
		else
		{
			this.lblPioche.setIcon(null);
		}

		this.panelDefausse.revalidate();
		this.panelDefausse.repaint();
	}


	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource() == this.btnPasser)
		{
			this.passerTour();
		}
	}
}

