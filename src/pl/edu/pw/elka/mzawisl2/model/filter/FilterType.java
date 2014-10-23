package pl.edu.pw.elka.mzawisl2.model.filter;

/**
 * Typy filtrów jakie mog¹ byæ nak³adane na obrazu.
 * 
 * @author Mateusz
 * 
 */
public enum FilterType {
	LAW_PASS(new int[] { 1, 1, 1, 1, 4, 1, 1, 1, 1 }),
	LP2(new int[] { 1, 1, 1, 1, 12, 1, 1, 1, 1 }),
	GAUSS_1(new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 }),
	GAUSS_2(new int[] { 1, 1, 2, 1, 1, 1, 2, 4, 2, 1, 2, 4, 8, 4, 2, 1, 2, 4, 2, 1, 1, 1, 2, 1, 1 }),
	HIGH_PASS(new int[] { 1, -2, 1, -2, 5, -2, 1, -2, 1 }),
	SHARPEN_MATRIX(new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 }),
	EDGE_DETECTION_MATRIX(new int[] { -1, -1, -1, -1, 8, -1, -1, -1, -1 }),
	BLUR_MATRIX(new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 }),
	HP1(new int[] { 0, -1, 0, -1, 5, -1, 0, -1, 0 }),
	HP2(new int[] { 1, -2, 1, -2, 5, -2, 1, -2, 1 }),
	HP3(new int[] { 0, -1, 0, -1, 20, -1, 0, -1, 0 });

	private int[] matrix;

	FilterType(int[] matrix) {
		this.matrix = matrix;
	}

	public int[] getMatrix() {
		return matrix;
	}

}
