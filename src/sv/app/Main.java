package sv.app;

import java.awt.EventQueue;

import sv.view.frame.MainFrame;

/**
 * Main server system.
 * 
 * @author Guido Tagliavini
 *
 */

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				new MainFrame();
			}
		});
	}
}
