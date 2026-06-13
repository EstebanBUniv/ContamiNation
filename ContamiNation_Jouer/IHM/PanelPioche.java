package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Component;

import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

public class PanelPioche extends JPanel implements ActionListener
{
	private Controleur ctrl;
	private FrameJeu   frameMere;

	private JButton    btnPasser;
	private JButton    btnQuitter;

	private JLabel     lbCartePioche;
	private JLabel     lblCarteActive;
	private JLabel     lblManche;
	private JLabel[]   defausse;
	private JPanel     panelDefausse;
	private int        numTour;

	private int        cptManche;
	private int        nbPasse;

	private ImageIcon  derniereCarte = null;

	public PanelPioche(FrameJeu frameMere, Controleur ctrl)
	{
		this.ctrl      = ctrl;
		this.frameMere = frameMere;

		// Struture du panel principal
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		this.setBackground(Controleur.COLOR_BACKGROUND);

		// Entête Numero de manche
		this.cptManche = 1;
		this.lblManche = new JLabel("MANCHE N°" + this.cptManche);
		this.lblManche.setFont(Controleur.POLICE_TITRE);
		this.lblManche.setForeground(Controleur.COLOR_FOREGROUND);
		this.lblManche.setAlignmentX(Component.CENTER_ALIGNMENT);


		// Panel Cartes Pioche et Carte Active
		JPanel panelCarte = new JPanel(new GridLayout(1, 2, 15, 0));
		panelCarte.setOpaque(false);

		JPanel panelPioche = new JPanel();
		JPanel panelActive = new JPanel();

		for ( JPanel p : new JPanel[]{panelPioche, panelActive})
		{
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setOpaque(false);
		}

		JLabel lbPioche = new JLabel("<html><center>Carte<br>suivante</center></html>");
		JLabel lbActive = new JLabel("<html><center>Carte<br>active</center></html>"  );

		lbPioche.setHorizontalAlignment(SwingConstants.CENTER);
		lbActive.setHorizontalAlignment(SwingConstants.CENTER);

		for ( JLabel lb : new JLabel[]{lbPioche, lbActive})
		{
			lb.setFont(Controleur.POLICE_TEXTE);
			lb.setForeground(Controleur.COLOR_FOREGROUND);
			lb.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		
		this.lbCartePioche = new JLabel("");
		this.lbCartePioche.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.lblCarteActive = new JLabel("");
		this.lblCarteActive.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Boutons d'actions
		JPanel panelBtn = new JPanel(new GridBagLayout());
		panelBtn.setOpaque(false);

		JPanel panelBtnVertical = new JPanel();
		panelBtnVertical.setLayout(new BoxLayout(panelBtnVertical, BoxLayout.Y_AXIS));
		panelBtnVertical.setOpaque(false);

		this.btnPasser  = new JButton("Passer le Tour");
		this.btnQuitter = new JButton("Quitter");

		for ( JButton btn : new JButton[]{this.btnPasser, this.btnQuitter})
		{
			btn.setFont(Controleur.POLICE_TEXTE);
			btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			btn.setForeground(Controleur.COLOR_FOREGROUND);
		}

		this.btnPasser.setBackground(Controleur.COLOR_BACKGROUND.brighter());
		this.btnQuitter.setBackground(new java.awt.Color(180, 70, 70));

		// Pile de defausse
		JPanel panelDefausseGlobal = new JPanel();
		panelDefausseGlobal.setLayout(new BoxLayout(panelDefausseGlobal, BoxLayout.Y_AXIS));
		panelDefausseGlobal.setOpaque(false);

		JLabel lbDefausse = new JLabel("Pile de défausse");
		lbDefausse.setFont(Controleur.POLICE_TEXTE);
		lbDefausse.setForeground(Controleur.COLOR_FOREGROUND);
		lbDefausse.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.panelDefausse = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		this.panelDefausse.setBackground(Controleur.COLOR_BACKGROUND.darker());
		this.defausse      = new JLabel[12];
		this.numTour       = 0;

		JScrollPane scrollDefausse = new JScrollPane(this.panelDefausse);
		scrollDefausse.setPreferredSize(new Dimension(160, 110)); 
		scrollDefausse.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollDefausse.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		//-------------------------------------------//
		// Positionnement des composants             //
		//-------------------------------------------//

		panelPioche.add(lbPioche);
		panelPioche.add(Box.createVerticalStrut(5));
		panelPioche.add(this.lbCartePioche);

		panelActive.add(lbActive);
		panelActive.add(Box.createVerticalStrut(5));
		panelActive.add(this.lblCarteActive);

		panelCarte.add(panelPioche);
		panelCarte.add(panelActive);

		panelBtnVertical.add(this.btnPasser);
		panelBtnVertical.add(Box.createVerticalStrut(10));
		panelBtnVertical.add(this.btnQuitter);
		panelBtn        .add(panelBtnVertical);

		panelDefausseGlobal.add(lbDefausse);
		panelDefausseGlobal.add(Box.createVerticalStrut(5));
		panelDefausseGlobal.add(scrollDefausse);

		this.add(this.lblManche);
		this.add(Box.createVerticalStrut(20));
		this.add(panelCarte);
		this.add(Box.createVerticalGlue());
		this.add(panelBtn);
		this.add(Box.createVerticalGlue());
		this.add(panelDefausseGlobal);

		//-------------------------------------------//
		// Activation des composants                 //
		//-------------------------------------------//

		this.btnPasser .addActionListener(this);
		this.btnQuitter.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == this.btnPasser)
			this.ctrl.forcerPassageTourCollectif();

		if (e.getSource() == this.btnQuitter)
			this.demanderConfirmation();
	}

	private void demanderConfirmation()
	{
		Object[] options = {"Oui", "Non"};
		int choix = JOptionPane.showOptionDialog(
			this.frameMere,
			"Êtes-vous sûr de vouloir quitter la partie ?",
			"Abandonner",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[1]
		);

		if (choix == JOptionPane.YES_OPTION)
			this.frameMere.changerPanel(new PanelMenu(this.frameMere, this.ctrl));
	}

	public void passerTour()
	{
		if (!this.ctrl.verifFinManche())
		{
			if (!this.ctrl.getModeDebiche() && !this.ctrl.getModeMulti())
			{
				this.ctrl.tirerCarte(0);
			}
			else
			{
				if(!this.ctrl.getModeDebiche() && this.ctrl.getModeMulti())
				{   
					if(this.nbPasse >= ctrl.getNbJoueur())
					{
						this.ctrl.tirerCarte(0);
						this.nbPasse = 0; 
					}
				}
				else
				{
					this.ctrl.appelerChoixCarte();
				}
			}   
		}
		else
		{
			this.cptManche++;
			this.lblManche.setText("MANCHE N°" + this.cptManche);
			this.panelDefausse.removeAll();
			this.derniereCarte = null;
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

		ImageIcon iconOriginal    = new ImageIcon("../images/cartes/" + this.ctrl.getCarteTiree() + ".png");
		Image     imgCarteActive  = iconOriginal.getImage().getScaledInstance(55, 80, Image.SCALE_SMOOTH);
		ImageIcon iconCarteActive = new ImageIcon(imgCarteActive);

		// Ajouter l'ancienne carte active à la défausse
		if (this.derniereCarte != null)
		{
			this.defausse[this.numTour] = new JLabel(this.derniereCarte);
			this.panelDefausse.add(this.defausse[this.numTour++]);
		}

		// Afficher la nouvelle carte active
		this.lblCarteActive.setIcon(iconCarteActive);
		this.derniereCarte = iconCarteActive;

		if (!this.ctrl.verifFinManche())
		{
			ImageIcon iconPioche = new ImageIcon("../images/cartes/" + this.ctrl.premiereCarte() + ".png");
			Image     imgPioche  = iconPioche.getImage().getScaledInstance(55, 80, Image.SCALE_SMOOTH);
			this.lbCartePioche.setIcon(new ImageIcon(imgPioche));
		}
		else
		{
			this.lbCartePioche.setIcon(null);
		}

		this.panelDefausse.revalidate();
		this.panelDefausse.repaint();
	}

	public void incrNbPasse()
	{
		this.nbPasse++;
	}
}