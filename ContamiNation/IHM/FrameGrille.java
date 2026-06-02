package ContamiNation.IHM;

import javax.swing.*;
import java.awt.BorderLayout;
import ContamiNation.Controleur;
import javax.swing.JFrame;

public class FrameGrille extends JFrame
{
	private PanelGrille panel;
	private Controleur  ctrl;
	private JTextField  txtNumZone;

	public FrameGrille(Controleur ctrl, int hauteur, int largeur)
	{
		this.setTitle   ("ContamiNation");
		this.setSize    (500,500);
		this.setLocation( 20,200); 
		
		this.ctrl = ctrl;

		this.panel = new PanelGrille(hauteur, largeur, this);
		
		this.txtNumZone = new JTextField(10);
		
		this.add(new JLabel("Initialisez les zones : "), BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		this.add(this.txtNumZone, BorderLayout.WEST);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void initBtn (String valeur, int hauteur, int largeur)
	{
		this.panel.initBtn(valeur, hauteur, largeur);
	}
	
	public void ajouterZone (int hauteur, int largeur)
	{
		if ( this.txtNumZone.getText().matches( "[0-9]+" ))
			this.ctrl.ajouterZone(hauteur, largeur, Integer.parseInt(this.txtNumZone.getText()));
	}
}
