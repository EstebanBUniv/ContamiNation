package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;

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

public class PanelGrille extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
	private int cptVirus;
 
	private JButton[][]  tabBtn;
	private Controleur   ctrl;
	private boolean      modeZone;
	private JPanel       panelGrille;
	private JPanel       panelBoutton, panelMode;

	private FrameSommet  frameMere;
	private JButton      btnValider;
	private JButton      btnAnnuler;
	private JButton      btnRetour;

	private JButton      btnPlus;
	private JButton      btnMoins;
	private JLabel       numZoneActuelle;
	private int          cptZone;

	private JPanel       panelZone, panelBas;
	
	private ButtonGroup  btgMode;
	private JRadioButton rbDrag;
	private JRadioButton rbClick;
	private JRadioButton rbSupp;
	private JRadioButton rbFull;

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
				if (this.frameMere != null && this.ctrl.aSommet(ligVirus, colVirus) && 
					this.ctrl.getEstBaseSommet(ligVirus, colVirus) != 0)
						this.cptVirus++;
		
		if (this.frameMere != null)
			this.frameMere.updateCptVirus(this.ctrl.getNbVirus() - (this.cptVirus - 1));
		
		for (int lig = 0; lig < this.tabBtn.length; lig++)
			for (int col = 0; col < this.tabBtn[lig].length; col++)
			{
				JButton button = new JButton();
				button.setRolloverEnabled(false);
				button.setBackground(Color.WHITE);
				button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				button.addMouseListener(this);
				button.addActionListener(this);
				button.addMouseMotionListener(this);

				this.tabBtn[lig][col] = button;
				this.panelGrille.add(button);
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
			
			this.btgMode   = new ButtonGroup();
			this.rbDrag    = new JRadioButton("Drag");
			this.rbClick   = new JRadioButton("Click");
			this.rbFull    = new JRadioButton("Full");
			this.rbSupp    = new JRadioButton("Supprimer");
			this.panelMode = new JPanel();

			this.btnPlus .setBackground(Controleur.COLOR_BACKGROUND);
			this.btnPlus .setForeground(Controleur.COLOR_FOREGROUND);
			this.btnMoins.setBackground(Controleur.COLOR_BACKGROUND);
			this.btnMoins.setForeground(Controleur.COLOR_FOREGROUND);
			
			this.btgMode.add(this.rbDrag);
			this.btgMode.add(this.rbClick);
			this.btgMode.add(this.rbFull);
			this.btgMode.add(this.rbSupp);
			
			this.rbClick.setSelected(true);
			
			this.btnPlus.addActionListener(this);
			this.btnMoins.addActionListener(this);
			
			this.panelMode.add(this.rbDrag);
			this.panelMode.add(this.rbClick);
			this.panelMode.add(this.rbFull);
			this.panelMode.add(this.rbSupp);

			this.panelZone = new JPanel(new BorderLayout());
			this.panelZone.add(this.btnMoins       , BorderLayout.WEST   );
			this.panelZone.add(this.numZoneActuelle, BorderLayout.CENTER );
			this.panelZone.add(this.btnPlus        , BorderLayout.EAST   );

			this.panelBas = new JPanel(new GridLayout(3, 1));
			this.panelBas.add(this.panelMode   );
			this.panelBas.add(this.panelZone   );
			this.panelBas.add(this.panelBoutton);

			this.add(this.panelBas, BorderLayout.SOUTH);
		}
		else
			this.add(this.panelBoutton, BorderLayout.SOUTH);

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
			for (int lig = 0; lig < this.tabBtn.length; lig++)
				for (int col = 0; col < this.tabBtn[0].length; col++)
				{
					if (e.getSource() == this.tabBtn[lig][col] && top instanceof FrameCreer)
						gererClicGrille(lig, col);
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
						if(this.ctrl.getZone(lig, col) == 0)
						{
							JOptionPane.showMessageDialog(this, "Il reste des cases sans zone !", "Attention", JOptionPane.WARNING_MESSAGE);
							return;
						}

				if (top instanceof FrameCreer)
					((FrameCreer) top).fermer();
			}
			 else
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

		if (this.frameMere != null && this.frameMere.getmodeBase())
			for (int lig = 0; lig < this.ctrl.getLig(); lig++)
				for (int col = 0; col < this.ctrl.getCol(); col++)
					if (e.getSource() == this.tabBtn[lig][col])
					{
						int idLibre = this.trouverIdLibre();
						if (this.ctrl.aSommet(lig, col) && idLibre != -1 && this.ctrl.getEstBaseSommet(lig, col) == 0)
						{
							this.ctrl.setSommet(lig, col, idLibre); 
							
							this.frameMere.updateCptVirus(this.ctrl.getNbVirus() - this.compterBasesPlacees());
							this.frameMere.repaint();
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
		if (!this.modeZone)
		{
			if (e.getButton() == MouseEvent.BUTTON3)
				for (int lig = 0; lig < this.tabBtn.length; lig++)
					for (int col = 0; col < this.tabBtn[0].length; col++)
						if (e.getSource() == this.tabBtn[lig][col] && this.ctrl.aSommet(lig, col))
						{
							if (this.ctrl.getEstBaseSommet(lig, col) != 0) 
								this.ctrl.retirerBaseSommet(lig, col);
							else 
								this.ctrl.supprimerSommet(lig, col);
							
							if (this.frameMere != null)
								this.frameMere.updateCptVirus(this.ctrl.getNbVirus() - this.compterBasesPlacees());
							
							this.frameMere.repaint();
						}
		}
		else
		{
			if (e.getButton() == MouseEvent.BUTTON3)
				for (int lig = 0; lig < this.tabBtn.length; lig++)
					for (int col = 0; col < this.tabBtn[0].length; col++)
						if (e.getSource() == this.tabBtn[lig][col])
						{
							this.ctrl.supprimerZone(lig, col);
							this.tabBtn[lig][col].setBackground(Color.WHITE);
							this.initBtn(this.ctrl.getCase(lig, col).toString(), lig, col);
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
				this.ctrl.reinitialiserZone(lig, col);
				this.tabBtn[lig][col].setBackground(Color.WHITE);
				this.initBtn(this.ctrl.getCase(lig, col).toString(), lig, col);
			}
	}

	public JButton getButton(int lig, int col) { return this.tabBtn[lig][col]; }

	public int getNbLig() { return this.tabBtn.length; }

	public int getNbCol() { return this.tabBtn[0].length; }

	public JButton getButtonAtPoint(Point p)
	{
		Component c = SwingUtilities.getDeepestComponentAt(this.panelGrille, p.x, p.y);
		if (c instanceof JButton)
			return (JButton)c;
		return null;
	}

	public void setEstNouveau(boolean val) { this.estNouveau = val; }

	public void initBtn(String valeur, int lig, int col)
	{
		this.tabBtn[lig][col].setText("");
		this.tabBtn[lig][col].setIcon(null);

		int zone = this.ctrl.getZone(lig, col);
		
		this.tabBtn[lig][col].setBackground(this.ctrl.getCouleurZone(zone));

		
		if (this.ctrl.aSommet(lig, col))
		{
			String symbole = this.ctrl.getSymboleSommet(lig, col);
			String chemin  = "../images/symboles/symbole_" + symbole + ".png";
			ImageIcon iconOriginal = new ImageIcon(chemin);

			if (iconOriginal.getIconWidth() > 0)
			{
				Image img = iconOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
				this.tabBtn[lig][col].setIcon(new ImageIcon(img));
			}
		}
		else
			this.tabBtn[lig][col].setIcon(null);
	}
	
	private int trouverIdLibre()
	{
		for (int id = 1; id <= this.ctrl.getNbVirus(); id++)
		{
			boolean estUtilise = false;
			for (int lig = 0; lig < this.getNbLig(); lig++)
				for (int col = 0; col < this.getNbCol(); col++)
					if (this.ctrl.aSommet(lig, col) && this.ctrl.getEstBaseSommet(lig, col) == id)
						estUtilise = true;
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
				if (this.ctrl.aSommet(lig, col) && this.ctrl.getEstBaseSommet(lig, col) != 0)
					nb++;
			}
		return nb;
	}

	public void mouseExited  (MouseEvent e) {}
	public void mouseEntered (MouseEvent e) {}
	public void mouseClicked (MouseEvent e) {}
	public void mouseMoved   (MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseDragged (MouseEvent e)
	{
		Component boutonDepart = (Component) e.getSource();
		Point posSouris = SwingUtilities.convertPoint(boutonDepart, e.getPoint(), this.panelGrille);
		Component composantSurvole = SwingUtilities.getDeepestComponentAt(this.panelGrille, posSouris.x, posSouris.y);
		Point p = getCoords(composantSurvole);
		
		if (p != null)
		{
			if (this.getOutilActif() == 1) // Mode Drag (Pinceau)
			{
				// RETOUR À LA NORMALE : On dessine direct sans se poser de questions !
				this.ctrl.ajouterZone(p.x, p.y, this.cptZone);
				this.initBtn("", p.x, p.y);
			}
			else if (this.getOutilActif() == 3) // Mode Gomme
			{
				this.ctrl.supprimerZone(p.x, p.y);
				this.tabBtn[p.x][p.y].setBackground(Color.WHITE);
				this.initBtn(this.ctrl.getCase(p.x, p.y).toString(), p.x, p.y);
			}
		}
	}

	private int getOutilActif() 
	{
		if (this.rbClick == null) return -1;
		if (rbClick.isSelected()) return 0; // Mode Clic normal
		if (rbDrag .isSelected()) return 1; // Mode Drag (Pinceau)
		if (rbFull .isSelected()) return 2; // Mode Full (Pot de peinture)
		if (rbSupp .isSelected()) return 3; // Mode Gomme
		return -1;
	}
	
	private Point getCoords(Object source) 
	{
		for (int lig = 0; lig < this.tabBtn.length; lig++)
			for (int col = 0; col < this.tabBtn[0].length; col++)
				if (source == this.tabBtn[lig][col])
					return new Point(lig, col);
		return null;
	}
	
	private void gererClicGrille(int lig, int col) 
	{
		if (this.getOutilActif() == 0) // Mode Clic Normal
		{
			this.ajusterZoneSiBesoin(lig, col); // <-- SÉCURITÉ ICI
			this.ctrl.ajouterZone(lig, col, this.cptZone);
			this.initBtn("", lig, col); 
		} 
		else if (this.getOutilActif() == 2) // Mode Pot de peinture (Full)
		{
			this.ajusterZoneSiBesoin(lig, col); // <-- SÉCURITÉ ICI
			boolean[][] visite = new boolean[this.getNbLig()][this.getNbCol()];
			this.ctrl.full(lig, col, this.ctrl.getCase(lig, col).getZone(), this.cptZone, visite);
			for (int ligFull = 0; ligFull < this.getNbLig(); ligFull++)
				for (int colFull = 0; colFull < this.getNbCol(); colFull++)
					this.initBtn("", ligFull, colFull); 
		}
		else if (this.getOutilActif() == 3) // Mode Supprimer
		{
			this.ctrl.supprimerZone(lig, col);
			this.tabBtn[lig][col].setBackground(Color.WHITE);
			this.initBtn(this.ctrl.getCase(lig, col).toString(), lig, col);
		}
	}
	
	private void actualiserCompteurZone(int nouvelleZone)
	{
		if (this.cptZone != nouvelleZone)
		{
			this.cptZone = nouvelleZone;
			if (this.numZoneActuelle != null)
			{
				this.numZoneActuelle.setText(String.valueOf(this.cptZone));
				if (this.panelZone != null) this.panelZone.repaint();
			}
		}
	}
	
	private void ajusterZoneSiBesoin(int lig, int col)
	{
		// Si la zone est déjà sur le plateau ET qu'on ne clique pas à côté d'elle
		if (this.ctrl.zoneExiste(this.cptZone) && !this.ctrl.estAdjacentZone(lig, col, this.cptZone))
		{
			this.cptZone++; // On force le passage à la zone suivante
			this.numZoneActuelle.setText(this.cptZone + "");
			if (this.panelZone != null) this.panelZone.repaint();
		}
	}
}