package game.main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Resources {
	
	public static BufferedImage boardSquare;
	
	public static void load() {
		boardSquare=loadImage("boardSquare.png");
	}
	
	private static BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(Resources.class.getResourceAsStream("/resources/"+filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
}
