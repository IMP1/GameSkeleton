package lib;

import java.awt.Rectangle;
import java.util.HashMap;

public class AutotileGenerator {
	private AutotileGenerator() {}
	
	private static class Autotile {
		
		private final static int CATEGORY_COUNT = 5;
		private final static int CORNER_COUNT = 4;
		
		final int[] superiors;
		final Rectangle[][] combinations = new Rectangle[CATEGORY_COUNT][CORNER_COUNT];
		
		public Autotile(int[] superiors, Rectangle quad) {
			this.superiors = superiors;
			int w = quad.width / 2;
			int h = quad.height / 2;
			for (int i = 0; i < CORNER_COUNT * CATEGORY_COUNT; i ++) {
				// CATEGORIES:
				// Surrounded by superiour tiles on all sides
				// Surrounded by superiour tiles, except the same tiles orthogongally
				// Surrounded by superiour tiles, except the same tiles vertically
				// Surrounded by superiour tiles, except the same tiles horizontally
				// Default
				int category = i / CORNER_COUNT;
				// CORNERS:
				// top left, top right, bottom left, bottom right
				int corner = i % CORNER_COUNT;
				int x = i % 2;
				int y = i / 2;
				combinations[category][corner] = new Rectangle(quad.x + x + w, quad.y + y * h, w, h);
			}
		}
		
	}
	
	public static class Tile {
		
		final int id;
		final Rectangle[] subtiles = new Rectangle[4];
		
		public Tile(int id) {
			this.id = id;
		}
		
		private void setCorner(int corner, Rectangle r) {
			subtiles[corner] = r;
		}

		public void draw(int x, int y) {
			jog.Graphics.draw(autotileImage, subtiles[0], x, y);
			jog.Graphics.draw(autotileImage, subtiles[1], x + subtiles[0].width, y);
			jog.Graphics.draw(autotileImage, subtiles[2], x, y + subtiles[0].height);
			jog.Graphics.draw(autotileImage, subtiles[3], x + subtiles[0].width, y + subtiles[0].height);
		}
		
	}
	
	private static HashMap<Integer, Autotile> autotiles = new HashMap<>();
	private static jog.Image autotileImage;
	public static boolean isOffMapSuperior = false;
	
	public static void setImage(jog.Image img) {
		autotileImage = img;
	}
	
	public static Autotile createAutotile(int id, int[] superiors, Rectangle quad) {
		Autotile a = new Autotile(superiors, quad);
		autotiles.put(id, a);
		return a;
	}
	
	public static void determineAutoTiles(Tile[][] map) {
		for (int j = 0; j < map.length; j ++) {
			for (int i = 0; i < map[j].length; i ++) {
				determineCorner(map, i, j, 0);
				determineCorner(map, i, j, 1);
				determineCorner(map, i, j, 2);
				determineCorner(map, i, j, 3);
			}
		}
	}
	
	private static void determineCorner(final Tile[][] map, final int i, final int j, final int corner) {
		Tile tile = map[j][i];
		Autotile autotile = autotiles.get(tile.id);
		int ox = (corner % 2) * 2 - 1;
		int oy = (corner / 2) * 2 - 1;
		if (isSuperior(map, i + ox, j, tile.id) && isSuperior(map, i, j + oy, tile.id)) {
			tile.setCorner(corner, autotile.combinations[0][corner]);
		} else if (isSuperior(map, i + ox, j + oy, tile.id) && !isSuperior(map, i + ox, j, tile.id) && !isSuperior(map, i, j + oy, tile.id)) {
			tile.setCorner(corner, autotile.combinations[1][corner]);
		} else if (isSuperior(map, i + ox, j, tile.id) && !isSuperior(map, i, j + oy, tile.id)) {
			tile.setCorner(corner, autotile.combinations[2][corner]);
		} else if (isSuperior(map, i, j + oy, tile.id) && !isSuperior(map, i + ox, j, tile.id)) {
			tile.setCorner(corner, autotile.combinations[3][corner]);
		} else {
			tile.setCorner(corner, autotile.combinations[4][corner]);
		}
	}
	
	private static boolean isSuperior(Tile[][] map, int i, int j, int tileID) {
		if (j < 0 || j >= map.length) return isOffMapSuperior;
		if (i < 0 || i >= map[j].length) return isOffMapSuperior;
		if (autotiles.get(tileID).superiors == null) return false;
		for (int n : autotiles.get(tileID).superiors) {
			if (n == map[j][i].id) return true;
		}
		return false;
	}
	
}
