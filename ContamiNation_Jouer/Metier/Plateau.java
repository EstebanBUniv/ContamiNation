package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;

import java.io.File;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Plateau
{
	private Controleur      ctrl;
	private int             col;
	private int             lig;
	private int             nbVirus;
	private int             idJoueur;
	private int             numManche;
	private int             pointTotal;
	private String          nom;
	private Case[][]        tabCases; 
	private File            fichierSource = null;
	private List<Virus>     lstVirus;
	private List<Integer>   lstPointParManche;
	private int             offsetVirus = 0;

	private boolean     modeDebiche = false;

	public static Plateau creerPlateau(int lig, int col, int nbVirus, String nom, Controleur ctrl, int idJoueur) 
	{
		if (col <= 0 || lig <= 0 || nbVirus <= 0) return null;
		return new Plateau(lig, col, nbVirus, nom, ctrl, idJoueur);
	}

	private Plateau(int lig, int col, int nbVirus, String nom, Controleur ctrl, int idJoueur) 
	{
		this.ctrl              = ctrl;
		this.col               = col;
		this.lig               = lig;
		this.nom               = nom;
		this.nbVirus           = nbVirus;
		this.idJoueur          = idJoueur;
		this.lstVirus          = new ArrayList<>();
		this.lstPointParManche = new ArrayList<>();
		this.tabCases          = new Case[lig][col];
		this.numManche         = 1;
		this.pointTotal        = 0;

		for (int i = 0; i < lig; i++) 
			for (int j = 0; j < col; j++)
				this.tabCases[i][j] = new Case(i, j);

		if (this.modeDebiche) 
			this.ctrl.appelerChoixCarte();
	}

	public int    getLig           ()          { return this.lig                              ; }
	public int    getCol           ()          { return this.col                              ; }
	public int    getNbVirus       ()          { return this.nbVirus                          ; }
	public String getNom           ()          { return this.nom                              ; }
	public File   getFichierSource ()          { return this.fichierSource                    ; }
	public Case   getCase(int lig, int col)    { return this.tabCases[lig][col]               ; }
	public Virus  getVirus(int index)          { return this.lstVirus.get(index)              ; }
	public List   getnbPointManche ()          { return this.lstPointParManche                ; }
	public int    getPointTotal    ()          { return this.pointTotal                       ; }
	public int    getNumManche     ()          { return this.numManche                        ; }
	public int    getIdJouer       ()          { return this.idJoueur                         ; }
	public int    getOffsetVirus   ()          { return this.offsetVirus                      ; }

	public Virus getVirusActif() 
	{ 
		int index = (this.offsetVirus + this.numManche - 1) % this.lstVirus.size();
		return this.lstVirus.get(index); 
	}

	public int getNumero() 
	{
		if (this.fichierSource != null) 
		{
			String name = this.fichierSource.getName();
			name = name.replaceAll("[^0-9]", "");
			if (!name.isEmpty()) 
				return Integer.parseInt(name);
		}
		return 0;
	}

	public String getNomVirus(int index) 
	{
		if (index >= 0 && index < this.lstVirus.size())
			return this.lstVirus.get(index).getNom();
		return "";
	}

	public void setFichierSource(File fichier) { this.fichierSource = fichier; }

	public void creerVirus(String nom) 
	{
		this.lstVirus.add(new Virus(nom));
	}

	public boolean mancheSuivante() 
	{
		this.pointTotal += this.calculManche();
		this.lstPointParManche.add(this.calculManche());
		if (this.numManche < this.lstVirus.size()) 
		{
			this.numManche++;
			return true;
		}
		return false;
	}

	public void ajouterZoneDirecte(int lig, int col, int zone) 
	{
		this.tabCases[lig][col].ajouterZone(zone);
	}

	public void ajouterSommet(int lig, int col, String symbole) 
	{
		this.tabCases[lig][col].ajouterSommet(symbole);
	}

	public void relierTousLesSommets() 
	{
		for (int i = 0; i < this.lig; i++) 
			for (int j = 0; j < this.col; j++) 
				if (this.tabCases[i][j].getSommet() != null) 
					this.tabCases[i][j].getSommet().resetVoisins();

		for (int i = 0; i < this.lig; i++) 
			for (int j = 0; j < this.col; j++) 
			{
				Sommet sommetCourant = this.tabCases[i][j].getSommet();
				if (sommetCourant != null) 
					chercherVoisins(i, j, sommetCourant);
			}
	}

	private void chercherVoisins(int lig, int col, Sommet sommetCourant) 
	{
		int[][] directions = { {-1, 0 }, {1, 0 }, {0, -1}, {0, 1},
			                   {-1, -1}, {-1, 1}, {1, -1}, {1, 1} };

		for (int i = 0; i < directions.length; i++) 
		{
			int     dLig = directions[i][0];
			int     dCol = directions[i][1];
			int     ligCherche = lig + dLig;
			int     colCherche = col + dCol;
			boolean continuer = true;

			while (ligCherche >= 0 && ligCherche < this.lig &&
				   colCherche >= 0 && colCherche < this.col &&
				   continuer) 
			{
				Sommet sommetTrouve = this.tabCases[ligCherche][colCherche].getSommet();
				if (sommetTrouve != null) 
				{
					sommetCourant.ajouterVoisin(i, sommetTrouve);
					continuer = false;
				}
				ligCherche += dLig;
				colCherche += dCol;
			}
		}
	}

	public void initialiserBaseVirus(Sommet base, int numeroManche) 
	{
		if (numeroManche > 0 && numeroManche <= this.lstVirus.size()) 
		{
			Virus v = this.lstVirus.get(numeroManche - 1);
			v.setBaseDepart(base);
		}
	}

	public int calculManche()
	{
		int nbSommetParZone = 0;
		ArrayList<Integer> zonesVisitees = new ArrayList<>();
		Virus virusActuel = this.getVirusActif();


		for (int lig = 0; lig < this.lig; lig++) 
		{
			for (int col = 0; col < this.col; col++) 
			{
				Case caseActuelle = this.tabCases[lig][col];

				if (caseActuelle.getAUnSommet()) 
				{
					Sommet sommet = caseActuelle.getSommet();

					if (sommet.getContamine() && sommet.getVirus() == virusActuel) 
					{
						int zoneDeLaCase = caseActuelle.getZone();

						if (zoneDeLaCase != 0 && !zonesVisitees.contains(zoneDeLaCase)) 
						{
							zonesVisitees.add(zoneDeLaCase);
							int tmpNbSommet = 1;

							for (int tmpLig = 0; tmpLig < this.lig; tmpLig++) 
							{
								for (int tmpCol = 0; tmpCol < this.col; tmpCol++) 
								{
									Case autreCase = this.tabCases[tmpLig][tmpCol];

									if (autreCase != caseActuelle &&
										autreCase.getZone() == zoneDeLaCase &&
										autreCase.getAUnSommet()) 
									{

										Sommet autreSommet = autreCase.getSommet();

										if (autreSommet.getContamine() && autreSommet.getVirus() == virusActuel) 
											tmpNbSommet++;
									}
								}
							}

							if (tmpNbSommet > nbSommetParZone) 
								nbSommetParZone = tmpNbSommet;
						}
					}
				}
			}
		}
		return nbSommetParZone * zonesVisitees.size();
	}

	public boolean verifSommet(Case caseAVerif, Carte carteTire, int choixForce)
	{
		if (!this.estCoupValide(caseAVerif, carteTire)) return false;

		Virus virusActuel = this.getVirusActif();

		Sommet nouveauSommet = caseAVerif.getSommet();

		if (virusActuel == null || nouveauSommet == null) return false;
		if (virusActuel.getConquis().isEmpty()) return false;

		LinkedList<Sommet> chemin = virusActuel.getConquis();
		Sommet tete  = chemin.getFirst();
		Sommet queue = chemin.getLast ();

		boolean peutTete  = virusActuel.toucheTete (nouveauSommet);
		boolean peutQueue = virusActuel.toucheQueue(nouveauSommet);

		if (!peutTete && !peutQueue) return false;

		Sommet extremiteChoisie = null;

		if ( peutTete && !peutQueue) extremiteChoisie = tete;
		if (!peutTete &&  peutQueue) extremiteChoisie = queue;

		if (peutTete && peutQueue)
		{
			if      (choixForce == 1) extremiteChoisie = tete;
			else if (choixForce == 2) extremiteChoisie = queue;
			else
			{
				boolean tetePossible = !this.arreteDejaColoree    (tete, nouveauSommet)
									&& !this.estCroisementInterdit(tete, nouveauSommet);

				boolean queuePossible = !this.arreteDejaColoree    (queue, nouveauSommet)
									 && !this.estCroisementInterdit(queue, nouveauSommet);

				if (tetePossible && !queuePossible) extremiteChoisie = tete;
				else if (!tetePossible && queuePossible) extremiteChoisie = queue;
				else if (tetePossible) extremiteChoisie = tete;
				else return false;
			}
		}

		if (extremiteChoisie == null) return false;
		if (this.arreteDejaColoree(extremiteChoisie, nouveauSommet)) return false;
		if (this.estCroisementInterdit(extremiteChoisie, nouveauSommet)) return false;

		virusActuel.ajouterSommetContamine(nouveauSommet, choixForce);
		nouveauSommet.setContamine(true);
		nouveauSommet.setProprietaire(virusActuel);
		return true;
	}


	public boolean estCroisementInterdit(Sommet s1, Sommet s2)
	{
		int lig1 = s1.getLigSommet();
		int col1 = s1.getColSommet();
		int lig2 = s2.getLigSommet();
		int col2 = s2.getColSommet();

    // Parcourir tous les virus pour voir si l'un d'eux occupe un segment qui croise le nôtre
    for (int i = 0; i < this.lstVirus.size(); i++)
    {
        Virus v = this.lstVirus.get(i);
        if (v != null && v.getConquis().size() > 1)
        {
            LinkedList<Sommet> chemin = v.getConquis();
            
            // On regarde chaque segment déjà tracé par ce virus
            for (int c = 0; c < chemin.size() - 1; c++)
            {
                Sommet v1 = chemin.get(c);
                Sommet v2 = chemin.get(c + 1);

                // est-ce que le nouveau coup [(lig1,col1) -> (lig2,col2)] croise le segment existant [v1 -> v2] ?
                if (seCroisentStrictement(lig1, col1, lig2, col2, 
                                          v1.getLigSommet(), v1.getColSommet(), 
                                          v2.getLigSommet(), v2.getColSommet()))
                {
                    return true; // Croisement interdit détecté !
                }
            }
        }
    }
    return false;
}

	private boolean arreteDejaColoree(Sommet s1, Sommet s2)
	{
		if (s1 == null || s2 == null) return false;

		for (Virus v : this.lstVirus)
		{
			if (v == null || v.getConquis() == null) continue;

			LinkedList<Sommet> chemin = v.getConquis();
			for (int i = 0; i < chemin.size() - 1; i++)
			{
				Sommet a = chemin.get(i);
				Sommet b = chemin.get(i + 1);

				if ((a == s1 && b == s2) || (a == s2 && b == s1))
					return true;
			}
		}
		return false;
	}

	public void preparerNouvelleManche() 
	{
		Virus virusActuel = this.getVirusActif();

		for (int l = 0; l < this.lig; l++) 
			for (int c = 0; c < this.col; c++) 
				if (this.tabCases[l][c].getAUnSommet()) 
				{
					Sommet s = this.tabCases[l][c].getSommet();
					s.setContamine(false);

					if (s.getVirus() == virusActuel) 
						s.setContamine(true);
				}
		
		for (Virus v : this.lstVirus) 
			if (v != virusActuel && v.getBaseDepart() != null)
				v.getBaseDepart().setContamine(true);

		int indexVirusActif = (this.offsetVirus + this.numManche - 1) % this.lstVirus.size();
		this.ctrl.changerCouleurManche(indexVirusActif, this.idJoueur);
		this.ctrl.changerImageBase(this.idJoueur);
		this.ctrl.changerLabelPropagation(this.idJoueur);
	}

	public boolean estCoupValide(Case caseAVerif, Carte carteTire) 
	{
		if (caseAVerif == null || carteTire == null || !caseAVerif.getAUnSommet()) 
			return false;

		if (this.arreteDejaColoree    (this.ctrl.getCaseSelectionnee().getSommet(), caseAVerif.getSommet())
									|| this.estCroisementInterdit(this.ctrl.getCaseSelectionnee().getSommet(), caseAVerif.getSommet()))
								return false;
		

		Virus virusActuel = this.getVirusActif();
		Sommet s = caseAVerif.getSommet();

		return virusActuel.estVoisinDeLExtremite(s) &&
			   !s.getContamine() &&
			   (carteTire.getSymbole().equals(s.getSymbole()) || carteTire.getSymbole().equals("Epidemie"));
	}

	public void setIndexVirusActif(int index)
	{
		
		this.offsetVirus = index; 
	}

	/**
	 * Vérifie si le segment [AB] et le segment [CD] se croisent strictement.
	 */
	private boolean seCroisentStrictement(int ligA, int colA, int ligB, int colB, 
										int ligC, int colC, int ligD, int colD)
	{
		int o1 = orientation(ligA, colA, ligB, colB, ligC, colC);
		int o2 = orientation(ligA, colA, ligB, colB, ligD, colD);
		int o3 = orientation(ligC, colC, ligD, colD, ligA, colA);
		int o4 = orientation(ligC, colC, ligD, colD, ligB, colB);

		// Les segments se croisent si et seulement si :
		// - C et D sont de côtés opposés de la droite (AB) (l'un est horaire, l'autre anti-horaire)
		// - A et B sont de côtés opposés de la droite (CD)
		return ((o1 == 1 && o2 == 2) || (o1 == 2 && o2 == 1)) &&
			((o3 == 1 && o4 == 2) || (o3 == 2 && o4 == 1));
	}

	/**
	 * Détermine l'orientation de trois points (1 = Horaire, 2 = Anti-horaire, 0 = Alignés)
	 */
	private int orientation(int lig1, int col1, int lig2, int col2, int lig3, int col3)
	{
		// Calcul du produit en croix (en considérant col comme X et lig comme Y)
		long val = (long)(lig2 - lig1) * (col3 - col2) - (long)(col2 - col1) * (lig3 - lig2);
		
		if (val == 0) return 0; // Les points sont alignés
		return (val > 0) ? 1 : 2; // 1 = Sens horaire, 2 = Sens anti-horaire
	}
}