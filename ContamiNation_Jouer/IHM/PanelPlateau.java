package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelPlateau extends JPanel
{
	// Attribut d'instance
	private FrameJeu frameMere;
	private Controleur ctrl;

	private PanelCase[][] tabPanel;

	private JPanel        panelCase;
	private JLabel        lbJoueur;

	private int lig;
	private int col;
	private int idJoueur;

	public PanelPlateau(FrameJeu frame, Controleur ctrl, int idJoueur)
	{
		this.frameMere = frame;
		this.ctrl      = ctrl;
		this.idJoueur  = idJoueur;

		this.setLayout(new BorderLayout(5, 5));

		this.lig = this.ctrl.getLig();
		this.col = this.ctrl.getCol();

		this.tabPanel = new PanelCase[this.lig][this.col];

		this.panelCase = new JPanel(new GridLayout(lig, col, 0, 0));

		if ( this.ctrl.getModeMulti() )
		{
			this.lbJoueur = new JLabel("Jouer " + (this.idJoueur+1));
			this.lbJoueur.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(this.lbJoueur, BorderLayout.NORTH);
		}
		


		//this.setLayout(new GridLayout(lig, col, 0, 0));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		int indexVirusActif = ctrl.getPlateau(idJoueur).getOffsetVirus() % ctrl.getPlateau(idJoueur).getNbVirus();
   		this.changerCouleurManche(indexVirusActif);

		for ( int cptLig = 0; cptLig < this.lig; cptLig++ )
		{
			for ( int cptCol = 0; cptCol < this.col; cptCol++ )
			{
				this.tabPanel[cptLig][cptCol] = new PanelCase(cptLig, cptCol, this.ctrl, this.idJoueur);
				this.panelCase.add(this.tabPanel[cptLig][cptCol]);
			}
		}

		this.add(this.panelCase);
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