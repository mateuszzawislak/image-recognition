package pl.edu.pw.elka.mzawisl2.model.segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.pw.elka.mzawisl2.model.Pixel;
import pl.edu.pw.elka.mzawisl2.model.Point;
import pl.edu.pw.elka.mzawisl2.model.util.PixelUtils;

public class Segmentator {

	/**
	 * Segmentuje przekazany obraz binarny.
	 * 
	 * @param pixels
	 *            - obraz poddawany segmentacji (binarny)
	 * @return listê segmentów obrazu binarnego
	 */
	public static List<Segment> getSegments(Pixel[][] pixels) {
		return segmentation(pixels);
	}

	/**
	 * Segmentuje przekazany obraz binarny.
	 * 
	 * Segmentacja przeprowadzana jest metod¹ rozrostu - przeszukiwaniem wszerz.
	 * 
	 * @param pixels
	 *            - obraz poddawany segmentacji (binarny)
	 * @return listê segmentów obrazu binarnego
	 */
	private static List<Segment> segmentation(Pixel[][] pixels) {
		int currentSegmentId = 0;
		Map<Integer, Segment> segments = new HashMap<Integer, Segment>();
		int height = pixels.length;
		int width = pixels[0].length;

		boolean[][] binaryPixels = new boolean[height][width];
		boolean[][] candidateTaken = new boolean[height][width];
		int[][] pointsAssignment = new int[height][width];

		for (int row = 0; row < height; ++row)
			for (int col = 0; col < width; ++col) {
				binaryPixels[row][col] = PixelUtils.isWhite(pixels[row][col]);
				candidateTaken[row][col] = false;
				pointsAssignment[row][col] = -1;
			}

		for (int row = 0; row < height; ++row) {
			for (int col = 0; col < width; ++col) {
				if (-1 == pointsAssignment[row][col]) {
					Segment segment = new Segment(currentSegmentId++);
					segments.put(segment.getId(), segment);
					boolean isWhiteSegment = binaryPixels[row][col];

					List<Point> candidates = new ArrayList<Point>();
					Point point = new Point(row, col);
					candidates.add(point);
					candidateTaken[row][col] = true;

					while (!candidates.isEmpty()) {
						Point candidate = candidates.get(0);
						candidates.remove(0);
						if (isWhiteSegment == binaryPixels[candidate.getRow()][candidate.getColumn()]) {
							segment.addPoint(candidate);
							pointsAssignment[candidate.getRow()][candidate.getColumn()] = segment.getId();

							if (0 <= candidate.getRow() - 1) {
								int r = candidate.getRow() - 1;
								int c = candidate.getColumn();
								if (-1 == pointsAssignment[r][c] && !candidateTaken[r][c]) {
									candidates.add(new Point(r, c));
									candidateTaken[r][c] = true;
								}
							}

							if (0 <= candidate.getColumn() - 1) {
								int r = candidate.getRow();
								int c = candidate.getColumn() - 1;
								if (-1 == pointsAssignment[r][c] && !candidateTaken[r][c]) {
									candidates.add(new Point(r, c));
									candidateTaken[r][c] = true;
								}
							}

							if (binaryPixels.length > candidate.getRow() + 1) {
								int r = candidate.getRow() + 1;
								int c = candidate.getColumn();
								if (-1 == pointsAssignment[r][c] && !candidateTaken[r][c]) {
									candidates.add(new Point(r, c));
									candidateTaken[r][c] = true;
								}
							}

							if (binaryPixels[0].length > candidate.getColumn() + 1) {
								int r = candidate.getRow();
								int c = candidate.getColumn() + 1;
								if (-1 == pointsAssignment[r][c] && !candidateTaken[r][c]) {
									candidates.add(new Point(r, c));
									candidateTaken[r][c] = true;
								}
							}
						} else {
							candidateTaken[candidate.getRow()][candidate.getColumn()] = false;
						}
					}
				}
			}
		}

		List<Segment> results = new ArrayList<Segment>();
		for (Integer id : segments.keySet())
			results.add(segments.get(id));

		return results;
	}

}
