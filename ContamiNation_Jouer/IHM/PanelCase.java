package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelCase extends JPanel implements ComponentListener, ActionListener
{
	private Controleur ctrl;
	
	private JButton    btnCase;
	private Image      imgSymbole;
	private Image      imgFond;
	private Image      imgBase;
	private Graphics2D g2;
	private int        lig;
	private int        col;

	public PanelCase(int lig, int col, Controleur ctrl)
	{
		this.ctrl        = ctrl;
		this.lig         = lig;
		this.col         = col;

		this.setLayout(new BorderLayout());
		this.setBorder(null);

		this.imgFond = getToolkit().getImage("../images/fond/fond_case.png");
		this.imgBase = getToolkit().getImage("../images/fond/base.png");

		if (this.ctrl.getCase(lig, col).getSommet() != null)
		{
			this.btnCase = new JButton();
			String symbole  = this.ctrl.getCase(lig, col).getSommet().getSymbole();
			this.imgSymbole = getToolkit().getImage("../images/symboles/symbole_" + symbole + ".png");

			Image img = imgSymbole.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			this.btnCase.setIcon(new ImageIcon(img));
		}
		else
		{
			this.btnCase = new JButton();
		}

		this.btnCase.setOpaque(false);
		this.btnCase.setContentAreaFilled(false);
		this.btnCase.setBorderPainted(false);
		this.btnCase.setFocusPainted(false);

		this.add(this.btnCase, BorderLayout.CENTER);
		this.addComponentListener(this);
		this.btnCase.addActionListener(this);
	}

	public int getTailleCase()
	{
		return Math.max(this.getWidth(), this.getHeight());
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.g2 = (Graphics2D) g.create();

		// 1. Dessin de la couleur de zone
		this.g2.setColor(this.ctrl.getCouleurZone(this.ctrl.getCase(this.lig, this.col).getZone()));
		this.g2.fillRect(0, 0, getWidth(), getHeight());

		// 2. Image de fond texturée
		if (this.imgFond != null)
		{
			this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			this.g2.drawImage(this.imgFond, 0, 0, getWidth(), getHeight(), this);
			this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}

		// 3. RESTAURATION DES SURBRILLANCES 

		if (ctrl.getCaseSelectionnee() != null && this.ctrl.getCase(this.lig, this.col).getSommet() != null)
		{
			if (this.ctrl.getCase(this.lig, this.col) == ctrl.getCaseSelectionnee())
			{
				// Premier clic : la case sélectionnée s'allume en Jaune
				this.g2.setColor(new Color(255, 200, 0, 150));
				this.g2.fillRect(0, 0, getWidth(), getHeight());
			}
			else if (ctrl.estVoisinAtteignable(this.ctrl.getCase(this.lig, this.col)))
			{
				// Les chemins cibles légaux s'allument en Vert
				this.g2.setColor(new Color(0, 220, 80, 120));
				this.g2.fillRect(0, 0, getWidth(), getHeight());
			}
		}

		// 4. Dessin de la base du virus colorée
		if (this.ctrl.getCase(this.lig, this.col).getSommet() != null && this.ctrl.getCase(this.lig, this.col).getSommet().getEstBase() > 0)
		{
			this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			Image baseTeintee = this.teinteImage(this.imgBase, getWidth(), getHeight(),
								this.ctrl.getCase(this.lig, this.col).getSommet().getVirus().getCouleur());
			this.g2.drawImage(baseTeintee, 0, 0, getWidth(), getHeight(), this);
		}

		this.g2.dispose();
	}

	public void componentResized(ComponentEvent e)
	{
		if (this.imgSymbole != null)
		{
			int   taille = (int)(this.getTailleCase() * 0.3);
			Image img    = this.imgSymbole.getScaledInstance(taille, taille, Image.SCALE_SMOOTH);
			this.btnCase.setIcon(new ImageIcon(img));
			this.revalidate();
			this.repaint();
		}
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentShown (ComponentEvent e) {}
	public void componentMoved (ComponentEvent e) {}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnCase)
		{
			this.ctrl.verifSommet(this.ctrl.getCase(this.lig, this.col));
		}
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