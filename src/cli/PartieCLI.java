package cli;

import java.awt.Color;
import java.awt.Rectangle;
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
				System.out.println(ConsoleColors.RESET + "Nombre de joueurs ? :");
				String eNbJoueur = sc.nextLine();
				int nbJoueur = Integer.parseInt(eNbJoueur);
				if (nbJoueur > 1 && nbJoueur < 9) {
					partie = new Partie(nbJoueur);
					partieRunner();
				}

				break;
			case 2:
				// New sur carte existante
				break;

			case 3:
				// Charger partie
				System.out.print("Charger partie de (chemin + nomFichier) : ");
				String filename = sc.nextLine();
				if (!filename.endsWith("ser")) {
					filename += ".ser";
				}
				try {
					partie = new Partie(filename);
				} catch (Exception e) {
					System.err.println(e.getMessage() + '\n');
				}

				partieRunner();
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

	/*
	 * VIEWER
	 */
	private void carteViewer() {
		System.out.println(partie.getCarte().toString());

		Joueur[] joueurs = partie.getJoueurs();
		for (int i = 0; i < joueurs.length; i++) {
			if (joueurs[i] != null)
				System.out.print(ConsoleColors.colorToASCIIBold(joueurs[i].getCouleur()) + joueurs[i] + " ");
		}
		System.out.println(ConsoleColors.RESET);
	}

	private void diceViewer(ArrayList<Integer> des, Color color) {
		String[] deUnicode = { "\u2680", "\u2681", "\u2682", "\u2683", "\u2684", "\u2685" };

		System.out.print(ConsoleColors.colorToASCIIBold(color));
		for (Integer de : des) {
			try {
				System.out.print(deUnicode[de - 1] + "");
			} catch (Exception e) {
				System.out.print("? ");
			}
		}
		System.out.print(ConsoleColors.RESET);
	}

	private void attaqueViewer(Map<String, ArrayList<Integer>> desAttaque, Territoire tAttaquant,
			Territoire tAttaquee) {
		ArrayList<Integer> desAttaquant = desAttaque.get("attaquant");
		ArrayList<Integer> desAttaquee = desAttaque.get("attaquee");

		diceViewer(desAttaquant, partie.getjAttaquant().getCouleur());
		System.out.printf(ConsoleColors.RESET + ConsoleColors.GREEN + " Terr. %d (Joueur %d, %d) ", tAttaquant.getId(),
				tAttaquant.getJoueur().getId(), Joueur.sommeDe(desAttaquant));

		diceViewer(desAttaquee, tAttaquee.getCouleurJoueur());
		System.out.printf(ConsoleColors.RESET + ConsoleColors.GREEN + " Terr. %d (Joueur %d, %d)\n", tAttaquee.getId(),
				tAttaquee.getJoueur().getId(), Joueur.sommeDe(desAttaquee));

		if (Joueur.sommeDe(desAttaquant) > Joueur.sommeDe(desAttaquee)) {
			System.out.println("=========== VICTOIRE ===========\n");
		} else {
			System.out.println(ConsoleColors.RED + "=========== DEFAITE  ===========\n");
		}

		System.out.println(ConsoleColors.RESET);

	}

	/*
	 * RUNNER
	 */
	public void partieRunner() {
		boolean arret = false;

		while (!arret) {
			// lancer les attaques
			arret = attaqueRunner();
			if (!arret) {
				// Si joueur a fini attaque, lancer fin tour
				partie.getjAttaquant().terminerTour();
				partie.setTourJoueur();
				partie.updatePartie();
			}

		}

		// arrivé ici, on a un gagnant
		System.out.printf(
				ConsoleColors.GREEN_BACKGROUND 
				+ "\n===========================================\n"
				  + "========== Victoire de Joueur %d ===========\n" 
				  + "===========================================\n\n\n",
				partie.getjGagnant().getId());
	}

	/*
	 * return true si exit
	 */
	public boolean attaqueRunner() {
		Joueur attaquant = partie.getjAttaquant();

		boolean error = false;
		boolean continuer = true;

		do {
			carteViewer();
			// joueur prompt
			System.out.print(ConsoleColors.colorToASCII(attaquant.getCouleur()) + "Joueur " + attaquant.getId() + ">> "
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
						+ "Fin tour           :" + ConsoleColors.BLACK + " end\n" + ConsoleColors.WHITE_BACKGROUND
						+ ConsoleColors.BLACK_BOLD + "Quitter la partie  :" + ConsoleColors.BLACK + " quit | exit\n");
				break;

			case "save":
				// sauver partie
				System.out.print("Sauvegarder dans (chemin + nomFichier) : ");
				String filename = sc.nextLine();
				if (!filename.endsWith("ser")) {
					filename += ".ser";
				}
				try {
					partie.serialize(filename);
				} catch (Exception e) {
					System.err.println(e.getMessage() + '\n');
				}
				break;

			case "end":
				continuer = false;
				error = false;
				break;

			case "quit":
			case "exit":
				return true;

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
						try {
							Map<String, ArrayList<Integer>> des = partie.getjAttaquant().attaquerTerritoire(tAttaquant,
									tAttaquee);
							attaqueViewer(des, tAttaquant, tAttaquee);
						} catch (Exception e) {
							System.err.println(e.getMessage());
						}

						// update Partie
						partie.updatePartie();

						if (partie.getjGagnant() != null)
							return true;

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

		return false;
	}

}
