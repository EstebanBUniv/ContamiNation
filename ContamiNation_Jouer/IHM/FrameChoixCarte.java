package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.event.*;

import javax.swing.*;

public class FrameChoixCarte extends JFrame
{
	private Controleur           ctrl;
	private ImageIcon[]          pioche;

	private JButton[]            cartes;

	private PanelChoixCarte      panelChoixCarte;



	public FrameChoixCarte(Controleur ctrl)
	{
		this.ctrl    = ctrl;

		this.setTitle("ContamiNation - Choix de la carte");
		this.setSize(800, 200);
		this.setLocation(100, 100);

		this.panelChoixCarte = new PanelChoixCarte(this.ctrl);


		this.add(this.panelChoixCarte);

		this.setVisible(true);


	}
}