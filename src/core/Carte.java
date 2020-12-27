package core;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Carte {
	private int nbJoueurs;
	private int nbTerritoire;
	private ArrayList<Territoire> territoires;
	private Territoire[][] carte = new Territoire[33][33];

	// Ctor avec règles du jeu
	public Carte(ArrayList<Joueur> joueurs) {
		this.nbJoueurs = joueurs.size();

		// générer un nombre de territoire entre 22 et 43 multiple du nombre de joueurs
		// (min|max)_nb_territoire = 0, nb_joueur = 2
		int[] min_nb_terr = { 22, 24, 24, 25, 24, 28, 24 };
		int[] coeff_max_nb_terr = { 10, 6, 4, 3, 3, 2, 2 };

		Random nb_aleat = new Random();
		this.nbTerritoire = nbJoueurs * nb_aleat.nextInt(coeff_max_nb_terr[nbJoueurs - 2] + 1)
				+ min_nb_terr[nbJoueurs - 2];
		this.territoires = new ArrayList<>(nbTerritoire);

		/*
		 * Création et placement des territoires dans la carte
		 * ===================================================
		 */
		int k = 0;
		while (k <= nbTerritoire) {
			Random randX = new Random();
			Random randY = new Random();

			int posX = randX.nextInt(29) + 2;
			int posY = randY.nextInt(29) + 2;
			
			// Si la position est valide, ajouter le territoire et l'agrandir !
			if (k == 0 || positionValide(posX, posY)) {
				// Si emplacement de la carte est vide, la remplir d'un nouveau territoire
				if (carte[posX][posY] == null) {
					k++; // augmenter le nb de territoire créé et le créer,...
					Territoire nouvTerr = new Territoire(k + 1);
					
					// ... le placer,...
					carte[posX][posY] = nouvTerr;
					// ... l'ajouter à la liste des territoires
					territoires.add(nouvTerr);
					// ... et l'agrandir
					agrandirTerrain(nouvTerr, posX, posY);
				}	
			}

		}
		
		/*
		 * Calcul des voisins pour chaque territoire
		 * =========================================
		 */
		for (int i = 0; i < 33; i++) {
			for (int j = 0; j < 33; j++) {
				// obtenir les voisins pour chaque territoire
				HashMap<String, Long> voisins = getTerrainsVoisins(i,j);
				
				// et les ajouter
				for (Map.Entry<String, Long> mapVoisin : voisins.entrySet()) {
					Long mapVoisinValue = mapVoisin.getValue();
					Territoire curTerr = carte[i][j];
					
					if (mapVoisinValue != 0 && curTerr != null) {
						// ne pas ajouter soi-même
						if (mapVoisinValue != curTerr.getId()) {
							curTerr.addVoisins(mapVoisinValue);
						}
					}
				}
			}
		}
		

		/*
		 * Distribution des territoires aux joueurs
		 * ========================================
		 */
		int nb_des_par_joueur = 5 * (nbTerritoire / nbJoueurs);
		int[] nbDesJoueurs = new int[nbJoueurs];
		Arrays.fill(nbDesJoueurs, nb_des_par_joueur);
		int nb_terr_par_joueur = nbTerritoire / nbJoueurs;

		// mélanger liste territoire
		Collections.shuffle(territoires);
		for (int i = 0; i < nbJoueurs; i++) {
			for (int j = 0; j <= nb_terr_par_joueur; j++) {
				Territoire terrJoueur = territoires.get(i * nb_terr_par_joueur + j);
				// attribution joueur
				// et ajout ce territoire a la liste du territoire du joueur
				Joueur joueur = joueurs.get(i);
				joueur.addTerritoire(terrJoueur);
				terrJoueur.setJoueur(joueur);

				// initialisation force à 1
				int force = 1;
				nbDesJoueurs[i] -= force;
				terrJoueur.setForce(force);

				territoires.set(i * nb_terr_par_joueur + j, terrJoueur);
			}
			
			// distribution des forces aux territoires du joueur
			while (nbDesJoueurs[i] > 0) {
				Random aleat = new Random();
				int randIndex = aleat.nextInt(nb_terr_par_joueur) + i*nb_terr_par_joueur;
				
				Territoire fTerr = territoires.get(randIndex);
				if (fTerr.getForce() +1 <= 8) {
					fTerr.setForce(fTerr.getForce() + 1);
					territoires.set(randIndex, fTerr);
					nbDesJoueurs[i]--;
				}
			}
			
			// verif somme des territoires
			int sommeDesTerr = 0;
			for (int t = 0; t <= nb_terr_par_joueur; t++) {
				sommeDesTerr += territoires.get(i * nb_terr_par_joueur + t).getForce();
			}
			
			System.out.printf("joueur:%d, dés:%s\n", i, sommeDesTerr == nb_des_par_joueur ? "OK" : "KO :(");
			
		}

	}

	public HashMap<String, Long> getTerrainsVoisins(int posX, int posY) {
		HashMap<String, Long> voisins = new HashMap<>();

		// verification voisins territoire
		// mettre id du territoire si existant, 0 sinon
		voisins.put("left", (posY - 1 >= 0 && carte[posX][posY - 1] != null) ? carte[posX][posY - 1].getId() : 0L);
		voisins.put("right", (posY + 1 < 33 && carte[posX][posY + 1] != null) ? carte[posX][posY + 1].getId() : 0L);

		// autres initialisations
		voisins.put("top-left", 0L);
		voisins.put("top-right", 0L);
		voisins.put("bottom-left", 0L);
		voisins.put("bottom-right", 0L);

		// top
		if (posX - 1 >= 0) {
			voisins.put("top-left",
					(posY - (posX + 1) % 2 >= 0 && carte[posX - 1][posY - (posX + 1) % 2] != null)
							? carte[posX - 1][posY - (posX + 1) % 2].getId()
							: 0L);
			voisins.put("top-right",
					(posY + posX % 2 < 33 && carte[posX - 1][posY + posX % 2] != null)
							? carte[posX - 1][posY + posX % 2].getId()
							: 0L);
		}
		// bottom
		if (posX + 1 < 33) {
			voisins.put("bottom-left",
					(posY - (posX + 1) % 2 >= 0 && carte[posX + 1][posY - (posX + 1) % 2] != null)
							? carte[posX + 1][posY - (posX + 1) % 2].getId()
							: 0L);
			voisins.put("bottom-right",
					(posY + posX % 2 < 33 && carte[posX + 1][posY + posX % 2] != null)
							? carte[posX + 1][posY + posX % 2].getId()
							: 0L);
		}

		return voisins;
	}

	protected boolean positionValide(int posX, int posY) {
		HashMap<String, Long> voisins = getTerrainsVoisins(posX, posY);
		
		int nbIdVide = 0;
		
		for(Map.Entry<String, Long> curEntry : voisins.entrySet()) {
			if(curEntry.getValue() != 0) {
				nbIdVide++;
			}
		}
		
		return nbIdVide > 0 && nbIdVide < 6;
	}
	
	protected void agrandirTerrain(Territoire territoire, int posX, int posY) {
		Random randTaille = new Random();
		int tailleTerr = randTaille.nextInt(25 - 7) + 7; // taille aproximative du terrain

		int t = 0;
		boolean forceContinue = false;
		LinkedList<Point> fileAgrTerr = new LinkedList<>();
		fileAgrTerr.add(new Point(posX, posY));

		while (t <= tailleTerr || forceContinue) {
			Point pt_courant = fileAgrTerr.poll();

			// generation nb aleatoire
			ArrayList<Boolean> nb_aleat = new ArrayList<>(Arrays.asList(true, true, true, true, false, false));
			Collections.shuffle(nb_aleat);

			if (pt_courant != null) {
				// expand le territoire à gauche, droite, haut-gauche, ... aléatoirement
				int curX = (int) pt_courant.getX();
				int curY = (int) pt_courant.getY();

				// gauche
				if (curY - 1 >= 0 && carte[curX][curY - 1] == null) {
					if (nb_aleat.get(0)) {
						fileAgrTerr.add(new Point(curX, curY - 1));
						carte[curX][curY - 1] = territoire;
						t++;
					}
				}

				// droite
				if (curY + 1 < 33 && carte[curX][curY + 1] == null) {
					if (nb_aleat.get(1)) {
						fileAgrTerr.add(new Point(curX, curY + 1));
						carte[curX][curY + 1] = territoire;
						t++;
					}
				}

				// top
				if (curX - 1 >= 0) {
					// gauche
					if (curY - (curX + 1) % 2 >= 0 && carte[curX - 1][curY - (curX + 1) % 2] == null) {
						if (nb_aleat.get(2)) {
							fileAgrTerr.add(new Point(curX - 1, curY - (curX + 1) % 2));
							carte[curX - 1][curY - (curX + 1) % 2] = territoire;
							t++;
						}
					}

					// droite
					if (curY + curX % 2 < 33 && carte[curX - 1][curY + curX % 2] == null) {
						if (nb_aleat.get(3)) {
							fileAgrTerr.add(new Point(curX - 1, curY + curX % 2));
							carte[curX - 1][curY + curX % 2] = territoire;
							t++;
						}
					}

				}

				// bottom
				if (curX + 1 < 33) {
					// gauche
					if (curY - (curX + 1) % 2 >= 0 && carte[curX + 1][curY - (curX + 1) % 2] == null) {
						if (nb_aleat.get(4)) {
							fileAgrTerr.add(new Point(curX + 1, curY - (curX + 1) % 2));
							carte[curX + 1][curY - (curX + 1) % 2] = territoire;
							t++;
						}
					}

					// droite
					if (curY + curX % 2 < 33 && carte[curX + 1][curY + curX % 2] == null) {
						if (nb_aleat.get(5)) {
							fileAgrTerr.add(new Point(curX + 1, curY + curX % 2));
							carte[curX + 1][curY + curX % 2] = territoire;
							t++;
						}
					}

				}

			} else {
				// On ne peut plus expand le terrain,
				if(!forceContinue) {
					forceContinue = true;
				} 
				// quitter la boucle
				else {
					forceContinue = false;
					t = tailleTerr + 1;					
				}
			}

		}
	}

	public Territoire[][] getCarte() {
		return carte; // rendre plus secure
	}

	@Override
	public String toString() {
		String result = "Carte: nbJoueurs=" + nbJoueurs + ", nbTerritoire=" + nbTerritoire + ",\ncarte=\n";
		for (int i = 0; i < 33; i++) {
			result += i % 2 == 1 ? "  " : "";
			for (int j = 0; j < 33; j++) {
				result += String.format("%2d ", carte[i][j] != null ? carte[i][j].getId() : 0);
			}
			result += "\n";
		}

		return result += "\n";
	}

	public static void main(String[] args) {
		int nbJoueurs = 7;
		Color[] colors = { Color.GREEN, Color.BLUE, Color.PINK, Color.ORANGE, Color.MAGENTA, Color.RED, Color.YELLOW };
		ArrayList<Joueur> joueurs = new ArrayList<>(nbJoueurs);
		System.out.println("Création joueurs...");
		for (int i = 0; i < nbJoueurs; i++) {
			Joueur newJoueur = new Joueur(i + 1, colors[i]);
			joueurs.add(newJoueur);
		}
		System.out.println("Création carte...");
		Carte map = new Carte(joueurs);
		System.out.println(map);
	}
}
