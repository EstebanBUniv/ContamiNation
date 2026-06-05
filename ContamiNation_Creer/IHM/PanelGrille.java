package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.Metier.Sommet;
import ContamiNation_Creer.Metier.Virus;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import java.awt.event.*;

import javax.swing.*;

/* 
SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelGrille extends JPanel implements ActionListener, MouseListener
{
	private int cptVirus;
 
	private JButton[][]  tabBtn;
	private Controleur   ctrl;
	private boolean      modeZone;
	private JPanel       panelGrille;
	private JPanel       panelBoutton;

	private FrameSommet frameMere;
	private JButton     btnValider;
	private JButton     btnAnnuler;
	private JButton     btnRetour;

	private boolean     estNouveau = false;

	public PanelGrille(int ligne, int colonne, Controleur ctrl, boolean modeZone, FrameSommet frameMere)
	{
		this.setLayout(new BorderLayout());

		this.ctrl      = ctrl;
		this.modeZone  = modeZone;

		this.frameMere = frameMere;

		this.setOpaque(false);

		this.panelGrille = new JPanel(new GridLayout(ligne, colonne));
		this.tabBtn      = new JButton[ligne][colonne];
		
		this.cptVirus = 1;
		
		for (int ligVirus = 0; ligVirus < this.ctrl.getLig(); ligVirus++)
			for (int colVirus = 0; colVirus < this.ctrl.getCol(); colVirus++)
				if (this.frameMere != null && this.ctrl.getCase(ligVirus, colVirus).getSommet() != null && 
					this.ctrl.getCase(ligVirus, colVirus).getSommet().getEstBase() != 0)
						this.cptVirus++;
		
		if (this.frameMere != null)
			this.frameMere.updateCptVirus(this.ctrl.getNbVirus() - (this.cptVirus - 1));
		
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
			this.btnAnnuler = new JButton("Annuler");
			this.btnValider = new JButton("Valider");
			this.btnRetour  = new JButton("Retour" );

			this.panelBoutton = new JPanel();
			this.panelBoutton.add(this.btnValider);
			this.panelBoutton.add(this.btnAnnuler);
			this.panelBoutton.add(this.btnRetour );

			this.add(this.panelBoutton, BorderLayout.SOUTH);

			this.btnValider.addActionListener(this);
			this.btnAnnuler.addActionListener(this);
			this.btnRetour .addActionListener(this);
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

	public void setEstNouveau(boolean val)
	{
		this.estNouveau = val;
	}

	public void initBtn(String valeur, int lig, int col)
	{
		this.tabBtn[lig][col].setText("");
		this.tabBtn[lig][col].setIcon(null);

		int zone = this.ctrl.getCase(lig, col).getZone();
		
		this.tabBtn[lig][col].setBackground(this.ctrl.getCouleurZone(zone));

		
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
		JFrame top = (JFrame)SwingUtilities.getWindowAncestor(this);

		if (this.modeZone)
		{
			for (int lig = 0; lig < this.tabBtn.length; lig++)
			{
				for (int col = 0; col < this.tabBtn[0].length; col++)
				{
					if (e.getSource() == this.tabBtn[lig][col])
					{
						if (top instanceof FrameCreer)
						{
							((FrameCreer)top).ajouterZone(lig, col);

							int indCouleur = this.ctrl.getCase(lig, col).getZone();
							this.tabBtn[lig][col].setBackground(this.ctrl.getCouleurZone(indCouleur));
						}
					}
				}
			}
		}

		if (this.modeZone && e.getSource() == this.btnValider)
		{
			for (int lig = 0; lig < this.tabBtn.length; lig++)
			{
				for (int col = 0; col < this.tabBtn[0].length; col++)
				{
					if (this.ctrl.getCase(lig, col).getZone() == 0)
					{
						JOptionPane.showMessageDialog(this, "Il reste des cases sans zone !", "Attention", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			}

			if (top instanceof FrameCreer)
				((FrameCreer)top).fermer();
		}

		if (this.modeZone && e.getSource() == this.btnAnnuler)
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
		if (this.frameMere != null)
		{
			if (this.frameMere.getmodeBase())
			{
				for (int lig = 0; lig < this.ctrl.getLig(); lig++)
				{
					for (int col = 0; col < this.ctrl.getCol(); col++)
					{
						if (e.getSource() == this.tabBtn[lig][col])
						{
							Sommet s = ctrl.getCase(lig, col).getSommet();

							int idLibre = this.trouverIdLibre();
						
							if (s != null && idLibre != -1 && s.getEstBase() == 0)
							{
								s.setBase(idLibre); 
								
								this.frameMere.updateCptVirus(this.ctrl.getNbVirus() - this.compterBasesPlacees());
								this.frameMere.repaint();
							}
						}
					}
				}
			}
		}

		if ( e.getSource() == this.btnRetour )
		{
			if ( top instanceof FrameCreer )
			{
				((FrameCreer) top).retirerPanel();
				((FrameCreer) top).setMinimumSize(new Dimension(900, 600));
				((FrameCreer) top).setSize(900, 600);
				
				if ( this.estNouveau )
				{
					((FrameCreer)(top)).changerPanel(new PanelParametre((FrameCreer)(top)));
				}
				else
				{
					((FrameCreer)(top)).changerPanel(new PanelSauvegarde((FrameCreer)(top), this.ctrl));
				}
			}
					
			if ( top instanceof FrameSommet )
			{
				System.out.println("retour");
			}

		}
	}

	public void mousePressed(MouseEvent e)
	{
		if (!this.modeZone )
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
							
							if (this.frameMere != null)
								this.frameMere.updateCptVirus(this.ctrl.getNbVirus() - this.compterBasesPlacees());
							
							this.frameMere.repaint();
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
	
	private int trouverIdLibre()
	{
		for (int id = 1; id <= this.ctrl.getNbVirus(); id++)
		{
			boolean estUtilise = false;
			for (int lig = 0; lig < this.getNbLig(); lig++)
			{
				for (int col = 0; col < this.getNbCol(); col++)
				{
					Sommet s = this.ctrl.getCase(lig, col).getSommet();
					if (s != null && s.getEstBase() == id)
						estUtilise = true;
				}
			}
			if (!estUtilise) return id;
		}
		return -1;
	}

	private int compterBasesPlacees()
	{
		int nb = 0;
		for (int lig = 0; lig < this.getNbLig(); lig++)
			for (int col = 0; col < this.getNbCol(); col++)
			{
				Sommet s = this.ctrl.getCase(lig, col).getSommet();
				if (s != null && s.getEstBase() != 0)
					nb++;
			}
		return nb;
	}

	public void mouseExited  (MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked (MouseEvent e) {}
}