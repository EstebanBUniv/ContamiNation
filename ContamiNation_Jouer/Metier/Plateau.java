package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Plateau
{
	private Controleur  ctrl;
	private int         col;
	private int         lig;
	private int         nbVirus;
	private int         numManche;
	private int         pointTotal;
	private String      nom;
	private Case[][]    tabCases; 
	private File        fichierSource = null;
	private List<Virus> lstVirus;

	private boolean     modeDebiche = false;

	public static Plateau creerPlateau(int lig, int col, int nbVirus, String nom, Controleur ctrl) {
		if (col <= 0 || lig <= 0 || nbVirus <= 0) return null;
		return new Plateau(lig, col, nbVirus, nom, ctrl);
	}

	private Plateau(int lig, int col, int nbVirus, String nom, Controleur ctrl) {
		this.ctrl = ctrl;
		this.col = col;
		this.lig = lig;
		this.nom = nom;
		this.nbVirus = nbVirus;
		this.lstVirus = new ArrayList<>();
		this.tabCases = new Case[lig][col];
		this.numManche = 1;
		this.pointTotal = 0;

		for (int i = 0; i < lig; i++) {
			for (int j = 0; j < col; j++) {
				this.tabCases[i][j] = new Case(i, j);
			}
		}

		if (this.modeDebiche) this.ctrl.appelerChoixCarte();
	}

	public int getLig()                   { return this.lig; }
	public int getCol()                   { return this.col; }
	public int getNbVirus()               { return this.nbVirus; }
	public String getNom()                { return this.nom; }
	public File getFichierSource()        { return this.fichierSource; }
	public Case getCase(int lig, int col) { return this.tabCases[lig][col]; }
	public Virus getVirus(int index)      { return this.lstVirus.get(index); }
	public Virus getVirusActif()          { return this.lstVirus.get(this.numManche - 1); }
	public int getPointTotal()            { return this.pointTotal; }
	public int getNumManche()             { return this.numManche; }

	public int getNumero() {
		if (this.fichierSource != null) {
			String name = this.fichierSource.getName();
			name = name.replaceAll("[^0-9]", "");
			if (!name.isEmpty()) return Integer.parseInt(name);
		}
		return 0;
	}

	public String getNomVirus(int index) {
		if (index >= 0 && index < this.lstVirus.size())
			return this.lstVirus.get(index).getNom();
		return "";
	}

	public void setFichierSource(File fichier) { this.fichierSource = fichier; }

	public void creerVirus(String nom) {
		this.lstVirus.add(new Virus(nom));
	}

	public boolean mancheSuivante() {
		this.pointTotal += this.calculManche();
		if (this.numManche < this.lstVirus.size()) {
			this.numManche++;
			return true;
		}
		return false;
	}

	public void ajouterZoneDirecte(int lig, int col, int zone) {
		this.tabCases[lig][col].ajouterZone(zone);
	}

	public void ajouterSommet(int lig, int col, String symbole) {
		this.tabCases[lig][col].ajouterSommet(symbole);
	}

	public void relierTousLesSommets() {
		for (int i = 0; i < this.lig; i++) {
			for (int j = 0; j < this.col; j++) {
				if (this.tabCases[i][j].getSommet() != null) {
					this.tabCases[i][j].getSommet().resetVoisins();
				}
			}
		}

		for (int i = 0; i < this.lig; i++) {
			for (int j = 0; j < this.col; j++) {
				Sommet sommetCourant = this.tabCases[i][j].getSommet();
				if (sommetCourant != null) chercherVoisins(i, j, sommetCourant);
			}
		}
	}

	private void chercherVoisins(int lig, int col, Sommet sommetCourant) {
		int[][] directions = {
			{-1, 0}, {1, 0}, {0, -1}, {0, 1},
			{-1, -1}, {-1, 1}, {1, -1}, {1, 1}
		};

		for (int i = 0; i < directions.length; i++) {
			int dLig = directions[i][0];
			int dCol = directions[i][1];
			int ligCherche = lig + dLig;
			int colCherche = col + dCol;
			boolean continuer = true;

			while (ligCherche >= 0 && ligCherche < this.lig &&
				   colCherche >= 0 && colCherche < this.col &&
				   continuer) {
				Sommet sommetTrouve = this.tabCases[ligCherche][colCherche].getSommet();
				if (sommetTrouve != null) {
					sommetCourant.ajouterVoisin(i, sommetTrouve);
					continuer = false;
				}
				ligCherche += dLig;
				colCherche += dCol;
			}
		}
	}

	public void initialiserBaseVirus(Sommet base, int numeroManche) {
		if (numeroManche > 0 && numeroManche <= this.lstVirus.size()) {
			Virus v = this.lstVirus.get(numeroManche - 1);
			v.setBaseDepart(base);
		}
	}

	public int calculManche() {
		int nbSommetParZone = 0;
		ArrayList<Integer> zonesVisitees = new ArrayList<>();
		Virus virusActuel = this.lstVirus.get(this.numManche - 1);

		for (int lig = 0; lig < this.lig; lig++) {
			for (int col = 0; col < this.col; col++) {
				Case caseActuelle = this.tabCases[lig][col];

				if (caseActuelle.getAUnSommet()) {
					Sommet sommet = caseActuelle.getSommet();

					if (sommet.getContamine() && sommet.getVirus() == virusActuel) {
						int zoneDeLaCase = caseActuelle.getZone();

						if (zoneDeLaCase != 0 && !zonesVisitees.contains(zoneDeLaCase)) {
							zonesVisitees.add(zoneDeLaCase);
							int tmpNbSommet = 1;

							for (int tmpLig = 0; tmpLig < this.lig; tmpLig++) {
								for (int tmpCol = 0; tmpCol < this.col; tmpCol++) {
									Case autreCase = this.tabCases[tmpLig][tmpCol];

									if (autreCase != caseActuelle &&
										autreCase.getZone() == zoneDeLaCase &&
										autreCase.getAUnSommet()) {

										Sommet autreSommet = autreCase.getSommet();

										if (autreSommet.getContamine() && autreSommet.getVirus() == virusActuel) {
											tmpNbSommet++;
										}
									}
								}
							}

							if (tmpNbSommet > nbSommetParZone) nbSommetParZone = tmpNbSommet;
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

		Virus virusActuel = this.lstVirus.get(this.numManche - 1);
		Sommet nouveauSommet = caseAVerif.getSommet();

		if (virusActuel == null || nouveauSommet == null) return false;
		if (virusActuel.getConquis().isEmpty()) return false;

		LinkedList<Sommet> chemin = virusActuel.getConquis();
		Sommet tete = chemin.getFirst();
		Sommet queue = chemin.getLast();

		boolean peutTete = virusActuel.toucheTete(nouveauSommet);
		boolean peutQueue = virusActuel.toucheQueue(nouveauSommet);

		if (!peutTete && !peutQueue) return false;

		Sommet extremiteChoisie = null;

		if (peutTete && !peutQueue) extremiteChoisie = tete;
		if (!peutTete && peutQueue) extremiteChoisie = queue;

		if (peutTete && peutQueue)
		{
			if (choixForce == 1) extremiteChoisie = tete;
			else if (choixForce == 2) extremiteChoisie = queue;
			else
			{
				boolean tetePossible = !this.arreteDejaColoree(tete, nouveauSommet)
									&& !this.estCroisementInterdit(tete, nouveauSommet);

				boolean queuePossible = !this.arreteDejaColoree(queue, nouveauSommet)
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

		// 1. Vérifier si c'est bien un déplacement diagonal (écart en ligne == écart en colonne)
		if (Math.abs(lig1 - lig2) != Math.abs(col1 - col2) || Math.abs(lig1 - lig2) == 0) {
			return false; // Ce n'est pas une diagonale ou c'est le même point, aucun risque !
		}

		// 2. Identifier le point le plus haut (ligA) et le point le plus bas (ligB)
		int ligA = Math.min(lig1, lig2);
		int ligB = Math.max(lig1, lig2);
		
		// Associer les bonnes colonnes
		int colA = (lig1 < lig2) ? col1 : col2;
		int colB = (lig1 < lig2) ? col2 : col1;

		// 3. Parcourir tous les virus pour voir si l'un d'eux occupe la diagonale adverse
		for (int i = 0; i < this.lstVirus.size(); i++)
		{
			Virus v = this.lstVirus.get(i);
			if (v != null && v.getConquis().size() > 1)
			{
				LinkedList<Sommet> chemin = v.getConquis();
				
				// On regarde chaque segment du chemin du virus
				for (int c = 0; c < chemin.size() - 1; c++)
				{
					Sommet v1 = chemin.get(c);
					Sommet v2 = chemin.get(c + 1);

					// Est-ce que ce segment relie (ligA, colB) et (ligB, colA) ?
					boolean conditionDirecte = (v1.getLigSommet() == ligA && v1.getColSommet() == colB && 
												v2.getLigSommet() == ligB && v2.getColSommet() == colA);
												
					boolean conditionInverse = (v2.getLigSommet() == ligA && v2.getColSommet() == colB && 
												v1.getLigSommet() == ligB && v1.getColSommet() == colA);

					if (conditionDirecte || conditionInverse)
					{
						return true; // La diagonale adverse est déjà prise ! Croisement interdit.
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
				{
					return true;
				}
			}
		}
		return false;
	}

	public void preparerNouvelleManche() {
		Virus virusActuel = this.lstVirus.get(this.numManche - 1);

		for (int l = 0; l < this.lig; l++) {
			for (int c = 0; c < this.col; c++) {
				if (this.tabCases[l][c].getAUnSommet()) {
					Sommet s = this.tabCases[l][c].getSommet();
					s.setContamine(false);

					if (s.getVirus() == virusActuel) {
						s.setContamine(true);
					}
				}
			}
		}

		for (Virus v : this.lstVirus) {
			if (v != virusActuel && v.getBaseDepart() != null) {
				v.getBaseDepart().setContamine(true);
			}
		}
	}

	public boolean estCoupValide(Case caseAVerif, Carte carteTire) {
		if (caseAVerif == null || carteTire == null || !caseAVerif.getAUnSommet()) return false;

		Virus virusActuel = this.lstVirus.get(this.numManche - 1);
		Sommet s = caseAVerif.getSommet();

		return virusActuel.estVoisinDeLExtremite(s) &&
			   !s.getContamine() &&
			   (carteTire.getSymbole().equals(s.getSymbole()) || carteTire.getSymbole().equals("Epidemie"));
	}
}