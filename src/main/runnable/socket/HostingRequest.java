package main.runnable.socket;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import main.consts.ConnectionConsts;
import main.dto.ConnectionNodeDTO;
import main.dto.HostNodeDTO;
import main.enums.GameMode;
import main.util.DataLoadUtil;

public class HostingRequest implements Runnable{

	private ConnectionNodeDTO serverData;
	private HostNodeDTO hostNodeDTO;
	
	private Scanner in;
	private PrintWriter out;

	private Object closeFlag;

	public HostingRequest(HostNodeDTO hostNodeDTO){
		this.closeFlag = false;
		this.hostNodeDTO = hostNodeDTO;
		this.serverData = DataLoadUtil.getMainServerProperties();
	}

	@Override
	public void run() {
		try{
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(serverData.getAddress(), serverData.getPort()));
			
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
			
			try{
				boolean exit = false;

				out.println("FLUSH");
				out.println("\\REQUEST_HOSTING");
				
				System.out.println("[PEER_1] Hosting requested");

				while(!exit && in.hasNextLine()){
					String response = in.nextLine();
					if(response.equals("\\REQUEST_ACCEPTED")){
						
						System.out.println("[PEER_1] Sending data");
						
						out.println(hostNodeDTO.buildJSONString());
						
						System.out.println("[PEER_1] Data sent");
						
						exit = true;
					}
				}

				synchronized(closeFlag){
					closeFlag.wait();
				}
				System.out.println("[PEER_1] Hosting request end");
			}finally{
				socket.close();
			}
		}catch(Exception e){
			e.printStackTrace(System.err);
		}
	}

	public Object getCloseFlag() {
		return closeFlag;
	}

	public void setCloseFlag(Object closeFlag) {
		this.closeFlag = closeFlag;
	}

	@Deprecated
	private void fillData_test(HostNodeDTO data){
		data.setAddress("127.0.0.1");
		data.setPort(ConnectionConsts.PEER_PORT);
		data.setName("Test Server");
		data.setGameMode(GameMode.MULTI_PLAYER);
		data.setServerName("PL3 Server");
	}

}
