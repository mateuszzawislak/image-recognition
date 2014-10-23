package pl.edu.pw.elka.mzawisl2.model;

public class Pixel {

	private int blue;

	private int green;

	private int red;

	public Pixel() {
	}

	public Pixel(int red, int green, int blue) {
		this.blue = blue;
		this.green = green;
		this.red = red;
	}

	public Pixel(Pixel pixel) {
		this(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
	}

	public int getBlue() {
		return blue;
	}

	public int getGray() {
		return (blue + green + red) / 3;
	}

	public int getGreen() {
		return green;
	}

	public int getRed() {
		return red;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public void setRed(int red) {
		this.red = red;
	}

}
