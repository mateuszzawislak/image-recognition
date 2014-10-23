package pl.edu.pw.elka.mzawisl2.model.finder.criterion;

import pl.edu.pw.elka.mzawisl2.model.GeometricMoments;

public abstract class Criterion {

	protected double idealValue;

	protected double leftDelta;

	protected double rightDelta;

	public double getIdealValue() {
		return idealValue;
	}

	public double getLeftDelta() {
		return leftDelta;
	}

	public double getRightDelta() {
		return rightDelta;
	}

	public abstract double getValue(GeometricMoments geometricMoments);

}
