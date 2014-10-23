package pl.edu.pw.elka.mzawisl2.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.edu.pw.elka.mzawisl2.model.Pixel;

public class FilterUtils {

	/**
	 * Binaryzuje obraz z zadan¹ granic¹.
	 * 
	 * @param pixels
	 *            - obraz poddawany binaryzacji
	 * @param threshold
	 *            - granica binaryzacji RGB
	 * @return przetworzony obraz
	 */
	public static Pixel[][] binarizeImage(Pixel[][] pixels, final int threshold) {
		return binarizeImage(pixels, threshold, threshold, threshold);
	}

	/**
	 * Binaryzuje obraz z zadan¹ymi wartoœciami granicznymi dla poszczególnych
	 * sk³¹dowych skali RGB.
	 * 
	 * @param pixels
	 *            - obraz poddawany binaryzacji
	 * @param redThreshold
	 *            - granica sk³adowej czerwonej
	 * @param greenThreshold
	 *            - granica sk³adowej zielonej
	 * @param blueThreshold
	 *            - granica sk³adowej niebieskiej
	 * @return przetworzony obraz
	 */
	public static Pixel[][] binarizeImage(Pixel[][] pixels, final int redThreshold, final int greenThreshold, final int blueThreshold) {
		int height = pixels.length;
		int width = pixels[0].length;

		Pixel[][] binarizedImage = new Pixel[height][width];

		for (int row = 0; row < height; ++row)
			for (int col = 0; col < width; ++col) {
				int red = pixels[row][col].getRed();
				int green = pixels[row][col].getGreen();
				int blue = pixels[row][col].getBlue();

				if (red > redThreshold && green > greenThreshold && blue > blueThreshold)
					binarizedImage[row][col] = new Pixel(255, 255, 255);
				else
					binarizedImage[row][col] = new Pixel(0, 0, 0);
			}

		return binarizedImage;
	}

	/**
	 * Filtruje obraz przekazanym filtrem o wskazanym rozmiarze okna.
	 * 
	 * @param pixels
	 *            - obraz do przetworzenia
	 * @param filter
	 *            - filtr
	 * @param filterSize
	 *            - rozmiar kwadratowego okna filtru
	 * @return przetworzony obraz
	 */
	public static Pixel[][] filterImage(Pixel[][] pixels, int[] filter, int filterSize) {
		if (filterSize % 2 != 1)
			throw new RuntimeException("Ivalid filter!");

		Pixel[][] results = new Pixel[pixels.length][pixels[0].length];

		int filterSum = 0;
		for (int i = 0; i < filter.length; ++i)
			filterSum += filter[i];
		filterSum = 0 == filterSum ? 1 : filterSum;

		for (int row = 0; row < pixels.length; ++row)
			for (int col = 0; col < pixels[0].length; ++col) {
				if ((row >= filterSize / 2 && row < (pixels.length - filterSize / 2)) && col >= filterSize / 2
						&& col < (pixels[0].length - filterSize / 2)) {
					int sumRed = 0, sumGreen = 0, sumBlue = 0;

					for (int r = row - filterSize / 2, i = 0; i < filterSize; ++i, ++r) {
						for (int c = col - filterSize / 2, j = 0; j < filterSize; ++j, ++c) {
							sumRed += pixels[r][c].getRed() * filter[i * filterSize + j];
							sumGreen += pixels[r][c].getGreen() * filter[i * filterSize + j];
							sumBlue += pixels[r][c].getBlue() * filter[i * filterSize + j];
						}
					}

					Pixel pixel = new Pixel();
					pixel.setRed(BaseUtils.getIntModulo256(sumRed / filterSum));
					pixel.setGreen(BaseUtils.getIntModulo256(sumGreen / filterSum));
					pixel.setBlue(BaseUtils.getIntModulo256(sumBlue / filterSum));
					results[row][col] = pixel;
				} else {
					results[row][col] = new Pixel(pixels[row][col]);
				}
			}

		return results;
	}

	/**
	 * Odwraca wartoœæ sk³¹dowych RGB przekazanego obrazu.
	 * 
	 * @param pixels
	 *            - obraz do przetworzenia
	 * @return przetworzony obraz
	 */
	public static Pixel[][] invertPixels(final Pixel[][] pixels) {
		Pixel[][] result = new Pixel[pixels.length][pixels[0].length];

		for (int row = 0; row < pixels.length; ++row)
			for (int col = 0; col < pixels[0].length; ++col) {
				Pixel originalPixel = pixels[row][col];
				result[row][col] = new Pixel(255 - originalPixel.getRed(), 255 - originalPixel.getGreen(), 255 - originalPixel.getBlue());
			}

		return result;
	}

	/**
	 * Przetwarza obraz rankingowym filtrem maksymalnym o zadanym rozmiarze
	 * okna.
	 * 
	 * @param pixels
	 *            - obraz do przetworzenia
	 * @param filterSize
	 *            - rozmiar okna filtru
	 * @return przetworzony obraz
	 */
	public static Pixel[][] maxFilterImage(Pixel[][] pixels, int filterSize) {
		if (filterSize % 2 != 1)
			throw new RuntimeException("Ivalid filter!");

		Pixel[][] results = new Pixel[pixels.length][pixels[0].length];

		for (int row = 0; row < pixels.length; ++row)
			for (int col = 0; col < pixels[0].length; ++col) {
				if ((row >= filterSize / 2 && row < (pixels.length - filterSize / 2)) && col >= filterSize / 2
						&& col < (pixels[0].length - filterSize / 2)) {
					List<Integer> redValues = new ArrayList<Integer>();
					List<Integer> blueValues = new ArrayList<Integer>();
					List<Integer> greenValues = new ArrayList<Integer>();

					for (int r = row - filterSize / 2, i = 0; i < filterSize; ++i, ++r) {
						for (int c = col - filterSize / 2, j = 0; j < filterSize; ++j, ++c) {
							redValues.add(pixels[r][c].getRed());
							blueValues.add(pixels[r][c].getBlue());
							greenValues.add(pixels[r][c].getGreen());
						}
					}

					Collections.sort(redValues);
					Collections.sort(blueValues);
					Collections.sort(greenValues);

					Pixel pixel = new Pixel();
					pixel.setRed(redValues.get(filterSize * filterSize - 1));
					pixel.setBlue(blueValues.get(filterSize * filterSize - 1));
					pixel.setGreen(greenValues.get(filterSize * filterSize - 1));
					results[row][col] = pixel;
				} else {
					results[row][col] = new Pixel(pixels[row][col]);
				}
			}

		return results;
	}

	/**
	 * Przetwarza obraz rankingowym filtrem medianowym o zadanym rozmiarze
	 * okna.
	 * 
	 * @param pixels
	 *            - obraz do przetworzenia
	 * @param filterSize
	 *            - rozmiar okna filtru
	 * @return przetworzony obraz
	 */
	public static Pixel[][] medianFilterImage(Pixel[][] pixels, int filterSize) {
		if (filterSize % 2 != 1)
			throw new RuntimeException("Ivalid filter!");

		Pixel[][] results = new Pixel[pixels.length][pixels[0].length];

		for (int row = 0; row < pixels.length; ++row)
			for (int col = 0; col < pixels[0].length; ++col) {
				if ((row >= filterSize / 2 && row < (pixels.length - filterSize / 2)) && col >= filterSize / 2
						&& col < (pixels[0].length - filterSize / 2)) {
					List<Integer> redValues = new ArrayList<Integer>();
					List<Integer> blueValues = new ArrayList<Integer>();
					List<Integer> greenValues = new ArrayList<Integer>();

					for (int r = row - filterSize / 2, i = 0; i < filterSize; ++i, ++r) {
						for (int c = col - filterSize / 2, j = 0; j < filterSize; ++j, ++c) {
							redValues.add(pixels[r][c].getRed());
							blueValues.add(pixels[r][c].getBlue());
							greenValues.add(pixels[r][c].getGreen());
						}
					}

					Collections.sort(redValues);
					Collections.sort(blueValues);
					Collections.sort(greenValues);

					Pixel pixel = new Pixel();
					pixel.setRed(redValues.get(filterSize * filterSize / 2));
					pixel.setBlue(blueValues.get(filterSize * filterSize / 2));
					pixel.setGreen(greenValues.get(filterSize * filterSize / 2));
					results[row][col] = pixel;
				} else {
					results[row][col] = new Pixel(pixels[row][col]);
				}
			}

		return results;
	}

	/**
	 * Przetwarza obraz rankingowym filtrem minimalnym o zadanym rozmiarze
	 * okna.
	 * 
	 * @param pixels
	 *            - obraz do przetworzenia
	 * @param filterSize
	 *            - rozmiar okna filtru
	 * @return przetworzony obraz
	 */
	public static Pixel[][] minFilterImage(Pixel[][] pixels, int filterSize) {
		if (filterSize % 2 != 1)
			throw new RuntimeException("Ivalid filter!");

		Pixel[][] results = new Pixel[pixels.length][pixels[0].length];

		for (int row = 0; row < pixels.length; ++row)
			for (int col = 0; col < pixels[0].length; ++col) {
				if ((row >= filterSize / 2 && row < (pixels.length - filterSize / 2)) && col >= filterSize / 2
						&& col < (pixels[0].length - filterSize / 2)) {
					List<Integer> redValues = new ArrayList<Integer>();
					List<Integer> blueValues = new ArrayList<Integer>();
					List<Integer> greenValues = new ArrayList<Integer>();

					for (int r = row - filterSize / 2, i = 0; i < filterSize; ++i, ++r) {
						for (int c = col - filterSize / 2, j = 0; j < filterSize; ++j, ++c) {
							redValues.add(pixels[r][c].getRed());
							blueValues.add(pixels[r][c].getBlue());
							greenValues.add(pixels[r][c].getGreen());
						}
					}

					Collections.sort(redValues);
					Collections.sort(blueValues);
					Collections.sort(greenValues);

					Pixel pixel = new Pixel();
					pixel.setRed(redValues.get(0));
					pixel.setBlue(blueValues.get(0));
					pixel.setGreen(greenValues.get(0));
					results[row][col] = pixel;
				} else {
					results[row][col] = new Pixel(pixels[row][col]);
				}
			}

		return results;
	}
}
