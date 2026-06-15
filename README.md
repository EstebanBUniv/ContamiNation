# ContamiNation : Éditeur de Plateaux (ContamiNation_Creer)

**ContamiNation_Creer** est l'outil d'édition de niveaux ContamiNation.

## Fonctionnalités Principales

* **Configuration** : Définition des dimensions de la grille (lignes/colonnes) et configuration du nombre et du nom des virus.
* **Édition des zones** : Outils de dessin (pinceau et pot de peinture) avec génération de couleurs dynamiques et algorithmes de vérification de l'intégrité des zones.
* **Gestion du Graphe** : Placement en "Drag & Drop" des sommets (Aéroport, Ville, Laboratoire, etc.) et assignation des bases virales.
* **Génération automatique** : Calcul et tracé des arêtes reliant les différents sommets du plateau.
* **Sauvegarde des données** : Sauvegarde, chargement, copie et renommage des plateaux générés sous forme de fichiers `.data`.

## Architecture Technique

Le projet respecte scrupuleusement le patron de conception **MVC (Modèle-Vue-Contrôleur)** :
* `Controleur.java`
* `Metier/` : Couche de données gérant la création (`Sommet`, `Case`, `Plateau`).
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
```

# ContamiNation - Règles et Fonctionnement du Jeu

ContamiNation est un jeu de stratégie et de plateau abstrait où les joueurs incarnent des virus cherchant à propager leur réseau d'infection. En reliant des sommets et en s'étendant à travers différentes zones colorées, chaque joueur tente d'accumuler le plus de points possible au fil des manches.

---

## But du Jeu

Le vainqueur est le joueur qui cumule le plus de points totaux à la fin de la partie. Les points sont calculés à la fin de chaque manche en fonction de la longueur et de la structure du réseau de propagation tracé par votre virus.

---

## Déroulement d'une Partie

Une partie se divise en plusieurs manches successives.

### 1. Structure du Plateau et Zones
* Le plateau est composé d'une grille de cases réparties dans différentes zones de couleurs.
* Certaines cases contiennent des sommets.
* Chaque joueur possède sa propre base de départ (le point initial d'infection de son virus).

### 2. Le Tour de Jeu
À chaque tour, une carte active est visible par tous les joueurs. Cette carte dicte les conditions ou les symboles autorisés pour ce tour-ci. 
Un joueur peut effectuer deux actions principales :

* **Sélectionner et Relier (Se propager) :**
1. Le joueur clique sur un sommet valide.
2. L'interface met en évidence les sommets voisins atteignables et légaux .
3. En cliquant sur un voisin, un lien physique (une arête colorée aux couleurs du virus) est tracé.
* **Passer son tour :** Si un joueur ne peut pas ou ne souhaite pas jouer, il peut cliquer sur "Passer le Tour".

### 3. Les Modes de Pioche
Selon la configuration de la partie, le tirage des cartes s'effectue de deux manières :
* **Mode Classique / Multi :** La carte suivante de la pile est automatiquement révélée et devient la carte active du tour.
---

## Fin de Manche et Défausse

* Chaque carte utilisée est envoyée dans la pile de défausse, dont l'historique reste visible en bas de l'écran.
* Lorsqu'une condition de fin de manche est atteinte (par exemple, si la pioche est vide ou que toutes les cartes foncés on été tirée) :
1. Les scores de la manche actuelle sont calculés.
2. la défausse est vidée, et une nouvelle manche commence avec un changement du virus actif.

---

## Fin de Partie et Classement

Une fois toutes les manches terminées, le jeu affiche un écran de fin contenant :
1. Le nom du vainqueur.
2. Le classement final détaillé affichant les points cumulés de chaque joueur.
3. Un bouton permettant de quitter proprement les connexions réseau et de retourner au menu principal.
