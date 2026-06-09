package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.IHM.FrameJeu;

import ContamiNation_Jouer.Metier.Case;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
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
		this.setBorder(null);
		
		this.casePlateau = casePlateau;
		this.imgFond     = getToolkit().getImage("../images/fond/fond_case.png");


		if (this.casePlateau.getSommet() != null)
		{
			this.btnCase = new JButton();

			String symbole = this.casePlateau.getSommet().getSymbole();

			Image imgSymbole = getToolkit().getImage("../images/symboles/symbole_" + symbole + ".png");
			int     taille = 50;
			Image   img    = imgSymbole.getScaledInstance(taille, taille, Image.SCALE_SMOOTH);
			btnCase.setIcon(new ImageIcon(img));
		}
		else
		{
			this.btnCase = new JButton();
		}

		this.btnCase.setOpaque(false);
		this.btnCase.setContentAreaFilled(false);
		this.btnCase.setBorderPainted(false);
		this.btnCase.setFocusPainted(false);

		this.casePlateau = casePlateau;

		this.add(this.btnCase, BorderLayout.CENTER);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		this.g2 = (Graphics2D) g;
		
		// Ajout de l'image du fond
		if ( imgFond != null )
			this.g2.drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
	}
}