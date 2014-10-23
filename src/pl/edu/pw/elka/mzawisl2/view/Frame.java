package pl.edu.pw.elka.mzawisl2.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.model.ImageProcessor;
import pl.edu.pw.elka.mzawisl2.model.filter.FilterType;
import pl.edu.pw.elka.mzawisl2.model.util.LogUtils;

/**
 * G³owne okno aplikacji.
 * 
 * W g³ównym menu dostêpne s¹ takie akcje jak: filtrowanie, binaryzacja oraz
 * inne etapy rozpoznawnaia obrazu.
 * 
 * @author Mateusz
 * 
 */
public class Frame extends JFrame {

	private static Logger log = Logger.getLogger(Frame.class);

	private static final long serialVersionUID = -6850525262613249438L;

	private static final String WINDOW_TITLE = "POBR - Mateusz Zawislak";

	private JPanel imagePanel = new JPanel();

	JScrollPane scrollPane;

	private ImageProcessor imageProcessor;

	private ProcessImage imageRepaintAction;

	private JMenuBar menuBar;

	private JMenu filterMenu;

	private JMenu binMenu;

	private JMenu fileMenu;

	public Frame() {
		super(WINDOW_TITLE);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocation(50, 50);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		initMenuBar();
		initFilterMenu();
		initBinarizationMenu();
		this.setJMenuBar(menuBar);

		scrollPane = new JScrollPane(imagePanel);
		add(scrollPane);

		imageRepaintAction = new ProcessImage() {

			@Override
			public void processImage(BufferedImage image) {
				repaintImage(image);

			}
		};

		setVisible(true);
	}

	/**
	 * funkcja aktywuj¹ca wszystkie opcje menu g³ównego
	 */
	private void activateAllButtons() {
		activateMenu(fileMenu);
		activateMenu(filterMenu);
		activateMenu(binMenu);
	}

	/**
	 * funkcja aktywuj¹ca pozycje w zadanym submenu
	 * 
	 * @param menu
	 *            - submenu do aktywowania
	 */
	private void activateMenu(JMenu menu) {
		if (null != menu.getMenuComponents())
			for (Component component : menu.getMenuComponents()) {
				try {
					JMenu subMenu = (JMenu) component;
					activateMenu(subMenu);
					continue;
				} catch (ClassCastException e) {
					// ignore...
				}
				try {
					JMenuItem menuItem = (JMenuItem) component;
					menuItem.setEnabled(true);
				} catch (ClassCastException e) {
					// ignore...
				}
			}
	}

	/**
	 * Funkcja generycznie tworz¹ca akcje binaryzacji o ró¿nym poiomie
	 * granicznym dla zadanej, pojedyñczej barwy RGB.
	 * 
	 * @param menu
	 *            - menu które ma zostaæ uzupe³nione
	 * @param rLevel
	 *            - czy ma to byæ binaryzacja koloru czerwonego
	 * @param gLevel
	 *            - czy ma to byæ binaryzacja koloru zielonego
	 * @param bLevel
	 *            - czy ma to byæ binaryzacja koloru niebieskiego
	 */
	private void fillBinarizationSubMenu(JMenu menu, final boolean rLevel, final boolean gLevel, final boolean bLevel) {
		for (int i = 10; i < 255; i += 10) {
			final int level = i;
			JMenuItem binaryMenuItem = new JMenuItem("" + level);
			binaryMenuItem.setEnabled(false);
			binaryMenuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new ImageFrame(imageProcessor.binarization(rLevel ? level : 0, gLevel ? level : 0, bLevel ? level : 0),
							"Binaryzacja rozpoznawania", imageRepaintAction);
				}

			});
			menu.add(binaryMenuItem);
		}
	}

	/**
	 * Inicjalizacja menu binaryzacji.
	 */
	private void initBinarizationMenu() {
		binMenu = new JMenu("Binaryzacja");

		JMenu subMenu = new JMenu("Ogólna");
		fillBinarizationSubMenu(subMenu, true, true, true);
		binMenu.add(subMenu);

		JMenu redMenu = new JMenu("Czerwony");
		fillBinarizationSubMenu(redMenu, true, false, false);
		binMenu.add(redMenu);

		JMenu greenMenu = new JMenu("Zielony");
		fillBinarizationSubMenu(greenMenu, false, true, false);
		binMenu.add(greenMenu);

		JMenu blueMenu = new JMenu("Niebieski");
		fillBinarizationSubMenu(blueMenu, false, false, true);
		binMenu.add(blueMenu);

		menuBar.add(binMenu);
	}

	/**
	 * Inicjalizacja procesu binaryzacji z progiem 140.
	 */
	private void initBinarizationMenuItem() {
		JMenuItem binaryMenuItem = new JMenuItem("Binaryzacja rozpoznawania");
		binaryMenuItem.setEnabled(false);
		binaryMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ImageFrame(imageProcessor.binarization(), "Binaryzacja rozpoznawania", imageRepaintAction);
			}

		});
		fileMenu.add(binaryMenuItem);
	}

	/**
	 * Inicjalizacja akcji wyboru zdjêcia.
	 */
	private void initFileChooserMenuItem() {
		JMenuItem fileChooserMenuItem = new JMenuItem("Wybierz zdjecie");
		fileChooserMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				try {
					File f = new File(new File(".").getCanonicalPath());
					chooser.setCurrentDirectory(f);
				} catch (IOException e1) {
					log.error("Internal exception: " + LogUtils.getDescr(e1));
				}

				chooser.setFileFilter(new FileFilter() {

					@Override
					public boolean accept(final File file) {
						if (file.isDirectory())
							return true;

						String ext = getExtension(file);
						if (ext != null)
							if (ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg") || ext.equals("gif") || ext.equals("bmp"))
								return true;

						return false;
					}

					@Override
					public String getDescription() {
						return "Images (png, jpg, gif, bmp)";
					}

					public String getExtension(final File file) {
						String ext = null;
						String s = file.getName();

						int i = s.lastIndexOf('.');
						if (i > 0 && i < s.length() - 1)
							ext = s.substring(i + 1).toLowerCase();

						return ext;
					}
				});

				int returnVal = chooser.showOpenDialog(Frame.this);

				if (JFileChooser.APPROVE_OPTION == returnVal) {
					File file = chooser.getSelectedFile();

					try {
						// zczytaj wybrane zdjêcie i ustaw jest w procesorze
						BufferedImage image = ImageIO.read(file);
						imageProcessor = new ImageProcessor(image);

						repaintImage(image);
						activateAllButtons();
					} catch (IOException exception) {
						log.error("Blad odczytu obrazka: " + LogUtils.getDescr(exception));
					}

				}
			}
		});
		fileMenu.add(fileChooserMenuItem);
	}

	/**
	 * Inicjalizacja submenu z filtrami.
	 */
	private void initFilterMenu() {
		filterMenu = new JMenu("Filtry");

		for (final FilterType filterType : FilterType.values()) {
			JMenuItem filterMenuItem = new JMenuItem(filterType.name());
			filterMenuItem.setEnabled(false);
			filterMenuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new ImageFrame(imageProcessor.filterImage(filterType), filterType.name(), imageRepaintAction);
				}
			});
			filterMenu.add(filterMenuItem);
		}

		filterMenu.addSeparator();
		initMedianFilterMenuItem();
		filterMenu.addSeparator();
		initMaxFilterMenuItem();
		filterMenu.addSeparator();
		initMinFilterMenuItem();

		menuBar.add(filterMenu);
	}

	/**
	 * Inicjalizacja przycisku wyszukiwania du¿ych symboli radioaktywnoœci z
	 * wykorzystaniem efektu erozji na wybranym zdjêciu.
	 */
	private void initFindBigSymbolsButton() {
		JMenuItem findButton = new JMenuItem("Wyszukaj du¿e symbole wykorzystuj¹c erozjê");
		findButton.setEnabled(false);
		findButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ImageFrame(imageProcessor.findBigRadioactiveSymbolsWithMinMax(), "Du¿e symbole po erozji", imageRepaintAction);
			}

		});
		fileMenu.add(findButton);
	}

	/**
	 * Inicjalizacja przycisku wyszukiwania symboli radioaktywnoœci na wybranym
	 * zdjêciu.
	 */
	private void initFinderButton() {
		JMenuItem testButton = new JMenuItem("Wyszukaj symbole radioaktywnoœci");
		testButton.setEnabled(false);
		testButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ImageFrame(imageProcessor.findRadioactiveSymbols(), "Znalezione symbole radioaktywnoœci", imageRepaintAction);
			}

		});
		fileMenu.add(testButton);
	}

	/**
	 * Inicjalizacja akcji odwrócenia kolorów obrazu.
	 */
	private void initInvertImageMenuItem() {
		JMenuItem invertImageMenuItem = new JMenuItem("Odwróæ kolory");
		invertImageMenuItem.setEnabled(false);
		invertImageMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ImageFrame(imageProcessor.getInvertedImage(), "Odwrócone kolory", imageRepaintAction);
			}
		});
		fileMenu.add(invertImageMenuItem);
	}

	/**
	 * Inicjalizacja akcji filtra maksymalnego.
	 */
	private void initMaxFilterMenuItem() {
		final int[] sizes = { 3, 5, 7 };
		for (int i = 0; i < sizes.length; ++i) {
			final int size = sizes[i];
			JMenuItem filterMenuItem = new JMenuItem("Maksymalny " + size);
			filterMenuItem.setEnabled(false);
			filterMenuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new ImageFrame(imageProcessor.maxFilter(size), imageRepaintAction);
				}
			});
			filterMenu.add(filterMenuItem);
		}
	}

	/**
	 * Inicjalizacja akcji filtra medianowego.
	 */
	private void initMedianFilterMenuItem() {
		final int[] sizes = { 3, 5, 7 };
		for (int i = 0; i < sizes.length; ++i) {
			final int size = sizes[i];
			JMenuItem filterMenuItem = new JMenuItem("Medianowy " + size);
			filterMenuItem.setEnabled(false);
			filterMenuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new ImageFrame(imageProcessor.medianFilter(size), imageRepaintAction);
				}
			});
			filterMenu.add(filterMenuItem);
		}
	}

	/**
	 * Inicjalizacja g³ównego menu programu.
	 */
	private void initMenuBar() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("Plik");

		initFileChooserMenuItem();
		fileMenu.addSeparator();
		initInvertImageMenuItem();
		initSegmentationMenuItem();
		initBinarizationMenuItem();
		fileMenu.addSeparator();
		initFindBigSymbolsButton();
		fileMenu.addSeparator();
		initFinderButton();
		fileMenu.addSeparator();
		initSaveMenuItem();

		menuBar.add(fileMenu);
	}

	/**
	 * Inicjalizacja akcji rankingowego filtra minimalnego.
	 */
	private void initMinFilterMenuItem() {
		final int[] sizes = { 3, 5, 7 };
		for (int i = 0; i < sizes.length; ++i) {
			final int size = sizes[i];
			JMenuItem filterMenuItem = new JMenuItem("Minimalny " + size);
			filterMenuItem.setEnabled(false);
			filterMenuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new ImageFrame(imageProcessor.minFilter(size), imageRepaintAction);
				}
			});
			filterMenu.add(filterMenuItem);
		}
	}

	/**
	 * Inicjalizacja akcji zapisu obrazu do pliku.
	 */
	private void initSaveMenuItem() {
		JMenuItem saveImageMenuItem = new JMenuItem("Zapisz plik jako");
		saveImageMenuItem.setEnabled(false);
		saveImageMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				try {
					File f = new File(new File(".").getCanonicalPath());
					chooser.setCurrentDirectory(f);

					int returnVal = chooser.showOpenDialog(Frame.this);

					if (JFileChooser.APPROVE_OPTION == returnVal) {
						File file = chooser.getSelectedFile();

						ImageIO.write(imageProcessor.getImage(), "jpeg", file);
					}
				} catch (IOException e1) {
					log.error("Blad zapisu obrazka: " + LogUtils.getDescr(e1));
				}
			}
		});
		fileMenu.add(saveImageMenuItem);
	}

	/**
	 * Inicjalizacja akcji segmentacji obrazu binarnego.
	 */
	private void initSegmentationMenuItem() {
		JMenuItem segmentationMenuItem = new JMenuItem("Segmentacja");
		segmentationMenuItem.setEnabled(false);
		segmentationMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ImageFrame(imageProcessor.segmentation(), "Segmentacja", imageRepaintAction);
			}
		});
		fileMenu.add(segmentationMenuItem);
	}

	/**
	 * Rysuje zadany obraz w g³ównym panelu okna.
	 * 
	 * @param image
	 *            - obraz do narysowania
	 */
	private void repaintImage(BufferedImage image) {
		imageProcessor = new ImageProcessor(image);

		ImagePanel panel = new ImagePanel(image);
		imagePanel.setPreferredSize(panel.getPreferredSize());
		imagePanel.removeAll();
		imagePanel.add(panel);
		scrollPane.setPreferredSize(imagePanel.getPreferredSize());
		revalidate();
		repaint();
	}
}
