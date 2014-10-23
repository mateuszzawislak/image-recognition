package pl.edu.pw.elka.mzawisl2.model;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.model.filter.FilterType;
import pl.edu.pw.elka.mzawisl2.model.finder.Finder;
import pl.edu.pw.elka.mzawisl2.model.segment.RadioactiveLogo;
import pl.edu.pw.elka.mzawisl2.model.segment.Segment;
import pl.edu.pw.elka.mzawisl2.model.segment.Segmentator;
import pl.edu.pw.elka.mzawisl2.model.util.FilterUtils;
import pl.edu.pw.elka.mzawisl2.model.util.PixelUtils;

/**
 * Klasa odpowiedzialna za g³ówn¹ logikê przetwarzania obrazów.
 * 
 * @author Mateusz
 * 
 */
public class ImageProcessor {

	private static Logger log = Logger.getLogger(ImageProcessor.class);

	private BufferedImage image;

	private Pixel[][] pixels;

	private static final int MIN_SEGMENT_PIXELS_NUMBER = 5;

	private static final int MIN_SEGMENT_PIXELS_NUMBER_FOR_BIGGER_SYMBOLS = 30;

	public ImageProcessor(final BufferedImage image) {
		this.image = image;
		pixels = new Pixel[image.getHeight()][image.getWidth()];
		readPixels(image);
	}

	/**
	 * Przeprowadza proces poprawy jakoœci obrazu oraz jego binaryzacji.
	 * 
	 * @return przetworzony obraz
	 */
	public BufferedImage binarization() {
		Pixel[][] betterPixels = FilterUtils.medianFilterImage(pixels, 3);
		betterPixels = FilterUtils.filterImage(betterPixels, FilterType.EDGE_DETECTION_MATRIX.getMatrix(),
				(int) Math.sqrt(FilterType.EDGE_DETECTION_MATRIX.getMatrix().length));

		betterPixels = FilterUtils.maxFilterImage(betterPixels, 3);
		betterPixels = FilterUtils.invertPixels(betterPixels);
		betterPixels = FilterUtils.binarizeImage(betterPixels, 180);
		return PixelUtils.createImageFromPixels(betterPixels);
	}

	/**
	 * Binaryzacja z zadanym progiem.
	 * 
	 * @param level
	 *            - próg binaryzacji
	 * @return przetworzony obraz
	 */
	public BufferedImage binarization(int level) {
		return PixelUtils.createImageFromPixels(FilterUtils.binarizeImage(pixels, level));
	}

	/**
	 * Binaryzacja poszczególnych sk³¹dowych pikesli w skali RGB.
	 * 
	 * @param rLevel
	 *            - próg dla koloru czerwonego
	 * @param gLevel
	 *            - próg dla koloru zielonego
	 * @param bLevel
	 *            - próg dla koloru niebieskiego
	 * @return przetworzony obraz
	 */
	public BufferedImage binarization(int rLevel, int gLevel, int bLevel) {
		return PixelUtils.createImageFromPixels(FilterUtils.binarizeImage(pixels, rLevel, gLevel, bLevel));
	}

	/**
	 * Filtruje zdjêcie z zadanym filtrem.
	 * 
	 * @param filterType
	 *            - rodzaj filtru jakiemu ma byæ poddany obraz.
	 * @return przetworzony obraz
	 */
	public BufferedImage filterImage(FilterType filterType) {
		return PixelUtils.createImageFromPixels(FilterUtils.filterImage(pixels, filterType.getMatrix(),
				(int) Math.sqrt(filterType.getMatrix().length)));
	}

	/**
	 * Przeprowadza pe³ny proces rozpoznawania symboli radioaktywnych na
	 * obrazie. Algorytm ten wykonuje efekt erozji. Ma³e symbole nie zostan¹
	 * zatem wykryte!
	 * 
	 * @return przetworzony obraz z zaznaczonymi symbolami radioaktywnoœci
	 */
	public BufferedImage findBigRadioactiveSymbolsWithMinMax() {
		Pixel[][] betterPixels = FilterUtils.filterImage(pixels, FilterType.GAUSS_1.getMatrix(),
				(int) Math.sqrt(FilterType.GAUSS_1.getMatrix().length));

		betterPixels = FilterUtils.binarizeImage(betterPixels, 0, 70, 0);

		betterPixels = FilterUtils.minFilterImage(betterPixels, 3);
		betterPixels = FilterUtils.maxFilterImage(betterPixels, 3);

		List<Segment> segments = Segmentator.getSegments(betterPixels);
		List<GeometricMoments> minSegments = new ArrayList<GeometricMoments>();

		int index = 0;
		for (Segment segment : segments) {
			if (segment.getPoints().size() > MIN_SEGMENT_PIXELS_NUMBER_FOR_BIGGER_SYMBOLS) {
				log.debug(index++);
				GeometricMoments geometricMoments = new GeometricMoments(segment);
				minSegments.add(geometricMoments);
				log.debug(geometricMoments.toString());
			}
		}

		Finder finder = new Finder(minSegments);
		Pixel[][] imageCopy = PixelUtils.copyPixels(pixels);

		if (null != finder.getWings() && !finder.getWings().isEmpty()) {
			for (RadioactiveLogo geomMoment : finder.getLogos()) {
				PixelUtils.drawRectangleOnPixels(imageCopy, geomMoment.getLeftTopPoint().getRow(),
						geomMoment.getLeftTopPoint().getColumn(), geomMoment.getWidth(), geomMoment.getHeight(), 5, new Pixel(0, 0, 255));
			}
		}

		return PixelUtils.createImageFromPixels(imageCopy);
	}

	/**
	 * Przeprowadza pe³ny proces rozpoznawania symboli radioaktywnych na
	 * obrazie.
	 * 
	 * @return przetworzony obraz z zaznaczonymi symbolami radioaktywnoœci
	 */
	public BufferedImage findRadioactiveSymbols() {
		Pixel[][] betterPixels = FilterUtils.filterImage(pixels, FilterType.GAUSS_1.getMatrix(),
				(int) Math.sqrt(FilterType.GAUSS_1.getMatrix().length));

		betterPixels = FilterUtils.binarizeImage(betterPixels, 0, 70, 0);

		List<Segment> segments = Segmentator.getSegments(betterPixels);
		List<GeometricMoments> minSegments = new ArrayList<GeometricMoments>();

		int index = 0;
		for (Segment segment : segments) {
			if (segment.getPoints().size() > MIN_SEGMENT_PIXELS_NUMBER) {
				log.debug("Segment id: " + index++);
				GeometricMoments geometricMoments = new GeometricMoments(segment);
				minSegments.add(geometricMoments);
				log.debug(geometricMoments.toString());
			}
		}

		Finder finder = new Finder(minSegments);
		Pixel[][] imageCopy = PixelUtils.copyPixels(pixels);

		if (null != finder.getWings() && !finder.getWings().isEmpty()) {
			for (RadioactiveLogo geomMoment : finder.getLogos()) {
				PixelUtils.drawRectangleOnPixels(imageCopy, geomMoment.getLeftTopPoint().getRow(),
						geomMoment.getLeftTopPoint().getColumn(), geomMoment.getWidth(), geomMoment.getHeight(), 5, new Pixel(0, 0, 255));
			}
		}

		return PixelUtils.createImageFromPixels(imageCopy);
	}

	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Odwraca wartoœci poszczególnych sk³¹dowych RGB obrazu.
	 * 
	 * @return obraz z odwróconymi kolorami
	 */
	public BufferedImage getInvertedImage() {
		return PixelUtils.createImageFromPixels(FilterUtils.invertPixels(pixels));
	}

	/**
	 * Filtruje obraz przy pomocy rankingowego filtra maksymalnego.
	 * 
	 * @param size
	 *            - rozmiar kwadratowego okna filtra
	 * @return przetworzony obraz
	 */
	public BufferedImage maxFilter(int size) {
		return PixelUtils.createImageFromPixels(FilterUtils.maxFilterImage(pixels, size));
	}

	/**
	 * Filtruje obraz przy pomocy rankingowego filtra medianowego.
	 * 
	 * @param size
	 *            - rozmiar kwadratowego okna filtra
	 * @return przetworzony obraz
	 */
	public BufferedImage medianFilter(int size) {
		return PixelUtils.createImageFromPixels(FilterUtils.medianFilterImage(pixels, size));
	}

	/**
	 * Filtruje obraz przy pomocy rankingowego filtra minimalnego.
	 * 
	 * @param size
	 *            - rozmiar kwadratowego okna filtra
	 * @return przetworzony obraz
	 */
	public BufferedImage minFilter(int size) {
		return PixelUtils.createImageFromPixels(FilterUtils.minFilterImage(pixels, size));
	}

	/**
	 * Przekszta³ca BufferedImage w tablicê obiektów typu Pixel.
	 * 
	 * @param image
	 *            - obraz do przetworzenia
	 */
	private void readPixels(BufferedImage image) {
		int[] packedPixels = new int[image.getWidth() * image.getHeight()];
		PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), packedPixels, 0, image.getWidth());
		try {
			pixelgrabber.grabPixels();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}

		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				int packedPixel = packedPixels[row * image.getWidth() + col];
				Pixel pixel = new Pixel();
				pixel.setRed((packedPixel >> 16) & 0xFF);
				pixel.setGreen((packedPixel >> 8) & 0xFF);
				pixel.setBlue((packedPixel >> 0) & 0xFF);
				pixels[row][col] = pixel;
			}
		}
	}

	/**
	 * Przeprowadza proces segmentacji na binarnym obrazie.
	 * 
	 * @retur przetworzony obraz
	 */
	public BufferedImage segmentation() {
		List<Segment> segments = Segmentator.getSegments(pixels);
		Pixel[][] newImage = PixelUtils.copyPixels(pixels);

		for (Segment segment : segments) {
			if (segment.getPoints().size() > MIN_SEGMENT_PIXELS_NUMBER) {
				Pixel randomPixel = PixelUtils.getRandomPixel();
				for (Point point : segment.getPoints()) {
					newImage[point.getRow()][point.getColumn()] = new Pixel(randomPixel);
				}
			}
		}

		return PixelUtils.createImageFromPixels(newImage);
	}

}
