package ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import core.Carte;
import core.Joueur;
import core.Territoire;

public class JCarte extends JPanel {
	// https://stackoverflow.com/questions/44012684/how-to-make-hexagonal-jbuttons
	// REMOVE "magic" number from StackOverflow answer

	private static final long serialVersionUID = 5558698877882870989L;
	private final static int ROWS = 33;
	private final static int COLUMNS = 33;

	private int t_width = 24;
	private int t_height = 24;

	private JTerritoire[][] carteTerritoires = new JTerritoire[ROWS][COLUMNS];
	private Territoire tAttaquant;
	
	public JCarte(Carte carte) {
		setLayout(null);
		setPreferredSize(new Dimension((int) Math.round(t_width * (COLUMNS + (Math.sqrt(3)/4))), (int) Math.round( t_height*(0.75*(ROWS -1) + 1) )));
		initGUI(carte);
	}

	public void initGUI(Carte carte) {
		Territoire[][] matCarte = carte.getCarte();

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				if (matCarte[row][col] != null) {
					carteTerritoires[row][col] = new JTerritoire(matCarte[row][col], carte.getTerrainsVoisins(row, col));
					carteTerritoires[row][col].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JTerritoire clickedButton = (JTerritoire) e.getSource();
							// clickedButton.setSelected(!clickedButton.isSelected());
							joueurAttaque(clickedButton.getTerritoire());
						}
					});
					add(carteTerritoires[row][col]);
				}
			}
		}
	}

	
	public void joueurAttaque(Territoire terrSelectionne) {
		// force repaint
		repaint();
		
		if (tAttaquant == null) {
			tAttaquant = terrSelectionne;
			tAttaquant.setSelected(true);
		} else {
			System.out.printf("Attaque Terr %d contre Terr %d\n", tAttaquant.getId(), terrSelectionne.getId());
			terrSelectionne.setSelected(true);
			// TODO lancer fct attaque ici !!!
			
			// remettre à 0 les territoires selectionnes
			tAttaquant.setSelected(false);
			terrSelectionne.setSelected(false);
			tAttaquant = null;
		}
	}
	

	@Override
	// Responsiveness de la JCarte
	public void paint(Graphics g) {
		int def_t_width = (int) Math.floor(getWidth() / (COLUMNS + (Math.sqrt(3)/4)));
		int def_t_height = (int) Math.floor(getHeight() / (0.75*(ROWS -1) + 1));
		
		// final width et height des JTerritoire (Rendre carré les JTerritoire)
		t_width = Math.min(def_t_width, def_t_height);
		t_height = t_width;
		
		// centrage JCarte
		int offsetY = (getHeight() - (int) Math.round( t_height*(0.75*(ROWS -1) + 1) )) / 2;
		int centeredOffsetX = (getWidth() - (int) Math.round(t_width * (COLUMNS + (Math.sqrt(3)/4)))) / 2; 
		
		// Mettre les JTerritoire à la même taille
		for (int row = 0; row < ROWS; row++)  {
			int offsetX = (row % 2 == 0 ? centeredOffsetX : centeredOffsetX + (int) Math.round(t_width / 2)) + (int) Math.round(0.125 * t_width);
			for (int col = 0; col < COLUMNS; col++) {
				if (carteTerritoires[row][col] != null) {
					carteTerritoires[row][col].setBounds(offsetX, offsetY, t_width, t_height);
				}
				offsetX += t_width;
			}
			offsetY += (int) (t_height * 0.75);
		}
		
		super.paint(g);
	}

	public static void main(String[] args) {
		int nbJoueurs = 7;
		Color[] colors = { Color.GREEN, Color.BLUE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.RED, Color.YELLOW };
		Joueur[] joueurs = new Joueur[nbJoueurs];
		System.out.println("Création joueurs...");
		for (int i = 0; i < nbJoueurs; i++) {
			Joueur newJoueur = new Joueur(i + 1, colors[i]);
			joueurs[i] = newJoueur;
		}
		System.out.println("Création carte...");
		Carte map = new Carte(joueurs);
		System.out.println(map);

		JCarte hexPattern = new JCarte(map);
		JFrame frame = new JFrame();
		frame.setTitle("Hexagon Pattern");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(hexPattern);
		frame.pack();
		frame.setVisible(true);
	}
}
