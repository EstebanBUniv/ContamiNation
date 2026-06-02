package ContamiNation;

import ContamiNation.Metier.*;
import ContamiNation.IHM.*;

public class Controleur
{
    private FramePlateau frame;

    private Plateau      plateau;


    public Controleur()
    {
        this.frame   = new FramePlateau(this);
    }

    public static void main (String[] args)
    {
        new Controleur();
    }

    public void creerPlateau(int hauteur, int largeur, int nbCouleur)
    {
        this.plateau = Plateau.creerPlateau( hauteur, largeur, nbCouleur);

        System.out.println(this.plateau);
    }

}