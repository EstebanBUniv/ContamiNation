package ContamiNation.IHM;

import javax.swing.*;

public class FramePlateau extends JFrame
{
	private PanelPlateau panel;

	public FramePlateau()
	{
		this.setTitle   ("ContamiNation");            
    	this.setSize    (500,500);             
    	this.setLocation( 20,200); 

		this.panel = new PanelPlateau();

		this.add(panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	}

	public static void main(String [] args)    
    { 
        new FramePlateau();                 
    }

}
