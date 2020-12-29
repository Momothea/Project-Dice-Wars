package core;

import java.awt.Color;
import java.util.*;

public class Territoire {
	private long id;
	private Joueur joueur;
	private int force;
	private Set<Long> voisins = new HashSet<>();
	
	private boolean isSelected = false;

	public Territoire(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Joueur getJoueur() {
		return joueur; // rendre plus secure
	}

	public void setJoueur(Joueur owner) {
		this.joueur = owner;
	}

	public Color getCouleurJoueur() {
		return joueur != null ? joueur.getCouleur() : Color.BLACK;
	}

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public Set<Long> getVoisins() {
		return voisins; // rendre plus secure
	}

	public void addVoisins(Long voisinId) {
		voisins.add(voisinId);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
