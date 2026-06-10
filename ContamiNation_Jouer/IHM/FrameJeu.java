package ContamiNation_Jouer.IHM;

import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ContamiNation_Jouer.IHM.PanelArrete;
import ContamiNation_Jouer.Controleur;

import javax.swing.JLayeredPane;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FrameJeu extends JFrame
{
	private Controleur   ctrl;
	private JPanel       panel;

	private PanelPlateau panelPlateau;
	private PanelArrete  panelArrete;
	private PanelPioche  panelPioche;
	
	public FrameJeu(Controleur ctrl)
	{
		this.ctrl = ctrl;
		this.setTitle("ContamiNation");
		this.setSize(900, 600);
		this.setMinimumSize(new Dimension(600, 300));
		this.setLocationRelativeTo(null);

		this.panelPioche = new PanelPioche(this.ctrl);
		this.panel       = new PanelMenu(this.ctrl, this);

		
		this.add(this.panel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public PanelPlateau getPanelPlateau()
	{
		return this.panelPlateau;
	}

	public void changerPanel(JPanel panel)
	{
		this.remove(this.panel);
		this.panel = panel;
		this.add(this.panel);

		this.revalidate();
		this.repaint();
	}

	public void afficherPlateau()
	{
		this.setLayout(new BorderLayout());
		this.panelPlateau  = new PanelPlateau(this, this.ctrl);
		this.panelArrete   = new PanelArrete(this.ctrl);
		JPanel centerPanel = new JPanel(null) { public boolean isOptimizedDrawingEnabled() { return false; } }; // Surcharge d'une méthode
		

		centerPanel.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)	// Listener de changement de taille de la frame
			{ 
				int w = centerPanel.getWidth();
				int h = centerPanel.getHeight();
				panelPlateau.setBounds(0, 0, w, h);
				panelArrete.setBounds(0, 0, w, h);
				panelPlateau.revalidate();
				panelArrete.repaint();
			}
		});

		centerPanel.add(this.panelArrete);
		centerPanel.add(this.panelPlateau);

		this.changerPanel(centerPanel);
		this.add(this.panelPioche, BorderLayout.WEST);
	}

	public JPanel[][] getTabPanel()
	{
		return this.panelPlateau.getTabPanel();
	}

}
