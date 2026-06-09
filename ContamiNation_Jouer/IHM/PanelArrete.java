package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ContamiNation_Jouer.Metier.Sommet;
import ContamiNation_Jouer.Metier.Virus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;

public class PanelArrete extends JPanel
{
	private Controleur ctrl;

	public PanelArrete(Controleur ctrl)
	{
		this.ctrl     = ctrl;
		this.setOpaque(false);

	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(3.0f));
		int marge = 30;

		// 1. ON DESSINE LE RÉSEAU DE BASE (TOUT EN NOIR)
		for (int lig = 0; lig < ctrl.getLig(); lig++)
		{
			for (int col = 0; col < ctrl.getCol(); col++)
			{
				Sommet s = ctrl.getCase(lig, col).getSommet();
				if (s != null)
				{
					g2d.setColor(Color.BLACK);
					for (Sommet voisin : s.getLstVoisin())
					{
						if (voisin != null)
						{
							JPanel panel1 = ctrl.getPanel(lig, col);
							JPanel panel2 = ctrl.getPanel(voisin.getLigSommet(), voisin.getColSommet());

							Point p1 = SwingUtilities.convertPoint(panel1.getParent(), panel1.getX() + panel1.getWidth() / 2, panel1.getY() + panel1.getHeight() / 2, this);
							Point p2 = SwingUtilities.convertPoint(panel2.getParent(), panel2.getX() + panel2.getWidth() / 2, panel2.getY() + panel2.getHeight() / 2, this);

							if (p1.distance(p2) > marge * 2) {
								double dirX = (p2.x - p1.x) / p1.distance(p2);
								double dirY = (p2.y - p1.y) / p1.distance(p2);
								g2d.drawLine((int)(p1.x + dirX * marge), (int)(p1.y + dirY * marge), (int)(p2.x - dirX * marge), (int)(p2.y - dirY * marge));
							}
						}
					}
				}
			}
		}

		// 2. ON DESSINE PAR-DESSUS LES CHEMINS DES VIRUS CONQUIS (EN COULEUR)
		// On parcourt tous les virus présents sur le plateau
		for (int i = 0; i < ctrl.getPlateau().getNbVirus(); i++)
		{
			Virus v = ctrl.getVirus(i);
			if (v != null && v.getConquis().size() > 1)
			{
				g2d.setColor(v.getCouleur());
				LinkedList<Sommet> chemin = v.getConquis();
				
				// On relie chaque sommet du chemin au suivant
				for (int c = 0; c < chemin.size() - 1; c++)
				{
					Sommet s1 = chemin.get(c);
					Sommet s2 = chemin.get(c + 1);
					
					JPanel panel1 = ctrl.getPanel(s1.getLigSommet(), s1.getColSommet());
					JPanel panel2 = ctrl.getPanel(s2.getLigSommet(), s2.getColSommet());

					Point p1 = SwingUtilities.convertPoint(panel1.getParent(), panel1.getX() + panel1.getWidth() / 2, panel1.getY() + panel1.getHeight() / 2, this);
					Point p2 = SwingUtilities.convertPoint(panel2.getParent(), panel2.getX() + panel2.getWidth() / 2, panel2.getY() + panel2.getHeight() / 2, this);

					double dirX = (p2.x - p1.x) / p1.distance(p2);
					double dirY = (p2.y - p1.y) / p1.distance(p2);
					g2d.drawLine((int)(p1.x + dirX * marge), (int)(p1.y + dirY * marge), (int)(p2.x - dirX * marge), (int)(p2.y - dirY * marge));
				}
			}
		}
	}
}