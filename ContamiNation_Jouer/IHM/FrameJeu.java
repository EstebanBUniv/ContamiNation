package ContamiNation_Jouer.IHM;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ContamiNation_Jouer.Controleur;

public class FrameJeu extends JFrame
{
	private Controleur  ctrl;
	private JPanel      panel;
	
	public FrameJeu(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("ContamiNation");
		this.setSize(600, 300);
		this.setLocationRelativeTo(null);

		this.panel = new PanelMenu(this.ctrl, this);
		
		this.add(this.panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void changerPanel(JPanel panel)
	{
		this.remove(this.panel);
		this.panel = panel;
		this.add(this.panel);
		this.revalidate();
		this.repaint();
	}
}
