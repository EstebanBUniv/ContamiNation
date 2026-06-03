package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Scrollbar;
import java.awt.Image;

import java.awt.event.*;

import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelMenu extends JPanel implements ActionListener, AdjustmentListener
{
	private final int VOL_MIN = 0;
	private final int VOL_MAX = 100;

	private int volume = 70;

	private FrameMenu  frame;
	private Controleur ctrl;

	private Graphics2D g2;

	private JPanel     panelBouton;
	private JPanel     panelVolume;

	private JButton    btnSolo;
	private JButton    btnMulti;

	private JScrollBar sbVolume;

	private Image      imgFond;

	public PanelMenu(Controleur ctrl, FrameMenu frame)
	{
		this.setLayout(new BorderLayout());
		this.imgFond = getToolkit().getImage("./images/BackGround/fond.png");
		
		this.frame = frame;
		this.ctrl  = ctrl;

		//-------------------------//
		// création des composants //
		//-------------------------//
		this.panelBouton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 100));
		this.panelVolume = new JPanel(new BorderLayout());

		this.panelBouton.setOpaque(false);
		this.panelVolume.setOpaque(false);
		this.setOpaque(false);

		this.btnSolo  = new JButton("Solo"       );
		this.btnMulti = new JButton("Multijoueur");

		this.btnSolo .setPreferredSize(new Dimension(100, 30));
		this.btnMulti.setPreferredSize(new Dimension(100, 30));

		this.sbVolume = new JScrollBar(Scrollbar.HORIZONTAL, this.volume, 1, this.VOL_MIN, this.VOL_MAX+1);

		//-------------------------------//
		// positionnement des composants //
		//-------------------------------//
		this.panelBouton.add(this.btnSolo);
		this.panelBouton.add(this.btnMulti);
		
		this.add(this.panelBouton, BorderLayout.CENTER);
		
		this.panelVolume.add(this.sbVolume, BorderLayout.EAST);

		this.add(this.panelVolume, BorderLayout.SOUTH);

		//---------------------------//
		// activation des composants //
		//---------------------------//
		this.btnSolo .addActionListener(this);
		this.btnMulti.addActionListener(this);

		this.sbVolume.addAdjustmentListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnSolo )
		{
			this.frame.dispose();
			new FramePlateau(this.ctrl);
		}

		if ( e.getSource() == this.btnMulti )
		{
			System.out.println("coming soon!");
		}
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		return;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g2 = (Graphics2D) g;
		
		// Ajout de l'image du fond
		if ( imgFond != null )
		{
			g2.drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
		}

		
	}
}