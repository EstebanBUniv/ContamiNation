package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;
import ContamiNation_Creer.Metier.Sommet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
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
	// Attribut d'instance
	private int         cptVirus;
 
	private JButton[][] tabBtn;
	private Controleur  ctrl;
	private boolean     modeZone;
	private JPanel      panelGrille;
	private JPanel      panelBoutton;

	private FrameSommet frameMere;
	private JButton     btnValider;
	private JButton     btnAnnuler;
	private JButton     btnRetour;

	private JButton     btnPlus;
	private JButton     btnMoins;
	private JLabel      numZoneActuelle;
	private int         cptZone;

	private JPanel      panelZone;
	private JPanel      panelBas;

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

		// Boutons communs aux deux modes
		this.btnRetour  = new JButton("Retour" );
		this.btnAnnuler = new JButton("Annuler");

		if (this.modeZone)
			this.btnValider = new JButton("Valider"     );
		else
   			this.btnValider = new JButton("Enregistrer" );

		this.panelBoutton = new JPanel();
		this.panelBoutton.add(this.btnRetour );
		this.panelBoutton.add(this.btnAnnuler);
		this.panelBoutton.add(this.btnValider);

		if (this.modeZone)
		{
			this.cptZone         = 1;
			this.numZoneActuelle = new JLabel("1", SwingConstants.CENTER);
			this.btnPlus         = new JButton("+");
			this.btnMoins        = new JButton("-");

			this.btnPlus .setBackground(Controleur.COLOR_BACKGROUND);
			this.btnPlus .setForeground(Controleur.COLOR_FOREGROUND);
			this.btnMoins.setBackground(Controleur.COLOR_BACKGROUND);
			this.btnMoins.setForeground(Controleur.COLOR_FOREGROUND);

			this.btnPlus.addActionListener(this);
			this.btnMoins.addActionListener(this);

			this.panelZone = new JPanel(new BorderLayout());
			this.panelZone.add(this.btnMoins       , BorderLayout.WEST  );
			this.panelZone.add(this.numZoneActuelle, BorderLayout.CENTER );
			this.panelZone.add(this.btnPlus        , BorderLayout.EAST  );

			this.panelBas = new JPanel(new GridLayout(2, 1));
			this.panelBas.add(this.panelZone   );
			this.panelBas.add(this.panelBoutton);

			this.add(this.panelBas, BorderLayout.SOUTH);
		}
		else
		{
			this.add(this.panelBoutton, BorderLayout.SOUTH);
		}

		// Listeners et couleurs communs
		JButton[] tabBtn = {this.btnRetour, this.btnAnnuler, this.btnValider};
		for (JButton btn : tabBtn )
		{
			btn.addActionListener(this);
			btn.setBackground(Controleur.COLOR_BACKGROUND);
			btn.setForeground(Controleur.COLOR_FOREGROUND);
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
							this.ajouterZone(lig, col);

							int indCouleur = this.ctrl.getCase(lig, col).getZone();
							this.tabBtn[lig][col].setBackground(this.ctrl.getCouleurZone(indCouleur));
						}
					}
				}
			}
		}

		if ( this.modeZone )
		{
			if (e.getSource() == this.btnPlus && this.cptZone < Integer.MAX_VALUE)
			{
				this.cptZone++;
				this.numZoneActuelle.setText(this.cptZone + "");
				this.panelZone.repaint();
			}

			if (e.getSource() == this.btnMoins && this.cptZone > 1)
			{
				this.cptZone--;
				this.numZoneActuelle.setText(this.cptZone + "");
				this.panelZone.repaint();
			}
		}

		if (e.getSource() == this.btnValider)
		{
			if (this.modeZone)
			{
				for (int lig = 0; lig < this.getNbLig(); lig++)
					for (int col = 0; col < this.getNbCol(); col++)
						if (this.ctrl.getCase(lig, col).getZone() == 0)
						{
							JOptionPane.showMessageDialog(this, "Il reste des cases sans zone !", "Attention", JOptionPane.WARNING_MESSAGE);
							return;
						}

				if (top instanceof FrameCreer)
					((FrameCreer) top).fermer();
			}
			 else
			{
				if (top instanceof FrameSommet)
				{
					FrameSommet fs = (FrameSommet) top;
					if (fs.getNbVirus() <= 0)
					{
						fs.getCtrl().enregistrer();
						fs.dispose();
					}
					else
						JOptionPane.showMessageDialog(this,
							"Vous devez placer toutes les bases de virus avant de sauvegarder !",
							"Attention", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		if (e.getSource() == this.btnAnnuler)
		{
			if (this.modeZone)
				this.supprimerToutesZones();
			else
			{
				this.supprimerTousSommets();
				if (this.frameMere != null)
					this.frameMere.updateCptVirus(this.ctrl.getNbVirus());
				this.repaint();
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

		if (e.getSource() == this.btnRetour)
		{
			if (top instanceof FrameCreer)
			{
				((FrameCreer) top).setMinimumSize(new Dimension(900, 600));
				((FrameCreer) top).setSize(900, 600);
				
				if (this.estNouveau)
					((FrameCreer) top).changerPanel(new PanelParametre((FrameCreer) top));
				else
					((FrameCreer) top).changerPanel(new PanelSauvegarde((FrameCreer) top, this.ctrl));
			}

			if (top instanceof FrameSommet)
			{
				if (!this.modeZone)
					this.supprimerTousSommets();

				((FrameSommet) top).getCtrl().OuvrirCreer();
				((FrameSommet) top).dispose();
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

	private void supprimerTousSommets()
	{
		for (int lig = 0; lig < this.getNbLig(); lig++)
			for (int col = 0; col < this.getNbCol(); col++)
				this.ctrl.supprimerSommet(lig, col);
	}

	private void supprimerToutesZones()
	{
		for (int lig = 0; lig < this.getNbLig(); lig++)
			for (int col = 0; col < this.getNbCol(); col++)
			{
				this.ctrl.getCase(lig, col).supprimerZone();
				this.tabBtn[lig][col].setBackground(Color.WHITE);
				this.initBtn(this.ctrl.getCase(lig, col).toString(), lig, col);
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

	public void ajouterZone (int lig, int col)
	{
		this.ctrl.ajouterZone(lig, col, this.cptZone);
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