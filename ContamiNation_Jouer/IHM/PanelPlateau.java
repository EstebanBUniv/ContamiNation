package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Metier.Case;

import ContamiNation_Jouer.Controleur;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class PanelPlateau extends JPanel
{
	private FrameJeu frameMere;
	private Controleur ctrl;

	private int lig;
	private int col;

	public PanelPlateau(FrameJeu frame, Controleur ctrl)
	{
		this.frameMere = frame;
		this.ctrl      = ctrl;

		this.lig = this.ctrl.getPlateau().getLig();
		this.col = this.ctrl.getPlateau().getCol();

		this.setLayout(new GridLayout(lig, col));

		for ( int cptLig = 0; cptLig < this.lig; cptLig++ )
		{
			for ( int cptCol = 0; cptCol < this.col; cptCol++ )
			{
				Case caseActuelle = this.ctrl.getPlateau().getCase(cptLig, cptCol);
				this.add(new PanelCase(caseActuelle));
			}
		}


	}
}