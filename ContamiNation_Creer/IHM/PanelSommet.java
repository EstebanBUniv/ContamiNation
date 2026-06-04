package ContamiNation_Creer.IHM;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelSommet extends JPanel implements MouseListener, MouseMotionListener
{
	private FrameSommet  frameMere;
	private JLabel[]    tabSymboles;
	private String[]    nomSymboles = {"Aeroport", "Entrepot", "Hopital", "Laboratoire", "Ville"};

	private JLabel      labelVolant;
	private String      symboleChoisi;
	private ImageIcon   iconChoisie;
	private int         clicX;
	private int         clicY;

	public PanelSommet(FrameSommet frameMere)
	{
		this.frameMere = frameMere;
		this.setLayout(new GridLayout(this.nomSymboles.length, 1, 10, 10));

		this.tabSymboles = new JLabel[this.nomSymboles.length];

		for (int i = 0; i < this.nomSymboles.length; i++)
		{
			String nomFoyer  = this.nomSymboles[i];
			String cheminImg = "../images/symboles/symbole_" + nomFoyer + ".png";

			ImageIcon iconOriginal = new ImageIcon(cheminImg);
			Image img50 = iconOriginal.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			ImageIcon icon50 = new ImageIcon(img50);

			this.tabSymboles[i] = new JLabel(nomFoyer, icon50, SwingConstants.CENTER);

			this.tabSymboles[i].addMouseListener(this);
			this.tabSymboles[i].addMouseMotionListener(this);

			this.add(this.tabSymboles[i]);
		}
	}

	public void mousePressed(MouseEvent e)
	{
		for (int i = 0; i < this.tabSymboles.length; i++)
		{
			if (e.getSource() == this.tabSymboles[i])
			{
				this.symboleChoisi = this.nomSymboles[i];
				JLabel labelClique = this.tabSymboles[i];
				this.iconChoisie   = (ImageIcon) labelClique.getIcon();

				this.clicX = e.getX();
				this.clicY = e.getY();

				JPanel vitre = this.frameMere.getVitre();
				vitre.removeAll();
				vitre.setLayout(null);
				vitre.setVisible(true);

				this.labelVolant = new JLabel(this.iconChoisie);
				this.labelVolant.setSize(50, 50);

				Point ptVitre = SwingUtilities.convertPoint(labelClique, e.getPoint(), vitre);
				this.labelVolant.setLocation(ptVitre.x - this.clicX, ptVitre.y - this.clicY);

				vitre.add(this.labelVolant);
				vitre.repaint();
			}
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		if (this.labelVolant != null)
		{
			JPanel vitre = this.frameMere.getVitre();
			Point ptVitre = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), vitre);
			this.labelVolant.setLocation(ptVitre.x - this.clicX, ptVitre.y - this.clicY);
			vitre.repaint();
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if (this.labelVolant != null)
		{
			JPanel vitre = this.frameMere.getVitre();
			PanelGrille panelCible = this.frameMere.getPanelGrille();

			Point ptPanel = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), panelCible);

			vitre.remove(this.labelVolant);
			vitre.setVisible(false);
			vitre.repaint();

			JButton cible = panelCible.getButtonAtPoint(ptPanel);

			if (cible != null)
			{
				boolean trouve = false;

				for (int lig = 0; lig < this.frameMere.getPanelGrille().getNbLig() && !trouve; lig++)
				{
					for (int col = 0; col < this.frameMere.getPanelGrille().getNbCol(); col++)
					{
						if (this.frameMere.getPanelGrille().getButton(lig, col) == cible && !trouve)
						{
							this.frameMere.getCtrl().ajouterSommet(lig, col, this.symboleChoisi);
							trouve = true;
						}
					}
				}
			}

			this.labelVolant   = null;
			this.iconChoisie   = null;
			this.symboleChoisi = null;
			this.frameMere.repaint();
		}
	}

	public void mouseMoved  (MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited (MouseEvent e) {}
}