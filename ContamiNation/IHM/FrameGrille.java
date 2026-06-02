package ContamiNation.IHM;

import ContamiNation.Metier.Case;

import javax.swing.*;
import java.awt.BorderLayout;
import ContamiNation.Controleur;
import javax.swing.JFrame;

public class FrameGrille extends JFrame
{
	private PanelGrille panel;
	private Controleur  ctrl;
	private JTextField  txtNumZone;

	public FrameGrille(Controleur ctrl, int lig, int col)
	{
		this.setTitle   ("ContamiNation");
		this.setSize    (500,500);
		this.setLocation( 20,200); 
		
		this.ctrl = ctrl;

		this.panel = new PanelGrille(lig, col, this.ctrl, true);
		
		this.txtNumZone = new JTextField(10);
		
		this.add(new JLabel("Initialisez les zones : "), BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		this.add(this.txtNumZone, BorderLayout.WEST);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void initBtn (String valeur, int lig, int col)
	{
		this.panel.initBtn(valeur, lig, col);
	}
	
	public void ajouterZone (int lig, int col)
	{
		if ( this.txtNumZone.getText().matches( "[0-9]+" ))
			this.ctrl.ajouterZone(lig, col, Integer.parseInt(this.txtNumZone.getText()));
	}

	public Case getCase(int lig, int col)
	{
		return ctrl.getCase(lig, col);
	}

	public void fermer()
	{
		this.dispose();
		this.ctrl.OuvrirSommet();
	}
	
	public PanelGrille getPanel() { return this.panel; }
}
