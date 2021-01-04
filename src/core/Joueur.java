package core;

import java.awt.Color;
import java.util.*;

public class Joueur {
	private long id;
	private Color couleur;
	private List<Territoire> listeTerritoire = new ArrayList<>();
	private static int nbTerritoire = 1;

	public Joueur(long id) {
		this.id = id;
	}

	public Joueur(long id, Color couleur) {
		this.id = id;
		this.couleur = couleur;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Color getCouleur() {
		return couleur;
	}

	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	public List<Territoire> getListeTerritoire() {
		return listeTerritoire; // rendre plus secure
	}

	public void addTerritoire(Territoire e) {
		listeTerritoire.add(e);
	}


	public static int getNbTerritoire() {
		return nbTerritoire;
	}

	public static void setNbTerritoire(int nbTerritoire) {
		Joueur.nbTerritoire = nbTerritoire;
	}

	@Override
	public String toString() {
		// return "Joueur [id=" + id + ", couleur=" + couleur + ", listeTerritoire=" +
		// listeTerritoire + "]";
		return String.format(
				"<html>" + "<h3 style='margin: 0.5em 0'>Joueur %d</h3>"
						+ "<p style='padding-left: 10px'>Nb terr: %d<small>/%d</small> (%d %%)</p>" + "</html>",
				id, listeTerritoire.size(), nbTerritoire, (listeTerritoire.size() * 100) / nbTerritoire);
	}

	/*
	 * Gestion attaque
	 */
	private static ArrayList<Integer> lanceDe(int nbDes) {
		ArrayList<Integer> des = new ArrayList<>(nbDes);
		for (int i = 0; i < nbDes; i++) {
			Random nb_aleat = new Random();
			int faceDe = nb_aleat.nextInt(6) + 1;
			des.add(faceDe);
		}
		return des;
	}

	// https://stackoverflow.com/a/49917501
	public static int sommeDe(ArrayList<Integer> des) {
		return des.stream().mapToInt(de -> de).sum();
	}
	
	private void victoireAttaque(Territoire tAttaquant, Territoire tAttaquee) {
		// Ajout territoire gagné
		addTerritoire(tAttaquee);
		
		// déplacer tout ses dés sur le territoire conquis sauf 1...
		int nbDeAttaquant = tAttaquant.getForce();
		tAttaquee.setForce(nbDeAttaquant - 1);
		tAttaquee.setJoueur(this);

		// ...qui reste sur le territoire de départ
		Territoire tDepart = listeTerritoire.stream().filter(t -> t.getId() == tAttaquant.getId())
				.findFirst().orElse(null);
		tDepart.setForce(1);
	}

	private void defaiteAttaque(Territoire tAttaquant) {
		// Ne conserve qu'un seul dé sur le territoire de départ...
		Territoire tDepart = listeTerritoire.stream().filter(t -> t.getId() == tAttaquant.getId())
				.findFirst().orElse(null);
		tDepart.setForce(1);
		// ...et le territoire attaqué reste inchangé
	}

	public Map<String, ArrayList<Integer>> attaquerTerritoire(Territoire tAttaquant, Territoire tAttaquee)
			throws InvalidMoveException {
		/*
		 * Vérifications =============
		 */

		// tAttaquant n'appartient pas à ce joueur
		if (tAttaquant.getJoueur().getId() != id) {
			throw new InvalidMoveException(String.format("Terr. %d ne vous appartient pas !", tAttaquant.getId()));
		}

		// tAttaquee est déjà à ce joueur
		if (tAttaquee.getJoueur().getId() == id) {
			throw new InvalidMoveException(String.format("Terr. %d vous appartient déjà", tAttaquee.getId()));
		}

		// tAttaquant a plus de 1 dés
		if (tAttaquant.getForce() <= 1) {
			throw new InvalidMoveException(String.format("Ne peut étendre Terr. %d (1 dé)", tAttaquant.getId()));
		}

		// tAttaquee est bien voisin de tAttaquant
		if (!tAttaquant.getVoisins().contains(tAttaquee.getId())) {
			throw new InvalidMoveException(
					String.format("Terr. %d n'est pas voisin de Terr. %d", tAttaquee.getId(), tAttaquant.getId()));
		}

		/*
		 * Attaque =======
		 */
		// lancer dé attaquant
		int nbDeAttaquant = tAttaquant.getForce();
		ArrayList<Integer> desAttaquant = Joueur.lanceDe(nbDeAttaquant);

		// lancer dé attaquee
		int nbDeAttaquee = tAttaquee.getForce();
		ArrayList<Integer> desAttaquee = Joueur.lanceDe(nbDeAttaquee);

		/*
		 * Résultat attaque ================
		 */
		if (Joueur.sommeDe(desAttaquant) > Joueur.sommeDe(desAttaquee)) {
			// Victoire
			victoireAttaque(tAttaquant, tAttaquee);
		} else {
			// Défaite
			defaiteAttaque(tAttaquant);
		}

		// retourner les dés de l'attaque
		Map<String, ArrayList<Integer>> des = new HashMap<>();
		des.put("attaquant", desAttaquant);
		des.put("attaquee", desAttaquee);
		return Collections.unmodifiableMap(des);
	}

	public void terminerTour() {

	}

}
