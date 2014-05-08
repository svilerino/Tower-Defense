package main.runnable.socket;

import java.io.IOException;  
import java.net.Socket;
import java.util.Scanner;
import main.dto.GameDataDTO;
import main.view.frame.GameFrame;

public class ConnectionInput implements Runnable{

	private Socket socket;
	private GameFrame gameFrame;
	private Object flag;
	private Boolean ready;

	private Scanner inStr;

	public ConnectionInput(Socket socket){
		this.socket = socket;
		this.ready = false;

		try {
			inStr = new Scanner(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ConnectionInput(Socket socket, GameFrame gameFrame){
		this(socket);
		this.gameFrame = gameFrame;
	}

	@Override
	public void run() {
		while(inStr.hasNextLine()){
			String response = inStr.nextLine();

			System.out.println(response);

			if(response.equals("\\DATA_TRANSFER")){
				GameDataDTO gameDataDTO = new GameDataDTO();
				gameDataDTO.buildObject(inStr.nextLine());
				gameFrame.updateGameData(gameDataDTO);
			}else if(response.equals("\\READY")){
				synchronized(flag){
					ready = true;
					flag.notifyAll();
				}
			}else if(response.equals("\\CHAT_MESSAGE")){
				String incomingMessage = inStr.nextLine();
				gameFrame.writeMessage("Other", incomingMessage);	//TODO AUTHOR
			}
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

	public Object getFlag() {
		return flag;
	}

	public void setFlag(Object flag) {
		this.flag = flag;
	}

	public Boolean isReady() {
		return ready;
	}

	public void setReady(Boolean ready) {
		this.ready = ready;
	}
}
