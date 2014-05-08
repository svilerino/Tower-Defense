package main.util;

import java.awt.Dimension; 
import java.awt.Toolkit;

/**
 * Stores the screen's width and height.
 * @author Guido Tagliavini
 * @since 1.0.0
 */

public class UserScreenUtil {
	private static final int SCREEN_WIDTH = getScreenSize().width;
	private static final int SCREEN_HEIGHT = getScreenSize().height;

	public static int getHeight(){
		return SCREEN_HEIGHT;
	}

	public static int getWidth(){
		return SCREEN_WIDTH;
	}

	private static Dimension getScreenSize(){
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * Avoids object construction
	 */
	private UserScreenUtil(){}
}
