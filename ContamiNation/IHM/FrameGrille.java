package ContamiNation.IHM;

import javax.swing.*;
import ContamiNation.Controleur;
import javax.swing.JFrame;

public class FrameGrille extends JFrame
{
	private PanelGrille panel;
	private Controleur  ctrl;

	public FrameGrille(Controleur ctrl, int hauteur, int largeur)
	{
		this.setTitle   ("ContamiNation");
		this.setSize    (500,500);
		this.setLocation( 20,200); 
		
		this.ctrl = ctrl;

		this.panel = new PanelGrille(hauteur, largeur);

		this.add(panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void initBtn (String valeur, int hauteur, int largeur)
	{
		this.panel.initBtn(valeur, hauteur, largeur);
	}
}
