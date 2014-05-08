package main.util.validator;

public class ChatValidator {
	
	public static boolean isValid(String message){
		message = message.trim();
		if(! message.equals("")){
			if(message.trim().charAt(0) != '\\'){
				return true;
			}
		}
		
		return false;
	}
}
