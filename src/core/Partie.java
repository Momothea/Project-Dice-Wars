package core;

import java.awt.Color;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Partie implements Serializable {
	private static final long serialVersionUID = -1377212323714822327L;
	public static final Color[] colors = { Color.GREEN, Color.BLUE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.RED,
			Color.YELLOW, Color.WHITE };

	private Joueur[] joueurs;
	private Carte carte;

	private int nbTour = 0;
	private Joueur jAttaquant; // joueur qui doit jouer
	private Joueur gagnant;

	public Partie(int nbJoueurs) {
		this.joueurs = new Joueur[nbJoueurs];
		this.gagnant = null;

		// Création joueur
		for (int i = 0; i < nbJoueurs; i++) {
			Joueur newJoueur = new Joueur(i + 1, colors[i]);
			joueurs[i] = newJoueur;
		}

		// Création et initialisation de la carte
		carte = new Carte(joueurs);

		// set tour du joueur qui aura l'honneur de démarrer les hostilités
		setTourJoueur();

	}

	/*
	 * structure du fichier csv : 
	 * ligne 1 : nbjoueurs nbterritoire 
	 * ligne 2 : force des terriroire dans l'ordre 
	 * ligne 3 : propriétaire des territoires dans l'ordre 
	 * ligne suivante 33*33 : carte du jeu 0 si vide id du territoire sinon
	 */
	public Partie(File csvFile) throws FileNotFoundException {
		this.gagnant = null;
		Scanner mapscan = new Scanner(csvFile);
		String[] info = mapscan.nextLine().split(";");
		int nbJoueurs = Integer.parseInt(info[0]);
		int nbTerritoires = Integer.parseInt(info[1]);

		this.joueurs = new Joueur[nbJoueurs];

		for (int i = 0; i < nbJoueurs; i++) {
			Joueur newJoueur = new Joueur(i + 1, colors[i]);
			joueurs[i] = newJoueur;
		}

		this.carte = new Carte(nbJoueurs, nbTerritoires, joueurs, mapscan);

		setTourJoueur();
	}

	public Joueur[] getJoueurs() {
		return joueurs; // rendre plus secure
	}

	public Carte getCarte() {
		return carte; // rendre plus secure
	}

	public int getNbTour() {
		return nbTour;
	}

	public Joueur getjAttaquant() {
		return jAttaquant;
	}

	public Joueur getjGagnant() {
		return gagnant;
	}

	public void setTourJoueur() {
		// passer au tour suivant
		do {
			Random aleat = new Random();
			int iJoueur = aleat.nextInt(joueurs.length);
			this.jAttaquant = joueurs[iJoueur];
		} while (jAttaquant == null);

		nbTour++;
	}

	public void updatePartie() {
		// supprimer les joueurs n'ayant plus de territoire
		List<Joueur> perdants = Arrays.stream(joueurs).filter(j -> (j == null ? -1 : j.getNbListeTerritoire()) == 0)
				.collect(Collectors.toList());

		for (int i = 0; i < perdants.size(); i++) {
			joueurs[Arrays.asList(joueurs).indexOf(perdants.get(i))] = null;
			System.out.printf("Joueur %d retiré du jeu\n", perdants.get(i).getId());
		}

		// determiné le gagnant
		gagnant = Arrays.stream(joueurs)
				.filter(j -> (j == null ? -1 : j.getNbListeTerritoire()) == Joueur.getNbTerritoireCarte()).findFirst()
				.orElse(null);

		if (gagnant != null)
			System.out.printf("Gagnant : Joueur %d\n", gagnant.getId());
	}

	/*
	 * SERIALISATION =============
	 */

	public void serialize(String filename) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = null;
		FileOutputStream fichier = null;
		try {
			// créer le fichier et écrire la partie
			fichier = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(this);
			oos.flush();
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (IOException e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		} finally {
			fichier.close();
			oos.close();

		}

	}

	// deserialisation
	public Partie(String filename) throws ClassNotFoundException, IOException {
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			// lecture fichier et lecture objet
			fis = new FileInputStream(filename);
			ois = new ObjectInputStream(fis);

			Partie resultat = (Partie) ois.readObject();

			// import objet
			this.carte = resultat.carte;
			this.jAttaquant = resultat.jAttaquant;
			this.joueurs = resultat.joueurs;
			this.nbTour = resultat.nbTour;

			Joueur.setNbTerritoireCarte(carte.getNbTerritoireCarte());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			ois.close();
			fis.close();
		}

	}

}
