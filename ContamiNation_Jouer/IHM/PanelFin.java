package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;
import ContamiNation_Jouer.Metier.Plateau;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelFin extends JPanel implements ActionListener
{
	private FrameJeu   frameMere;
	private Controleur ctrl;
	private Image      imgFond;
	private JButton    btnMenu;

	public PanelFin(FrameJeu frameMere, Controleur ctrl, String text) 	
	{
		this.frameMere = frameMere;
		this.ctrl      = ctrl;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.imgFond = getToolkit().getImage("../images/fond/fond2.png");
		this.setOpaque(false);

		JPanel panelContenu = new JPanel() 
		{
			protected void paintComponent(Graphics g) 
			{
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(new Color(25, 30, 40, 220)); 
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				g2d.dispose();
			}
		};
		
		panelContenu.setLayout(new BoxLayout(panelContenu, BoxLayout.Y_AXIS));
		panelContenu.setOpaque(false);
		panelContenu.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
		panelContenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panelContenu.setMaximumSize(new Dimension(550, 400));

		JLabel labelFin = new JLabel(text);
		labelFin.setFont(Controleur.POLICE_TITRE);
		labelFin.setForeground(Controleur.COLOR_FOREGROUND);
		labelFin.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelContenu.add(labelFin);
		
		panelContenu.add(Box.createVerticalStrut(25));

		JLabel lblTableau = new JLabel("--- CLASSEMENT FINAL ---");
		lblTableau.setFont(Controleur.POLICE_TEXTE);
		lblTableau.setForeground(Controleur.COLOR_FOREGROUND);
		lblTableau.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelContenu.add(lblTableau);
		
		panelContenu.add(Box.createVerticalStrut(15));

		for (int i = 0; i < this.ctrl.getNbJoueur(); i++)
		{
			Plateau p = this.ctrl.getPlateau(i);
			String nomVirus = (p.getVirusActif() != null) ? p.getVirusActif().getNom() : "Inconnu";
			String detailScore = "Joueur " + (i + 1) + p.getPointTotal() + " points";
			
			JLabel lblJoueurScore = new JLabel(detailScore);
			lblJoueurScore.setFont(Controleur.POLICE_TEXTE);
			lblJoueurScore.setForeground(Controleur.COLOR_FOREGROUND);
			lblJoueurScore.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			panelContenu.add(lblJoueurScore);
			panelContenu.add(Box.createVerticalStrut(10));
		}

		panelContenu.add(Box.createVerticalStrut(20));

		this.btnMenu = new JButton("Retour au Menu Principal");
		this.btnMenu.setBackground(Controleur.COLOR_BACKGROUND);
		this.btnMenu.setForeground(Controleur.COLOR_FOREGROUND);
		this.btnMenu.setFont(Controleur.POLICE_TEXTE);
		this.btnMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.btnMenu.addActionListener(this);
		panelContenu.add(this.btnMenu);

		this.add(Box.createVerticalGlue());
		this.add(panelContenu);
		this.add(Box.createVerticalGlue());
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnMenu)
		{
			this.ctrl.fermerReseau();
			this.frameMere.changerPanel(new PanelMenu(this.frameMere, this.ctrl));
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if (imgFond != null)
			g2.drawImage(imgFond, 0, 0, getWidth(), getHeight(), this);
	}
}