package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Metier.Sommet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelSommet extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{
	private FrameSommet frameMere;
	private JLabel[]    tabSymboles;
	private JButton     btnBase;
	private String[]    nomSymboles = {"Aeroport", "Entrepot", "Hopital", "Laboratoire", "Ville"};

	private JLabel      labelVolant;
	private String      symboleChoisi;
	private ImageIcon   iconChoisie;
	private int         clicX;
	private int         clicY;
	
	public PanelSommet(FrameSommet frameMere)
	{
		this.frameMere = frameMere;
		this.setLayout(new GridLayout(this.nomSymboles.length + 1, 1, 10, 10));
		
		this.tabSymboles = new JLabel[this.nomSymboles.length];
		this.btnBase     = new JButton("Placer les bases " + this.frameMere.getNbVirus());
		this.btnBase.setBackground(Color.RED);
		
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

	
		this.btnBase.addActionListener(this);

		this.add(this.btnBase);

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

			// 1. Convertir les coordonnées
			Point ptPanel = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), panelCible);

			// 2. Trouver le bouton sous la souris
			Component c = SwingUtilities.getDeepestComponentAt(panelCible, ptPanel.x, ptPanel.y);

			vitre.remove(this.labelVolant);
			vitre.setVisible(false);
			vitre.repaint();

			if (c instanceof JButton)
			{
				JButton cible = (JButton) c;
				// 3. Récupérer les coordonnées directement via la propriété
				Point p = (Point) cible.getClientProperty("coords");

				if (p != null)
				{
					int lig = p.x;
					int col = p.y;
					
					int baseExistante = this.frameMere.getCtrl().getEstBaseSommet(lig, col);
					this.frameMere.getCtrl().ajouterSommet(lig, col, this.symboleChoisi);

					if (baseExistante != 0)
					{
						this.frameMere.getCtrl().setBaseSommet(lig, col, baseExistante);
					}
				}
			}

			this.labelVolant   = null;
			this.iconChoisie   = null;
			this.symboleChoisi = null;
			this.frameMere.repaint();
		}
	}


	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnBase && this.frameMere != null)
		{
			this.frameMere.modeBase(! (this.frameMere.getmodeBase()));
			this.btnBase.setText("Placer les bases " + this.frameMere.getNbVirus());
			if (this.frameMere.getmodeBase())
				this.btnBase.setBackground(Color.GREEN);
			else
				this.btnBase.setBackground(Color.RED);
		}
	}
	
	public void updateTexteBouton(int nbRestant) 
	{
		this.btnBase.setText("Placer les bases " + nbRestant);
	}

	public void mouseMoved  (MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited (MouseEvent e) {}
}