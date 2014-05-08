package sv.runnable.socket;

import java.io.IOException;   
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import main.dto.HostNodeDTO;
import main.dto.TableDataDTO;

/**
 *
 * @author Guido Tagliavini
 *
 */

public class PlayerConnection implements Runnable{

	private Socket socket;
	private ConnectionListener connectionListener;
	private PrintWriter outStr;
	private Scanner inStr;

	public PlayerConnection(Socket socket, ConnectionListener connectionListener){
		this.socket = socket;
		this.connectionListener = connectionListener;
		try {
			this.outStr = new PrintWriter(socket.getOutputStream(), true);
			this.inStr = new Scanner(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		try{
			try{
				HostNodeDTO playerData = null;

				boolean exit = false;

				try{
					while(!exit && inStr.hasNextLine()){
						String request = inStr.nextLine();
						outStr.println("FLUSH");

						if(request.equals("\\REQUEST_HOSTING")){
							outStr.println("\\REQUEST_ACCEPTED");

							playerData = new HostNodeDTO();
							playerData.buildObject(inStr.nextLine());
							connectionListener.addData(playerData);

						}else if (request.equals("\\REQUEST_SERVERS")){

							System.out.println("[SERVER] Request received");

							outStr.println("\\REQUEST_ACCEPTED");

							System.out.println("[SERVER] Sending object");
							
							System.out.println(connectionListener.getData());
							
							TableDataDTO tableDataDTO = new TableDataDTO(connectionListener.getData());
							outStr.println(tableDataDTO.buildJSONString());

							System.out.println("[SERVER] Object sent");
						}
					}
				}finally{
					if(playerData != null){
						connectionListener.removeData(playerData);
					}
				}
			}finally{
				socket.close();
			}
		}catch (Exception e){
			e.printStackTrace(System.err);
		}
	}
}
