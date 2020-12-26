package core;

import java.awt.Color;
import java.util.*;

public class Territoire {
	private long id;
	private long idJoueur;
	private Color couleurJoueur;
	private int force;
	private Set<Integer> voisins = new HashSet<>();
	
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

	public long getIdJoueur() {
		return idJoueur;
	}

	public void setIdJoueur(long idJoueur) {
		this.idJoueur = idJoueur;
	}

	public Color getCouleurJoueur() {
		return couleurJoueur;
	}

	public void setCouleurJoueur(Color couleurJoueur) {
		this.couleurJoueur = couleurJoueur;
	}

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public Set<Integer> getVoisins() {
		return voisins;
	}

	public void setVoisins(Set<Integer> voisins) {
		this.voisins = voisins;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
