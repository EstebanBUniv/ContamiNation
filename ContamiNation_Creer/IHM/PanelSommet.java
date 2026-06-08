package ContamiNation_Creer.IHM;

import ContamiNation_Creer.Controleur;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.Component;
import java.awt.Image;

import java.awt.event.*;

import javax.swing.*;

/* SAE 2.01 | Développement d'une application 
* @author  : THEARD Gregory , COURTOIS Rafael , SALMON William , RICHARD Jenny, BIDAUX Esteban 
* Groupe   : 3
*/

public class PanelSommet extends JPanel implements ActionListener
{
    private FrameSommet frameMere;
    private JLabel[] tabSymboles;
    private JButton btnBase;
    private String[] nomSymboles = {"Aeroport", "Entrepot", "Hopital", "Laboratoire", "Ville"};

    private JLabel labelVolant;
    private String symboleChoisi;
    private ImageIcon iconChoisie;
    private int clicX, clicY;

    // Constructeur
    public PanelSommet(FrameSommet frameMere)
    {
        this.frameMere = frameMere;
        this.setLayout(new GridLayout(this.nomSymboles.length + 1, 1, 10, 10));
        
        this.tabSymboles = new JLabel[this.nomSymboles.length];
		
        this.btnBase = new JButton("Placer les bases " + this.frameMere.getNbVirus());
        this.btnBase.setBackground(new Color(50,50,50));
		this.btnBase.setForeground(Controleur.COLOR_FOREGROUND);
        
        GereSouris gestSouris = new GereSouris();

        for (int i = 0; i < this.nomSymboles.length; i++)
        {
            String nomFoyer = this.nomSymboles[i];
            String cheminImg = "../images/symboles/symbole_" + nomFoyer + ".png";
            
            ImageIcon iconOriginal = new ImageIcon(cheminImg);
            Image img50 = iconOriginal.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon icon50 = new ImageIcon(img50);
            
            this.tabSymboles[i] = new JLabel(nomFoyer, icon50, SwingConstants.CENTER);
            this.tabSymboles[i].addMouseListener(gestSouris);
            this.tabSymboles[i].addMouseMotionListener(gestSouris);
            
            this.add(this.tabSymboles[i]);
        }
        
        this.btnBase.addActionListener(this);
        this.add(this.btnBase);
    }

    public void actionPerformed(ActionEvent e)
    {
        // Alterne le mode de placement des bases
        if (e.getSource() == this.btnBase && this.frameMere != null)
        {
            this.frameMere.modeBase(!(this.frameMere.getmodeBase()));
            this.btnBase.setText("Placer les bases " + this.frameMere.getNbVirus());
            this.btnBase.setBackground(this.frameMere.getmodeBase() ? Controleur.COLOR_BACKGROUND : new Color(50,50,50));
        }
    }

    // Met à jour le texte du bouton selon le nombre de virus restants
    public void updateTexteBouton(int nbRestant) 
    {
        this.btnBase.setText("Placer les bases " + nbRestant);
    }

    //Gestionnaire de Drag & Drop

    private class GereSouris extends MouseAdapter
    {
		// Récupère le symbole selectionné
        public void mousePressed(MouseEvent e)
        {
            for (int i = 0; i < tabSymboles.length; i++)
            {
                if (e.getSource() == tabSymboles[i])
                {
                    symboleChoisi = nomSymboles[i];
                    JLabel labelClique = tabSymboles[i];
                    iconChoisie = (ImageIcon) labelClique.getIcon();
                    clicX = e.getX();
                    clicY = e.getY();

                    JPanel vitre = frameMere.getVitre();
                    vitre.removeAll();
                    vitre.setLayout(null);
                    vitre.setVisible(true);

                    labelVolant = new JLabel(iconChoisie);
                    labelVolant.setSize(50, 50);

                    Point ptVitre = SwingUtilities.convertPoint(labelClique, e.getPoint(), vitre);
                    labelVolant.setLocation(ptVitre.x - clicX, ptVitre.y - clicY);

                    vitre.add(labelVolant);
                    vitre.repaint();
                }
            }
        }
		
		// Affiche le symbole quand on le déplace
        public void mouseDragged(MouseEvent e)
        {
            if (labelVolant != null)
            {
                JPanel vitre = frameMere.getVitre();
                Point ptVitre = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), vitre);
                labelVolant.setLocation(ptVitre.x - clicX, ptVitre.y - clicY);
                vitre.repaint();
            }
        }
		
		// Place le symbole sur le bouton choisi
        public void mouseReleased(MouseEvent e)
        {
            if (labelVolant != null)
            {
                JPanel vitre = frameMere.getVitre();
                PanelGrille panelCible = frameMere.getPanelGrille();

                Point ptPanel = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), panelCible);
                Component c = SwingUtilities.getDeepestComponentAt(panelCible, ptPanel.x, ptPanel.y);

                vitre.remove(labelVolant);
                vitre.setVisible(false);
                vitre.repaint();

                if (c instanceof JButton)
                {
                    Point p = (Point) ((JButton) c).getClientProperty("coords");
                    if (p != null)
                    {
                        int baseExistante = frameMere.getCtrl().getEstBaseSommet(p.x, p.y);
                        frameMere.getCtrl().ajouterSommet(p.x, p.y, symboleChoisi);
                        if (baseExistante != 0) frameMere.getCtrl().setBaseSommet(p.x, p.y, baseExistante);
                    }
                }

                labelVolant = null;
                iconChoisie = null;
                symboleChoisi = null;
                frameMere.repaint();
            }
        }
    }
}