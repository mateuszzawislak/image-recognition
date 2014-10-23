package pl.edu.pw.elka.mzawisl2.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.model.util.LogUtils;

/**
 * Panel przetrzymuj¹cy obraz
 * 
 * @author Mateusz
 * 
 */
public class ImagePanel extends JPanel {

	private static Logger log = Logger.getLogger(ImagePanel.class);

	private static final long serialVersionUID = 7286599097878850608L;

	private BufferedImage image;

	public ImagePanel(BufferedImage image) {
		super();

		this.image = image;
		setPreferedImageSize();
	}

	public ImagePanel(File imageFile) {
		super();

		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			log.error("Blad odczytu obrazka: " + LogUtils.getDescr(e));
			return;
		}

		setPreferedImageSize();
	}

	@Override
	public void paint(Graphics grphcs) {
		super.paintComponent(grphcs);
		Graphics2D g2d = (Graphics2D) grphcs;
		g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
	}

	private void setPreferedImageSize() {
		Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
		setPreferredSize(dimension);
	}
}
