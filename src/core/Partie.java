package core;

import java.awt.Color;

public class Partie {
	private Joueur[] joueurs;
	private Carte carte;
	
	public Partie(int nbJoueurs) {
		Color[] colors = { Color.GREEN, Color.BLUE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.RED, Color.YELLOW };
		this.joueurs = new Joueur[nbJoueurs];
		
		// Création joueur
		for (int i = 0; i < nbJoueurs; i++) {
			Joueur newJoueur = new Joueur(i + 1, colors[i]);
			joueurs[i] = newJoueur;
		}
		
		// Création et initialisation de la carte
		carte = new Carte(joueurs);
		
	}

	public Joueur[] getJoueurs() {
		return joueurs; // rendre plus secure
	}

	public Carte getCarte() {
		return carte; // rendre plus secure
	}
	
	
}
