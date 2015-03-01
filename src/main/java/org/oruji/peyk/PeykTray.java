package org.oruji.peyk;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public final class PeykTray {

	private static PeykTray peykTray = null;

	private TrayIcon trayIcon = null;

	public static PeykTray getTray() {
		if (peykTray == null) {
			peykTray = new PeykTray();
		}

		return peykTray;
	}

	private PeykTray() {
		// Check the SystemTray is supported
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		PopupMenu popup = new PopupMenu();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("peyk.png").getFile());

			trayIcon = new TrayIcon(ImageIO.read(file).getScaledInstance(32,
					32, Image.SCALE_SMOOTH), null);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// get the system tray
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a pop-up menu components
		MenuItem aboutItem = new MenuItem("Show Window");
		aboutItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				PeykFrame.getFrame().setVisible(true);
			}
		});

		MenuItem exitItem = new MenuItem("Quit");
		exitItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});

		// Add components to pop-up menu
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}