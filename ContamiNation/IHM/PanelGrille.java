package ContamiNation.IHM;

import ContamiNation.Metier.Case;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import java.awt.Image;

public class PanelGrille extends JPanel implements ActionListener, MouseListener
{
	//private int r = 0;
	//private int g = 0;
	//private int b = 0;	
	
	Color[] tabCouleurs = {
    new Color(255, 255, 255),  // Blanc
    new Color(0, 255, 0),      // Vert
    new Color(0, 0, 255),      // Bleu
    new Color(255, 255, 0),    // Jaune
    new Color(255, 165, 0),    // Orange
    new Color(255, 192, 203),  // Rose
    new Color(128, 0, 128),    // Violet
    new Color(0, 255, 255),    // Cyan
    new Color(165, 42, 42),    // Marron
    new Color(128, 128, 128),  // Gris
    new Color(0, 128, 0),      // Vert foncé
    new Color(0, 0, 128),      // Bleu marine
    new Color(255, 215, 0),    // Or
    new Color(64, 224, 208),   // Turquoise
    new Color(220, 20, 60)     // Crimson
	};

	private JButton[][] tabBtn;
	private FrameGrille frameMere;
	private JPanel panelGrille;
	private JPanel panelBoutton;

	private JButton valider;
	private JButton annuler;
	
	public PanelGrille(int ligne, int colonne, FrameGrille frameMere)
	{
		this.setLayout(new BorderLayout());

		this.panelGrille = new JPanel();
		this.panelGrille.setLayout(new GridLayout(ligne, colonne));
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
				this.tabBtn[lig][col].addMouseListener(this);

				this.panelGrille.add(this.tabBtn[lig][col]);
			}
		}

		this.annuler      = new JButton("Annuler");
		this.valider      = new JButton("Valider");

		this.panelBoutton = new JPanel();

		this.panelBoutton.add(this.valider);
		this.panelBoutton.add(this.annuler);

		this.add(this.panelBoutton, BorderLayout.SOUTH);
		this.add(this.panelGrille);
		
		this.valider.addActionListener(this);
		this.annuler.addActionListener(this);

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
					//this.tabBtn[lig][col].setBackground(nextColor());
					this.tabBtn[lig][col].setBackground(this.tabCouleurs[indCouleur]);
				}
		
		if (e.getSource() == this.valider)
		{
			for (int lig = 0; lig < this.tabBtn.length;lig++)
				for (int col = 0; col < this.tabBtn[0].length;col++)
					if (this.frameMere.getCase(lig, col).getZone() == 0){ return; }

			this.frameMere.fermer();
		}

		if (e.getSource() == this.annuler)
		{
			for (int lig = 0; lig < this.tabBtn.length;lig++)
				for (int col = 0; col < this.tabBtn[0].length;col++)
				{
					this.frameMere.getCase(lig, col).supprimerZone();

					int indCouleur = this.frameMere.getCase(lig, col).getZone();

					this.tabBtn[lig][col].setBackground(this.tabCouleurs[indCouleur]);
					this.initBtn(this.frameMere.getCase(lig, col).toString() + "", lig, col);
				}
		}
	}

	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			for (int lig = 0; lig < this.tabBtn.length;lig++)
				for (int col = 0; col < this.tabBtn[0].length;col++)
					if (e.getSource() == this.tabBtn[lig][col])
					{
						this.frameMere.getCase(lig, col).supprimerZone();

						int indCouleur = this.frameMere.getCase(lig, col).getZone();
						//this.tabBtn[lig][col].setBackground(nextColor());
						this.tabBtn[lig][col].setBackground(this.tabCouleurs[indCouleur]);
						this.initBtn(this.frameMere.getCase(lig, col).toString() + "", lig, col);
					}
		}
	}

	/*private Color nextColor()
	{
		Color c = new Color(r, g, b);

		r = (r + 67) % 256;
		g = (g + 113) % 256;
		b = (b + 193) % 256;

		return c;
	}*/

	public void mouseExited(MouseEvent e) {return;}
	public void mouseEntered(MouseEvent e) {return;}
	public void mouseReleased(MouseEvent e) {return;}
	public void mouseClicked(MouseEvent e) {return;}

}
