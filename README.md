# ContamiNation : Éditeur de Plateaux (ContamiNation_Creer)

**ContamiNation_Creer** est l'outil d'édition de niveaux ContamiNation.

## Fonctionnalités Principales

* **Configuration sur mesure** : Définition des dimensions de la grille (lignes/colonnes) et configuration du nombre et du nom des virus.
* **Édition des zones** : Outils de dessin (pinceau et pot de peinture) avec génération de couleurs dynamiques et algorithmes de vérification de l'intégrité des zones.
* **Gestion du Graphe** : Placement en "Drag & Drop" des sommets (Aéroport, Ville, Laboratoire, etc.) et assignation des bases virales.
* **Génération automatique** : Calcul et tracé des arêtes reliant les différents sommets du plateau.
* **Persistance des données** : Sauvegarde, chargement, copie et renommage des plateaux générés sous forme de fichiers `.data`.

## Architecture Technique

Le projet respecte scrupuleusement le patron de conception **MVC (Modèle-Vue-Contrôleur)** afin de garantir une séparation stricte entre les données et l'affichage :
* `Controleur.java` : Chef d'orchestre de l'application.
* `Metier/` : Couche de données experte gérant le Graphe (`Sommet`, `Case`, `Plateau`) et l'algorithmique.
* `IHM/` : Interfaces graphiques Swing (`FrameCreer`, `FrameSommet`, `PanelGrille`, etc.).

## Installation et Exécution

Voici la procédure complète pour compiler les sources et lancer l'interface de création depuis un terminal (nécessite le JDK Java) :

**1. Cloner le dépôt localement :**

```bash
git clone git@github.com:EstebanBUniv/ContamiNation.git
cd ./ContamiNation_Creer
javac "@compile.list" -d ../class
cd ../class
java ContamiNation_Creer.Controleur
