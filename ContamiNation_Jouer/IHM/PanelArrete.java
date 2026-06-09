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

		int marge   = 30;

		for (int lig = 0; lig < ctrl.getLig(); lig++)
		{
			for (int col = 0; col < ctrl.getCol(); col++)
			{
				Sommet s = ctrl.getCase(lig, col).getSommet();

				if (s != null)
				{
					g2d.setColor(Color.BLACK);

					// Dessin des arêtes
					Sommet[] lstVoisins = s.getLstVoisin();

					for (int i = 0; i < lstVoisins.length; i++)
					{
						if (lstVoisins[i] != null)
						{
							JPanel panel1 = ctrl.getPanel(lig, col);
							JPanel panel2 = ctrl.getPanel(lstVoisins[i].getLigSommet(), lstVoisins[i].getColSommet());

							Point p1 = SwingUtilities.convertPoint(panel1.getParent(), panel1.getX() + panel1.getWidth() / 2, panel1.getY() + panel1.getHeight() / 2, this);
							Point p2 = SwingUtilities.convertPoint(panel2.getParent(), panel2.getX() + panel2.getWidth() / 2, panel2.getY() + panel2.getHeight() / 2, this);

							// Calcul du vecteur directeur (dx, dy) entre les deux sommets
							double dx       = p2.x - p1.x;
							double dy       = p2.y - p1.y;
							
							// Théorème de Pythagore : distance = sqrt(dx² + dy²)
							double distance = Math.sqrt(dx * dx + dy * dy);

							if (distance > 0)
							{
								double dirX = dx / distance;
								double dirY = dy / distance;
								
								// Application de la marge :
                                // On déplace le point de départ et d'arrivée le long de la ligne
                                // pour que l'arête s'arrête au bord du cercle et non au centre.
								int debutX = (int) (p1.x + (dirX * marge));
								int debutY = (int) (p1.y + (dirY * marge));
								int finX   = (int) (p2.x - (dirX * marge));
								int finY   = (int) (p2.y - (dirY * marge));
								
								// Condition pour éviter de dessiner si les sommets sont trop proches
								if (distance > marge * 2)
								{
									if(s.getEstBase() != 0)
									{
										Virus virus = this.ctrl.getVirus(s.getEstBase() - 1);
										LinkedList<Sommet> lstConquis = virus.getConquis();
										for (Sommet sommet : lstConquis)
										{
											g2d.setColor(virus.getCouleur());
											g2d.drawLine(debutX, debutY, finX, finY);
										}
									}
									else
									{
										g2d.setColor(Color.BLACK);
										g2d.drawLine(debutX, debutY, finX, finY);
									}	

								}
							}
						}
					}
				}

			}
		}
	}
}