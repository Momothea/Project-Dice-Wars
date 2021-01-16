package cli;

import java.awt.Rectangle;
import java.util.Scanner;

import ui.components.JHelp;

public class PartieCLI {
	private Scanner sc = new Scanner(System.in);

	private int menu() {
		boolean error = false;
		int result = 0;

		do {
			// http://www.patorjk.com/software/taag/#p=display&f=Doom&t=Dice%20Wars
			System.out.println(ConsoleColors.RESET + 
					"______ _            _    _                \n"
					+ "|  _  (_)          | |  | |               \n" 
					+ "| | | |_  ___ ___  | |  | | __ _ _ __ ___ \n"
					+ "| | | | |/ __/ _ \\ | |/\\| |/ _` | '__/ __|\n"
					+ "| |/ /| | (_|  __/ \\  /\\  / (_| | |  \\__ \\\n"
					+ "|___/ |_|\\___\\___|  \\/  \\/ \\__,_|_|  |___/\n"
					+ "Projet Java Efrei L3, Nil Paul Moise     \n");

			System.out.println(ConsoleColors.BLACK_BOLD 
					+ "Nouvelle partie...          " + ConsoleColors.RESET
					+ "   3) Charger une partie\n" 
					+ "  1) sur une nouvelle carte    4) Règles du jeu\n"
					+ "  2) sur une carte existante   5) Quitter");
			if (error) {
				System.err.println("Votre saisie était incorrecte !");
			}
			System.out.print(">> " + ConsoleColors.GREEN);

			try {
				result = Integer.parseInt(sc.nextLine());
				error = result < 1 || result > 5;
			} catch (Exception e) {
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
				new JHelp(new Rectangle(0,0,0,480));
				break;

			default:
				break;
			}
		} while (choix != 5);

		System.out.println("A bientot");
	}

	public static void main(String[] args) {
		PartieCLI cli = new PartieCLI();
		// run partie
		cli.runCLI();

	}

}
