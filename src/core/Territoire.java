package core;

import java.util.*;

public class Territoire {
	private long id;
	private long idJoueur;
	private int force;
	private Set<Integer> voisins = new HashSet<>();
	
	
	public Territoire(long id) {
		this.id = id;
	}
	
	
}
