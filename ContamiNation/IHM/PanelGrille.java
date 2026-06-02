package ContamiNation.IHM;

import ContamiNation.Metier.Case;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import java.awt.Image;

public class PanelGrille extends JPanel implements ActionListener
{
	private final Color[] tabCouleurs = { Color.WHITE,
										  Color.RED,
  										  Color.BLUE,
  										  Color.GREEN,
  										  Color.YELLOW,
  										  Color.ORANGE,
  										  Color.PINK,
  										  Color.CYAN,
										  Color.MAGENTA,
										  Color.GRAY,
  										  Color.LIGHT_GRAY,
  										  Color.DARK_GRAY,
										  Color.BLACK       };
											
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
				JButton button = new JButton();

				button.setBackground(new Color(255, 255, 255));
				
				this.tabBtn[lig][col] = button;
				this.tabBtn[lig][col].setOpaque(true);
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
				{
					this.frameMere.ajouterZone(lig, col);

					int indCouleur = this.frameMere.getCase(lig, col).getZone();
					this.tabBtn[lig][col].setBackground(this.tabCouleurs[indCouleur]);
				}
					

	
	}

}
