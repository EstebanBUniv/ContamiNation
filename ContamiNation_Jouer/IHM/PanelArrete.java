package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import java.awt.event.ComponentListener;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class PanelArrete extends JPanel
{
	private ArrayList<String> arretesColorees;
	private String            cleArrete;
	// Attribut d'instance
	private Controleur ctrl;
	private int        marge;
	public PanelArrete(Controleur ctrl)
	{
		this.ctrl            = ctrl;
		this.arretesColorees = new ArrayList<>();
		this.setOpaque(false);

	}

	//----------------------------//
	// Méthodes d'implémentations //
	//----------------------------//
	public void componentResized(ComponentEvent e)
	{
		this.marge = (int)(this.ctrl.getTailleCase() * 0.1);
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentShown (ComponentEvent e) {}
	public void componentMoved (ComponentEvent e) {}


	// Affiche des arrètes
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		this.arretesColorees.clear();

		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(3.0f)); // choix de la taille des traits
		// 1. ON DESSINE LE RÉSEAU DE BASE (TOUT EN NOIR)
		this.marge = (int)(this.ctrl.getTailleCase() * 0.2);
		for (int lig = 0; lig < ctrl.getLig(); lig++)
		{
			for (int col = 0; col < ctrl.getCol(); col++)
			{
				if (ctrl.getCase(lig, col).getSommet() != null)
				{
					g2d.setColor(Color.BLACK);
					for (int cptVoisin = 0 ; cptVoisin < ctrl.getCase(lig, col).getSommet().getLstVoisin().length ; cptVoisin++)
					{
						if (ctrl.getCase(lig, col).getSommet().getVoisin(cptVoisin) != null)
						{
							JPanel panel1 = ctrl.getPanel(lig, col);
							JPanel panel2 = ctrl.getPanel(ctrl.getCase(lig, col).getSommet().getVoisin(cptVoisin).getLigSommet(), 
														  ctrl.getCase(lig, col).getSommet().getVoisin(cptVoisin).getColSommet());

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

		for (int i = 0; i < ctrl.getPlateau().getNbVirus(); i++)
		{
			if (ctrl.getVirus(i) != null && ctrl.getVirus(i).getConquis().size() > 1)
			{
				
				for (int c = 0; c < ctrl.getVirus(i).getConquis().size() - 1; c++)
				{

		
					g2d.setColor(ctrl.getVirus(i).getCouleur());
						
					JPanel panel1 = ctrl.getPanel(ctrl.getVirus(i).getConquis().get(c).getLigSommet(), ctrl.getVirus(i).getConquis().get(c).getColSommet());
					JPanel panel2 = ctrl.getPanel(ctrl.getVirus(i).getConquis().get(c + 1).getLigSommet(), ctrl.getVirus(i).getConquis().get(c + 1).getColSommet());
						
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
