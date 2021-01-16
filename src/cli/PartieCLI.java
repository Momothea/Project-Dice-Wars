package cli;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import core.InvalidMoveException;
import core.Joueur;
import core.Partie;
import core.Territoire;
import ui.components.JHelp;

public class PartieCLI {
	private Scanner sc = new Scanner(System.in);
	private Partie partie;

	private int menu() {
		boolean error = false;
		int result = 0;

		do {
			System.out.println(ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_BOLD
					+ "Nouvelle partie...          " + ConsoleColors.RESET + "   3) Charger une partie\n"
					+ "  1) sur une nouvelle carte    4) Règles du jeu\n"
					+ "  2) sur une carte existante   5) Quitter");
			System.out.print(ConsoleColors.RESET + ">> " + ConsoleColors.GREEN);

			try {
				result = Integer.parseInt(sc.nextLine());
				error = result < 1 || result > 5;
			} catch (Exception e) {
				System.err.println("Votre saisie EST incorrecte !");
				error = true;
			}
		} while (error);

		return result;
	}

	public void runCLI() {
		int choix = 0;

		do {
			choix = menu();

			switch (choix) {
			case 1:
				// Nouvelle partie

				break;
			case 2:
				// New sur carte existante
				break;

			case 3:
				// Charger partie
				break;

			case 4:
				// Règles du jeu
				new JHelp(new Rectangle(0, 0, 0, 480));
				break;

			default:
				break;
			}
		} while (choix != 5);

		System.out.println("A bientot");
	}

	public static void main(String[] args) {
		// http://www.patorjk.com/software/taag/#p=display&f=Doom&t=Dice%20Wars
		System.out.println(ConsoleColors.RESET + "______ _            _    _                \n"
				+ "|  _  (_)          | |  | |               \n" + "| | | |_  ___ ___  | |  | | __ _ _ __ ___ \n"
				+ "| | | | |/ __/ _ \\ | |/\\| |/ _` | '__/ __|\n" + "| |/ /| | (_|  __/ \\  /\\  / (_| | |  \\__ \\\n"
				+ "|___/ |_|\\___\\___|  \\/  \\/ \\__,_|_|  |___/\n" + "Projet Java Efrei L3, Nil Paul Moise     \n");

		PartieCLI cli = new PartieCLI();
		// run partie
		cli.runCLI();

	}

	/*
	 * VIEWER
	 */
	public void carteViewer() {
		String BOLD = "\033[1";

		System.out.println(partie.getCarte().toString());
		// afficher liste joueur coloré
		System.out.print(BOLD);

		Joueur[] joueurs = partie.getJoueurs();
		for (int i = 0; i < joueurs.length; i++) {
			System.out.printf("Joueur %d ", joueurs[i]);
		}
		System.out.println(ConsoleColors.RESET);
	}

	/*
	 * RUNNER
	 */
	public void partieRunner() {
		while (partie.getjAttaquant() == null) {
			// lancer les attaques
			attaqueRunner();
			// Si joueur a fini attaque, lancer fin partie
			partie.getjAttaquant().terminerTour();

		}

		// arrivé ici, on a un gagnant
		System.out.printf("Victoire de %d\n", partie.getjGagnant());
	}

	public void attaqueRunner() {
		Joueur attaquant = partie.getjAttaquant();

		boolean error = false;
		boolean continuer = true;
		do {
			carteViewer();
			// joueur prompt
			System.out.print(ConsoleColors.ColorToASCII(attaquant.getCouleur()) + "Joueur" + attaquant.getId() + ">> "
					+ ConsoleColors.RESET);
			String ordre = sc.nextLine();

			// tokenise entrée utilisateur
			switch (ordre) {
			case "help":
				// aff mini help
				System.out.println(ConsoleColors.WHITE_BACKGROUND_BRIGHT + ConsoleColors.BLACK_BOLD
						+ ConsoleColors.BLACK_UNDERLINED + "Actions disponibles                               \n"
						+ ConsoleColors.RESET + ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_BOLD
						+ "Attaquer           :" + ConsoleColors.BLACK + " <id tAttaquant> <idtAttaquer>\n"
						+ ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_BOLD + "Sauvegarder partie :"
						+ ConsoleColors.BLACK + " save\n" + ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_BOLD
						+ "Fin tour           :" + ConsoleColors.BLACK + " end\n");
				break;

			case "save":
				// sauver partie
				String filename = sc.nextLine();
				try {
					partie = new Partie(filename);
				} catch (ClassNotFoundException | IOException e) {
					System.err.println(e.getMessage());
				}
				break;

			case "end":
				continuer = false;
				break;

			default:
				String[] tOrdreAttaque = ordre.split(" ");
				if (tOrdreAttaque.length == 2) {
					// recherche tAttaquant
					try {
						Long tAttaquantID = Long.valueOf(tOrdreAttaque[0]);
						Long tAttaqueeID = Long.valueOf(tOrdreAttaque[1]);

						// Serch territoire
						List<Territoire> territoires = partie.getCarte().getTerritoires();
						Territoire tAttaquant = territoires.stream().filter(t -> t.getId() == tAttaquantID).findFirst()
								.orElse(null);
						Territoire tAttaquee = territoires.stream().filter(t -> t.getId() == tAttaqueeID).findFirst()
								.orElse(null);

						// lancer une erreur si l'utilisateur met un id invalide
						if (tAttaquant == null || tAttaquee == null) {
							throw new InvalidMoveException("ID des territoires à attaquer invalide");
						}

						// attaque
						// TODO affichage des dés
						Map<String, ArrayList<Integer>> des = partie.getjAttaquant().attaquerTerritoire(tAttaquant,
								tAttaquee);

						error = false;
					} catch (Exception e) {
						System.err.println(e.getMessage());
						error = true;
					}
				} else {
					// l'utilisateur n'a pas regarder la mini help
					System.err.println("Saisie invalide.\nEntrer help pour plus d'information");
					error = true;
				}
			}

		} while (error || continuer);
	}

}
