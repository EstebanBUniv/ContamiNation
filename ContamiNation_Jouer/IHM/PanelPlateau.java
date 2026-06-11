package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PanelPlateau extends JPanel
{
	// Attribut d'instance
	private FrameJeu frameMere;
	private Controleur ctrl;

	private PanelCase[][] tabPanel;

	private int lig;
	private int col;
	private int idJoueur;

	public PanelPlateau(FrameJeu frame, Controleur ctrl, int idJoueur)
	{
		this.frameMere = frame;
		this.ctrl      = ctrl;
		this.idJoueur  = idJoueur;

		this.lig = this.ctrl.getLig();
		this.col = this.ctrl.getCol();

		this.tabPanel = new PanelCase[this.lig][this.col];

		this.setLayout(new GridLayout(lig, col, 0, 0));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		int indexVirusActif = ctrl.getPlateau(idJoueur).getOffsetVirus() % ctrl.getPlateau(idJoueur).getNbVirus();
   		this.changerCouleurManche(indexVirusActif);

		for ( int cptLig = 0; cptLig < this.lig; cptLig++ )
		{
			for ( int cptCol = 0; cptCol < this.col; cptCol++ )
			{
				this.tabPanel[cptLig][cptCol] = new PanelCase(cptLig, cptCol, this.ctrl, this.idJoueur);
				this.add(this.tabPanel[cptLig][cptCol]);
			}
		}
	}

	public void changerCouleurManche(int num)
	{
		this.setBackground(ctrl.getCouleurVirus(this.idJoueur, num));
	}

	public void changerImageBase()
	{
		for ( int lig = 0; lig < this.tabPanel.length; lig++ )
			for ( int col = 0; col < this.tabPanel[lig].length; col++ )
				this.tabPanel[lig][col].initImgBase();
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
	public PanelCase getPanel(int lig, int col) { return this.tabPanel[lig][col]; }
}