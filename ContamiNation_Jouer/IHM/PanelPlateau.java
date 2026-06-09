package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Metier.Case;

import ContamiNation_Jouer.Controleur;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class PanelPlateau extends JPanel
{
	private FrameJeu frameMere;
	private Controleur ctrl;

	private JPanel[][] tabPanel;

	private int lig;
	private int col;

	public PanelPlateau(FrameJeu frame, Controleur ctrl)
	{
		this.frameMere = frame;
		this.ctrl      = ctrl;

		this.lig = this.ctrl.getPlateau().getLig();
		this.col = this.ctrl.getPlateau().getCol();

		this.tabPanel = new JPanel[this.lig][this.col];

		this.setLayout(new GridLayout(lig, col, 0, 0));

		for ( int cptLig = 0; cptLig < this.lig; cptLig++ )
		{
			for ( int cptCol = 0; cptCol < this.col; cptCol++ )
			{
				this.tabPanel[cptLig][cptCol] = new PanelCase(this.ctrl.getPlateau().getCase(cptLig, cptCol), this.ctrl);
				this.add(this.tabPanel[cptLig][cptCol]);
			}
		}
	}

	public JPanel[][] getTabPanel() { return this.tabPanel; }
}