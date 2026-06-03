package ContamiNation.IHM;

import ContamiNation.Controleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.*;

import javax.swing.*;

public class PanelGrille extends JPanel implements ActionListener, MouseListener
{
	Color[] tabCouleurs = {
		new Color(255, 255, 255),
		new Color(0, 255, 0),
		new Color(0, 0, 255),
		new Color(255, 255, 0),
		new Color(255, 165, 0),
		new Color(255, 192, 203),
		new Color(128, 0, 128),
		new Color(0, 255, 255),
		new Color(165, 42, 42),
		new Color(128, 128, 128),
		new Color(0, 128, 0),
		new Color(0, 0, 128),
		new Color(255, 215, 0),
		new Color(64, 224, 208),
		new Color(220, 20, 60)
	};

	private JButton[][] tabBtn;
	private Controleur  ctrl;
	private boolean     modeZone;
	private JPanel      panelGrille;
	private JPanel      panelBoutton;

	private JButton valider;
	private JButton annuler;
	
	public PanelGrille(int ligne, int colonne, Controleur ctrl, boolean modeZone)
	{
		this.setLayout(new BorderLayout());

		this.ctrl     = ctrl;
		this.modeZone = modeZone;

		this.panelGrille = new JPanel(new GridLayout(ligne, colonne));
		this.tabBtn      = new JButton[ligne][colonne];

		for (int lig = 0; lig < this.tabBtn.length; lig++)
		{
			for (int col = 0; col < this.tabBtn[lig].length; col++)
			{
				JButton button = new JButton();
				button.setBackground(Color.WHITE);
				button.setOpaque(true);
				button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				button.addMouseListener(this);
				button.addActionListener(this);

				this.tabBtn[lig][col] = button;
				this.panelGrille.add(button);
			}
		}

		this.add(this.panelGrille, BorderLayout.CENTER);

		if (this.modeZone)
		{
			this.annuler = new JButton("Annuler");
			this.valider = new JButton("Valider");

			this.panelBoutton = new JPanel();
			this.panelBoutton.add(this.valider);
			this.panelBoutton.add(this.annuler);

			this.add(this.panelBoutton, BorderLayout.SOUTH);

			this.valider.addActionListener(this);
			this.annuler.addActionListener(this);
		}
	}

	public JButton getButton(int lig, int col)
	{
		return this.tabBtn[lig][col];
	}

	public int getNbLig()
	{
		return this.tabBtn.length;
	}

	public int getNbCol()
	{
		return this.tabBtn[0].length;
	}

	public JButton getButtonAtPoint(Point p)
	{
		Component c = SwingUtilities.getDeepestComponentAt(this.panelGrille, p.x, p.y); // Prend le composant des coordonnées précises
		if (c instanceof JButton) // Vérifie si c'est un bouton
			return (JButton)c; // Le transforme en bouton si c'est le cas
		return null;
	}

	public void initBtn(String valeur, int lig, int col)
	{
		this.tabBtn[lig][col].setText(valeur);

		int zone = this.ctrl.getCase(lig, col).getZone();
		if (zone >= 0 && zone < this.tabCouleurs.length)
			this.tabBtn[lig][col].setBackground(this.tabCouleurs[zone]);

		if (this.ctrl.getCase(lig, col).getSommet() != null)
		{
			String symbole = this.ctrl.getCase(lig, col).getSommet().getSymbole();
			String chemin = "./images/symboles/symbole_" + symbole + ".png";
			ImageIcon iconOriginal = new ImageIcon(chemin);

			if (iconOriginal.getIconWidth() > 0) // Vérifie si l'image existe (plus grand que 0 pixel)
			{
				Image img = iconOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Réduit la taille de l'image 
				this.tabBtn[lig][col].setIcon(new ImageIcon(img)); // Pose l'image sur le bouton
			}
		}
		else
		{
			this.tabBtn[lig][col].setIcon(null); // Supprime l'image sinon
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (this.modeZone)
		{
			for (int lig = 0; lig < this.tabBtn.length; lig++)
			{
				for (int col = 0; col < this.tabBtn[0].length; col++)
				{
					if (e.getSource() == this.tabBtn[lig][col])
					{
						Frame top = (Frame)SwingUtilities.getWindowAncestor(this); // Permet de récupérer sa frame actuelle
						if (top instanceof FrameGrille)
						{
							((FrameGrille)top).ajouterZone(lig, col);

							int indCouleur = this.ctrl.getCase(lig, col).getZone();
							this.tabBtn[lig][col].setBackground(this.tabCouleurs[indCouleur]);
						}
					}
				}
			}
		}
		
		if (this.modeZone && e.getSource() == this.valider)
		{
			for (int lig = 0; lig < this.tabBtn.length; lig++)
			{
				for (int col = 0; col < this.tabBtn[0].length; col++)
				{
					if (this.ctrl.getCase(lig, col).getZone() == 0)
						return;
				}
			}

			Frame top = (Frame)SwingUtilities.getWindowAncestor(this); // Permet de récupérer sa frame actuelle
			if (top instanceof FrameGrille)
				((FrameGrille)top).fermer();
		}

		if (this.modeZone && e.getSource() == this.annuler)
		{
			for (int lig = 0; lig < this.tabBtn.length; lig++)
			{
				for (int col = 0; col < this.tabBtn[0].length; col++)
				{
					this.ctrl.getCase(lig, col).supprimerZone();
					this.tabBtn[lig][col].setBackground(this.tabCouleurs[0]);
					this.initBtn(this.ctrl.getCase(lig, col).toString(), lig, col);
				}
			}
		}
	}

	public void mousePressed(MouseEvent e)
	{
		if (!this.modeZone)
		{
			if (e.getButton() == MouseEvent.BUTTON3)
			{
				for (int lig = 0; lig < this.tabBtn.length; lig++)
				{
					for (int col = 0; col < this.tabBtn[0].length; col++)
					{
						if (e.getSource() == this.tabBtn[lig][col])
						{
							this.ctrl.supprimerSommet(lig, col);
						}
					}
				}
			
			}	
		}
		else
		{
			if (e.getButton() == MouseEvent.BUTTON3)
			{
				for (int lig = 0; lig < this.tabBtn.length; lig++)
				{
					for (int col = 0; col < this.tabBtn[0].length; col++)
					{
						if (e.getSource() == this.tabBtn[lig][col])
						{
							this.ctrl.getCase(lig, col).supprimerZone();
							this.tabBtn[lig][col].setBackground(this.tabCouleurs[0]);
							this.initBtn(this.ctrl.getCase(lig, col).toString(), lig, col);
						}
					}
				}
			}
		}
	}

	public void mouseExited  (MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked (MouseEvent e) {}
}