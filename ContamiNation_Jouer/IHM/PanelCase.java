package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;
import ContamiNation_Jouer.IHM.FrameJeu;
import ContamiNation_Jouer.Controleur;
import ContamiNation_Jouer.Metier.Case;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.AlphaComposite;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


public class PanelCase extends JPanel implements ComponentListener, ActionListener
{
	private Controleur ctrl;

	private Case       casePlateau;
	
	private JButton    btnCase;

	private Image      imgSymbole;
	private Image      imgFond;
	private Graphics2D g2;
	
	private Controleur ctrl;

	public PanelCase(Case casePlateau, Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setLayout(new BorderLayout());
		this.setBorder(null);
		this.ctrl = ctrl;
		
		this.casePlateau = casePlateau;
		this.imgFond     = getToolkit().getImage("../images/fond/fond_case.png");


		if (this.casePlateau.getSommet() != null)
		{
			this.btnCase = new JButton();

			String symbole = this.casePlateau.getSommet().getSymbole();

			this.imgSymbole = getToolkit().getImage("../images/symboles/symbole_" + symbole + ".png");
			Image img        = imgSymbole.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
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


		this.addComponentListener(this);
    this.btnCase.addActionListener(this);
	}

	public int getTailleCase()
	{
		return Math.max(this.getWidth(), this.getHeight());
  {
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		this.g2 = (Graphics2D) g.create();

		this.g2.setColor(ctrl.getCouleurZone(this.casePlateau.getZone()));
		this.g2.fillRect(0, 0, getWidth(), getHeight());

		if (this.imgFond != null)
		{
			this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			this.g2.drawImage(this.imgFond, 0, 0, getWidth(), getHeight(), this);
		}

		this.g2.dispose();
	}

	public void componentResized(ComponentEvent e)
	{
		if ( this.imgSymbole != null )
		{
			int taille = (int)(this.getTailleCase() * 0.3); 
			
			Image img = imgSymbole.getScaledInstance(taille, taille, Image.SCALE_SMOOTH);
			this.btnCase.setIcon(new ImageIcon(img));

			this.revalidate();
			this.repaint();
		}
		
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentShown (ComponentEvent e) {}
	public void componentMoved (ComponentEvent e) {}

	
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == this.btnCase)
			this.ctrl.verifSommet(this.casePlateau);
	}
}