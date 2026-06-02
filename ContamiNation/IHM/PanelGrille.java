package ContamiNation.IHM;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class PanelGrille extends JPanel implements ActionListener
{
	private JButton[][] tabBtn;
	private FrameGrille frameMere;
	
	public PanelGrille(int ligne, int colonne, FrameGrille frameMere)
	{
		this.setLayout(new GridLayout(ligne, colonne));
		this.frameMere = frameMere;
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.tabBtn = new JButton[ligne][colonne];
		

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		for(int lig = 0; lig < tabBtn.length; lig++)
		{
			for (int col = 0; col < tabBtn[lig].length; col++)
			{
				this.tabBtn[lig][col] = new JButton();
				this.tabBtn[lig][col].setOpaque(false);
				this.tabBtn[lig][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

				this.add(this.tabBtn[lig][col]);
			}
		}
		
		for (int lig = 0; lig < this.tabBtn.length; lig++)
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
