package pl.edu.pw.elka.mzawisl2.model.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.List;
import java.util.Random;

import pl.edu.pw.elka.mzawisl2.model.Pixel;
import pl.edu.pw.elka.mzawisl2.model.Point;
import pl.edu.pw.elka.mzawisl2.model.segment.Segment;

public class PixelUtils {

	/**
	 * Konwertuje wartoœæ RGB piksela na int.
	 * 
	 * @param pixel
	 *            - zadany piksel
	 * @return wartoœæ RGB piksela wyra¿ona w postaci int
	 */
	public static int convertPixelToInt(Pixel pixel) {
		return (0xFF << 24) | (pixel.getRed() << 16) | (pixel.getGreen() << 8) | pixel.getBlue();
	}

	/**
	 * Klonuje piksele do nowej tablicy.
	 * 
	 * @param pixels
	 *            - piksele do sklonowania.
	 * @return Sklonowana tablica pikseli.
	 */
	public static Pixel[][] copyPixels(Pixel[][] pixels) {
		Pixel[][] newPixels = new Pixel[pixels.length][pixels[0].length];

		for (int row = 0; row < pixels.length; ++row)
			for (int col = 0; col < pixels[0].length; ++col)
				newPixels[row][col] = new Pixel(pixels[row][col]);

		return newPixels;
	}

	/**
	 * Konwertuje tablicê pikseli na obrazek BufferedImage.
	 * 
	 * @param pixels
	 *            - tablica pikseli do konwersji na BufferedImage
	 * @return skonwertowany obraz BufferedImage
	 */
	public static BufferedImage createImageFromPixels(Pixel[][] pixels) {
		int[] intPixels = new int[pixels.length * pixels[0].length];

		for (int row = 0; row < pixels.length; ++row)
			for (int col = 0; col < pixels[0].length; ++col)
				intPixels[row * pixels[0].length + col] = convertPixelToInt(pixels[row][col]);

		Image img = Toolkit.getDefaultToolkit().createImage(
				new MemoryImageSource(pixels[0].length, pixels.length, intPixels, 0, pixels[0].length));

		BufferedImage bufImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufImg.getGraphics();
		g.drawImage(img, 0, 0, null);

		return bufImg;
	}

	/**
	 * Rysuje czerwony, wype³niony prostok¹t na zadanym obrazie.
	 * 
	 * @param pixels
	 *            - obraz na którym ma byæ narysowany prostok¹t
	 * @param leftTopRow
	 *            - numer wiersza lewego-górnego piksela prostok¹ta
	 * @param leftTopColumn
	 *            - numer kolumny lewego-górnego piksela prostok¹ta
	 * @param width
	 *            - szerokoœæ prostok¹ta
	 * @param height
	 *            - wysokoœæ prostok¹ta
	 */
	public static void drawFilledRectangleOnPixels(Pixel[][] pixels, int leftTopRow, int leftTopColumn, int width, int height) {
		Pixel pixel = new Pixel(255, 0, 0);

		for (int row = 0; row < height; ++row)
			for (int col = 0; col < width; ++col)
				pixels[row + leftTopRow][col + leftTopColumn] = new Pixel(pixel);
	}

	/**
	 * Rysuje obwódkê prostok¹ta na zadanym obrazie.
	 * 
	 * @param pixels
	 *            - obraz na którym ma byæ narysowany prostok¹t
	 * @param leftTopRow
	 *            - numer wiersza lewego-górnego piksela prostok¹ta
	 * @param leftTopColumn
	 *            - numer kolumny lewego-górnego piksela prostok¹ta
	 * @param width
	 *            - szerokoœæ prostok¹ta
	 * @param height
	 *            - wysokoœæ prostok¹ta
	 * @param thickness
	 *            - gruboœæ obwódki prostok¹ta
	 * @param color
	 *            - kolor prostok¹ta
	 * 
	 */
	public static void drawRectangleOnPixels(Pixel[][] pixels, int leftTopRow, int leftTopColumn, int width, int height, int thickness,
			Pixel color) {
		for (int row = leftTopRow; row < leftTopRow + height; ++row)
			for (int col = leftTopColumn; col < leftTopColumn + width; ++col) {
				if ((row <= leftTopRow + thickness || row >= leftTopRow + height - thickness)
						|| (col <= leftTopColumn + thickness || col >= leftTopColumn + width - thickness))
					pixels[row][col] = new Pixel(color);
			}
	}

	/**
	 * Koloruje zadane segmenty na przekazanym obrazie.
	 * 
	 * @param pixels
	 *            - oraz na którym maj¹ byæ pokolorowane segmenty
	 * @param segments
	 *            - segmenty do pokolorowania
	 */
	public static void drawSegmentsOnPixels(Pixel[][] pixels, List<Segment> segments) {
		for (Segment segment : segments) {
			Pixel randomPixel = PixelUtils.getRandomPixel();
			for (Point point : segment.getPoints()) {
				pixels[point.getRow()][point.getColumn()] = new Pixel(randomPixel);
			}
		}
	}

	public static Pixel getRandomPixel() {
		Random generator = new Random();
		return new Pixel(generator.nextInt(256), generator.nextInt(256), generator.nextInt(256));
	}

	public static boolean isWhite(Pixel pixel) {
		return 255 == pixel.getRed() && 255 == pixel.getGreen() && 255 == pixel.getBlue();
	}

}
