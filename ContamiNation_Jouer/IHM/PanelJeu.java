package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;
import ContamiNation_Jouer.IHM.FrameJeu;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class PanelJeu extends JPanel implements ActionListener
{
	private FrameJeu  frame;
	private Controleur ctrl;

	private JButton    btnSolo;
	private JButton    btnMulti;

	private Image      imgFond;

	private Graphics2D g2;

	public PanelJeu(Controleur ctrl, FrameJeu frame)
	{
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 100));

		this.imgFond = getToolkit().getImage("../images/fond/fond.png");
		
		this.frame = frame;
		this.ctrl  = ctrl;

		this.setOpaque(false);

		//-------------------------//
		// création des composants //
		//-------------------------//

		this.btnSolo  = new JButton("Solo"       );
		this.btnMulti = new JButton("Multijoueur");

		this.btnSolo .setPreferredSize(new Dimension(100, 30));
		this.btnMulti.setPreferredSize(new Dimension(100, 30));

		//-------------------------------//
		// positionnement des composants //
		//-------------------------------//

		this.add(this.btnSolo );
		this.add(this.btnMulti);

		//---------------------------//
		// activation des composants //
		//---------------------------//

		this.btnSolo .addActionListener(this);
		this.btnMulti.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnSolo )
		{
			System.out.println("Jeu solo");
		}

		if ( e.getSource() == this.btnMulti )
		{
			System.out.println("coming soon!");
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		this.g2 = (Graphics2D) g;
		
		// Ajout de l'image du fond
		if ( imgFond != null )
		{
			this.g2.drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
		}

		
	}

}
