package ContamiNation.IHM;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class PanelGrille extends JPanel implements ActionListener
{
	private JButton[][] tabBtn;
	private FrameGrille frameMere;
	
	public PanelGrille(int hauteur, int largeur, FrameGrille frameMere)
	{
		this.setLayout(new GridLayout(hauteur,largeur));
		this.frameMere = frameMere;
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.tabBtn = new JButton[hauteur][largeur];
		

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		for(int i = 0; i < tabBtn.length; i++)
		{
			for (int j = 0; j < tabBtn[i].length; j++)
			{
				this.tabBtn[i][j] = new JButton();
				this.tabBtn[i][j].setOpaque(false);
				this.tabBtn[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

				this.add(this.tabBtn[i][j]);
			}
		}
		
		for (int lig = 0; lig < this.tabBtn.length;lig++)
			for (int col = 0; col < this.tabBtn[0].length;col++)
				this.tabBtn[lig][col].addActionListener(this);
	}
	
	public void initBtn (String valeur, int lig, int col)
	{
		this.tabBtn[lig][col].setText(valeur);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		for (int lig = 0; lig < this.tabBtn.length;lig++)
			for (int col = 0; col < this.tabBtn[0].length;col++)
				if (e.getSource() == this.tabBtn[lig][col])
					frameMere.ajouterZone(lig, col);
	
	}

}
