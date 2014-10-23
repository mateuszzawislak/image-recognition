package pl.edu.pw.elka.mzawisl2.model.finder.criterion.util;

import pl.edu.pw.elka.mzawisl2.model.finder.criterion.Criterion;

public class CriterionUtils {

	/**
	 * Sprawdza czy zadana warto�� spe�nia przekazane kryterium.
	 * 
	 * @param criterion
	 *            - kryterium do sprawdzenia
	 * @param value
	 *            - warto�� do sprawdzenia
	 * @return warto�� podobie�stwa zgodnie z zadanym kryterium. 0 oznacza
	 *         niespe�nienie kryterium.
	 */
	public static double checkCriterion(Criterion criterion, double value) {
		if (value <= criterion.getIdealValue() + criterion.getRightDelta() && value >= criterion.getIdealValue() - criterion.getLeftDelta())
			return 1 - Math.abs(value - criterion.getIdealValue()) / (criterion.getLeftDelta() + criterion.getRightDelta());
		else
			return 0.;
	}
}
