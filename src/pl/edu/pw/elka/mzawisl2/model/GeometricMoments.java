package pl.edu.pw.elka.mzawisl2.model;

import pl.edu.pw.elka.mzawisl2.model.segment.Segment;

/**
 * Klasa wyliczaj¹ca oraz przechowuj¹ca wartoœci niezmiennników geometrycznych
 * dla zadanego segmentu.
 * 
 * @author Mateusz
 * 
 */
public class GeometricMoments {

	private Segment segment;

	private double m[][] = new double[4][4];

	private double mc[][] = new double[4][4];

	private double M[] = new double[10];

	private double W[] = new double[9];

	private int centerRow, centerCol;

	private int maxRow, maxCol, minRow, minCol;

	private double rMin, rMax;

	/** maksymalna odleg³oœæ miêdzy punktami segmentu */
	private double lMax;

	private int area;

	private int parimeter;

	public GeometricMoments(Segment segment) {
		this.segment = segment;
		calculateNormalMoments();
		calculateCentralMoments();
		calculateM();
		calculateW();
	}

	private void calculateBasicParameters() {
		area = segment.getPoints().size();

		minRow = Integer.MAX_VALUE;
		minCol = Integer.MAX_VALUE;
		maxRow = 0;
		maxCol = 0;

		for (Point point : segment.getPoints()) {
			minRow = Math.min(point.getRow(), minRow);
			maxRow = Math.max(point.getRow(), maxRow);
			minCol = Math.min(point.getColumn(), minCol);
			maxCol = Math.max(point.getColumn(), maxCol);
		}

		int segmentWidth = maxCol - minCol + 1;
		int segmentHeight = maxRow - minRow + 1;

		boolean[][] binaryPixels = new boolean[segmentHeight][segmentWidth];

		for (int row = 0; row < segmentHeight; ++row) {
			for (int col = 0; col < segmentWidth; ++col) {
				binaryPixels[row][col] = false;
			}
		}

		// stwórz czarno-bia³y prostok¹t obejmuj¹cy segment
		for (Point point : segment.getPoints()) {
			binaryPixels[point.getRow() - minRow][point.getColumn() - minCol] = true;
		}

		rMax = Integer.MIN_VALUE;
		rMin = Integer.MAX_VALUE;

		for (int row = 0; row < segmentHeight; ++row) {
			for (int col = 0; col < segmentWidth; ++col) {
				if (binaryPixels[row][col]) {
					if (0 == row || 0 == col || segmentHeight - 1 == row || segmentWidth - 1 == col) {
						parimeter++;

						double dist = Math.sqrt(Math.pow(centerRow - row, 2) + Math.pow(centerCol - col, 2));
						rMin = Math.min(rMin, dist);
						rMax = Math.max(rMax, dist);
					} else {
						for (int r = row - 1; r <= row + 1; ++r) {
							for (int c = col - 1; c <= col + 1; ++c) {
								if (!binaryPixels[r][c]) {
									parimeter++;

									double dist = Math.sqrt(Math.pow(centerRow - r, 2) + Math.pow(centerCol - c, 2));
									rMin = Math.min(rMin, dist);
									rMax = Math.max(rMax, dist);

									break;
								}
							}
						}
					}
				}
			}
		}

		lMax = (maxRow - minRow > maxCol - minCol ? maxRow - minRow : maxCol - minCol);
	}

	private void calculateCentralMoments() {
		mc[0][0] = m[0][0];
		mc[0][1] = 0;
		mc[1][0] = 0;
		mc[1][1] = m[1][1] - m[1][0] * m[0][1] / m[0][0];
		mc[2][0] = m[2][0] - m[1][0] * m[1][0] / m[0][0];
		mc[0][2] = m[0][2] - m[0][1] * m[0][1] / m[0][0];
		mc[2][1] = m[2][1] - 2 * m[1][1] * centerRow - m[2][0] * centerCol + 2 * m[0][1] * centerRow * centerRow;
		mc[1][2] = m[1][2] - 2 * m[1][1] * centerCol - m[0][2] * centerRow + 2 * m[1][0] * centerCol * centerCol;
		mc[3][0] = m[3][0] - 3 * m[2][0] * centerRow + 2 * m[1][0] * centerRow * centerRow;
		mc[0][3] = m[0][3] - 3 * m[0][2] * centerCol + 2 * m[0][1] * centerCol * centerCol;
	}

	/**
	 * Wyznacza wartoœci wszystkich niezmienników geometrycznych.
	 */
	private void calculateM() {
		M[0] = (mc[2][0] + mc[0][2]) / Math.pow(mc[0][0], 2);
		M[1] = (Math.pow(mc[2][0] - mc[0][2], 2) + 4 * Math.pow(mc[1][1], 2)) / Math.pow(mc[0][0], 4);
		M[2] = (Math.pow(mc[3][0] - 3 * mc[1][2], 2) + Math.pow(3 * mc[2][1] - mc[0][3], 2)) / Math.pow(mc[0][0], 5);
		M[3] = (Math.pow(mc[3][0] + mc[1][2], 2) + Math.pow(mc[2][1] + mc[0][3], 2)) / Math.pow(mc[0][0], 5);
		M[4] = ((mc[3][0] - 3 * mc[1][2]) * (mc[3][0] + mc[1][2])
				* (Math.pow(mc[3][0] + mc[1][2], 2) - 3 * Math.pow(mc[2][1] + mc[0][3], 2)) + (3 * mc[2][1] - mc[0][3])
				* (mc[2][1] + mc[0][3]) * (3 * Math.pow(mc[3][0] + mc[1][2], 2) - Math.pow(mc[2][1] + mc[0][3], 2)))
				/ Math.pow(mc[0][0], 10);
		M[5] = ((mc[2][0] - mc[0][2]) * (Math.pow(mc[3][0] + mc[1][2], 2) - Math.pow(mc[2][1] + mc[0][3], 2)) + 4 * mc[1][1]
				* (mc[3][0] + mc[1][2]) * (mc[2][1] + mc[0][3]))
				/ Math.pow(mc[0][0], 7);
		M[6] = (mc[2][0] * mc[0][2] - Math.pow(mc[1][1], 2)) / Math.pow(mc[0][0], 4);
		M[7] = (mc[3][0] * mc[1][2] + mc[2][1] * mc[0][3] - Math.pow(mc[1][2], 2) - Math.pow(mc[2][1], 2)) / Math.pow(mc[0][0], 5);
		M[8] = (mc[2][0] * (mc[2][1] * mc[0][3] - mc[1][2] * mc[1][2]) + mc[0][2] * (mc[0][3] * mc[1][2] - Math.pow(mc[2][1], 2)) - mc[1][1]
				* (mc[3][0] * mc[0][3] - mc[2][1] * mc[1][2]))
				/ Math.pow(mc[0][0], 7);
		M[9] = (Math.pow(mc[3][0] * mc[0][3] - mc[1][2] * mc[2][1], 2) - 4 * (mc[3][0] * mc[1][2] - Math.pow(mc[2][1], 2))
				* (mc[0][3] * mc[2][1] - mc[1][2]))
				/ Math.pow(mc[0][0], 10);
	}

	private void calculateNormalMoments() {
		for (Point point : segment.getPoints()) {
			int x = point.getRow();
			int y = point.getColumn();

			double sum = 1;
			for (int i = 0; i < m.length; ++i) {
				double innerSum = sum;
				for (int j = 0; j < m[i].length; ++j) {
					m[i][j] += innerSum;
					innerSum *= y;
				}
				sum *= x;
			}
		}

		centerRow = (int) (m[1][0] / m[0][0]);
		centerCol = (int) (m[0][1] / m[0][0]);
	}

	/**
	 * Wyznacza wartoœci wspó³czynnikó kszta³tu.
	 */
	private void calculateW() {
		calculateBasicParameters();

		W[0] = 2 * Math.sqrt(area / Math.PI);
		W[1] = parimeter / Math.PI;
		W[2] = parimeter / (2 * Math.sqrt(Math.PI * area)) - 1;

		W[6] = rMin / rMax;
		W[7] = lMax / parimeter;
		W[8] = (2 * Math.sqrt(Math.PI * area)) / parimeter;
	}

	public int getCenterCol() {
		return centerCol;
	}

	public int getCenterRow() {
		return centerRow;
	}

	public double getlMax() {
		return lMax;
	}

	public double getM(int nr) {
		if (nr < 1 || 10 < nr)
			throw new IllegalArgumentException("Invalid moment!");

		return M[nr - 1];
	}

	public int getMaxCol() {
		return maxCol;
	}

	public int getMaxRow() {
		return maxRow;
	}

	public int getMinCol() {
		return minCol;
	}

	public int getMinRow() {
		return minRow;
	}

	public double getrMax() {
		return rMax;
	}

	public double getrMin() {
		return rMin;
	}

	public double getW(int nr) {
		if (nr < 1 || 9 < nr)
			throw new IllegalArgumentException("Invalid moment!");

		return W[nr - 1];
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();

		for (int i = 0; i < W.length; ++i)
			str.append("W" + (i + 1) + ": " + W[i] + "\n");

		str.append("\n");

		for (int i = 0; i < M.length; ++i)
			str.append("M" + (i + 1) + ": " + M[i] + "\n");

		return str.toString();
	}

}
