package ContamiNation_Jouer.IHM;

import ContamiNation_Jouer.Controleur;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;

import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.util.ArrayList;
import java.util.LinkedList;

public class PanelArrete extends JPanel
{
	/*----------------------------*/
	/*  Attributs de la classe    */
	/*----------------------------*/

	private ArrayList<String> arretesColorees;
	private String            cleArrete;
	private Controleur   ctrl;
	private int          marge;
	private int          idJoueur;
	private PanelPlateau monPlateau;
	
	public PanelArrete(Controleur ctrl, int idJoueur, PanelPlateau monPlateau)
	{
		this.ctrl            = ctrl;
		this.idJoueur        = idJoueur;
		this.monPlateau      = monPlateau;
		this.arretesColorees = new ArrayList<>();
		this.setOpaque(false);

	}

	//----------------------------//
	// Méthodes d'implémentations //
	//----------------------------//
	public void componentResized(ComponentEvent e)
	{
		this.marge = (int)(this.monPlateau.getTailleCase() * 0.1);
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentShown (ComponentEvent e) {}
	public void componentMoved (ComponentEvent e) {}


	public void paintComponent(Graphics g) 
	{
		// Appel obligatoire à la classe parente pour nettoyer le composant avant de redessiner
		super.paintComponent(g);
		
		// Réinitialisation de la liste des arêtes colorées
		this.arretesColorees.clear();

		// Activation du dessin en 2D et configuration de l'épaisseur des lignes (3 pixels)
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(1.0f)); 

		
		// Calcul de la marge (10% de la taille d'une case) pour ne pas coller les lignes au centre
		this.marge = (int)(this.monPlateau.getTailleCase() * 0.1);

		
		
		// Parcours de toutes las cases du plateau (lignes et colonnes)
		for (int lig = 0; lig < ctrl.getLig(); lig++) 
		{
			for (int col = 0; col < ctrl.getCol(); col++) 
			{
				// Si la case contient un sommet appartenant au joueur
				if (ctrl.possedeSommet(lig, col, this.idJoueur)) 
				{
					g2d.setColor(Color.BLACK); // Les lignes de base seront noires

					// Parcours des 8 directions possibles autour de la case (haut, bas, diagonales...)
					for (int dir = 0; dir < 8; dir++) 
					{
						// Si le sommet a un voisin connecté dans cette direction
						if (ctrl.possedeVoisin(lig, col, dir, this.idJoueur)) 
						{
							// Récupération des coordonnées (ligne, colonne) du voisin
							int ligV = ctrl.getLigVoisin(lig, col, dir, this.idJoueur);
							int colV = ctrl.getColVoisin(lig, col, dir, this.idJoueur);

							// Récupération des cases graphiques (JPanel) correspondantes
							JPanel panel1 = this.monPlateau.getPanel(lig, col);
							JPanel panel2 = this.monPlateau.getPanel(ligV, colV);

							// Calcul et conversion des coordonnées du centre de chaque case
							Point p1 = SwingUtilities.convertPoint(panel1.getParent(), panel1.getX() + panel1.getWidth() / 2, panel1.getY() + panel1.getHeight() / 2, this);
							Point p2 = SwingUtilities.convertPoint(panel2.getParent(), panel2.getX() + panel2.getWidth() / 2, panel2.getY() + panel2.getHeight() / 2, this);

							// Si la distance entre les deux points est suffisante, on trace la ligne
							if (p1.distance(p2) > this.marge * 2) {
								// Calcul du vecteur de direction pour appliquer la marge
								double dirX = (p2.x - p1.x) / p1.distance(p2);
								double dirY = (p2.y - p1.y) / p1.distance(p2);
								
								// Dessin de la ligne noire raccourcie par la marge pour l'esthétique
								g2d.drawLine((int)(p1.x + dirX * this.marge), (int)(p1.y + dirY * this.marge), (int)(p2.x - dirX * this.marge), (int)(p2.y - dirY * this.marge));
							}
						}
					}
				}
			}
		}

		
		// Parcours de tous les virus du joueur
		for (int i = 0; i < ctrl.getNbVirus(this.idJoueur); i++) 
		{
			// Récupération de la taille du chemin emprunté par ce virus
			int tailleChemin = ctrl.getTailleCheminVirus(this.idJoueur, i);
			
			// On ne dessine un chemin que s'il relie au moins deux points
			if (tailleChemin > 1) 
			{
				// On applique la couleur spécifique à ce virus
				g2d.setColor(ctrl.getCouleurVirus(this.idJoueur, i));
				g2d.setStroke(new BasicStroke(3.0f));

				// Parcours de chaque segment du chemin
				for (int c = 0; c < tailleChemin - 1; c++) 

				{
					// Coordonnées du point actuel (A)
					int lig1 = ctrl.getLigChemin(this.idJoueur, i, c);
					int col1 = ctrl.getColChemin(this.idJoueur, i, c);
					
					// Coordonnées du point suivant (B)
					int lig2 = ctrl.getLigChemin(this.idJoueur, i, c + 1);
					int col2 = ctrl.getColChemin(this.idJoueur, i, c + 1);

					// Récupération des JPanels des deux points
					JPanel panel1 = this.monPlateau.getPanel(lig1, col1);
					JPanel panel2 = this.monPlateau.getPanel(lig2, col2);
						
					// Calcul du centre des deux points sur l'écran
					Point p1 = SwingUtilities.convertPoint(panel1.getParent(), panel1.getX() + panel1.getWidth() / 2, panel1.getY() + panel1.getHeight() / 2, this);
					Point p2 = SwingUtilities.convertPoint(panel2.getParent(), panel2.getX() + panel2.getWidth() / 2, panel2.getY() + panel2.getHeight() / 2, this);

					// Calcul mathématique pour réduire la ligne et laisser la marge
					double dirX = (p2.x - p1.x) / p1.distance(p2);
					double dirY = (p2.y - p1.y) / p1.distance(p2);
					
					// Dessin de la ligne colorée du virus entre les deux points
					g2d.drawLine((int)(p1.x + dirX * this.marge), (int)(p1.y + dirY * this.marge), (int)(p2.x - dirX * this.marge), (int)(p2.y - dirY * this.marge));
				}
			}
		}
	}
}
	