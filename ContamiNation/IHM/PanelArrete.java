package ContamiNation.IHM;

import ContamiNation.Controleur;
import ContamiNation.Metier.*;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PanelArrete extends JPanel
{
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

		for (int lig = 0; lig < ctrl.getLig(); lig++)
		{
			for (int col = 0; col < ctrl.getCol(); col++)
			{
				Sommet s = ctrl.getCase(lig, col).getSommet();

				if (s != null)
				{
					Sommet[] lstVoisins = s.getLstVoisin();
					for (int i = 0; i < lstVoisins.length; i++)
					{
						if (lstVoisins[i] != null)
						{
							JButton btn1 = ctrl.getButton(lig, col);
							JButton btn2 = ctrl.getButton(lstVoisins[i].getLigSommet(), lstVoisins[i].getColSommet());

							Point p1 = SwingUtilities.convertPoint(btn1.getParent(), btn1.getX() + btn1.getWidth() / 2, btn1.getY() + btn1.getHeight() / 2, this);
							Point p2 = SwingUtilities.convertPoint(btn2.getParent(), btn2.getX() + btn2.getWidth() / 2, btn2.getY() + btn2.getHeight() / 2, this);

							g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
						}
					}
				}
			}
		}
	}
}