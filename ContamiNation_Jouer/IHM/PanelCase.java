package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;
import ContamiNation_Jouer.Metier.Case;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


public class PanelCase extends JPanel implements ComponentListener, ActionListener
{
	// Attribut d'instance
	private Controleur ctrl;

	private Case       casePlateau;
	
	private JButton    btnCase;

	private Image      imgSymbole;
	private Image      imgFond;
	private Image      imgBase;
	private Graphics2D g2;
	private int        taille;
	
	public PanelCase(Case casePlateau, Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setLayout(new BorderLayout());
		this.setBorder(null);
		this.ctrl = ctrl;
		
		this.casePlateau = casePlateau;
		this.imgFond     = getToolkit().getImage("../images/fond/fond_case.png");
		this.imgBase     = getToolkit().getImage("../images/fond/base.png");


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

	//---------------//
	//   getters     //
	//---------------//
	public int getTailleCase()
	{
		return Math.max(this.getWidth(), this.getHeight());
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		this.g2 = (Graphics2D) g.create();

		this.g2.setColor(ctrl.getCouleurZone(this.casePlateau.getZone()));
		this.g2.fillRect(0, 0, getWidth(), getHeight());

		// dessine une image de fond
		if (this.imgFond != null)
		{
			this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			this.g2.drawImage(this.imgFond, 0, 0, getWidth(), getHeight(), this);
		}

		// dessine un symbole si il s'agit d'une base
		if (this.casePlateau.getSommet() != null)
			if (this.casePlateau.getSommet().getEstBase() > 0)
			{
				this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // remet l'opacité à 100%
				// change la couleur de imgBase
				Image baseTeintee = this.teinteImage(this.imgBase, getWidth(), getHeight(),
									this.casePlateau.getSommet().getVirus().getCouleur());
				
				// dessine la nouvelle imgBase avec sa couleur
				this.g2.drawImage(baseTeintee, 0, 0, getWidth(), getHeight(), this);
			}
				

		this.g2.dispose();
	}

	//----------------------------//
	// Méthodes d'implémentations //
	//----------------------------//
	public void componentResized(ComponentEvent e)
	{
		if ( this.imgSymbole != null )
		{
			this.taille = (int)(this.getTailleCase() * 0.3); 
			
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

	private Image teinteImage(Image img, int w, int h, Color couleur)
	{
		BufferedImage imgRet = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D    g2     = imgRet.createGraphics();

		g2.drawImage(img, 0, 0, w, h, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
		g2.setColor(couleur);
		g2.fillRect(0, 0, w, h);

		g2.dispose();
		return imgRet;
	}
}
