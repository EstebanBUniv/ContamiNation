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

public class PanelGrille extends JPanel implements ActionListener
{
	// Attribut d'instance
	private int         cptVirus;
 
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


	// Constructeur
	public PanelGrille(int ligne, int colonne, Controleur ctrl, boolean modeZone, FrameSommet frameMere)
	{
		this.setLayout(new BorderLayout());

		this.ctrl      = ctrl;
		this.modeZone  = modeZone;
		this.frameMere = frameMere;

		this.setOpaque(false);

		this.panelGrille = new JPanel(new GridLayout(ligne, colonne));
		this.tabBtn      = new JButton[ligne][colonne];
		GereSouris gereSouris = new GereSouris();
		
		this.cptVirus = 1;
		
		// Initialisation du compteur de virus
		for (int ligVirus = 0; ligVirus < this.ctrl.getLig(); ligVirus++)
			for (int colVirus = 0; colVirus < this.ctrl.getCol(); colVirus++)
				if (this.frameMere != null && this.ctrl.aSommet(ligVirus, colVirus) && 
					this.ctrl.getEstBaseSommet(ligVirus, colVirus) != 0)
						this.cptVirus++;
		
		if (this.frameMere != null)
			this.frameMere.updateCptVirus(this.ctrl.getNbVirus() - (this.cptVirus - 1));
		
		// Création de la grille de boutons
		for (int lig = 0; lig < this.tabBtn.length; lig++)
			for (int col = 0; col < this.tabBtn[lig].length; col++)
			{
				JButton button = new JButton();
				button.setRolloverEnabled(false);
				button.setBackground(Color.WHITE);
				button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				button.addMouseListener(gereSouris);
				button.addActionListener(this);
				button.addMouseMotionListener(gereSouris);
				button.putClientProperty("coords", new Point(lig, col));

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
		
		// Initialisation des menus et boutons dans le mode Zone
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
			
			this.btgMode.add(this.rbDrag );
			this.btgMode.add(this.rbClick);
			this.btgMode.add(this.rbFull );
			this.btgMode.add(this.rbSupp );
			
			this.rbClick.setSelected(true);
			
			this.panelZone = new JPanel(new BorderLayout());
			this.panelBas  = new JPanel(new GridLayout(3, 1));
			
			this.panelMode.add(this.rbDrag);
			this.panelMode.add(this.rbClick);
			this.panelMode.add(this.rbFull);
			this.panelMode.add(this.rbSupp);

			this.panelZone.add(this.btnMoins       , BorderLayout.WEST   );
			this.panelZone.add(this.numZoneActuelle, BorderLayout.CENTER );
			this.panelZone.add(this.btnPlus        , BorderLayout.EAST   );

			this.panelBas.add(this.panelMode   );
			this.panelBas.add(this.panelZone   );
			this.panelBas.add(this.panelBoutton);

			this.add(this.panelBas, BorderLayout.SOUTH);
			
			this.btnPlus .addActionListener(this);
			this.btnMoins.addActionListener(this);
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
	
	// Gère les clics sur les boutons
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		JFrame top = (JFrame) SwingUtilities.getWindowAncestor(this);

		if (source instanceof JButton) {
			Point p = (Point) ((JButton) source).getClientProperty("coords");
			
			if (p != null) {
				if (this.modeZone && top instanceof FrameCreer) 
					gererClicGrille(p.x, p.y);
				else if (this.frameMere != null && this.frameMere.getmodeBase()) 
				{
					int idLibre = this.trouverIdLibre();
					if (this.ctrl.aSommet(p.x, p.y) && idLibre != -1 && this.ctrl.getEstBaseSommet(p.x, p.y) == 0) 
					{
						this.ctrl.setSommet(p.x, p.y, idLibre);
						this.frameMere.updateCptVirus(this.ctrl.getNbVirus() - this.compterBasesPlacees());
						this.frameMere.repaint();
					}
				}
				return;
			}
		}

		if (this.modeZone) 
		{
			if (source == this.btnPlus && this.cptZone < Integer.MAX_VALUE) 
			{
				this.cptZone++;
				this.numZoneActuelle.setText(String.valueOf(this.cptZone));
				this.panelZone.repaint();
			} 
			else if (source == this.btnMoins && this.cptZone > 1) 
			{
				this.cptZone--;
				this.numZoneActuelle.setText(String.valueOf(this.cptZone));
				this.panelZone.repaint();
			}
		}

		if (source == this.btnValider) 
			traiterValidation(top);
		if (source == this.btnAnnuler)
			traiterAnnulation();
		if (source == this.btnRetour)
			traiterRetour(top);
	}

	// Valide le plateau ou sauvegarde les données
	private void traiterValidation(JFrame top) 
	{
		if (this.modeZone) 
		{
			for (int lig = 0; lig < getNbLig(); lig++)
				for (int col = 0; col < getNbCol(); col++)
					if (this.ctrl.getZone(lig, col) == 0) 
					{
						JOptionPane.showMessageDialog(this, "Il reste des cases sans zone !", "Attention", JOptionPane.WARNING_MESSAGE);
						return;
					}
			if (top instanceof FrameCreer) 
				((FrameCreer) top).fermer();
		} 
		if (top instanceof FrameSommet) 
		{
			FrameSommet fs = (FrameSommet) top;
			if (fs.getNbVirus() <= 0)
			{
				fs.getCtrl().enregistrer(); 
				fs.dispose();
			}
			else
				JOptionPane.showMessageDialog(this, "Placez toutes les bases de virus !", "Attention", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	// Réinitialise les sommets ou les zones
	private void traiterAnnulation() 
	{
		if (this.modeZone) 
			supprimerToutesZones();
		else
		{
			supprimerTousSommets();
			if (this.frameMere != null) 
				this.frameMere.updateCptVirus(this.ctrl.getNbVirus());
			this.repaint();
		}
	}
	
	// Gère le retour vers les écrans précédents
	private void traiterRetour(JFrame top) 
	{
		if (top instanceof FrameCreer)
		{
			((FrameCreer) top).setSize(900, 600);
			((FrameCreer) top).changerPanel(this.estNouveau ? new PanelParametre((FrameCreer) top) : new PanelSauvegarde((FrameCreer) top, this.ctrl));
		} else if (top instanceof FrameSommet) 
		{
			if (!this.modeZone) supprimerTousSommets();
			((FrameSommet) top).getCtrl().OuvrirCreer();
			((FrameSommet) top).dispose();
		}
	}
	
	// Supprime tous les sommets de la grille
	private void supprimerTousSommets()
	{
		for (int lig = 0; lig < this.getNbLig(); lig++)
			for (int col = 0; col < this.getNbCol(); col++)
				this.ctrl.supprimerSommet(lig, col);
	}
	
	// Réinitialise toutes les zones du plateau
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

	// Initialise l'apparence visuelle d'un bouton
	public void initBtn(String valeur, int lig, int col)
	{
		JButton btn = this.tabBtn[lig][col];
		btn.setText("");
		btn.setBackground(this.ctrl.getCouleurZone(this.ctrl.getZone(lig, col)));

		String symbole = this.ctrl.aSommet(lig, col) ? this.ctrl.getSymboleSommet(lig, col) : null;
		if (symbole != null) 
		{
			ImageIcon icon = new ImageIcon("../images/symboles/symbole_" + symbole + ".png");
			if (icon.getIconWidth() > 0) 
			{
				btn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
				return;
			}
		}
		btn.setIcon(null);
	}
	
	// Trouve un ID virus non utilisé
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
	
	// Compte les bases de virus placées
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
	
	// Logique de modification de zone (clic)
	private void gererClicGrille(int lig, int col) 
	{
		if (this.getOutilActif() == 0) // Mode Clic Normal
		{
			this.ajusterZoneSiBesoin(lig, col); 
			this.ctrl.ajouterZone(lig, col, this.cptZone);
			this.initBtn("", lig, col); 
		} 
		else if (this.getOutilActif() == 2) // Mode Pot de peinture (Full)
		{
			this.ajusterZoneSiBesoin(lig, col);
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
	
	// Ajuste dynamiquement le numéro de zone lors du tracé
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
	
	//Définit si le plateau est nouveau
	public void setEstNouveau(boolean val) { this.estNouveau = val; }

	/*----------------------------*/
	/*  Getters                   */
	/*----------------------------*/
	
	public JButton getButton(int lig, int col) { return this.tabBtn[lig][col]; }

	public int getNbLig() { return this.tabBtn.length; }

	public int getNbCol() { return this.tabBtn[0].length; }
	
	private int getOutilActif() 
	{
		if (this.rbClick == null) return -1;
		if (rbClick.isSelected()) return 0; // Mode Clic normal
		if (rbDrag .isSelected()) return 1; // Mode Drag (Pinceau)
		if (rbFull .isSelected()) return 2; // Mode Full (Pot de peinture)
		if (rbSupp .isSelected()) return 3; // Mode Gomme
		return -1;
	}
	
	//  GereSouris pour tout les implements
	private class GereSouris extends MouseAdapter
    {
        public void mousePressed(MouseEvent e) 
		{
			if (e.getButton() == MouseEvent.BUTTON3 && e.getSource() instanceof JButton) 
			{
				Point p = (Point) ((JButton) e.getSource()).getClientProperty("coords");
				if (p == null) return;
				
				if (!PanelGrille.this.modeZone) 
				{
					if (PanelGrille.this.ctrl.aSommet(p.x, p.y)) 
					{
						if (PanelGrille.this.ctrl.getEstBaseSommet(p.x, p.y) != 0) 
							PanelGrille.this.ctrl.retirerBaseSommet(p.x, p.y);
						else 
							PanelGrille.this.ctrl.supprimerSommet(p.x, p.y);
						
						if (PanelGrille.this.frameMere != null) 
							PanelGrille.this.frameMere.updateCptVirus(PanelGrille.this.ctrl.getNbVirus() - PanelGrille.this.compterBasesPlacees());
						PanelGrille.this.frameMere.repaint();
					}
				} 
				else 
				{
					PanelGrille.this.ctrl.supprimerZone(p.x, p.y);
					((JButton)e.getSource()).setBackground(Color.WHITE);
					PanelGrille.this.initBtn(PanelGrille.this.ctrl.getCase(p.x, p.y).toString(), p.x, p.y);
				}
			}
		}

       public void mouseDragged (MouseEvent e)
		{
			Component boutonDepart = (Component) e.getSource();
			Point posSouris = SwingUtilities.convertPoint(boutonDepart, e.getPoint(), PanelGrille.this.panelGrille);
			Component composantSurvole = SwingUtilities.getDeepestComponentAt(PanelGrille.this.panelGrille, posSouris.x, posSouris.y);
			
			if (composantSurvole instanceof JButton)
			{
				Point p = (Point) ((JButton)composantSurvole).getClientProperty("coords");
				
				if (p != null)
				{
					if (PanelGrille.this.getOutilActif() == 1) // Mode Drag (Pinceau)
					{
						PanelGrille.this.ctrl.ajouterZone(p.x, p.y, PanelGrille.this.cptZone);
						PanelGrille.this.initBtn("", p.x, p.y);
					}
					else if (PanelGrille.this.getOutilActif() == 3) // Mode Gomme
					{
						PanelGrille.this.ctrl.supprimerZone(p.x, p.y);
						PanelGrille.this.tabBtn[p.x][p.y].setBackground(Color.WHITE);
						PanelGrille.this.initBtn(PanelGrille.this.ctrl.getCase(p.x, p.y).toString(), p.x, p.y);
					}
				}
			}
		}
    }
}