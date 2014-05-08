package main.runnable.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.consts.ConnectionConsts;

public class IncomingConnectionListener implements Runnable{

	//private HostingRequest requestRunnable;
	private Socket socket;
	private Object closeFlag;

	public IncomingConnectionListener(Object closeFlag){//HostingRequest requestRunnable){
		//this.requestRunnable = requestRunnable;
		this.closeFlag = closeFlag;
	}

	@Override
	public void run() {
		
		System.out.println("[PEER_1] Awaiting connection");
		
		try{
			ServerSocket s = new ServerSocket(ConnectionConsts.PEER_PORT);
			socket = s.accept();
			
			System.out.println("[PEER_1] Connection accepted");
			
			synchronized(closeFlag){
				closeFlag.notifyAll();	//closes the request thread
			}
		}catch(IOException e){
			e.printStackTrace(System.err);
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
