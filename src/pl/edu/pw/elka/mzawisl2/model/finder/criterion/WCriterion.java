package pl.edu.pw.elka.mzawisl2.model.finder.criterion;

import pl.edu.pw.elka.mzawisl2.model.GeometricMoments;

public class WCriterion extends Criterion {

	private int wIndex;

	/**
	 * Kryterium na wsp�cznnik kszta�tu.
	 * 
	 * @param idealValue
	 *            - idealna warto�� danego wsp�czynnika
	 * @param leftDelta
	 *            - pr�g b��du warto�ci ni�szej od idealnej
	 * @param rightDelta
	 *            - pr�g b��du warto�ci wy�szej od idealnej
	 * @param wIndex
	 *            - index wsp�czynnika np. dla W8 wynosi on 8
	 */
	public WCriterion(double idealValue, double leftDelta, double rightDelta, int wIndex) {
		this.wIndex = wIndex;
		this.idealValue = idealValue;
		this.leftDelta = leftDelta;
		this.rightDelta = rightDelta;
	}

	@Override
	public double getValue(GeometricMoments geometricMoments) {
		return geometricMoments.getW(wIndex);
	}

}
