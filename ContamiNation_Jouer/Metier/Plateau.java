package ContamiNation_Jouer.Metier;

import ContamiNation_Jouer.Controleur;
import java.io.File;
import java.util.ArrayList;
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
		if ( col <= 0 || lig <= 0 || nbVirus <=0) return null;
		return new Plateau(lig, col, nbVirus, nom, ctrl);
	}

	private Plateau(int lig, int col, int nbVirus, String nom, Controleur ctrl) {
		this.ctrl = ctrl;
		this.col  = col; this.lig  = lig; this.nom  = nom; this.nbVirus  = nbVirus;
		this.lstVirus = new ArrayList<>();
		this.tabCases = new Case[lig][col];
		this.numManche  = 1; this.pointTotal = 0;

		for (int i = 0; i < lig; i++) {
			for (int j = 0; j < col; j++) {
				this.tabCases[i][j] = new Case(i, j);
			}
		}
		
		if (this.modeDebiche) this.ctrl.appelerChoixCarte();
	}

	public int    getLig()                   { return this.lig                ; }
	public int    getCol()                   { return this.col                ; }
	public int    getNbVirus()               { return this.nbVirus            ; }
	public String getNom()                   { return this.nom                ; }
	public File   getFichierSource()         { return this.fichierSource      ; }
	public Case   getCase(int lig, int col)  { return this.tabCases[lig][col] ; }
	public Virus  getVirus(int index)        { return this.lstVirus.get(index); }
	public Virus  getVirusActif()            { return this.lstVirus.get(this.numManche - 1); }
	public int    getPointTotal()            { return this.pointTotal         ; }
	public int    getNumManche ()            { return this.numManche          ; }

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
	public void creerVirus(String nom) { this.lstVirus.add(new Virus(nom)); }
	
	public boolean mancheSuivante() 
	{
		this.pointTotal += this.calculManche();
		if (this.numManche < this.lstVirus.size()) 
		{
			this.numManche++;
			return true;
		}
		return false;
	}

	public void ajouterZoneDirecte(int lig, int col, int zone) { this.tabCases[lig][col].ajouterZone(zone); }
	public void ajouterSommet(int lig, int col, String symbole) { this.tabCases[lig][col].ajouterSommet(symbole); }

	public void relierTousLesSommets() {
		for (int i = 0; i < this.lig; i++)
			for (int j = 0; j < this.col; j++)
				if (this.tabCases[i][j].getSommet() != null)
					this.tabCases[i][j].getSommet().resetVoisins();
			
		for (int i = 0; i < this.lig; i++) {
			for(int j = 0; j < this.col; j++) {
				Sommet sommetCourant = this.tabCases[i][j].getSommet();
				if (sommetCourant != null) chercherVoisins(i, j, sommetCourant);
			}
		}
	}

	private void chercherVoisins(int lig, int col, Sommet sommetCourant) {
		int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
		for (int i = 0; i < directions.length; i++) {
			int dLig = directions[i][0], dCol = directions[i][1];
			int ligCherche = lig + dLig, colCherche = col + dCol;
			boolean continuer = true;
			while (ligCherche >= 0 && ligCherche < this.lig && colCherche >= 0 && colCherche < this.col && continuer) {
				Sommet sommetTrouve = this.tabCases[ligCherche][colCherche].getSommet();
				if (sommetTrouve != null) {
					sommetCourant.ajouterVoisin(i, sommetTrouve);
					continuer = false; 
				}
				ligCherche += dLig; colCherche += dCol;
			}
		}
	}
	
	public void initialiserBaseVirus(Sommet base, int numeroManche) {
		if (numeroManche > 0 && numeroManche <= this.lstVirus.size()) {
			Virus v = this.lstVirus.get(numeroManche - 1);
			v.setBaseDepart(base);
			base.setProprietaire(v); // LA LIGNE QUI CORRIGE LE CRASH
		}
	}

	public int calculManche() 
	{
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
		Virus virusActuel = this.lstVirus.get(this.numManche - 1);
		
		if ( carteTire != null &&
		     caseAVerif .getAUnSommet() &&
			 virusActuel.estVoisinDeLExtremite(caseAVerif.getSommet()) &&
			 !caseAVerif.getSommet().getContamine() &&
			 (carteTire.getSymbole().equals(caseAVerif.getSommet().getSymbole()) || carteTire.getSymbole().equals("Epidemie")))
		{
			virusActuel.ajouterSommetContamine(caseAVerif.getSommet(), choixForce);
			caseAVerif.getSommet().setContamine(true);
			caseAVerif.getSommet().setProprietaire(virusActuel);
			return true;
		}
		return false;
	}
	
	public void preparerNouvelleManche() 
	{
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
		Virus virusActuel = this.lstVirus.get(this.numManche - 1);
		if (carteTire == null || !caseAVerif.getAUnSommet()) return false;
		Sommet s = caseAVerif.getSommet();
		return virusActuel.estVoisinDeLExtremite(s) && 
			   !s.getContamine() && 
			   (carteTire.getSymbole().equals(s.getSymbole()) || carteTire.getSymbole().equals("Epidemie"));
	}
}