import java.util.Scanner;

/**
 * 
 * Gestion de l'affichage du menu dans la console ainsi que de la grille de jeu.
 * 
 * @author Guillaume Carré
 * @author Oscar Bourat
 *
 */
public class Console {

	// Attributs
	public static int mode;
	public static boolean partieCommencé;
	public static int count=0;
	public static int Difficulte=0;

	// Methodes

	/**
	 * Réinitialise les attributs de la classe lorsque une personne souhaite rejouer
	 */

	public static void reset() {
		mode=0;
		partieCommencé=false;
		count=0;
		Difficulte=0;
	}

	/**
	 * Initialise le jeu en affichant le menu et en lançant la partie
	 */
	public static void init() {
		menu();
		Player[] players = setPlayingPlayer();
		Player p1 = players[0];
		Player p2 = players[1];
		if (Grid.firstPlayers == 1) {
			game(p1, p2);
		} else {
			game(p2, p1);
		}
		drawSeparator();
		System.out.println("* Partie Terminée !!");
		Player gagnant = Grid.getWinner(p1, p2);
		if (gagnant == null) {
			System.out.println("Egalité !!");
		} else {
			System.out.println("Le gagnant est : "+ gagnant.getName() );
		}

		System.out.println("Score "+p1.getName()+": "+ p1.getScore());
		System.out.println("Score "+p2.getName()+": "+ p2.getScore());
	}

	/**
	 * Vérifie si la valeur entrée est un nombre
	 *
	 * @param s
	 * @param defaultValue	valeur par défaut
	 * @return  la valeur lorsqu'elle est un nombre, defaultValue si aucune valeur n'est entré
	 */
	public static int isNumber(String s, int defaultValue) {
		Scanner sc = new Scanner(System.in);
		try {
			if (!s.isEmpty()) {
				return Integer.parseInt(s);
			} else {
				return defaultValue;
			}
		} catch (Exception e) {
			System.out.println("La valeur entrée n'est pas un nombre. Réessayez.");
			return isNumber(sc.nextLine(), defaultValue);
		}
	}

	/**
	 * Trace une ligne de séparation de la longueur de la grille
	 */
	public static void drawSeparator() {
		int s = Grid.size;
		int longueur = s+2+(3*s/2);
		String ligne = "";
		for (int k=0; k < longueur; k++) {
			ligne += "-";
		}
		System.out.println(ligne);
	}

	/**
	 * Lance le menu permettant de sélectionner la taille de la grille, et le mode de jeu
	 */
	public static void menu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("+------------------------+");
		System.out.println("|     Dots and Boxes     |");
		System.out.println("+------------------------+");
		System.out.print("> Taille de la grille ? ");
		Grid.Taille = isNumber(sc.nextLine(), 2);
		while (Grid.Taille > 5 || Grid.Taille < 2) {
			System.out.println("La taille entrée n'est pas dans les bornes possibles (2 à 5). Réessayez.");
			Grid.Taille = isNumber(sc.nextLine(), 2);
		}
		System.out.println("> Mode de jeu :");
		System.out.println("  * J1 vs J2 (1)");
		System.out.println("  * J1 vs ordi (2)");
		mode = isNumber(sc.nextLine(), 1);
		while (mode != 1 && mode != 2){
			System.out.println("La valeur entrée n'est pas disponible. Réessayez.");
			mode = isNumber(sc.nextLine(), 1);
		}
		System.out.println("> Nombre d'handicap (trait en pointillés) :");
		Grid.Handicap = isNumber(sc.nextLine(), 0);
		int max = (Grid.Taille+1)*Grid.Taille*2;
		while (Grid.Handicap > max || Grid.Handicap < 0) {
			if (Grid.Handicap > max) {
				System.out.println("Le nombre en pointillés dépasse le maximum ("+max+"). Réessayez.");
			} else {
				System.out.println("Le nombre entré n'est pas valide. Réessayez.");
			}
			Grid.Handicap = isNumber(sc.nextLine(), 0);
		}
		
		new Grid(Grid.Taille, Grid.Handicap);
		if (mode == 2) {
			System.out.println("> Niveau de l'IA :");
			System.out.println("  * Niveau 0 (1)");
			System.out.println("  * Niveau 1 (2)");
			System.out.println("  * Niveau 2 (3)");
			Difficulte = isNumber(sc.nextLine(), 1);
			while (Difficulte < 1 || Difficulte > 3) {
				System.out.println("Le nombre entré n'est pas valide. Réessayez.");
				Difficulte = isNumber(sc.nextLine(), 1);
			}
		}
		System.out.println("> Quel joueur commence (1 ou 2)");
		Grid.firstPlayers = isNumber(sc.nextLine(), 1);
		while (Grid.firstPlayers != 1 && Grid.firstPlayers != 2) {
			System.out.println("Le nombre entré n'est pas valide. Réessayez.");
			Grid.firstPlayers = isNumber(sc.nextLine(), 1);
		}
		;
	}

	/**
	 * Demande le nom des joueurs
	 * @return Les joueurs 1 et 2
	 */
	public static Player[] setPlayingPlayer() {
		Scanner sc = new Scanner(System.in);
		if (mode == 1) {
			System.out.println("Mode de jeu: Joueur vs Joueur");
			System.out.print("> Nom joueur 1: ");
			Player p1 = new Player(sc.nextLine(), 0);
			System.out.print("> Nom joueur 2: ");
			Player p2 = new Player(sc.nextLine(), 0);
			Player[] tab = {p1, p2};
			
			return tab;
		} else if (mode == 2) {
			System.out.println("Mode de jeu: Joueur vs Ordi");
			System.out.print("> Nom joueur 1: ");
			Player p1 = new Player(sc.nextLine(), 0);
			Player p2 = new Player("Ordi", Difficulte);
			Player[] tab = {p1, p2};
			
			return tab;
		} else {
			
			return null;
		}
	}

	/**
	 * Fait jouer un joueur et le fait rejouer s'il forme un nouveau carré
	 * @param p
	 */
	public static void turn(Player p) {
		System.out.println("C'est au tour de "+p.getName()+" !");
		if (p.getIntelligence() > 0) {
			while (!Grid.turnIsFinished) {
				p.setLevel(Grid.grid);
			}
		} else {
			p.move();
		}
		Grid.turnIsFinished = false;
		boolean rejoue = Grid.checkSquare(p);
		if (rejoue && Grid.checkPartie()) {
			drawSeparator();
			System.out.println("Nouveau carré formé !!");
			draw(Grid.grid);
			drawSeparator();
			turn(p);
		}
	}


	/**
	 * Gère l'alternance des joueurs
	 * @param p1
	 * @param p2
	 */
	public static void game(Player p1, Player p2) {
		partieCommencé = true;
		drawSeparator();
		while (Grid.checkPartie()) {
			draw(Grid.grid);
			drawSeparator();
			turn(p1);
			drawSeparator();
			draw(Grid.grid);
			if (Grid.checkPartie()) {
				drawSeparator();
				turn(p2);
				drawSeparator();
				if (!Grid.checkPartie()) {
					draw(Grid.grid);
				}
			}
		}
	}

	/**
	 * Trace la grille
	 * @param grid
	 */
	public static void draw(Object[][] grid) {
		int length = grid[0].length;
		for (int k=0; k<length; k++) {
			for (Object obj: grid[k]) {
				if (obj instanceof Point) {
					System.out.printf("%-2d",((Point) obj).getId());
				} else if (obj instanceof Line) {
					if (((Line) obj).isHorizontal()) {
						System.out.printf("%-3s",((Line) obj).getState());
					} else {
						System.out.printf("%-2s",((Line) obj).getState());
					}
				} else if (obj instanceof Player) {
					System.out.printf("%-3s"," "+((Player) obj).getName().toUpperCase().charAt(0)+" ");
				}
			}
			System.out.println("");
		}
	}

	// Main
	public static void main(String[] args) {
		while (true) {
			init();
			System.out.println();
			System.out.println("> Si vous souhaitez rejouer, taper (1)");
			Scanner sc = new Scanner(System.in);
			if (isNumber(sc.nextLine(), 1) == 1) {
				
				Grid.reset();
				Console.reset();
			} else {
				
				break;
			}
		}
	}
}
