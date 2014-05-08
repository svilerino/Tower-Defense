package main.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 *
 * 		   Tomada la Idea de la IP Publica de
 *         http://foro.elhacker.net/java/obtener_mi_propia_ip_publica_en_java-t289242.0.html
 *         Ajustado para manejar objetos InetAddress para una mejor integracion
 *         con la parte de sockets del juego
 *
 * @author Silvio Vilerino
 */
public class NetUtil {

	public static InetAddress getPrimaryPrivateIP() throws UnknownHostException {
		return InetAddress.getLocalHost();
	}

	public static InetAddress getPublicIP() throws UnknownHostException {
		String resolvedIP = "<Not resolved>";
		System.out.println("[ INFO] Resolving Public IP...");
		try {
			URL urlReference = new URL("http://www.whatismyip.org/");
			HttpURLConnection urlConnection = (HttpURLConnection) urlReference
					.openConnection();
			InputStream in = urlConnection.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader tempBr = new BufferedReader(reader);
			resolvedIP = tempBr.readLine();
			tempBr.close();
			in.close();
			System.out
					.println("[ INFO] Public IP Resolved into: " + resolvedIP);
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			resolvedIP = "[ ERROR] No es posible resolver la direccion IP";
		}
		return InetAddress.getByName(resolvedIP);
	}

	public static void main(String args[]) {
		try {
			InetAddress ia = NetUtil.getPublicIP();
			System.out.println("Mi IP Publica es: " + ia.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace(System.err);
		}

		try {
			InetAddress ia = NetUtil.getPrimaryPrivateIP();
			System.out.println("Mi IP Privada es: " + ia.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace(System.err);
		}
	}
}
