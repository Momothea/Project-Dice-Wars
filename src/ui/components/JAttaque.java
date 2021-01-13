package ui.components;

import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import core.Joueur;
import core.Territoire;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.io.Serializable;
import javax.swing.SwingConstants;

public class JAttaque extends JPanel implements Serializable {
	private static final long serialVersionUID = -8978592864734150762L;

	private final static String cardAttaqueID = "attaque Joueur panel";
	private final static String cardErrorID = "error panel";

	private JPanel cardAttaque;
	private JPanel cardError;

	private JLabel lblErreur;
	private JLabel lblAttaquant = new JLabel();
	private JLabel lblAttaque = new JLabel();

	private JPanel dicesAttaquant = new JPanel();
	private JPanel dicesAttaque = new JPanel();

	/**
	 * Create the panel.
	 */
	public JAttaque() {
		setLayout(new CardLayout());
		setBorder(new EmptyBorder(0, 20, 0, 20));

		/*
		 * UI Attaque ==========
		 */
		cardAttaque = new JPanel(new GridLayout(1, 2, 15, 0));

		// Partie attaquant
		JPanel panelAttaquant = new JPanel(new BorderLayout());
		JLabel label = new JLabel("<html>" + "<h3 style='margin: 0.5em 0'>Terr. Attaquant</h3>" + "</html>");
		label.setHorizontalAlignment(SwingConstants.TRAILING); // aligner cet élément à droite
		panelAttaquant.add(label, BorderLayout.NORTH);
		// Aligner les dés à gauche
		FlowLayout flowLayout = (FlowLayout) dicesAttaquant.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		panelAttaquant.add(dicesAttaquant, BorderLayout.CENTER);
		// détail territoire
		panelAttaquant.add(lblAttaquant, BorderLayout.SOUTH);
		cardAttaque.add(panelAttaquant);

		// Partie attaque
		JPanel panelAttaquee = new JPanel(new BorderLayout());
		panelAttaquee.add(new JLabel("<html>" + "<h3 style='margin: 0.5em 0'>Terr. Attaqué</h3>" + "</html>"),
				BorderLayout.NORTH);
		// Aligné les dés à gauche
		FlowLayout flowLayout_1 = (FlowLayout) dicesAttaque.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEADING);
		panelAttaquee.add(dicesAttaque, BorderLayout.CENTER);
		// détail territoire
		panelAttaquee.add(lblAttaque, BorderLayout.SOUTH);
		cardAttaque.add(panelAttaquee);

		/*
		 * UI Erreur =========
		 */
		cardError = new JPanel();
		lblErreur = new JLabel(
				"<html>" + "<h2 style='margin: 0.5em 0'>Aucun territoire à attaquer n'a été sélectionné</h2>"
						+ "<ul style='margin-top: 5px'>" + "<li>Cliquer sur le territoire à étendre</li>"
						+ "<li>Cliquer ensuite sur le territoire à attaquer</li>" + "</ul>" + "</html>");
		cardError.add(lblErreur);

		// Mettre ces composant dans la cardLayout
		add(cardAttaque, cardAttaqueID);
		add(cardError, cardErrorID);

		// afficher Error
		((CardLayout) this.getLayout()).show(this, cardErrorID);
	}

	public void showAttaque(Territoire terrAttaquant, Territoire terrAttaque) {

		// Set texte information territoire
		lblAttaquant.setText(String.format("<html>"
				+ "<p style='color: green'><strong>Terr. %d</strong> (Joueur %d)</p>" + "</html>",
				terrAttaquant.getId(), terrAttaquant.getJoueur().getId()));
		lblAttaquant.setHorizontalAlignment(SwingConstants.TRAILING); // aligner cet élément à droite
		lblAttaque.setText(String.format("<html>"
				+ "<p style='color: green'><strong>Terr. %d</strong> (Joueur %d)</p>" + "</html>",
				terrAttaque.getId(), terrAttaque.getJoueur().getId()));

		/*
		 * Ajouter les dés des joueurs ===========================
		 */

		// retirer les dés déjà présent
		dicesAttaquant.removeAll();
		dicesAttaque.removeAll();

		// ajout dé attaquant
		int nbDeAttaquant = terrAttaquant.getForce();
		Color colorAttaquant = terrAttaquant.getCouleurJoueur();
		for (int i = 0; i < nbDeAttaquant; i++) {
			dicesAttaquant.add(new JDice(i, colorAttaquant));
		}

		// ajout dé attaqué
		int nbDeAttaque = terrAttaque.getForce();
		Color colorAttaque = terrAttaque.getCouleurJoueur();
		for (int i = 0; i < nbDeAttaque; i++) {
			dicesAttaque.add(new JDice(i, colorAttaque));
		}

		// maj affichage dés
		dicesAttaquant.invalidate();
		dicesAttaque.invalidate();

		// afficher Attaque
		((CardLayout) this.getLayout()).show(this, cardAttaqueID);
	}

	public static void main(String[] args) {
		Joueur j1 = new Joueur(1);
		j1.setCouleur(Color.BLUE);
		Joueur j2 = new Joueur(2);
		j2.setCouleur(Color.RED);

		Territoire t1 = new Territoire(1);
		t1.setJoueur(j1);
		Territoire t2 = new Territoire(2);
		t2.setJoueur(j2);

		JAttaque testAttaque = new JAttaque();
		testAttaque.showAttaque(t1, t2);
		JFrame frame = new JFrame();
		frame.setTitle("Test Attaque");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(testAttaque);
		frame.pack();
		frame.setVisible(true);
	}

}
