package pl.edu.pw.elka.mzawisl2;

import java.awt.EventQueue;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.model.util.LogUtils;
import pl.edu.pw.elka.mzawisl2.view.Frame;

/**
 * POBR - rozpoznawanie symboli radioaktywnoœci
 * 
 * @version 1.0
 * @author Mateusz Zawiœlak
 * 
 */
public class Main {

	private static Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		try {
			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					new Frame();
				}
			});
		} catch (Throwable t) {
			log.error("Internal error: " + LogUtils.getDescr(t));
		}
	}
}
