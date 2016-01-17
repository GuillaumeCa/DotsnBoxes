public class Line {

	private String type;
	private String state;
	private Point lpo, rpo;
	private int line, column;

	public Line(String type, String state, Point lpo, Point rpo, int line, int column) {
		this.type = type;
		this.state = state;
		this.lpo = lpo;
		this.rpo = rpo;
		this.line = line;
		this.column = column;
	}

	/**
	 * Vérifie si la ligne est horizontale
	 *
	 * @return	true si la ligne est horizontale, false sinon
	 */
	public boolean isHorizontal() {
		if (rpo.getId() - lpo.getId() == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Vérifie si la ligne est prise
	 *
	 * @return true si la ligne est prise, false sinon
	 */
	public boolean isTaken() {
		if (type == "solid") {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Dessine graphiquement la ligne
	 */
	public void draw() {
		StdDraw.setPenRadius(0.01);
		if (type == "solid") {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(lpo.getLine(), lpo.getColumn(), rpo.getLine(), rpo.getColumn());
		}
		if (type == "dashed") {
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.line(lpo.getLine(), lpo.getColumn(), rpo.getLine(), rpo.getColumn());
		}
	}

	/**
	 * Vérifie si la ligne est sélectionnée et modifie l'état de la ligne dans la grille
	 */
	public void updateLine() {
		double x0 = lpo.getLine();
		double y0 = lpo.getColumn();
		double x1 = rpo.getLine();
		double y1 = rpo.getColumn();

		if (Graphique.isMouseInArea(x0, y0, x1, y1, 0.05) && StdDraw.mousePressed() && type != "solid") {
			if (type == "dashed") {
				while (StdDraw.mousePressed()) {
					type = "";
					state = "";
					Grid.turnIsFinished = true;
				}
			} else {
				while (StdDraw.mousePressed()) {
					type = "solid";
					Grid.turnIsFinished = true;
				}
			}
		}
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public Point getLpo() {
		return lpo;
	}

	public void setLpo(Point lpo) {
		this.lpo = lpo;
	}

	public Point getRpo() {
		return rpo;
	}

	public void setRpo(Point rpo) {
		this.rpo = rpo;
	}

	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}



}
