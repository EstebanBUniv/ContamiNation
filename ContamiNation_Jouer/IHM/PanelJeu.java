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
import java.awt.Image;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelJeu extends JPanel implements ActionListener
{
	private final String IMGBOUTONS = "../images/boutons/imageBouton.png";

	private FrameJeu   frame;
	private Controleur ctrl;

	private JButton    btnSolo;
	private JButton    btnMulti;

	private JLabel     lblSolo;
	private JLabel     lblMulti;

	private Image      imgFond;
	private ImageIcon  iconOriginalBouton;

	private Graphics2D g2;

	public PanelJeu(Controleur ctrl, FrameJeu frame)
	{
		this.setLayout(new GridBagLayout());

		this.imgFond = getToolkit().getImage("../images/fond/fond.png");

		this.iconOriginalBouton = new ImageIcon(this.IMGBOUTONS);
		
		this.frame = frame;
		this.ctrl  = ctrl;

		this.setOpaque(false);

		//-------------------------//
		// création des composants //
		//-------------------------//

		int largeurBtn  = (int)(this.frame.getWidth()  * 0.30);
		int longueurBtn = (int)(this.frame.getHeight() * 0.10);

		Image     img          = iconOriginalBouton.getImage().getScaledInstance(largeurBtn, longueurBtn, Image.SCALE_SMOOTH);
		ImageIcon icon         = new ImageIcon(img);

		this.btnSolo  = new JButton(" ", icon);
		this.btnMulti = new JButton(" ", icon);

		this.btnSolo .setPreferredSize(new Dimension(largeurBtn, longueurBtn));
		this.btnMulti.setPreferredSize(new Dimension(largeurBtn, longueurBtn));

		this.btnSolo.setContentAreaFilled(false);
		this.btnSolo.setBorderPainted(false);
		this.btnSolo.setFocusPainted(false);

		this.btnMulti.setContentAreaFilled(false);
		this.btnMulti.setBorderPainted(false);
		this.btnMulti.setFocusPainted(false);
		
		this.btnSolo.setLayout(new BorderLayout());
		this.lblSolo = new JLabel("Solo", SwingConstants.CENTER);
        this.lblSolo.setFont(new Font("Arial", Font.BOLD, 18));
        this.lblSolo.setForeground(Color.RED);
        this.btnSolo.add(this.lblSolo, BorderLayout.CENTER);

		this.btnMulti.setLayout(new BorderLayout());
		this.lblMulti = new JLabel("Multijoueur", SwingConstants.CENTER);
        this.lblMulti.setFont(new Font("Arial", Font.BOLD, 18));
        this.lblMulti.setForeground(Color.RED);
        this.btnMulti.add(this.lblMulti, BorderLayout.CENTER);

		//-------------------------------//
		// positionnement des composants //
		//-------------------------------//

		JPanel conteneurBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        conteneurBoutons.setOpaque(false);

        conteneurBoutons.add(this.btnSolo);
        conteneurBoutons.add(this.btnMulti);

        this.add(conteneurBoutons);

		//---------------------------//
		// activation des composants //
		//---------------------------//

		this.btnSolo .addActionListener(this);
		this.btnMulti.addActionListener(this);

		this.initResizeListener();
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

	private void initResizeListener()
	{
		this.frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {

				int largeurBtn  = (int)(frame.getWidth()  * 0.30);
				int longueurBtn = (int)(frame.getHeight() * 0.10);

				btnSolo.setPreferredSize (new Dimension(largeurBtn, longueurBtn));
				btnMulti.setPreferredSize(new Dimension(largeurBtn, longueurBtn));

                Image img = iconOriginalBouton.getImage().getScaledInstance(largeurBtn, longueurBtn, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(img);
                
                btnSolo.setIcon (icon);
                btnMulti.setIcon(icon);

				int taillePolice = Math.max(12, (int)(longueurBtn * 0.35));
				lblSolo.setFont (new Font("Arial", Font.BOLD, taillePolice));
				lblMulti.setFont(new Font("Arial", Font.BOLD, taillePolice));

				revalidate();
				repaint();
			}
		});
	}

}
