package main.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Clase utilitaria para manejo de archivos.
 *
 * @author Guido Tagliavini
 * @since v1.0.0
 */

public class FileUtil {

	/**
	 * Método de escritura en formato texto a un archivo.
	 *
	 * @author Guido Tagliavini
	 */
	public static void writeText(String path, Vector<String> data){
		File file = getLowestAvailablePath(path, "txt");

		if(file.exists()){
			file.delete();
			System.out.println("[ INFO] Overwriting file (" + file.getPath() + ")");
		}

		try{
			PrintWriter printWriter = new PrintWriter(new FileWriter(file));
			try{
				for(int i = 0 ; i < data.size() ; i++){
					printWriter.println(data.get(i) + "\n");
				}
			}finally{
				printWriter.flush();
				printWriter.close();
			}

		}catch (IOException e){
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Método de lectura en formato texto desde un archivo.
	 *
	 * @autor Guido Tagliavini
	 * @param path
	 * @return
	 */
	public static Vector<String> readText(String path){
		File file = new File(path);
		Vector<String> result = new Vector<String>();

		if(file.exists()){
			try{
				Scanner scanner = new Scanner(new FileReader(file));
				try{
					while(scanner.hasNextLine()){
						result.add(scanner.next());
					}
				}finally{
					scanner.close();
				}
			}catch(IOException e){
				e.printStackTrace(System.err);
			}
		}else{
			System.err.println("[ERROR] Non-existing file (" + file.getPath() + ")");
		}

		return result;
	}


	/**
	 * Método escritura de imágenes a un archivo.
	 *
	 * @autor Guido Tagliavini
	 * @param path
	 * @return
	 */
	public static void writeImage(String path, BufferedImage bufferedImage){
		File file = getLowestAvailablePath(path, "png");

		if(file.exists()){
			file.delete();
			System.out.println("[ INFO] Overwriting file (" + file.getPath() + ")");
		}

		try{
			ImageIO.write(bufferedImage, "png", file);
		}catch(IOException e){
			e.printStackTrace(System.err);
		}
	}


	/**
	 * Método lectura de imágenes desde un archivo.
	 *
	 * @author Silvio Vilerino
	 * @param path
	 */
	public static BufferedImage readImage(String path) {
		File file = new File(path);
		BufferedImage result = null;

		try {
			result = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		return result;
	}

	/**
	 * Método lectura de imágenes desde un archivo.(multiples imagenes como un gif)
	 *
	 * @author Silvio Vilerino
	 * @param path
	 */
	public static BufferedImage[] readImages(String path){
		BufferedImage[] imgArray = null;
		try {
			ImageInputStream in=ImageIO.createImageInputStream(new File(path));
			ImageReader reader=ImageIO.getImageReadersBySuffix("gif").next();
			reader.setInput(in);
			imgArray=new BufferedImage[reader.getNumImages(true)];
			for(int a=0;a<reader.getNumImages(true);a++){
				imgArray[a]=reader.read(a);
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return imgArray;
	}


	public static String[] buildString (Vector<String> Vector){
		String[] result = new String[Vector.size()];

		for(int i = 0 ; i < result.length ; i++){
			result[i] = Vector.get(i);
		}

		return result;
	}


	/**
	 * Encuentra el path asociado más pequeño, agregando un índice.
	 * @author Guido Tagliavini
	 * @param fileName
	 * @param suffix
	 * @return
	 */
	public static File getLowestAvailablePath(String fileName, String suffix){
		File file;
		int n = 0;

		do{
			file = new File(fileName + "(" + n + ")" + "." + suffix);
			++ n;
		}while(file.exists());

		return file;
	}

	/**
	 * @author Silvio Vilerino
	 * @deprecated
	 * @since 25-10-2010
	 */
	public static URL getURL(File file){
		URL result = null;
		try {
			result = file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace(System.err);
		}

		return result;
	}

	private FileUtil(){}

	/**
	 * Devuelve lista de imagenes jpg de un directorio
	 * @author Silvio Vilerino
	 * @param extension
	 * @param dirPath
	 * @return
	 */
	public static File[] listFilesFromDir(String dirPath, final String extension){
		File fl=new File(dirPath);
		if(fl.isDirectory()){
			return fl.listFiles(new FilenameFilter(){

				public boolean accept(File dir, String name) {
					if(name.endsWith(extension)) return true;
					return false;
				}

			});
		}
		return null;
	}
}
