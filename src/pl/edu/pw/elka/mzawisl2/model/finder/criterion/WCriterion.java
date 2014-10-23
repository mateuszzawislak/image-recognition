package pl.edu.pw.elka.mzawisl2.model.finder.criterion;

import pl.edu.pw.elka.mzawisl2.model.GeometricMoments;

public class WCriterion extends Criterion {

	private int wIndex;

	/**
	 * Kryterium na wspó³cznnik kszta³tu.
	 * 
	 * @param idealValue
	 *            - idealna wartoœæ danego wspó³czynnika
	 * @param leftDelta
	 *            - próg b³êdu wartoœci ni¿szej od idealnej
	 * @param rightDelta
	 *            - próg b³êdu wartoœci wy¿szej od idealnej
	 * @param wIndex
	 *            - index wspó³czynnika np. dla W8 wynosi on 8
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
