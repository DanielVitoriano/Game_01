package World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game_main.Game;
import entities.Enemy;
import entities.Entity;
import entities.MoneyBag;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy ++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000) { //preto
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
					else if(pixelAtual == 0xFFffffff) { //branco
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					}
					else if(pixelAtual == 0xFFff0000) { //vermelho
						Enemy en = new Enemy(xx * 16, yy * 15, 32, 32, Entity.enemy);
						Game.entities.add(en);
						Game.enemies.add(en);
					}
					else if(pixelAtual == 0xFF000cff) { //azul
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					}
					else if(pixelAtual == 0xFF0bf300) {//verde
						Game.entities.add(new MoneyBag(xx * 16, yy * 16, 16,16, Entity.money_bag));
					}

				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
		xNext += 8;
		yNext += 24;
		
		int x1 = xNext / TILE_SIZE; 
		int y1 = yNext / TILE_SIZE;
		if(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) {
			return false;
		}
		
		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		if(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) {
			return false;
		}
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext - 8 + TILE_SIZE - 1) / TILE_SIZE;
		if(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) {
			return false;
		}
		
		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext - 8 + TILE_SIZE - 1) / TILE_SIZE;
		if(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile){
			return false;
		}
		return true;
		
		/*return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile)); */
	}
	
	public void render(Graphics g) {
		
		int xstart = Camera.getX() / 16;
		int ystart = Camera.getY() / 16;
		
		int xfinal = xstart + (Game.WIDTH / 16);
		int yfinal = ystart + (Game.HEIGHT / 16);
		
		for(int xx = xstart; xx <= xfinal; xx ++) {
			for(int yy = ystart; yy <= yfinal; yy ++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + ( yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
