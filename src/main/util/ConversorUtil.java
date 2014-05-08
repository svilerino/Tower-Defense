package main.util;

import java.awt.Color; 
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import main.enums.GameMode;
import main.enums.ShootStyle;
/**
 * @author Silvio Vileriño
 * @author Guido Tagliavini
 *
 */
public class ConversorUtil {

	/**
	 *
	 * 12 is a constant between pixels and logic coordinates.
	 *
	 * 12  Xpixel -------- 1 logic Xcoordinate
	 * 300 Xpixel --------25 logic Xcoordinate
	 *
	 *
	 * 12  Ypixel -------- 1 logic  Ycoordinate
	 * 516 Ypixel -------- 43 logic Ycoordinate
	 *
	 */
	public static Integer pixelsToLogic(Integer pixels){
		return pixels/12;
	}

	public static Integer logicToPixels(Integer logic){
		return logic * 12;
	}

	public static GameMode stringToGameType(String strGameType){
		if(strGameType.equals("SINGLE_PLAYER"))
			return GameMode.SINGLE_PLAYER;
		if(strGameType.equals("MULTI_PLAYER"))
			return GameMode.MULTI_PLAYER;

		return null;
	}

	public static Color stringToColor(String strColor){
		if(strColor.equals("RED"))
			return Color.RED;
		if(strColor.equals("BLUE"))
			return Color.BLUE;
		if(strColor.equals("CYAN"))
			return Color.CYAN;
		if(strColor.equals("GREEN"))
			return Color.GREEN;
		if(strColor.equals("ORANGE"))
			return Color.ORANGE;
		if(strColor.equals("YELLOW"))
			return Color.YELLOW;
		if(strColor.equals("MAGENTA"))
			return Color.MAGENTA;
		if(strColor.equals("DARK_GRAY"))
			return Color.DARK_GRAY;

		return null;
	}

	/**
	 * @author Silvio Vileriño
	 * @param fileObj Objeto File a obtener la URL
	 * @return URL de la ruta del objeto File
	 * @throws MalformedURLException
	 */
	public static URL fileToURL(File fileObj) throws MalformedURLException{
		return fileObj.toURI().toURL();
	}

	/**
	 * @author Silvio Vileriño
	 * @param fileObj Objeto File a obtener la URI
	 * @return URI de la ruta del objeto File
	 */
	public static URI fileToURI(File fileObj){
		return fileObj.toURI();
	}

	public static ShootStyle stringToShoot(String shootType){
		if(shootType.toLowerCase().equals("laser")){
			return ShootStyle.LASER;
		}
		if(shootType.toLowerCase().equals("ball")){
			return ShootStyle.BALL;
		}
		return null;
	}

}
