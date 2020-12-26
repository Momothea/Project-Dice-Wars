package core;

import java.awt.Color;
import java.util.*;

public class Joueur {
	private long id;
	private Color couleur;
	private Set<Territoire> listeTerritoire = new HashSet<>();
	
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

	
	
	public void attaquerTerritoire(Territoire tAttaquant, Territoire tAtaquee) {
		
	}
	
	public void terminerTour() {
		
	}
	
}
