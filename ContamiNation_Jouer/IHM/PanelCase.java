package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.IHM.FrameJeu;
import ContamiNation_Jouer.Controleur;

import ContamiNation_Jouer.Metier.Case;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelCase extends JPanel implements ActionListener
{
	private FrameJeu   frameMere;
	private Case       casePlateau;
	
	private JButton    btnCase;

	private Image      imgFond;
	private Graphics2D g2;
	
	private Controleur ctrl;

	public PanelCase(Case casePlateau, Controleur ctrl)
	{
		this.setLayout(new BorderLayout());
		this.setBorder(null);
		this.ctrl = ctrl;
		
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
		
		this.btnCase.addActionListener(this);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		this.g2 = (Graphics2D) g;
		
		// Ajout de l'image du fond
		if ( imgFond != null )
			this.g2.drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
	}
	
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == this.btnCase)
			this.ctrl.verifSommet(this.casePlateau);
	}
}