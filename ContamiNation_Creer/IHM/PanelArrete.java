package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.Metier.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelArrete extends JPanel
{
	// Attribut d'instance
	private Controleur ctrl;

	public PanelArrete(Controleur ctrl)
	{
		this.ctrl = ctrl;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3.0f));

		int marge   = 30;
		int rayon   = 30; 

		for (int lig = 0; lig < ctrl.getLig(); lig++)
		{
			for (int col = 0; col < ctrl.getCol(); col++)
			{
				Sommet s = ctrl.getCase(lig, col).getSommet();

				if (s != null)
				{
					JButton btn = ctrl.getButton(lig, col);
					Point centre = SwingUtilities.convertPoint(
						btn.getParent(),
						btn.getX() + btn.getWidth()  / 2,
						btn.getY() + btn.getHeight() / 2,
						this
					);
					if (s.getEstBase() != 0)
						g2d.setColor(Virus.getCouleur(s.getEstBase()));

					g2d.drawOval(centre.x - rayon, centre.y - rayon, rayon * 2, rayon * 2);

					g2d.setColor(Color.BLACK);

					// Dessin des arêtes
					Sommet[] lstVoisins = s.getLstVoisin();
					for (int i = 0; i < lstVoisins.length; i++)
					{
						if (lstVoisins[i] != null)
						{
							JButton btn1 = ctrl.getButton(lig, col);
							JButton btn2 = ctrl.getButton(lstVoisins[i].getLigSommet(), lstVoisins[i].getColSommet());

							Point p1 = SwingUtilities.convertPoint(btn1.getParent(), btn1.getX() + btn1.getWidth() / 2, btn1.getY() + btn1.getHeight() / 2, this);
							Point p2 = SwingUtilities.convertPoint(btn2.getParent(), btn2.getX() + btn2.getWidth() / 2, btn2.getY() + btn2.getHeight() / 2, this);

							double dx       = p2.x - p1.x;
							double dy       = p2.y - p1.y;
							double distance = Math.sqrt(dx * dx + dy * dy);

							if (distance > 0)
							{
								double dirX = dx / distance;
								double dirY = dy / distance;

								int debutX = (int) (p1.x + (dirX * marge));
								int debutY = (int) (p1.y + (dirY * marge));
								int finX   = (int) (p2.x - (dirX * marge));
								int finY   = (int) (p2.y - (dirY * marge));

								if (distance > marge * 2)
								{
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