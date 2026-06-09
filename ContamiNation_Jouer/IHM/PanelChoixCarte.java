package ContamiNation_Jouer.IHM;


import ContamiNation_Jouer.Controleur;
import java.awt.event.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.GridLayout;


public class PanelChoixCarte extends JPanel implements ActionListener
{
	private Controleur  ctrl;
	private Image[] pioche;

	private JButton[]   cartes;

	private JPanel      panelChoixCarte;

	public PanelChoixCarte(Controleur ctrl)
	{
		this.ctrl = ctrl;
		
		this.panelChoixCarte = new JPanel();

		this.setLayout(new GridLayout(1,this.ctrl.getTaillePioche()));

		this.pioche  = new Image[this.ctrl.getTaillePioche()]; 
		this.cartes  = new JButton[this.ctrl.getTaillePioche()];

		
		for (int cpt = 0 ; cpt < this.ctrl.getTaillePioche() ; cpt++)
		{
			String chemin = "../images/cartes/" + this.ctrl.getCarte(cpt) + ".png";
			ImageIcon iconeOriginale = new ImageIcon(chemin);
			
			Image imgRedimensionnee = iconeOriginale.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			
			this.pioche[cpt] = imgRedimensionnee;
			
			this.cartes[cpt] = new JButton(new ImageIcon(imgRedimensionnee));
			this.cartes[cpt].addActionListener(this);
		}



		for (int cpt = 0 ; cpt < this.ctrl.getTaillePioche() ; cpt++)
		{
			this.add(this.cartes[cpt]);
		}

		this.setVisible(true);

	}


	public void actionPerformed(ActionEvent e)
	{
		for (int cpt = 0 ; cpt < this.ctrl.getTaillePioche() ; cpt++)
		{
			if (e.getSource() == this.cartes[cpt])
			{
				this.ctrl.tirerCarte(cpt);
			}
		}
	}
}	