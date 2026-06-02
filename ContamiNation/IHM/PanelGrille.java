package ContamiNation.IHM;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class PanelGrille extends JPanel
{
	private JLabel[][] tabLbl;
	
	public PanelGrille()
	{
		this.setLayout(new GridLayout(10,10)); //a modifier
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.tabLbl = new JLabel[10][10]; //a modifier
		

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		for(int i = 0; i < tabLbl.length; i++)
		{
			for (int j = 0; j < tabLbl[i].length; j++)
			{
				this.tabLbl[i][j] = new JLabel();
				this.tabLbl[i][j].setOpaque(true);
				this.tabLbl[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

				this.add(this.tabLbl[i][j]);
			}
		}
	}
}
