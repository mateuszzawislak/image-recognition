package pl.edu.pw.elka.mzawisl2.model.finder.criterion;

import pl.edu.pw.elka.mzawisl2.model.GeometricMoments;

public class MCriterion extends Criterion {

	private int mIndex;

	/**
	 * Kryterium na warto�� zadanego niezmiennika geometrycznego.
	 * 
	 * @param idealValue
	 *            - idealna warto�� danego niezmiennika
	 * @param leftDelta
	 *            - pr�g b��du warto�ci ni�szej od idealnej
	 * @param rightDelta
	 *            - pr�g b��du warto�ci wy�szej od idealnej
	 * @param wIndex
	 *            - index niezmiennika np. dla M7 wynosi on 7
	 */
	public MCriterion(double idealValue, double leftDelta, double rightDelta, int mIndex) {
		this.mIndex = mIndex;
		this.idealValue = idealValue;
		this.leftDelta = leftDelta;
		this.rightDelta = rightDelta;
	}

	@Override
	public double getValue(GeometricMoments geometricMoments) {
		return geometricMoments.getM(mIndex);
	}

}
