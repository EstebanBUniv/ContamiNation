package ContamiNation.IHM;

import ContamiNation.Metier.Case;
import ContamiNation.Controleur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FrameSommet extends JFrame implements ActionListener
{
	private PanelGrille panelGrille;
	private PanelSommet panelOutils;
	private Controleur  ctrl;
	private PanelArrete panelArrete;
	private JButton     btnSave;

	public FrameSommet(Controleur ctrl, int lig, int col)
	{
		this.ctrl = ctrl;

		this.panelGrille = new PanelGrille(lig, col, ctrl, false);
		this.panelOutils = new PanelSommet(this);
		this.panelArrete = new PanelArrete(this.ctrl);
		this.btnSave     = new JButton("Enregistrer");

		this.panelArrete.setOpaque(false);

		JPanel centerPanel = new JPanel(null) { public boolean isOptimizedDrawingEnabled() { return false; } }; // Surcharge d'une méthode
		centerPanel.add(this.panelArrete); 
		centerPanel.add(this.panelGrille);

		centerPanel.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				int w = centerPanel.getWidth();
				int h = centerPanel.getHeight();
				panelGrille.setBounds(0, 0, w, h);
				panelArrete.setBounds(0, 0, w, h);
				panelGrille.revalidate();
				panelArrete.repaint();
			}
		});

		this.setTitle("ContamiNation - Sommets");
		this.setSize(900, 700);
		this.setLayout(new BorderLayout());
		this.add(centerPanel,       BorderLayout.CENTER);
		this.add(this.panelOutils,  BorderLayout.EAST);
		this.add(this.btnSave,      BorderLayout.SOUTH);

		this.btnSave.addActionListener(this);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnSave)
		{
			this.ctrl.enregistrer();
			this.dispose();
			ctrl.ouvrirJeu();
		}
	}

	public void initBtn(String valeur, int lig, int col)
	{
		this.panelGrille.initBtn(valeur, lig, col);
		this.panelArrete.repaint();
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

	public PanelGrille getPanel()
	{
		return this.panelGrille;
	}
}