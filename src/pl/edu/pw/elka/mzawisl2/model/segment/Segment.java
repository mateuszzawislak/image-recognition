package pl.edu.pw.elka.mzawisl2.model.segment;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.elka.mzawisl2.model.Point;

public class Segment {

	private int id;

	private List<Point> points;

	public Segment(int id) {
		this.id = id;
		this.points = new ArrayList<Point>();
	}

	public void addPoint(Point point) {
		this.points.add(point);
	}

	public int getId() {
		return id;
	}

	public List<Point> getPoints() {
		return points;
	}

}
