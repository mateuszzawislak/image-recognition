package pl.edu.pw.elka.mzawisl2.model;

public class Point {

	private int row;

	private int column;

	public Point(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	@Override
	public int hashCode() {
		return row * 100000 + column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setRow(int row) {
		this.row = row;
	}
}
