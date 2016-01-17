import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {

	private int score;
	private String name;
	private int intelligence=0;
	private int column;
	private int line;

	public Player(String name, int intelligence) {
		score = 0;
		this.name = name;
		this.intelligence = intelligence;
	}

	public Player(String name, int intelligence, int column, int line) {
		score = 0;
		this.name = name;
		this.intelligence = intelligence;
		this.column = column;
		this.line = line;
	}

	/**
	 * Demande les coordonnés de la ligne au joueur et vérifie l'entrée
	 */
	public void move() {
		Scanner sc = new Scanner(System.in);
		System.out.println("> Veuillez entrer la position de la ligne:");
		String[] position = sc.nextLine().split(",");
		try {
			int lpo = Integer.parseInt(position[0]);
			int rpo = Integer.parseInt(position[1]);
			if (!Grid.setLine(lpo, rpo, "solid")) {
				System.out.println("La position est incorrecte. Réessayez.");
				move();
			}
		} catch (Exception e) {
			System.out.println("La position est incorrecte. Réessayez.");
			move();
		}
		sc.close();
	}

	/**
	 * Affiche graphiquement la première lettre du joueur
	 */
	public void draw() {
		Font playerFont = new Font("Futura", Font.PLAIN, 40);
		StdDraw.setFont(playerFont);
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.text(line, column, name.toUpperCase().substring(0, 1));
	}

	/**
	 * Génère une ligne aléatoirement entre deux points disponible de la grille
	 *
	 * @return	la ligne générée, null si elle n'existe pas
	 */
	private static Line playRandom() {
		int k = (int) (Math.random()*(Grid.size+1));
		int i = (int) (Math.random()*(Grid.size+1));


		if (k%2 == 0 && i%2 != 0 && !((Line) Grid.grid[k][i]).isTaken()) {
			Point lpo = (Point) Grid.grid[k][i-1];
			Point rpo = (Point) Grid.grid[k][i+1];
			return new Line(((Line) Grid.grid[k][i]).getType(), "", lpo, rpo, k, i);
		}
		if (k%2 != 0 && i%2 == 0 && !((Line) Grid.grid[k][i]).isTaken()) {
			Point lpo = (Point) Grid.grid[k-1][i];
			Point rpo = (Point) Grid.grid[k+1][i];
			return new Line(((Line) Grid.grid[k][i]).getType(), "", lpo, rpo, k, i);
		}
		return null;
	}

	/**
	 * Sélectionne le niveau de l'IA selon le niveau d'intelligence choisi
	 *
	 * @param grid
	 */
	public void setLevel(Object[][] grid) {
		int level = intelligence;
		if (level == 1) {
			level0(grid);
		}
		if (level == 2) {
			level1(grid);
		}
		if (level == 3) {
			level2(grid);
		}
	}

	/**
	 * IA de level 0 (joue aléatoirement)
	 *
	 * @param grid
	 */
	private static void level0(Object[][] grid) {
		while (!Grid.turnIsFinished) {
			Line line = playRandom();
			if (line != null) {
				if (line.getType() == "dashed") {
					grid[line.getLine()][line.getColumn()] = new Line("", "", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
				} else if (line.getType() != "solid"){
					if (line.isHorizontal()) {
						grid[line.getLine()][line.getColumn()] = new Line("solid", " - ", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
					} else {
						grid[line.getLine()][line.getColumn()] = new Line("solid", "|", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
					}
				}
				Grid.turnIsFinished = true;
			}
		}
	}

	/**
	 * IA de level 1 :<br>
	 * 1. Ferme les carré ayant 3 côtés fermés<br>
	 * 2. Joue aléatoirement sinon
	 *
	 * @param grid
	 */
	private static void level1(Object[][] grid) {
		while (!Grid.turnIsFinished) {
			Line line=null;
			int size = grid[0].length;
			for (int i=0; i<size; i++) {
				for (int k=0; k<size; k++) {
					if (k%2 == 0 && i%2 == 0 && k < size-1 && i < size-1) {
						boolean top = ((Line) grid[k+1][i]).isTaken();
						boolean bottom = ((Line) grid[k+1][i+2]).isTaken();
						boolean left = ((Line) grid[k][i+1]).isTaken();
						boolean right = ((Line) grid[k+2][i+1]).isTaken();
						int a=0;
						if (top) a++;
						if (left) a++;
						if (right) a++;
						if (bottom) a++;
						if (a == 3) {
							if (!top) line=(Line) grid[k+1][i];
							if (!left) line=(Line) grid[k][i+1];
							if (!right) line=(Line) grid[k+2][i+1];
							if (!bottom) line=(Line) grid[k+1][i+2];
							if (line.getType() == ""){
								if (line.isHorizontal()) {
									grid[line.getLine()][line.getColumn()] = new Line("solid", " - ", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
								} else {
									grid[line.getLine()][line.getColumn()] = new Line("solid", "|", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
								}
							} else if (line.getType() == "dashed"){
								grid[line.getLine()][line.getColumn()] = new Line("", "", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
							}
							Grid.turnIsFinished = true;
							break;
						}
					}
				}
			}
			while (!Grid.turnIsFinished) {
				line = playRandom();
				if (line != null) {
					if (line.getType() == "dashed") {
						grid[line.getLine()][line.getColumn()] = new Line("", "", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
					} else if (line.getType() != "solid"){
						if (line.isHorizontal()) {
							grid[line.getLine()][line.getColumn()] = new Line("solid", " - ", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
						} else {
							grid[line.getLine()][line.getColumn()] = new Line("solid", "|", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
						}
					}
					Grid.turnIsFinished = true;
				}
			}
		}
	}

	/**
	 * Vérifie si la ligne est présente dans la liste
	 *
	 * @param list
	 * @param l
	 * @return	true si la ligne est présente, false sinon
	 */
	private static boolean isLineExisting(List<Line> list, Line l) {
		for (Line li: list) {
			if (li.equals(l)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * IA de level 2 :<br>
	 * 1. Ferme les carré ayant 3 côtés fermés<br>
	 * 2. Ne ferme pas un des côté des carrés ayant 2 côtés fermés<br>
	 * 3. Joue aléatoirement sinon
	 *
	 * @param grid
	 */
	private static void level2(Object[][] grid) {
		List<Line> listeForbiddenLines = new ArrayList<Line>();
		Line line=null;
		int size = grid[0].length;
		for (int i=0; i<size; i++) {
			for (int k=0; k<size; k++) {
				if (k%2 == 0 && i%2 == 0 && k < size-1 && i < size-1) {
					boolean top = ((Line) grid[k+1][i]).isTaken();
					boolean bottom = ((Line) grid[k+1][i+2]).isTaken();
					boolean left = ((Line) grid[k][i+1]).isTaken();
					boolean right = ((Line) grid[k+2][i+1]).isTaken();
					int a=0;
					if (top) a++;
					if (left) a++;
					if (right) a++;
					if (bottom) a++;

					if (a == 2) {
						if (!top && ((Line) grid[k+1][i]).getType() != "dashed" && !isLineExisting(listeForbiddenLines, (Line) grid[k+1][i])) {
							listeForbiddenLines.add((Line) grid[k+1][i]);
						}
						if (!left && ((Line) grid[k][i+1]).getType() != "dashed" && !isLineExisting(listeForbiddenLines, (Line) grid[k][i+1])) {
							listeForbiddenLines.add((Line) grid[k][i+1]);
						}
						if (!right && ((Line) grid[k+2][i+1]).getType() != "dashed" && !isLineExisting(listeForbiddenLines, (Line) grid[k+2][i+1])) {
							listeForbiddenLines.add((Line) grid[k+2][i+1]);
						}
						if (!bottom && ((Line) grid[k+1][i+2]).getType() != "dashed" && !isLineExisting(listeForbiddenLines, (Line) grid[k+1][i+2])) {
							listeForbiddenLines.add((Line) grid[k+1][i+2]);
						}
					}

					if (a == 3) {
						if (!top) line=(Line) grid[k+1][i];
						if (!left) line=(Line) grid[k][i+1];
						if (!right) line=(Line) grid[k+2][i+1];
						if (!bottom) line=(Line) grid[k+1][i+2];
						if (line.getType() == ""){
							if (line.isHorizontal()) {
								grid[line.getLine()][line.getColumn()] = new Line("solid", " - ", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
							} else {
								grid[line.getLine()][line.getColumn()] = new Line("solid", "|", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
							}
							Grid.turnIsFinished = true;
							break;
						} else {
							if (!isLineExisting(listeForbiddenLines, line))
							listeForbiddenLines.add(line);
						}
					}
				}
			}
		}
		while (!Grid.turnIsFinished) {
			line = playRandom();
			if (line != null) {
				boolean isForbidden = false;
				if (Grid.remainingLine() != listeForbiddenLines.size()) {
					for (Line l: listeForbiddenLines) {
						if (l.getColumn() == line.getColumn() && l.getLine() == line.getLine()) {
							isForbidden = true;
						}
					}
				}

				if (!isForbidden) {
					if (line.getType() == "dashed") {
						grid[line.getLine()][line.getColumn()] = new Line("", "", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
					} else if (line.getType() != "solid"){
						if (line.isHorizontal()) {
							grid[line.getLine()][line.getColumn()] = new Line("solid", " - ", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
						} else {
							grid[line.getLine()][line.getColumn()] = new Line("solid", "|", line.getLpo(), line.getRpo(), line.getLine(), line.getColumn());
						}
					}
					Grid.turnIsFinished = true;
				}
			}
		}
	}


	// Getters and Setters

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getScore() {
		return score;
	}

	public void incrementScore(int incremente) {
		this.score += incremente;
		Grid.scoreGlobal += incremente;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}
}
