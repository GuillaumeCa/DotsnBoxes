
public class Point {
	
	// Attributs
	private int id;
	private int column, line;
	
	// Constructeur
	public Point(int id, int column, int line) {
		this.id = id;
		this.column = column;
		this.line = line;
	}
	public Point(int id) {
		this.id = id;
	}
	
	/**
	 * Affiche graphiquement le point
	 */
	public void draw() {
		StdDraw.setPenRadius(0.025);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.point(column, line);
	}
	
	// Getters & Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
}
