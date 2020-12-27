package core;

import java.awt.Color;
import java.util.ArrayList;

public class Partie {
	private ArrayList<Joueur> joueurs;
	private Carte carte;
	
	public Partie(int nbJoueurs) {
		Color[] colors = { Color.GREEN, Color.BLUE, Color.PINK, Color.ORANGE, Color.MAGENTA, Color.RED, Color.YELLOW };
		this.joueurs = new ArrayList<>();
		
		// Création joueur
		for (int i = 0; i < nbJoueurs; i++) {
			Joueur newJoueur = new Joueur(i + 1, colors[i]);
			joueurs.add(newJoueur);
		}
		
		// Création et initialisation de la carte
		carte = new Carte(joueurs);
		
	}

	public ArrayList<Joueur> getJoueurs() {
		return joueurs; // rendre plus secure
	}

	public Carte getCarte() {
		return carte; // rendre plus secure
	}
	
	
}
