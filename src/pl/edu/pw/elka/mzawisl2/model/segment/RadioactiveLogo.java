package pl.edu.pw.elka.mzawisl2.model.segment;

import pl.edu.pw.elka.mzawisl2.model.Point;

public class RadioactiveLogo {

	private Point leftTopPoint;

	private int width;

	private int height;

	public RadioactiveLogo(Point leftTopPoint, int width, int height) {
		super();
		this.leftTopPoint = leftTopPoint;
		this.width = width;
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public Point getLeftTopPoint() {
		return leftTopPoint;
	}

	public int getWidth() {
		return width;
	}

}
