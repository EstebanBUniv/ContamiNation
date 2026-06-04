package ContamiNation_Creer.IHM;

import javax.swing.*;

import ContamiNation_Creer.Controleur;

public class FrameBase extends JFrame
{
	private PanelBase  panelBase;
	private Controleur ctrl;

	public FrameBase(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("ContamiNation - SommetsBase");
		this.setSize(900, 700);

		this.panelBase = new PanelBase(ctrl);

		this.add(this.panelBase);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
