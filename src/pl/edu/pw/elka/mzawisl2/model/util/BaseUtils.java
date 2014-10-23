package pl.edu.pw.elka.mzawisl2.model.util;

import java.util.Collection;

import pl.edu.pw.elka.mzawisl2.model.Point;

public class BaseUtils {

	/**
	 * Wyznacza �rodek tr�jk�ta.
	 * 
	 * @param p1
	 *            - pierwszy wierzcho�ek tr�jk�ta
	 * @param p2
	 *            - drugi wierzcho�ek tr�jk�ta
	 * @param p3
	 *            - trzeci wierzcho�ek tr�jk�ta
	 * @return punkt �rodkowy tr�jk�ta
	 */
	public static Point getCenter(Point p1, Point p2, Point p3) {
		double centerRow = (p1.getRow() + p2.getRow() + p3.getRow()) / 3;
		double centerColumn = (p1.getColumn() + p2.getColumn() + p3.getColumn()) / 3;
		return new Point((int) centerRow, (int) centerColumn);
	}

	/**
	 * Liczy odleg�o�� euklidesow� mi�dzy punktami obrazu.
	 * 
	 * @param p1
	 *            - pierwszy punkt
	 * @param p2
	 *            - drugi punkt
	 * @return odleg�o�� euklidesowa mi�dzy punktami
	 */
	public static double getDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.getRow() - p2.getRow(), 2) + Math.pow(p1.getColumn() - p2.getColumn(), 2));
	}

	public static int getIntModulo256(int number) {
		return number < 0 ? 0 : (number > 255 ? 255 : number);
	}

	public static <T extends Collection<C>, C> boolean isNullOrEmpty(T ref) {
		if (null == ref || ref.isEmpty()) {
			return true;
		}
		return false;
	}

}
