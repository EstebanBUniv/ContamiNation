package ContamiNation.IHM;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class PanelGrille extends JPanel
{
	private JButton[][] tabLbl;
	
	public PanelGrille(int hauteur, int largeur)
	{
		this.setLayout(new GridLayout(hauteur,largeur));
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.tabLbl = new JButton[hauteur][largeur];
		

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		for(int i = 0; i < tabLbl.length; i++)
		{
			for (int j = 0; j < tabLbl[i].length; j++)
			{
				this.tabLbl[i][j] = new JButton();
				this.tabLbl[i][j].setOpaque(false);
				this.tabLbl[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

				this.add(this.tabLbl[i][j]);
			}
		}
	}
	
	public void initBtn (String valeur, int hauteur, int largeur)
	{
		this.tabLbl[hauteur][largeur].setText(valeur);
	}
}
