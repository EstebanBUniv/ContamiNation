package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Metier.Case;
import ContamiNation_Creer.Controleur;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class FrameSommet extends JFrame
{
	private PanelGrille panelGrille;
	private PanelSommet panelOutils;
	private Controleur  ctrl;
	private PanelArrete panelArrete;

	private Boolean     modeBase;
	private int         cptVirus;

	public FrameSommet(Controleur ctrl, int lig, int col)
	{
		this.setTitle("ContamiNation - Sommets");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());

		this.ctrl = ctrl;

		//-------------------------//
		// Création des composants //
		//-------------------------//

		this.cptVirus    = this.ctrl.getNbVirus();
		this.panelOutils = new PanelSommet(this);
		this.panelGrille = new PanelGrille(lig, col, ctrl, false, this);
		this.panelArrete = new PanelArrete(this.ctrl);
		this.modeBase    = false;

		this.panelArrete.setOpaque(false);

		JPanel centerPanel = new JPanel(null) { public boolean isOptimizedDrawingEnabled() { return false; } }; // Surcharge d'une méthode

		centerPanel.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{ 
				int w = centerPanel.getWidth();
				int h = centerPanel.getHeight();
				panelGrille.setBounds(0, 0, w, h);
				panelArrete.setBounds(0, 0, w, h);
				panelGrille.revalidate();
				panelArrete.repaint();
			}
		});

		//-------------------------------//
		// Positionnement des composants //
		//-------------------------------//

		centerPanel.add(this.panelArrete); 
		centerPanel.add(this.panelGrille);

		this.add(centerPanel,      BorderLayout.CENTER);
		this.add(this.panelOutils, BorderLayout.EAST  );

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}

	public void initBtn(String valeur, int lig, int col)
	{
		this.panelGrille.initBtn(valeur, lig, col);
		this.panelArrete.repaint();
	}

	public Case getCase(int lig, int col)
	{
		return this.ctrl.getCase(lig, col);
	}

	public boolean getmodeBase()
	{
		return this.modeBase;
	}

	public PanelGrille getPanelGrille()
	{
		return this.panelGrille;
	}

	public Controleur getCtrl()
	{
		return this.ctrl;
	}

	public JPanel getVitre()
	{
		return (JPanel)this.getGlassPane();
	}

	public void modeBase(boolean valeur)
	{
		this.modeBase = valeur;
	}

	public int getNbVirus()
	{
		return this.cptVirus;
	}

	public void updateCptVirus(int cptVirus)
	{
		this.cptVirus = cptVirus;
		this.panelOutils.updateTexteBouton(this.cptVirus);
	}

}