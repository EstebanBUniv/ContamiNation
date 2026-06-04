package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Component;

import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics;

import java.awt.event.*;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelGrille extends JPanel implements ActionListener, MouseListener
{
	private int r = 0;
	private int g = 0;
	private int b = 0;
 
	private JButton[][] tabBtn;
	private Controleur  ctrl;
	private boolean     modeZone;
	private JPanel      panelGrille;
	private JPanel      panelBoutton;

	private JButton     valider;
	private JButton     annuler;

	private Map<Integer, Color> couleursZones;

	public PanelGrille(int ligne, int colonne, Controleur ctrl, boolean modeZone)
	{
		
		this.setLayout(new BorderLayout());

		this.ctrl      = ctrl;
		this.modeZone  = modeZone;

		this.couleursZones = this.ctrl.getCouleurZone();
		this.setOpaque(false);

		this.panelGrille = new JPanel(new GridLayout(ligne, colonne));
		this.tabBtn      = new JButton[ligne][colonne];


	

		for (int lig = 0; lig < this.tabBtn.length; lig++)
		{
			for (int col = 0; col < this.tabBtn[lig].length; col++)
			{
				JButton button = new JButton();
				button.setRolloverEnabled(false);
				button.setBackground(Color.WHITE);
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
		Component c = SwingUtilities.getDeepestComponentAt(this.panelGrille, p.x, p.y);
		if (c instanceof JButton)
			return (JButton)c;
		return null;
	}

	public void initBtn(String valeur, int lig, int col)
	{
		this.tabBtn[lig][col].setText("");
		this.tabBtn[lig][col].setIcon(null);

		int zone = this.ctrl.getCase(lig, col).getZone();
		this.tabBtn[lig][col].setBackground(getCouleurZone(zone));

		if (this.ctrl.getCase(lig, col).getSommet() != null)
		{
			String symbole = this.ctrl.getCase(lig, col).getSommet().getSymbole();
			String chemin  = "../images/symboles/symbole_" + symbole + ".png";
			ImageIcon iconOriginal = new ImageIcon(chemin);

			if (iconOriginal.getIconWidth() > 0)
			{
				Image img = iconOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
				this.tabBtn[lig][col].setIcon(new ImageIcon(img));
			}
		}
		else
		{
			this.tabBtn[lig][col].setIcon(null);
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
						JFrame top = (JFrame)SwingUtilities.getWindowAncestor(this);
						if (top instanceof FrameCreer)
						{
							((FrameCreer)top).ajouterZone(lig, col);

							int indCouleur = this.ctrl.getCase(lig, col).getZone();
							this.tabBtn[lig][col].setBackground(getCouleurZone(indCouleur));
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

			JFrame top = (JFrame)SwingUtilities.getWindowAncestor(this);
			if (top instanceof FrameCreer)
				((FrameCreer)top).fermer();
		}

		if (this.modeZone && e.getSource() == this.annuler)
		{
			for (int lig = 0; lig < this.tabBtn.length; lig++)
			{
				for (int col = 0; col < this.tabBtn[0].length; col++)
				{
					this.ctrl.getCase(lig, col).supprimerZone();
					this.tabBtn[lig][col].setBackground(Color.WHITE);
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
							this.ctrl.supprimerZone(lig, col);
							this.tabBtn[lig][col].setBackground(Color.WHITE);
							this.initBtn(this.ctrl.getCase(lig, col).toString(), lig, col);
						}
					}
				}
			}
		}
	}

	private Color nextColor()
	{
		
		r = (r + 67) % 256;
		g = (g + 113) % 256;
		b = (b + 193) % 256;
		
		Color c = new Color(r, g, b);
		
		return c;
	}

	private Color getCouleurZone(int numZone)
	{
		if (numZone == 0)
			return Color.WHITE;

		if (!couleursZones.containsKey(numZone))
			couleursZones.put(numZone, nextColor());

		return couleursZones.get(numZone);
	}

	public void mouseExited  (MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked (MouseEvent e) {}
}