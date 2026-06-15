package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;

import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.util.ArrayList;
import java.util.LinkedList;

public class PanelArrete extends JPanel
{
	private ArrayList<String> arretesColorees;
	private String            cleArrete;
	// Attribut d'instance
	private Controleur   ctrl;
	private int          marge;
	private int          idJoueur;
	private PanelPlateau monPlateau;
	
	public PanelArrete(Controleur ctrl, int idJoueur, PanelPlateau monPlateau)
	{
		this.ctrl            = ctrl;
		this.idJoueur        = idJoueur;
		this.monPlateau      = monPlateau;
		this.arretesColorees = new ArrayList<>();
		this.setOpaque(false);

	}

	//----------------------------//
	// Méthodes d'implémentations //
	//----------------------------//
	public void componentResized(ComponentEvent e)
	{
		this.marge = (int)(this.monPlateau.getTailleCase() * 0.1);
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentShown (ComponentEvent e) {}
	public void componentMoved (ComponentEvent e) {}


	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.arretesColorees.clear();

		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(1.0f)); 
		this.marge = (int)(this.monPlateau.getTailleCase() * 0.1);
		for (int lig = 0; lig < ctrl.getLig(); lig++)
		{
			for (int col = 0; col < ctrl.getCol(); col++)
			{
				if (ctrl.possedeSommet(lig, col, this.idJoueur))
				{
					g2d.setColor(Color.BLACK);
					for (int dir = 0; dir < 8; dir++)
					{
						if (ctrl.possedeVoisin(lig, col, dir, this.idJoueur))
						{
							int ligV = ctrl.getLigVoisin(lig, col, dir, this.idJoueur);
							int colV = ctrl.getColVoisin(lig, col, dir, this.idJoueur);

							JPanel panel1 = this.monPlateau.getPanel(lig, col);
							JPanel panel2 = this.monPlateau.getPanel(ligV, colV);

							Point p1 = SwingUtilities.convertPoint(panel1.getParent(), panel1.getX() + panel1.getWidth() / 2, panel1.getY() + panel1.getHeight() / 2, this);
							Point p2 = SwingUtilities.convertPoint(panel2.getParent(), panel2.getX() + panel2.getWidth() / 2, panel2.getY() + panel2.getHeight() / 2, this);

							if (p1.distance(p2) > this.marge * 2) {
								double dirX = (p2.x - p1.x) / p1.distance(p2);
								double dirY = (p2.y - p1.y) / p1.distance(p2);
								g2d.drawLine((int)(p1.x + dirX * this.marge), (int)(p1.y + dirY * this.marge), (int)(p2.x - dirX * this.marge), (int)(p2.y - dirY * this.marge));
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < ctrl.getNbVirus(this.idJoueur); i++)
		{
			int tailleChemin = ctrl.getTailleCheminVirus(this.idJoueur, i);
			if (tailleChemin > 1)
			{
				g2d.setColor(ctrl.getCouleurVirus(this.idJoueur, i));
				g2d.setStroke(new BasicStroke(3.0f));
				for (int c = 0; c < tailleChemin - 1; c++)
				{
					int lig1 = ctrl.getLigChemin(this.idJoueur, i, c);
					int col1 = ctrl.getColChemin(this.idJoueur, i, c);
					
					int lig2 = ctrl.getLigChemin(this.idJoueur, i, c + 1);
					int col2 = ctrl.getColChemin(this.idJoueur, i, c + 1);

					JPanel panel1 = this.monPlateau.getPanel(lig1, col1);
					JPanel panel2 = this.monPlateau.getPanel(lig2, col2);
						
					Point p1 = SwingUtilities.convertPoint(panel1.getParent(), panel1.getX() + panel1.getWidth() / 2, panel1.getY() + panel1.getHeight() / 2, this);
					Point p2 = SwingUtilities.convertPoint(panel2.getParent(), panel2.getX() + panel2.getWidth() / 2, panel2.getY() + panel2.getHeight() / 2, this);

					double dirX = (p2.x - p1.x) / p1.distance(p2);
					double dirY = (p2.y - p1.y) / p1.distance(p2);
					g2d.drawLine((int)(p1.x + dirX * this.marge), (int)(p1.y + dirY * this.marge), (int)(p2.x - dirX * this.marge), (int)(p2.y - dirY * this.marge));
				}
			}
		}
	}
}
