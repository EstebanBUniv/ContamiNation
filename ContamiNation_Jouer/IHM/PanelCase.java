package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.IHM.FrameJeu;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelCase extends JPanel
{
	private FrameJeu   frameMere;
	private Case       casePlateau;
	
	private JButton    btnCase;

	private Image      imgFond;
	private Graphics2D g2;

	public PanelCase(Case casePlateau)
	{
		this.setLayout(new BorderLayout());

		this.imgFond = getToolkit().getImage("../images/fond/case.png");

		this.btnCase     = new JButton(new ImageIcon("../images/symboles/Ville.png"));
		this.casePlateau = casePlateau;
		this.btnCase.setOpaque(false);

		this.add(this.btnCase);
	}
}