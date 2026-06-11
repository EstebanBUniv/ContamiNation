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
	private int        idJoueur;

	public PanelCase(int lig, int col, Controleur ctrl, int idJoueur)
	{
		this.ctrl        = ctrl;
		this.lig         = lig;
		this.col         = col;
		this.idJoueur    = idJoueur;

		this.setLayout(new BorderLayout());
		this.setBorder(null);

		this.initImgFond();
		this.initImgBase();

		if (this.ctrl.getCase(this.lig, this.col, this.idJoueur).getSommet() != null)
		{
			this.btnCase = new JButton();
			String symbole  = this.ctrl.getCase(lig, col, this.idJoueur).getSommet().getSymbole();
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

	private void initImgFond()
	{
		int[][] directions =
		{
			{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
		};

		// côtés adjacents à chaque coin
		int[][] dependances = { {0, 2}, {0, 3}, {1, 2}, {1, 3} };

		boolean[] memeZone = new boolean[8];
		int        zoneCourante = this.ctrl.getCase(this.lig, this.col, this.idJoueur).getZone();

		// teste les 8 directions
		for (int cptDir = 0; cptDir < 8; cptDir++)
		{
			int ligVoisin = this.lig + directions[cptDir][0];
			int colVoisin = this.col + directions[cptDir][1];

			memeZone[cptDir] = ligVoisin >= 0 && ligVoisin < this.ctrl.getLig() &&
							colVoisin >= 0 && colVoisin < this.ctrl.getCol() &&
							this.ctrl.getCase(ligVoisin, colVoisin, this.idJoueur).getZone() == zoneCourante;
		}

		// annuler les coins si leurs côtés adjacents ne sont pas tous dans la même zone
		for (int cptCoin = 0; cptCoin < 4; cptCoin++)
		{
			int indiceCoin = cptCoin + 4;
			int cote1      = dependances[cptCoin][0];
			int cote2      = dependances[cptCoin][1];

			if (!memeZone[cote1] || !memeZone[cote2])
				memeZone[indiceCoin] = false; // coin ignoré
		}

		// construire le nom du fichier
		StringBuilder numeroCase = new StringBuilder();
		for (int cptDir = 0; cptDir < 8; cptDir++)
			if (memeZone[cptDir])
				numeroCase.append(cptDir);

		this.imgFond = getToolkit().getImage("../images/fond/case/fond_case_" + numeroCase + ".png");
	}

	public void initImgBase()
	{
		String chemin = "../images/fond/case/base";

		if ( this.ctrl.getCase(this.lig, this.col, this.idJoueur).getSommet() != null)
			if ( this.ctrl.getCase(this.lig, this.col, this.idJoueur).getSommet().getEstBase() == this.ctrl.getPlateau(idJoueur).getNumManche() )
				chemin += "Actuelle";

		chemin += ".png";

		this.imgBase = getToolkit().getImage(chemin);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.g2 = (Graphics2D) g.create();

		// 1. Dessin de la couleur de zone
		this.g2.setColor(this.ctrl.getCouleurZone(this.ctrl.getCase(this.lig, this.col, this.idJoueur).getZone()));
		this.g2.fillRect(0, 0, getWidth(), getHeight());

		// 2. Image de fond texturée
		if (this.imgFond != null)
		{
			this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			this.g2.drawImage(this.imgFond, 0, 0, getWidth(), getHeight(), this);
			this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}

		// 3. RESTAURATION DES SURBRILLANCES 

		if (ctrl.getCaseSelectionnee() != null && this.ctrl.getCase(this.lig, this.col, this.idJoueur).getSommet() != null)
		{
			if (this.ctrl.getCase(this.lig, this.col, this.idJoueur) == ctrl.getCaseSelectionnee())
			{
				// Premier clic : la case sélectionnée s'allume en Jaune
				this.g2.setColor(new Color(255, 200, 0, 150));
				this.g2.fillRect(0, 0, getWidth(), getHeight());
			}
			else if (ctrl.estVoisinAtteignableMulti(this.ctrl.getCase(this.lig, this.col, this.idJoueur), this.idJoueur))
			{
				// Les chemins cibles légaux s'allument en Vert
				this.g2.setColor(new Color(0, 220, 80, 120));
				this.g2.fillRect(0, 0, getWidth(), getHeight());
			}
		}

		// 4. Dessin de la base du virus colorée
		if (this.ctrl.getCase(this.lig, this.col, this.idJoueur).getSommet() != null && this.ctrl.getCase(this.lig, this.col, this.idJoueur).getSommet().getEstBase() > 0)
		{
			this.g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			Image baseTeintee = this.teinteImage(this.imgBase, getWidth(), getHeight(),
								this.ctrl.getCase(this.lig, this.col, this.idJoueur).getSommet().getVirus().getCouleur());
			this.g2.drawImage(baseTeintee, 0, 0, getWidth(), getHeight(), this);
		}

		this.g2.dispose();
	}

	public void componentResized(ComponentEvent e)
	{
		if (this.imgSymbole != null)
		{
			int   taille = (int)(this.getTailleCase() * 0.3);
			if (taille <= 0) return;
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
			this.ctrl.verifSommet(this.ctrl.getCase(this.lig, this.col, this.idJoueur), this.idJoueur);
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