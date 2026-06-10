package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class PanelPlateau extends JPanel
{
	// Attribut d'instance
	private FrameJeu frameMere;
	private Controleur ctrl;

	private PanelCase[][] tabPanel;

	private int lig;
	private int col;

	public PanelPlateau(FrameJeu frame, Controleur ctrl)
	{
		this.frameMere = frame;
		this.ctrl      = ctrl;

		this.lig = this.ctrl.getPlateau().getLig();
		this.col = this.ctrl.getPlateau().getCol();

		this.tabPanel = new PanelCase[this.lig][this.col];

		this.setLayout(new GridLayout(lig, col, 0, 0));

		for ( int cptLig = 0; cptLig < this.lig; cptLig++ )
		{
			for ( int cptCol = 0; cptCol < this.col; cptCol++ )
			{
				this.tabPanel[cptLig][cptCol] = new PanelCase(this.ctrl.getCase(cptLig, cptCol), this.ctrl);
				this.add(this.tabPanel[cptLig][cptCol]);
			}
		}
	}

	//--------------//
	//    getters   //
	//--------------//
	public int getTailleCase()
	{
		if ( this.tabPanel != null )
			return this.tabPanel[0][0].getTailleCase();
		return 0;
	}

	public PanelCase[][] getTabPanel() { return this.tabPanel; }
}