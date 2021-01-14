package core;

import java.awt.Color;
import java.io.Serializable;
import java.util.*;

public class Territoire implements Serializable {
	private static final long serialVersionUID = -7293972875499218379L;
	
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
	
	public static int maxTContigu(List<Territoire> listeTerritoire) {
		int maxcontigu = 0;
		for (Territoire territoire : listeTerritoire) {
			// initialisation
			int count = 0;
			ArrayList<Territoire> contigu = new ArrayList<>();
			ArrayList<Long> traite = new ArrayList<>();
			contigu.add(territoire);
			// parcours des territoire comme pour les graphes
			while (!contigu.isEmpty()) {
				// on regarde pour chaque voisin du territoire en cours si ils appartienent au
				// joueur
				for (Long Voisin_id : contigu.get(0).getVoisins()) {
					// cas dans lequel le territoire à déja été comptabilisé comme voisin
					if (traite.contains(Voisin_id)) {
						continue;
					}
					for (Territoire possede : listeTerritoire) {
						if (Voisin_id == possede.getId() && !contigu.contains(possede)) {
							contigu.add(possede);
						}
					}
				}
				// note le territoire comme traité
				traite.add(contigu.remove(0).getId());
				count++;
			}
			// fin d'itération on update le max
			if (count > maxcontigu) {
				maxcontigu = count;
			}
			// condition pour gagner du temps, inutile de continuer si un territoire
			// suffisement grand à été trouvé
			if (maxcontigu >= (int) (listeTerritoire.size() / 2) + 1) {
				break;
			}
		}
		return maxcontigu;
	}
	
}
