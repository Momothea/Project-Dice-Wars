package ui.components;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import core.Joueur;
import core.Territoire;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;

public class JAttaque extends JPanel {
	private static final long serialVersionUID = -6452048116001459318L;
	
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

	public void showError(String msg) {
		lblErreur.setText(String.format("<html>"
				+ "<h2 style='margin: 0.5em 0; text-align: center; color: red'>Mouvement invalide</h2>"
				+ "<p style='text-align: center; color: red'>%s</p>"
				+ "<p style='text-align: center'>Attaquer un autre territoire ou terminer votre tour</p>" + "</html>",
				msg));

		// afficher Error
		((CardLayout) this.getLayout()).show(this, cardErrorID);
	}

	public void showAttaque(Territoire terrAttaquant, Territoire terrAttaque,
			Map<String, ArrayList<Integer>> desAttaque) {
		ArrayList<Integer> desAttaquant = desAttaque.get("attaquant");
		ArrayList<Integer> desAttaquee = desAttaque.get("attaquee");

		// Set texte information territoire
		lblAttaquant.setText(String.format(
				"<html>" + "<p style='color: green'><strong>Terr. %d</strong> (Joueur %d, %d)</p>" + "</html>",
				terrAttaquant.getId(), terrAttaquant.getJoueur().getId(), Joueur.sommeDe(desAttaquant)));
		lblAttaquant.setHorizontalAlignment(SwingConstants.TRAILING); // aligner cet élément à droite
		lblAttaque.setText(String.format(
				"<html>" + "<p style='color: green'><strong>Terr. %d</strong> (Joueur %d, %d)</p>" + "</html>",
				terrAttaque.getId(), terrAttaque.getJoueur().getId(), Joueur.sommeDe(desAttaquee)));

		/*
		 * Ajouter les dés des joueurs ===========================
		 */

		// retirer les affichages des dés déjà présent
		dicesAttaquant.removeAll();
		dicesAttaque.removeAll();

		// ajout dé attaquant
		Color colorAttaquant = terrAttaquant.getCouleurJoueur();
		for (int i = 0; i < desAttaquant.size(); i++) {
			dicesAttaquant.add(new JDice((int) desAttaquant.get(i), colorAttaquant));
		}

		// ajout dé attaqué
		Color colorAttaque = terrAttaque.getCouleurJoueur();
		for (int i = 0; i < desAttaquee.size(); i++) {
			dicesAttaque.add(new JDice((int) desAttaquee.get(i), colorAttaque));
		}

		// maj affichage dés
		dicesAttaquant.invalidate();
		dicesAttaque.invalidate();

		// afficher Attaque
		((CardLayout) this.getLayout()).show(this, cardAttaqueID);
	}

}
