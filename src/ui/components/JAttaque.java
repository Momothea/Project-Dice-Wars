package ui.components;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import core.Joueur;
import core.Territoire;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JAttaque extends JPanel {
	private static final long serialVersionUID = -8978592864734150762L;
	private Territoire terrAttaquant;
	private Territoire terrAttaque;
	
	private JPanel panelAttaque;
	private JLabel lblErreur;
	private JLabel lblAttaquant = new JLabel();
	private JLabel lblAttaque = new JLabel();

	/**
	 * Create the panel.
	 */
	public JAttaque() {
		/*
		 * UI Attaque
		 * ==========
		 */
		panelAttaque = new JPanel(new GridLayout(1, 2, 0, 0));

		// Partie attaquant
		Box panelAttaquant = new Box(BoxLayout.Y_AXIS);
		panelAttaque.add(panelAttaquant);
		panelAttaquant.add(lblAttaquant);

		// Partie attaque
		Box panelAttaquee = new Box(BoxLayout.Y_AXIS);
		panelAttaque.add(panelAttaquee);
		panelAttaquee.add(lblAttaque);
		
		/*
		 * UI Erreur
		 * =========
		 */
		lblErreur = new JLabel("<html>"
				+ "<h2 style='margin: 0.5em 0'>Aucun territoire à attaquer n'a été sélectionner</h2>"
				+ "<ul style='margin-top: 5px'>"
				+ "<li>Cliquer sur le territoire à étendre</li>"
				+ "<li>Cliquer ensuite sur le territoire à attaquer</li>"
				+ "</ul>"
				+ "</html>");
		
		// initialiser ce composant à error
		add(lblErreur);

	}
	
	public void showAttaque(Territoire terrAttaquant, Territoire terrAttaque) {
		this.terrAttaquant = terrAttaquant;
		this.terrAttaque = terrAttaque;
		
		// Set texte information territoire
		lblAttaquant.setText(String.format(
				"<html>" + "<h3 style='margin: 0.5em 0'>Terr. Attaquant</h3>"
						+ "<p style='color: green'><strong>Terr. %d</strong> (Joueur %d)</p>" + "</html>",
				terrAttaquant.getId(), terrAttaquant.getJoueur().getId()));
		lblAttaque.setText(String.format(
				"<html>" + "<h3 style='margin: 0.5em 0'>Terr. Attaqué</h3>"
						+ "<p style='color: green'><strong>Terr. %d</strong> (Joueur %d)</p>" + "</html>",
				terrAttaque.getId(), terrAttaque.getJoueur().getId()));
	}
	
	
	public static void main(String[] args) {
		/*Joueur j1 = new Joueur(1);
		j1.setCouleur(Color.BLUE);
		Joueur j2 = new Joueur(2);
		j2.setCouleur(Color.RED);
		
		Territoire t1 = new Territoire(1);
		t1.setJoueur(j1);
		Territoire t2 = new Territoire(2);
		t2.setJoueur(j2);*/
		
		JAttaque testAttaque = new JAttaque();
		JFrame frame = new JFrame();
		frame.setTitle("Test Attaque");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(testAttaque);
		frame.pack();
		frame.setVisible(true);
	}

}
