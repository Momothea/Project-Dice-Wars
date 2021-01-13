package core;

import java.awt.Color;
import java.io.*;

public class Partie implements Serializable {
	private Joueur[] joueurs;
	private Carte carte;

	private int nbTour = 0;
	private int iJoueurTour = 0; // indice du joueur qui doit jouer
	
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

	public int getNbTour() {
		return nbTour;
	}

	public int getiJoueurTour() {
		return iJoueurTour;
	}
	public void serializePartie(Partie partie) throws FileNotFoundException, IOException {
		ObjectOutputStream oos = null;
		try {
			final FileOutputStream fichier = new FileOutputStream("partie.ser");
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(partie);
			oos.flush();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally {
			try{
				if(oos!= null){
					oos.flush();
					oos.close();
				}
			}   catch( final IOException e){
				e.printStackTrace();
			}
		}

	}


	public Partie desriliaze(String partie) throws ClassNotFoundException, IOException{
		FileInputStream fis = new FileInputStream(partie);
		try{
			ObjectInputStream ois = new ObjectInputStream(fis);
			try{
				Partie resultat = (Partie) ois.readObject();
				return resultat;
			} finally {
				ois.close();
			}
		}finally {
			fis.close();
		}

	}
	
	
}
