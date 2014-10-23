package pl.edu.pw.elka.mzawisl2.view;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Okno w którym jest wyœwietlane zdjêcie.
 * W menu g³ównym tego okna jest pozycja z akcj¹ zwrotn¹ na danym obrazie.
 * 
 * @author Mateusz
 * 
 */
public class ImageFrame extends JFrame {

	private static final long serialVersionUID = 4263623719023399690L;

	private static final String DEFAULT_TITLE = "Zdjecie";

	private ImagePanel panel;

	private ProcessImage processImage;

	private JMenuBar menuBar;

	private BufferedImage image;

	public ImageFrame(BufferedImage image, ProcessImage processImage) {
		this(image, DEFAULT_TITLE, processImage);
	}

	public ImageFrame(BufferedImage image, String title, ProcessImage processImage) {
		super(title);
		this.processImage = processImage;
		this.image = image;
		init();
	}

	private void init() {
		setBounds(50, 50, 900, 700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		panel = new ImagePanel(image);
		panel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

		ScrollPane scroll = new ScrollPane();
		scroll.add(panel);

		getContentPane().add(scroll);

		menuBar = new JMenuBar();
		JMenu menu = new JMenu("Plik");
		JMenuItem menuItem = new JMenuItem("Ustaw jako zdjêcie g³ówne");

		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				processImage.processImage(image);
			}
		});

		menu.add(menuItem);
		menuBar.add(menu);

		this.setJMenuBar(menuBar);
	}
}
