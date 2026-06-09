package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelMenu extends JPanel implements ActionListener
{
	private FrameJeu   frameMere;
	private Controleur ctrl;

	private JPanel     panelBouton;

	private JButton    btnSolo;
	private JButton    btnMulti;
	private JButton    btnQuitter;

	private Image      imgFond;
	private Graphics2D g2;
	
	public PanelMenu(Controleur ctrl, FrameJeu frame)
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.imgFond = getToolkit().getImage("../images/fond/fond2.png");
		
		this.frameMere = frame;
		this.ctrl      = ctrl;

		this.setOpaque(false);

		//-------------------------//
		// création des composants //
		//-------------------------//

		this.btnSolo    = new JButton("Solo");
		this.btnMulti   = new JButton("Multijoueur");
		this.btnQuitter = new JButton("Quitter");

		JButton[] tabBtn = {this.btnSolo, this.btnMulti, this.btnQuitter};
		for ( JButton btn : tabBtn )
		{
			btn.setBackground(Controleur.COLOR_BACKGROUND);
			btn.setForeground(Controleur.COLOR_FOREGROUND);
		}

		this.panelBouton = new JPanel(new GridLayout(3, 1, 0, 15));
		this.panelBouton.setBorder(BorderFactory.createEmptyBorder(0, (int)(this.frameMere.getWidth()*0.3),
		                                                           0, (int)(this.frameMere.getWidth()*0.3)
															      ));
		this.panelBouton.setOpaque(false);

		//-------------------------------//
		// positionnement des composants //
		//-------------------------------//

		for ( JButton btn : tabBtn )
			this.panelBouton.add(btn);

		this.add(this.creerTitre(), BorderLayout.NORTH);

		this.add(Box.createVerticalGlue());
		this.add(this.panelBouton);
		this.add(Box.createVerticalGlue());

		//---------------------------//
		// activation des composants //
		//---------------------------//

		for ( JButton btn : tabBtn )
			btn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnSolo )
			this.frameMere.changerPanel(new PanelNiveau(this.frameMere, this.ctrl));

		if ( e.getSource() == this.btnMulti )
			System.out.println("coming soon!");

		if ( e.getSource() == this.btnQuitter )
			this.frameMere.dispose();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		this.g2 = (Graphics2D) g;
		
		// Ajout de l'image du fond
		if ( imgFond != null )
			this.g2.drawImage ( imgFond, 0 , 0, getWidth(), getHeight(), this );
	}

	private JLabel creerTitre()
	{
		ImageIcon icon    = new ImageIcon("../images/Titre.png");
		int       largeur = (int)(this.frameMere.getWidth() * 0.60);
		int       hauteur = icon.getIconHeight() * largeur / icon.getIconWidth();
		Image     img     = icon.getImage().getScaledInstance(largeur, hauteur, Image.SCALE_SMOOTH);

		JLabel label = new JLabel(new ImageIcon(img));
   		label.setAlignmentX(CENTER_ALIGNMENT);

		return label;
	}
}
