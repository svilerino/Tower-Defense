package services;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Logging Class Service
 * @author Silvio Vileriño
 *
 */
public class LoggerService {

	private static Logger logger;
	private static Class sender;

	public LoggerService(Class sender){
		this.sender=sender;
		logger=Logger.getLogger(sender.getName());
		File file = new File(System.getProperty("user.home") + "/PL3");

		if(!file.exists())
			file.mkdir();

		try {
			FileHandler handler = new FileHandler("%h/PL3/execution_" + sender.getName() + ".log");
			logger.addHandler(handler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void submitLog(Level level, String message){
		logger.log(level, message);
	}

	//TODO fijate como es el caso para cuando tiras una exception
}
