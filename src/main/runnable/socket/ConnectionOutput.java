package main.runnable.socket;

import java.io.IOException;  
import java.io.PrintWriter;
import java.net.Socket;

import main.dto.GameDataDTO;
import main.enums.ConnectionAction;
import main.view.frame.GameFrame;
 
public class ConnectionOutput implements Runnable{

	private Socket socket;
	private GameFrame gameFrame;
	private ConnectionAction connectionAction;
	private Object data;

	private PrintWriter outStr;

	public ConnectionOutput(Socket socket){
		this.socket = socket;

		try {
			this.outStr = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void run() {
		//try{
		outStr.println("FLUSH");

		if(connectionAction == ConnectionAction.SEND_MESSAGE){
			String message = (String) data;
			outStr.println("\\CHAT_MESSAGE");
			outStr.println(message);
		}else if(connectionAction == ConnectionAction.SEND_GAME_DATA){
			outStr.println("\\DATA_TRANSFER");
			GameDataDTO gameDataDTO = (GameDataDTO) data;
			outStr.println(gameDataDTO.buildJSONString());
		}else if(connectionAction == ConnectionAction.SEND_READY){
			outStr.println("\\READY");
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public GameFrame getGameFrame() {
		return gameFrame;
	}

	public void setGameFrame(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}

	public ConnectionAction getConnectionAction() {
		return connectionAction;
	}

	public void setConnectionAction(ConnectionAction connectionAction) {
		this.connectionAction = connectionAction;
	}

	public synchronized Object getData() {
		return data;
	}

	public synchronized void setData(Object data) {
		this.data = data;
	}

}
