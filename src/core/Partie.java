package core;

import java.awt.Color;
import java.io.*;

public class Partie implements Serializable {
	private static final long serialVersionUID = -1377212323714822327L;
	
	private Joueur[] joueurs;
	private Carte carte;

	private int nbTour = 0;
	private Joueur jAttaquant; // joueur qui doit jouer
	
	public Partie(int nbJoueurs) {
		Color[] colors = { Color.GREEN, Color.BLUE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.RED, Color.YELLOW, Color.WHITE };
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

	public int getNbTour() {
		return nbTour;
	}

	public Joueur getjAttaquant() {
		return jAttaquant;
	}

	public void setjAttaquant(Joueur jAttaquant) {
		this.jAttaquant = jAttaquant;
	}
	
	/*
	 * SERIALISATION
	 * =============
	 */

	public void serialize(String filename) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = null;
		try {
			// créer le fichier et écrire la partie
			final FileOutputStream fichier = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(this);
			oos.flush();
		}
		catch(FileNotFoundException e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
		catch(IOException e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
		finally {
			try {
				if(oos!= null){
					oos.flush(); // tu n'a pas déjà plus en haut
					oos.close();
				}
			}   catch(final IOException e) {
				e.printStackTrace();
			}
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
		} 
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		finally {
				ois.close();
				fis.close();
		}	

	}
	
}
