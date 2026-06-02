package ContamiNation.IHM;

import javax.swing.*;

import javax.swing.JFrame;

public class FrameGrille extends JFrame
{
	private PanelGrille panel;

	public FrameGrille()
	{
		this.setTitle   ("ContamiNation");            
    	this.setSize    (500,500);             
    	this.setLocation( 20,200); 

		this.panel = new PanelGrille();

		this.add(panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	}

	public static void main(String [] args)    
    { 
        new FrameGrille();                 
    }
}
