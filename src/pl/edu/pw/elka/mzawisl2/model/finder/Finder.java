package pl.edu.pw.elka.mzawisl2.model.finder;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.elka.mzawisl2.model.GeometricMoments;
import pl.edu.pw.elka.mzawisl2.model.Point;
import pl.edu.pw.elka.mzawisl2.model.finder.criterion.Criterion;
import pl.edu.pw.elka.mzawisl2.model.finder.criterion.MCriterion;
import pl.edu.pw.elka.mzawisl2.model.finder.criterion.WCriterion;
import pl.edu.pw.elka.mzawisl2.model.finder.criterion.util.CriterionUtils;
import pl.edu.pw.elka.mzawisl2.model.segment.RadioactiveLogo;
import pl.edu.pw.elka.mzawisl2.model.util.BaseUtils;

/**
 * Klasa odpowiedzialna za analizê i rozpoznawanie wsród przekazanych segmentów
 * symboli radioaktywnoœci.
 * 
 * @author Mateusz
 * 
 */
public class Finder {

	private List<GeometricMoments> geometricMoments;

	private List<GeometricMoments> wings = new ArrayList<GeometricMoments>();

	private List<GeometricMoments> circles = new ArrayList<GeometricMoments>();

	private List<RadioactiveLogo> logos = new ArrayList<RadioactiveLogo>();

	public Finder(List<GeometricMoments> geometricMoments) {
		this.geometricMoments = geometricMoments;

		findRadioactive();
	}

	/**
	 * Sprawdza czy zadany segment mo¿e byæ œrodkowym ko³em symbolu
	 * radioaktywnoœci.
	 * Wykorzystywane wspó³czynniki: M1, M7, W7, W8 oraz W9.
	 * 
	 * @param geometricMoments
	 *            - segment poddawany analizie
	 * @return - wartoœæ miary podobieñstwa do œrodkowego ko³a symbolu
	 *         radioaktywnoœci
	 */
	private double checkIfCircle(GeometricMoments geometricMoments) {
		double M1 = 0.15966790312724127, m1l = 0.01042701161492792, m1r = 0.02602274169838208;
		double M7 = 0.006313599729171538, m7l = 5.837032930951398E-3, m7r = 3.7947501694461944E-4;

		double W7 = 0.7219645951930945, w7l = 0.3660525033768029, w7r = 0.3317094243684528;
		double W8 = 0.21052631578947367, w8l = 0.03762911952779144, w8r = 0.07947368421052635;
		double W9 = 0.7664665301213042, w9l = 0.4160606998979035863, w9r = 0.493021900745301305;

		List<Criterion> circleCriterions = new ArrayList<Criterion>();
		circleCriterions.add(new MCriterion(M1, m1l, m1r, 1));
		circleCriterions.add(new MCriterion(M7, m7l, m7r, 7));

		circleCriterions.add(new WCriterion(W8, w8l, w8r, 8));
		circleCriterions.add(new WCriterion(W7, w7l, w7r, 7));
		circleCriterions.add(new WCriterion(W9, w9l, w9r, 9));

		double prob = 0.;

		for (Criterion criterion : circleCriterions) {
			double criterionProbabilty = CriterionUtils.checkCriterion(criterion, criterion.getValue(geometricMoments));
			if (criterionProbabilty == 0.)
				return 0.;
			else
				prob += criterionProbabilty;
		}

		return prob / circleCriterions.size();
	}

	/**
	 * Sprawdza czy zadany segment mo¿e byæ skrzyd³em symbolu
	 * radioaktywnoœci.
	 * Wykorzystywane wspó³czynniki: M1, M7, W7, W8 oraz W9.
	 * 
	 * @param geometricMoments
	 *            - segment poddawany analizie
	 * @return - wartoœæ miary podobieñstwa do skrzyd³a symbolu
	 *         radioaktywnoœci
	 */
	private double checkIfWing(GeometricMoments geometricMoments) {
		double M1 = 0.17161797583438057, m1l = 0.01525078833438114, m1r = 0.032691472407806695;
		double M7 = 0.007058110443992356, m7l = 3.2675531938969127E-3, m7r = 3.8955945221067515E-3;

		double W7 = 0.5817398749983579, w7l = 0.5707998357533121, w7r = 0.40045400785054363;
		double W8 = 0.1694915254237288, w8l = 0.118214511671272998, w8r = 0.130508474576271205;
		double W9 = 0.4623881847207013, w9l = 0.16103858496313058, w9r = 0.21048143064540505;

		List<Criterion> wingCriterions = new ArrayList<Criterion>();

		wingCriterions.add(new MCriterion(M1, m1l, m1r, 1));
		wingCriterions.add(new MCriterion(M7, m7l, m7r, 7));

		wingCriterions.add(new WCriterion(W7, w7l, w7r, 7));
		wingCriterions.add(new WCriterion(W8, w8l, w8r, 8));
		wingCriterions.add(new WCriterion(W9, w9l, w9r, 9));

		double prob = 0.;

		for (Criterion criterion : wingCriterions) {
			double criterionProbabilty = CriterionUtils.checkCriterion(criterion, criterion.getValue(geometricMoments));
			if (criterionProbabilty == 0.)
				return 0.;
			else
				prob += criterionProbabilty;
		}

		return prob / wingCriterions.size();
	}

	private void checkOneSegment(GeometricMoments geometricMoments) {
		if (checkIfWing(geometricMoments) > 0.) {
			wings.add(geometricMoments);
		}

		if (checkIfCircle(geometricMoments) > 0.) {
			circles.add(geometricMoments);
		}
	}

	/**
	 * Odszukuje pe³ne symbole radioaktywnoœci.
	 */
	private void findRadioactive() {
		for (GeometricMoments geometricMoment : geometricMoments) {
			checkOneSegment(geometricMoment);
		}

		// sprawdz wzajemne po³o¿enie kó³ oraz skrzyde³ symboli
		if (!BaseUtils.isNullOrEmpty(wings) && wings.size() >= 3) {
			for (int i = 0; i < wings.size(); ++i) {
				for (int j = i + 1; j < wings.size(); ++j) {
					for (int k = j + 1; k < wings.size(); ++k) {
						GeometricMoments wing1 = wings.get(i);
						GeometricMoments wing2 = wings.get(j);
						GeometricMoments wing3 = wings.get(k);

						Point wing1Center = new Point(wing1.getCenterRow(), wing1.getCenterCol());
						Point wing2Center = new Point(wing2.getCenterRow(), wing2.getCenterCol());
						Point wing3Center = new Point(wing3.getCenterRow(), wing3.getCenterCol());

						double disance1 = BaseUtils.getDistance(wing1Center, wing2Center);
						double disance2 = BaseUtils.getDistance(wing2Center, wing3Center);
						double disance3 = BaseUtils.getDistance(wing3Center, wing1Center);

						// sprawdŸ czy œrodki skrzyde³ tworz¹ trójk¹t
						// równoboczny
						if (Math.abs(disance1 - disance2) / disance1 < 0.4 && Math.abs(disance2 - disance3) / disance2 < 0.4
								&& Math.abs(disance3 - disance1) / disance3 < 0.4) {

							Point point = BaseUtils.getCenter(wing1Center, wing2Center, wing3Center);

							if (!BaseUtils.isNullOrEmpty(circles)) {
								for (GeometricMoments center : circles) {
									// sprawdz jak deleko jest œrodek ko³a od
									// œrodka znalezionego trójk¹ta skrzyde³
									if (BaseUtils.getDistance(new Point(center.getCenterRow(), center.getCenterCol()), point) <= center
											.getlMax()) {
										int maxRow = Math.max(wing1.getMaxRow(), wing2.getMaxRow());
										maxRow = Math.max(maxRow, wing3.getMaxRow());

										int maxCol = Math.max(wing1.getMaxCol(), wing2.getMaxCol());
										maxCol = Math.max(maxCol, wing3.getMaxCol());

										int minCol = Math.min(wing1.getMinCol(), wing2.getMinCol());
										minCol = Math.min(minCol, wing3.getMinCol());

										int minRow = Math.min(wing1.getMinRow(), wing2.getMinRow());
										minRow = Math.min(minRow, wing3.getMinRow());

										logos.add(new RadioactiveLogo(new Point(minRow, minCol), maxCol - minCol, maxRow - minRow));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public List<GeometricMoments> getCircles() {
		return circles;
	}

	public List<RadioactiveLogo> getLogos() {
		return logos;
	}

	public List<GeometricMoments> getWings() {
		return wings;
	}

}
