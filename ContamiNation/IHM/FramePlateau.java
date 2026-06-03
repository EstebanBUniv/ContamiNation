package ContamiNation.IHM;
import  ContamiNation.Controleur;
import javax.swing.*;

public class FramePlateau extends JFrame
{
	private PanelPlateau panel;
	private Controleur   ctrl;

	public FramePlateau(Controleur ctrl)
	{
		this.setTitle   ("ContamiNation");
		this.setSize    (500,500);
		this.setLocation( 20,200); 
		
		this.ctrl  = ctrl;
		this.panel = new PanelPlateau(this);
		
		this.add(panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void creerPlateau( int lig, int col, int nbCouleur)
	{
		this.dispose();
		this.ctrl.creerPlateau(lig, col, nbCouleur);
	}
	
	public void charger()
	{
		this.ctrl.charger();
		this.dispose();
	}
}
