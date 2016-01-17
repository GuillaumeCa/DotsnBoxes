import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

/**
 * 
 * Gestion de l'affichage du menu graphique ainsi que de la grille de jeu graphique
 * 
 * @author Guillaume Carré
 * @author Oscar Bourat
 *
 */
public class Graphique {
	
	// Attributs
	public static int mode=1;
	public static boolean partieCommencé;
	public static int count=0;
	public static double marge;
	public static Player currentplayer;
	public static int Taille=2;
	public static int Difficulte=0;
	public static int edit = 1;
	public static String J1="1-joueur";
	public static String J2="2-joueur";
	public static Color colorJ1=StdDraw.GREEN;
	public static Color colorJ2=StdDraw.BOOK_BLUE;
	public int releaseTime = 200;
	
	/**
	 * Initialise le jeu en affichant le menu et en lançant la partie
	 */
	public static void init() {
		launchMenu();
		new Grid(Taille, Grid.Handicap);
		int largeur = Grid.size;
		double marge = 0.2*largeur;
		Graphique.marge = marge;
		StdDraw.setXscale(-marge,largeur+marge);
		StdDraw.setYscale(largeur+marge,-marge);
		Grid.p1 = new Player(J1, 0);
		if (mode == 2) {
			Grid.p2 = new Player("Ordi", Difficulte+1);
		} else {
			Grid.p2 = new Player(J2, 0);
		}
		if (Grid.firstPlayers == 1) {
			game(Grid.p1, Grid.p2);
		} else {
			game(Grid.p2, Grid.p1);
		}
	}
	
	/**
	 * Réinitialise les attributs de la classe lorsque une personne souhaite rejouer
	 */
	public static void reset() {
		mode=1;
		partieCommencé=false;
		count=0;
		marge=0;
		currentplayer=null;
		Taille=2;
		Difficulte=0;
		edit = 1;
		J1="1-joueur";
		J2="2-joueur";
		colorJ1=StdDraw.GREEN;
		colorJ2=StdDraw.BOOK_BLUE;
	}
	
	/**
	 * Test si la touche a été relaché
	 * 
	 * @param key	touche appuyé
	 * @return		true lorsque la touche est relaché false si la touche n'est pas appuyé
	 */
	private static boolean isKeyReleased(int key) {
		if (!StdDraw.isKeyPressed(key)) {
			return false;
		} else {
			while (StdDraw.isKeyPressed(key)) {
			}
			return true;
		}
	}
	
	/**
	 * Test si le bouton de la souris à été relaché
	 * 
	 * @param key	touche appuyé
	 * @return		true lorsque le bouton de la souris est relaché false si le bouton de la souris n'est pas appuyé
	 */
	private static boolean mouseReleased() {
		if (!StdDraw.mousePressed()) {
			return false;
		} else {
			while (StdDraw.mousePressed()) {
			}
			return true;
		}
	}

	/**
	 * Détecte si la souris se situe dans une aire
	 * 
	 * @param x0		coordonné x du point de départ
	 * @param y0		coordonné y du point de départ
	 * @param x1		coordonné x du point d'arrivé
	 * @param y1		coordonné y du point d'arrivé
	 * @param epaisseur	epaisseur de la ligne
	 * @return 			true si la souris est dans la zone, false sinon
	 */
	public static boolean isMouseInArea(double x0, double y0, double x1, double y1, double epaisseur) {
		double x = StdDraw.mouseX();
		double y = StdDraw.mouseY();
		epaisseur = epaisseur*(Grid.size+1);
		if (y0 == y1) {
			y0 -= epaisseur/2;
			y1 += epaisseur/2;
			x0 += epaisseur/2;
			x1 -= epaisseur/2;
		} else if (x0 == x1) {
			x0 -= epaisseur/2;
			x1 += epaisseur/2;
			y0 += epaisseur/2;
			y1 -= epaisseur/2;
		}
		
		if (x > x0 && x < x1 && y > y0 && y < y1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Permet de détecter l'activation d'un bouton à une certaine position
	 * @param x			position x du centre
	 * @param y			position y du centre
	 * @param width		largeur
	 * @param height	hauteur
	 * @return			true si le bouton est activé, false sinon
	 */
	public static boolean isButtonPressed(
			double x,
			double y,
			double width,
			double height)
	{
		if (isMouseInArea(x-width, y-height, x+width, y+height, 0) && StdDraw.mousePressed()) {
			return true;
		}
		return false;
	}

	/**
	 * Détecte si la souris passe entre deux points pouvant être reliés et affiche en surimpression un trait de la couleur du joueur
	 * 
	 * @param currentplayer	joueur qui joue actuellement
	 */
	public static void hover(Player currentplayer) {
		int size = Grid.grid[0].length;
		Color color;
		if (currentplayer == Grid.p1) {
			color = colorJ1;
		} else {
			color = colorJ2;
		}
		for (int k=0; k<size; k++) {
			for (Object obj: Grid.grid[k]) {
				if (obj instanceof Line) {
					if (((Line) obj).getType() != "solid") {
						double x0 = ((Line) obj).getLpo().getLine();
						double y0 = ((Line) obj).getLpo().getColumn();
						double x1 = ((Line) obj).getRpo().getLine();
						double y1 = ((Line) obj).getRpo().getColumn();
						if (isMouseInArea(x0, y0, x1, y1, 0.05)) {
							StdDraw.setPenColor(color);
							StdDraw.setPenRadius(0.02);
							StdDraw.line(x0, y0, x1, y1);
						}
					}
				}
			}
		}
	}

	/**
	 * Modifie une ligne dans la grille de jeu
	 * 
	 * @param grid grille de jeu
	 */
	public static void setLine(Object[][] grid) {
		int size = grid[0].length;
		for (int k=0; k<size; k++) {
			for (Object obj: grid[k]) {
				if (obj instanceof Line) {
					((Line) obj).updateLine();
				}
			}
		}
	}

	/**
	 * Affiche du texte avec un fond de couleur
	 * 
	 * @param content 		texte du label
	 * @param background	couleur d'arriere plan
	 * @param text			couleur du texte
	 * @param x				position x du centre
	 * @param y				position y du centre
	 * @param font			police du texte
	 * @param width			largeur du label
	 * @param height		hauteur du label
	 */
	public static void drawLabel(
			String content,
			Color background,
			Color text,
			double x,
			double y,
			Font font,
			double width,
			double height)
	{
		StdDraw.setPenColor(background);
		StdDraw.filledRectangle(x, y, width, height);
		
		StdDraw.setFont(font);
		StdDraw.setPenColor(text);
		StdDraw.text(x, y, content);
		
	}
	
	/**
	 * Affiche une zone de texte modifiable
	 * 
	 * @param content		texte du bouton
	 * @param background	couleur d'arriere plan
	 * @param text			couleur du texte
	 * @param x				position x du côté gauche
	 * @param y				position y du centre
	 * @param font			police du texte
	 * @param width			largeur de la zone de texte
	 * @param height		hauteur de la zone de texte
	 * @param edit			etat de la zone de texte
	 * @param joueur		joueur correspondant à la zone de texte
	 */
	public static void drawTextArea(
			String content,
			Color background,
			Color text,
			double x,
			double y,
			Font font,
			double width,
			double height,
			boolean edit,
			int joueur)
	{
		StdDraw.setPenColor(background);
		StdDraw.filledRectangle(x+width, y, width, height);
		
		if (edit == true) {
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.filledRectangle(x+width, y, width, height);
			
			if (StdDraw.hasNextKeyTyped()) {
				if (isKeyReleased(KeyEvent.VK_BACK_SPACE)) {
					if (joueur == 1 && J1.length() > 0) {
						J1 = J1.substring(0, J1.length()-1);
						
					} 
					if (joueur == 2 && J2.length() > 0) {
						J2 = J2.substring(0, J2.length()-1);
					}
				} else {
					char c = StdDraw.nextKeyTyped();
					if (joueur == 1 && J1.length() < 12) {
						if (c != '\b') {
							J1 += c;
						}
					}
					if (joueur == 2 && J2.length() < 12) {
						if (c != '\b') {
							J2 += c;
						}
					}
				}
			}
			
			
			
		}
		StdDraw.setFont(font);
		StdDraw.setPenColor(text);
		if (joueur == 1) {
			StdDraw.textLeft(x+0.5, y, J1);
		} 
		if (joueur == 2) {
			StdDraw.textLeft(x+0.5, y, J2);
		}
	}
	
	/**
	 * Affiche un bouton
	 * 
	 * @param content		texte du bouton
	 * @param background	couleur d'arriere plan
	 * @param text			couleur du texte
	 * @param overlay		couleur d'arriere plan lors du survol
	 * @param x				position x du centre
	 * @param y				position y du centre
	 * @param font			police du texte
	 * @param width			largeur du bouton
	 * @param height		hauteur du bouton
	 * @param Mode			bouton activé ou non
	 */
	public static void drawButton(
			String content,
			Color background,
			Color text,
			Color overlay,
			double x,
			double y,
			Font font,
			double width,
			double height,
			boolean Mode)
	{
		StdDraw.setPenColor(background);
		StdDraw.filledRectangle(x, y, width, height);
		
		if (isMouseInArea(x-width, y-height, x+width, y+height, 0) || Mode) {
			StdDraw.setPenColor(overlay);
			StdDraw.filledRectangle(x, y, width, height);
		}
		
		StdDraw.setFont(font);
		StdDraw.setPenColor(text);
		StdDraw.text(x, y, content);
		
	}
	
	
	/**
	 * Affiche un compteur avec des boutons + et - pour incrémenter ou décrementer un nombre.
	 * @param x			position x du centre
	 * @param y			position y du centre
	 * @param start		valeur minimum du compteur
	 * @param end		valeur maximum du compteur
	 * @param step		pas du compteur
	 * @param compteur	valeur du compteur
	 * @return			nouvelle valeur du compteur
	 */
	public static int drawStepper(double x, double y, int start, int end, int step, int compteur) {

		Color background = StdDraw.LIGHT_GRAY;
		Color label = StdDraw.WHITE;
		Color count = StdDraw.BLACK;
		Font flabel = new Font("Helvetica", Font.BOLD, 20);
		Font fcount = new Font("Futura", Font.PLAIN, 20);
		
		// label -
		StdDraw.setPenColor(background);
		StdDraw.filledCircle(x-2, y, 0.5);
		StdDraw.setFont(flabel);
		StdDraw.setPenColor(label);
		StdDraw.text(x-2, y, "-");
		
		// label +
		StdDraw.setPenColor(background);
		StdDraw.filledCircle(x+2, y, 0.5);
		StdDraw.setFont(flabel);
		StdDraw.setPenColor(label);
		StdDraw.text(x+2, y, "+");
		
		// Count
		StdDraw.setFont(fcount);
		StdDraw.setPenColor(count);
		StdDraw.text(x, y, Integer.toString(compteur));
		
		if (isMouseInArea(x-2.5, y-0.5, x-1.5, y+0.5, 0) && compteur > start) {
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.filledCircle(x-2, y, 0.5);
			StdDraw.setFont(flabel);
			StdDraw.setPenColor(label);
			StdDraw.text(x-2, y, "-");
			if (mouseReleased()) {
				compteur -= step;
			}
		}
		
		if (isMouseInArea(x+1.5, y-0.5, x+2.5, y+0.5, 0) && compteur < end) {
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.filledCircle(x+2, y, 0.5);
			StdDraw.setFont(flabel);
			StdDraw.setPenColor(label);
			StdDraw.text(x+2, y, "+");
			if (mouseReleased()) {
				compteur += step;
			}
		}
		return compteur;
	}
	
	/**
	 * Lance le menu permettant de sélectionner le mode de jeu, la taille de la grille,
	 * la difficulté de l'IA, le nombre de traits en pointillés (Handicap) et le nom du ou
	 * des joueurs.
	 */
	public static void launchMenu() {
		StdDraw.setXscale(-10,10);
		StdDraw.setYscale(-10,10);
		boolean Start = false;
		Font titre = new Font("Futura", Font.PLAIN, 40);
		Font bouton = new Font("Futura", Font.PLAIN, 20);
		Font label = new Font("Futura", Font.PLAIN, 30);
		while (!Start) {
			// Titre jeu
			drawLabel("Dots n Boxes", StdDraw.DARK_GRAY, StdDraw.WHITE, 0, 9, titre, 20, 2);
			
			// Sélection du mode de jeu
			StdDraw.setFont(label);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.textLeft(-10, 5.5, "Mode");
			
			
			// Mode J1 vs J2
			if (isButtonPressed(-2, 5.5, 2.5, 0.8)) {
				mode = 1;
			}
			if (mode == 1){
				drawButton("J1 vs J2", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, -2, 5.5, bouton, 2.5, 0.8, true);
			} else {
				drawButton("J1 vs J2", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, -2, 5.5, bouton, 2.5, 0.8, false);

			}
			
			// Mode J vs Ordi
			if (isButtonPressed(4, 5.5, 2.5, 0.8)) {
				mode = 2;
			}
			
			if (mode == 2) {
				drawButton("J1 vs Ordi", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, 4, 5.5, bouton, 2.5, 0.8, true);
			} else {
				drawButton("J1 vs Ordi", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, 4, 5.5, bouton, 2.5, 0.8, false);
			}
			
			// Sélection taille du jeu
			StdDraw.setFont(label);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.textLeft(-10, 3, "Taille");
			
			// compteur + -
			Taille = drawStepper(0, 3, 2, 5, 1, Taille);
			
			int max = (Taille+1)*Taille*2;
			if (Grid.Handicap > max) Grid.Handicap = max;
			Grid.Handicap = drawStepper(0, 0.5, 0, max, 1, Grid.Handicap);
			
			if (mode == 2) {
				Difficulte = drawStepper(0, -2, 0, 2, 1, Difficulte);
				// Selection difficulte
				StdDraw.setFont(label);
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.textLeft(-10, -2, "Difficulté");
			}
			
			
			
			// Sélection Handicap
			StdDraw.setFont(label);
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.textLeft(-10, 0.5, "Handicap");
			
			// Selection nom Joueur 1
			StdDraw.setFont(label);
			StdDraw.setPenColor(StdDraw.BLACK);
			if (mode == 1) {
				StdDraw.textLeft(-10, -2, "Nom J1");
				if (isButtonPressed(1, -2, 5, 1)) {
					edit = 1;
				}
				if (edit == 1) {
					drawTextArea("test", StdDraw.LIGHT_GRAY, StdDraw.WHITE, -4, -2, label, 5, 1, true, 1);
				} else {
					drawTextArea("test", StdDraw.LIGHT_GRAY, StdDraw.WHITE, -4, -2, label, 5, 1, false, 1);
				}
			} else {
				StdDraw.textLeft(-10, -4.5, "Nom J1");
				if (isButtonPressed(1, -4.5, 5, 1)) {
					edit = 1;
				}
				if (edit == 1) {
					drawTextArea("test", StdDraw.LIGHT_GRAY, StdDraw.WHITE, -4, -4.5, label, 5, 1, true, 1);
				} else {
					drawTextArea("test", StdDraw.LIGHT_GRAY, StdDraw.WHITE, -4, -4.5, label, 5, 1, false, 1);
				}
			}
			
			if (mode == 2) {
				edit = 1;
			}
			
			if (mode == 1) {
				// Selection nom Joueur 2
				StdDraw.setFont(label);
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.textLeft(-10, -4.5, "Nom J2");
				if (isButtonPressed(1, -4.5, 5, 1)) {
					edit = 2;
				}
				if (edit == 2) {
					drawTextArea("test", StdDraw.LIGHT_GRAY, StdDraw.WHITE, -4, -4.5, label, 5, 1, true, 2);
				} else {
					drawTextArea("test", StdDraw.LIGHT_GRAY, StdDraw.WHITE, -4, -4.5, label, 5, 1, false, 2);
				}
			}
			
			// Selection Premier joueur
			StdDraw.setPenColor(StdDraw.BLACK);
			
			StdDraw.textLeft(-10, -7, "1er Joueur");
			if (isButtonPressed(-2, -7, 1, 0.8)) {
				Grid.firstPlayers = 1;
			}
			if (Grid.firstPlayers == 1) {
				drawButton("J1", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, -2, -7, bouton, 1, 0.8, true);
			} else {
				drawButton("J1", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, -2, -7, bouton, 1, 0.8, false);
			}
			
			
			if (mode == 2) {
				if (isButtonPressed(1, -7, 1.5, 0.8)) {
					Grid.firstPlayers = 2;
				}
				if (Grid.firstPlayers == 2) {
					drawButton("Ordi", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, 1, -7, bouton, 1.5, 0.8, true);
				} else {
					drawButton("Ordi", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, 1, -7, bouton, 1.5, 0.8, false);
				}
			} else {
				if (isButtonPressed(1, -7, 1, 0.8)) {
					Grid.firstPlayers = 2;
				}
				if (Grid.firstPlayers == 2) {
					drawButton("J2", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, 1, -7, bouton, 1, 0.8, true);
				} else {
					drawButton("J2", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, 1, -7, bouton, 1, 0.8, false);
				}
			}
			
			
			// Bouton Commencer
			drawButton("Commencer", StdDraw.LIGHT_GRAY, StdDraw.WHITE, StdDraw.GRAY, 0, -10, bouton, 20, 1, false);
			if ((isButtonPressed(0, -10, 20, 1) && J1 != "" && J2 != "" && mode == 1) || (isButtonPressed(0, -10, 20, 1) && J1 != "" && mode == 2)) {
				Start = true;
			}
			
			StdDraw.show(16);
			StdDraw.clear();
		}
	}
	
	
	/**
	 * Affichage de texte au-dessus de la grille de jeu
	 * 
	 * @param p			joueur
	 * @param size		taille de la grille
	 * @param gagnant	gagnant de la partie
	 */
	public static void showHeader(Player p, int size, Player gagnant) {
		Font Titre = new Font("Futura", Font.PLAIN, 30);
		Font sousTitre = new Font("Futura", Font.ITALIC, 20);
		Color color;
		if (currentplayer == Grid.p1) {
			color = colorJ1;
		} else {
			color = colorJ2;
		}
		if (Grid.checkPartie()) {
			StdDraw.setFont(Titre);
			StdDraw.setPenColor(color);
			String annonce = "Tour de ";
			StdDraw.text(size/2, -0.8*marge, annonce+p.getName());
		} else {
			StdDraw.setFont(Titre);
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.text(size/2, -0.8*marge, "Fin de la partie !");
			StdDraw.setFont(sousTitre);
			StdDraw.setPenColor(StdDraw.BLACK);
			if (gagnant == null) {
				StdDraw.text(size/2, -0.3*marge, "Egalité !!");
			} else {
				StdDraw.text(size/2, -0.3*marge, gagnant.getName()+" a gagné !");
			}
		}
	}
	
	/**
	 * Affiche le score des joueurs sous la grille de jeu
	 * 
	 * @param p1 	joueur 1
	 * @param p2	joueur 2
	 */
	public static void showScore(Player p1, Player p2) {
		Font score = new Font("Futura", Font.PLAIN, 20);
		StdDraw.setFont(score);
		StdDraw.setPenColor(colorJ1);
		StdDraw.textLeft(0, Grid.size+marge*0.5, "Score "+p1.getName().toUpperCase().charAt(0)+": "+p1.getScore());
		StdDraw.setPenColor(colorJ2);
		StdDraw.textRight(Grid.size, Grid.size+marge*0.5, "Score "+p2.getName().toUpperCase().charAt(0)+": "+p2.getScore());
	}
	
	
	/**
	 * Met à jour l'affichage de la grille de jeu ainsi que de l'affichage de l'en-tête et du score
	 * 
	 * @param p	joueur
	 * @param gagnant
	 */
	public static void displayUpdate(Player p, Player gagnant) {
		draw(Grid.grid);
		showHeader(p, Grid.grid.length, gagnant);
		showScore(Grid.p1, Grid.p2);
		StdDraw.show(16);
		StdDraw.clear();
	}

	/** 
	 * Trace le plateau de jeu
	 * 
	 * @param grid grille de jeu
	 */
	public static void draw(Object[][] grid) {
		int size = grid[0].length;
		for (int k=0; k<size; k++) {
			for (Object obj: grid[k]) {
				if (obj instanceof Line) {
					((Line) obj).draw();
				}
			}
		}
		hover(currentplayer);
		for (int k=0; k<size; k++) {
			for (Object obj: grid[k]) {
				if (obj instanceof Point) {
					((Point) obj).draw();
				}
				if (obj instanceof Player) {
					((Player) obj).draw();
				}
			}
		}
	}
	
	/**
	 * Fait jouer et rejouer le joueur si nécessaire
	 * 
	 * @param p joueur
	 */
	public static void turn(Player p) {
		while (!Grid.turnIsFinished) {
			if (p.getIntelligence() > 0) {
				p.setLevel(Grid.grid);
			} else {
				setLine(Grid.grid);
			}
			displayUpdate(p, null);
		}
		Grid.turnIsFinished = false;
		boolean rejoue = Grid.checkSquare(p);
		
		if (rejoue && Grid.checkPartie()) {
			turn(p);
		}
	}
	
	
	/**
	 * Gère l'alternance J1/J2 et relance le jeu si nécessaire
	 * 
	 * @param p1	joueur 1
	 * @param p2	joueur 2
	 */
	public static void game(Player p1, Player p2) {
		partieCommencé = true;
		while (Grid.checkPartie()) {
			currentplayer = p1;
			turn(p1);
			if (Grid.checkPartie()) {
				currentplayer = p2;
				turn(p2);
			}
		}
		// Si fin de la partie
		while (!Grid.rejoue) {
			draw(Grid.grid);
			showHeader(null, Grid.grid.length, Grid.getWinner(p1, p2));
			showScore(Grid.p1, Grid.p2);
			drawButton("Rejouer", StdDraw.RED, StdDraw.WHITE, StdDraw.BOOK_RED, (Grid.size+1)/2, (Grid.size+1)/2, new Font("Futura", Font.BOLD, 40), 0.3*(Grid.size+1), 0.1*(Grid.size+1), false);
			Grid.rejoue = isButtonPressed((Grid.size+1)/2, (Grid.size+1)/2, 0.3*(Grid.size+1), 0.1*(Grid.size+1));
			StdDraw.show(16);
			StdDraw.clear();
		}
		Graphique.reset();
		Grid.reset();
	}
	
	
	public static void main(String[] args) {
		while (true) {
			init();
		}
	}
}
