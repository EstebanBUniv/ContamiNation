package ContamiNation.IHM;

import javax.swing.JFrame;

import ContamiNation.Controleur;

public class FrameJeu extends JFrame
{
	private Controleur  ctrl;
	private PanelJeu    panelJeu;
	
	public FrameJeu(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("ContamiNation");
		this.setSize(900, 700);
		this.setLocationRelativeTo(null);

		this.panelJeu = new PanelJeu();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
