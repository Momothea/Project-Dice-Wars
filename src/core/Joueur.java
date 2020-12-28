package core;

import java.awt.Color;
import java.util.*;

public class Joueur {
	private long id;
	private Color couleur;
	private Set<Territoire> listeTerritoire = new HashSet<>();
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

	public Set<Territoire> getListeTerritoire() {
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
		return String.format("<html>"
				+ "<h3 style='margin: 0.5em 0'>Joueur %d</h3>"
				+ "<p style='padding-left: 10px'>Nb terr: %d<small>/%d</small> (%d %%)</p>"
				+ "</html>", 
				id, listeTerritoire.size(), nbTerritoire, (listeTerritoire.size()*100) / nbTerritoire);
	}

	public void attaquerTerritoire(Territoire tAttaquant, Territoire tAtaquee) {

	}

	public void terminerTour() {

	}

}
