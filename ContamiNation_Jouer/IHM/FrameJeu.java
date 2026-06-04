package ContamiNation_Jouer.IHM;

import javax.swing.JFrame;

import ContamiNation_Jouer.Controleur;

public class FrameJeu extends JFrame
{
	private Controleur  ctrl;
	private PanelJeu    panelJeu;
	
	public FrameJeu(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("ContamiNation");
		this.setSize(600, 300);
		this.setLocationRelativeTo(null);

		this.panelJeu = new PanelJeu(this.ctrl, this);

		this.add(this.panelJeu);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
