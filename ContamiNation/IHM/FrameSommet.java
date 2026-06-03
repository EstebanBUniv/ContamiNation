package ContamiNation.IHM;

import ContamiNation.Metier.Case;
import ContamiNation.Controleur;

import javax.swing.*;
import java.awt.*;

public class FrameSommet extends JFrame
{
	private PanelGrille panelGrille;
	private PanelSommet panelOutils;
	private Controleur  ctrl;

	public FrameSommet(Controleur ctrl, int lig, int col)
	{
		this.setTitle("ContamiNation - Sommets");
		this.setSize(900, 700);
		this.ctrl = ctrl;

		JPanel glass = new JPanel(null);
		glass.setOpaque(false);
		this.setGlassPane(glass);

		this.panelGrille = new PanelGrille(lig, col, ctrl, false);
		this.panelOutils = new PanelSommet(this);

		this.setLayout(new BorderLayout());
		this.add(this.panelGrille, BorderLayout.CENTER);
		this.add(this.panelOutils, BorderLayout.EAST);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void initBtn(String valeur, int lig, int col)
	{
		this.panelGrille.initBtn(valeur, lig, col);
	}

	public Case getCase(int lig, int col)
	{
		return this.ctrl.getCase(lig, col);
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
}