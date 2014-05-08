package main.app;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import services.LoggerService;

import main.consts.LookAndFeelConsts;
import main.runnable.view.LoadFrameRunnable;
import main.view.frame.LoadGameFrame;
import main.view.frame.MainFrame;

/**
 * @author Pablo Labin
 * @author Ignacio Maldonado
 * @author Silvio Vileriño
 * @author Guido Tagliavini
 */

public class Main {

	public static LoggerService log=new LoggerService(new Main().getClass());

	public static void main(String args[]){
		log.submitLog(Level.INFO, "Program Started");

		EventQueue.invokeLater(new Runnable(){
			public void run(){
				new Thread(new LoadFrameRunnable(LookAndFeelConsts.INIT_LOAD_PROGRESSBAR_DELAY, new LoadGameFrame(new MainFrame(), LookAndFeelConsts.LOAD_PROGRESSBAR_COLOR))).start();
				log.submitLog(Level.INFO, "Loading Images in RAM...");
			}
		});
	}
}