package ContamiNation.IHM;

import ContamiNation.Controleur;
import ContamiNation.Metier.*;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PanelArrete extends JPanel
{
    private Controleur ctrl;
    private Sommet[]   lstVoisins;

    public PanelArrete(Controleur ctrl)
    {
        this.ctrl       = ctrl;
        this.lstVoisins = new Sommet[8];
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

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
                            g.drawLine(
                                ctrl.getButton(lig, col).getX() + ctrl.getButton(lig, col).getWidth() / 2,
                                ctrl.getButton(lig, col).getY() + ctrl.getButton(lig, col).getHeight() / 2,
                                ctrl.getButton(lstVoisins[i].getLigSommet(), lstVoisins[i].getColSommet()).getX() + ctrl.getButton(lig, col).getWidth() / 2,
                                ctrl.getButton(lstVoisins[i].getLigSommet(), lstVoisins[i].getColSommet()).getY() + ctrl.getButton(lig, col).getHeight() / 2
                            );
                        }
                    }
                }
            }
        }
    }
}