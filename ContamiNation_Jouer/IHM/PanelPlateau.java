package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelPlateau extends JPanel
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/

	private FrameJeu   frameMere;
	private Controleur ctrl;

	private PanelCase[][] tabPanel;

	private JPanel        panelCase;
	private JLabel        lbJoueur;

	private int lig;
	private int col;
	private int idJoueur;

	public PanelPlateau(FrameJeu frame, Controleur ctrl, int idJoueur)
	{

		/*----------------------------*/
		/*  Création des composants   */
		/*----------------------------*/
		this.frameMere = frame;
		this.ctrl      = ctrl;
		this.idJoueur  = idJoueur;

		this.setLayout(new BorderLayout(5, 5));

		this.lig = this.ctrl.getLig();
		this.col = this.ctrl.getCol();

		

		this.tabPanel = new PanelCase[this.lig][this.col];

		this.panelCase = new JPanel(new GridLayout(lig, col, 0, 0));
		this.panelCase.setOpaque(false);

		if ( this.ctrl.getModeMulti() )
			this.lbJoueur = new JLabel("Joueur " + (this.idJoueur+1) + " - Propagation de " + this.ctrl.getVirus(this.idJoueur));
		else
			this.lbJoueur = new JLabel("Propagation de " + this.ctrl.getVirus(this.idJoueur));

		this.lbJoueur.setFont(Controleur.POLICE_TEXTE);
		this.lbJoueur.setHorizontalAlignment(SwingConstants.CENTER);

		/*-------------------------------------*/
		/*  Positionnement des compostants     */
		/*-------------------------------------*/

		this.add(this.lbJoueur, BorderLayout.NORTH);
		
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
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

	// Change le nom du virus à chaque manche passée et prends en compte le mode multijoueur
	public void changerLabelPropagation()
	{
		if ( this.ctrl.getModeMulti())
			this.lbJoueur.setText("Joueur " + (this.idJoueur+1) + " - Propagation de " + this.ctrl.getVirus(this.idJoueur));
		else
			this.lbJoueur.setText("Propagation de " + this.ctrl.getVirus(this.idJoueur));
	}

	// Change la couleur autour du plateau en fonction du virus
	public void changerCouleurManche(int num)
	{
		this.setBackground(ctrl.getCouleurVirus(this.idJoueur, num));
	}

	// Initie l'image en une image de base pour la différencier
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