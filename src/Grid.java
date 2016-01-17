import java.util.Scanner;

/**
 * 
 * Classe principale de DotsnBoxes.<br>
 * Elle gère les fonctions logiques du jeu comme le stockage des informations de la grille de jeu
 * ou l'accès et la modification de ces informations.
 * 
 * @author Guillaume Carré
 * @author Oscar Bourat
 *
 */
public class Grid {
	
	// Attributs
	
	public static int size;
	public static int dashed;
	public static Object[][] grid;
	public static Player p1;
	public static Player p2;
	public static int firstPlayers = 1;
	public static int scoreGlobal;
	public static int Handicap=0;
	public static boolean rejoue=false;
	public static int Taille=2;
	public static boolean turnIsFinished=false;
	
	// Constructeur
	public Grid(int size, int dashed) {
		Grid.size = 2*size;
		Grid.dashed = dashed;
		grid = new Object[2*size+1][2*size+1];
		generatePoints();
		generateElements();
		generateDashed(dashed);
	}
	
	// Méthodes
	
	/**
	 * Réinitialise les attributs de la classe lorsque une personne souhaite rejouer
	 */
	public static void reset() {
		size = 0;
		dashed = 0;
		grid = null;
		p1 = null;
		p2 = null;
		scoreGlobal = 0;
		Handicap = 0;
		rejoue = false;
		Taille = 2;
		turnIsFinished = false;
	}
	
	/**
	 * Vérifie si la partie est terminé
	 * 
	 * @return true si la partie est en cours, false sinon
	 */
	public static boolean checkPartie() {
		if (scoreGlobal < (Grid.size*Grid.size)/4) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Génère les points de la grille de jeu
	 */
	public static void generatePoints() {
		int count = 1;
		for (int k=0; k<size+1; k++) {
			for (int i=0; i<size+1; i++) {
				if (k%2 == 0 && i%2 == 0) {
					grid[k][i] = new Point(count, k, i);
					count++;
				}
			}
		}
	}
	
	/**
	 * Génère les lignes et les joueurs de la grille de jeu
	 */
	public static void generateElements() {
		for (int k=0; k<size+1; k++) {
			for (int i=0; i<size+1; i++) {
				if (k%2 == 0 && i%2 != 0) {
					Point lpo = (Point) grid[k][i-1];
					Point rpo = (Point) grid[k][i+1];
					grid[k][i] = new Line("", "   ", lpo, rpo, k, i);
				} else if (k%2 != 0 && i%2 == 0) {
					Point lpo = (Point) grid[k-1][i];
					Point rpo = (Point) grid[k+1][i];
					grid[k][i] = new Line("", "  ", lpo, rpo, k, i);
				} else if (k%2 != 0 && i%2 != 0) {
					grid[k][i] = new Player(" ", 0);
				}
			}
		}
	}
	
	/**
	 * Place une ligne pointillé aléatoirement sur la grille
	 * 
	 * @return	true si la ligne est placée, false sinon
	 */
	public static boolean aleatDashed() {
		int k = (int) (Math.random()*(Grid.size+1));
		int i = (int) (Math.random()*(Grid.size+1));
		
		if (k%2 == 0 && i%2 != 0 && ((Line) grid[k][i]).getType() != "dashed") {
			Point lpo = (Point) grid[k][i-1];
			Point rpo = (Point) grid[k][i+1];
			String line = lineType(lpo.getId(), rpo.getId(), "dashed");
			grid[k][i] = new Line("dashed", line, lpo, rpo, k, i);
			return true;
		} else if (k%2 != 0 && i%2 == 0 && ((Line) grid[k][i]).getType() != "dashed") {
			Point lpo = (Point) grid[k-1][i];
			Point rpo = (Point) grid[k+1][i];
			String line = lineType(lpo.getId(), rpo.getId(), "dashed");
			grid[k][i] = new Line("dashed", line, lpo, rpo, k, i);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Place des lignes en pointillé aléatoirement sur la grille
	 * 
	 * @param nbr	nombre de lignes en pointillé
	 */
	public static void generateDashed(int nbr) {
		for (int i = 0; i < nbr; i++) {
			boolean cond = false;
			while (!cond) {
				cond = aleatDashed();
			}
		}
	}
	
	/**
	 * Donne le nombre de lignes restantes sur la grilles
	 * 
	 * @return	le nombre de lignes restantes
	 */
	public static int remainingLine() {
		int count=0;
		int size = grid[0].length;
		for (int k=0; k<size; k++) {
			for (Object obj: grid[k]) {
				if (obj instanceof Line) {
					if (!((Line) obj).isTaken()) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * Vérifie si les carrés sont formés et note le joueur ayant remporté le carré
	 * 
	 * @param p	joueur
	 * @return	true si un nouveau carré est formé, false sinon
	 */
	public static boolean checkSquare(Player p) {
		boolean newSquare = false;
		for (int i=0; i<size+1; i++) {
			for (int k=0; k<size+1; k++) {
				if (k%2 == 0 && i%2 == 0 && k < size && i < size) {
					boolean top = ((Line) grid[k+1][i]).isTaken();
					boolean bottom = ((Line) grid[k+1][i+2]).isTaken();
					boolean left = ((Line) grid[k][i+1]).isTaken();
					boolean right = ((Line) grid[k+2][i+1]).isTaken();
					boolean player = ((Player) grid[k+1][i+1]).getName() == " ";
					if (top && bottom && left && right && player) {
						setSquare(p, k+1, i+1);
						newSquare = true;
					}
				}
			}
		}
		return newSquare;
	}
	
	/**
	 * Affiche la lettre du gagnant du carré à l'emplacement (c, l)
	 * 
	 * @param p	joueur
	 * @param c	colonne
	 * @param l	ligne
	 */
	public static void setSquare(Player p, int c, int l) {
		grid[c][l] = new Player(p.getName(), 0, c, l);
		p.incrementScore(1);
	}
	
	/**
	 * Renvoie le tracé de la ligne selon le type
	 * 
	 * @param lpo	point de départ du trait
	 * @param rpo	point d'arrivé du trait
	 * @param type
	 * @return 		String du tracé de la ligne
	 */
	public static String lineType(int lpo, int rpo, String type) {
		if (type == "solid") {
			if ((rpo - lpo) == 1) {
				return " - ";
			} else if ((rpo - lpo) == (size/2)+1) {
				return "| ";
			} else {
				return "";
			}
		} else if (type == "dashed") {
			if ((rpo - lpo) == 1) {
				return "...";
			} else if ((rpo - lpo) == (size/2)+1) {
				return ". ";
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
	
	/**
	 *  Modifie une ligne à la position (a, b) avec une ligne de type 'type'
	 * 
	 * @param a		colonne
	 * @param b		ligne
	 * @param type	
	 * @return		true si la ligne est modifié, false sinon
	 */
	public static boolean setLine(int a, int b, String type) {
		int lpo = Math.min(a, b);
		int rpo = Math.max(a, b);
		Point fpoint = new Point(lpo);
		Point lpoint = new Point(rpo);
		String line = lineType(fpoint.getId(), lpoint.getId(), type);
		for (int k=0; k<size+1; k++) {
			for (int i=0; i<size+1; i++) {
				if (grid[k][i] instanceof Line) {
					if (((Line) grid[k][i]).getLpo().getId() == fpoint.getId() && ((Line) grid[k][i]).getRpo().getId() == lpoint.getId()) {
						if (((Line) grid[k][i]).getType() == "dashed") {
							grid[k][i] = new Line("", "", fpoint, lpoint, k, i);
							return true;
						} else if (((Line) grid[k][i]).getType() != "solid"){
							grid[k][i] = new Line(type, line, fpoint, lpoint, k, i);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Donne le gagnant de la partie
	 * 
	 * @param p1	joueur 1
	 * @param p2	joueur 2
	 * @return		le joueur gagnant, null si égalité
	 */
	public static Player getWinner(Player p1, Player p2) {
		if (p1.getScore() > p2.getScore()) {
			return p1;
		} else if (p1.getScore() == p2.getScore()) {
			return null;
		} else {
			return p2;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Sélectionner le mode d'affichage:");
		System.out.println("> Graphique (1)");
		System.out.println("> Console (2)");
		int mode = sc.nextInt();
		if (mode == 1) {
			Graphique.main(args);
		}
		if (mode == 2) {
			Console.main(args);
		}
		sc.close();
	}
}
