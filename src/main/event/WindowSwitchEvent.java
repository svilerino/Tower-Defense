package main.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Adds the capability to switch windows, both by clicking a JButton or closing
 * the window.
 *
 * @author Guido Tagliavini
 * @since 1.0.0
 */

public class WindowSwitchEvent implements WindowListener, ActionListener {
	private JFrame activeFrame;
	private JFrame nextFrame;

	public WindowSwitchEvent(JFrame activeFrame, JFrame nextFrame) {
		this.activeFrame = activeFrame;
		this.nextFrame = nextFrame;
	}

	public void actionPerformed(ActionEvent e) {
		activeFrame.setVisible(false);
		nextFrame.setVisible(true);
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		activeFrame.setVisible(false);
		nextFrame.setVisible(true);
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

}
