package main.runnable.socket;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import main.dto.ConnectionNodeDTO;
import main.dto.TableDataDTO;
import main.util.DataLoadUtil;

public class ServerListRequest implements Runnable{

	private final static Integer TIMEOUT = 100000;

	private ConnectionNodeDTO node;
	
	private Scanner in;
	private PrintWriter out;

	private Vector<Vector<String>> playersData;
	private Vector<ConnectionNodeDTO> connectionsData;


	public ServerListRequest(ConnectionNodeDTO node){
		ConnectionNodeDTO data = DataLoadUtil.getMainServerProperties();
		
		this.playersData = new Vector<Vector<String>>();
		this.connectionsData = new Vector<ConnectionNodeDTO>();
		this.node = new ConnectionNodeDTO();
		this.node.setAddress(data.getAddress());
		this.node.setPort(data.getPort());
	}

	@Override
	public void run() {
		try{
			Socket socket = new Socket();
			socket.setSoTimeout(TIMEOUT);
			socket.connect(new InetSocketAddress(node.getAddress(), node.getPort()));
			
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);

			System.out.println("[PEER_2] Connecting to: " + node.getAddress() + ":" + node.getPort());

			try{
				boolean exit = false;

				out.println("FLUSH");
				out.println("\\REQUEST_SERVERS");

				System.out.println("[PEER_2] Request sent");

				while(!exit && in.hasNextLine()){
					String response = in.nextLine();
					if(response.equals("\\REQUEST_ACCEPTED")){

						System.out.println("[PEER_2] Receiving object");
						
						TableDataDTO tableDataDTO = new TableDataDTO();
						tableDataDTO.buildObject(in.nextLine());

						System.out.println("[PEER_2] Object received");

						updatePlayersData(tableDataDTO.getTable());
						updateConnectionsData(tableDataDTO.getTable());
						exit = true;

						System.out.println("[PEER_2] Updated info");
					}
				}
				System.out.println("[PEER_2] Closing connection");
			}finally{
				socket.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public Vector<Vector<String>> getPlayersData(){
		return playersData;
	}

	public Vector<ConnectionNodeDTO> getConnectionsData(){
		return connectionsData;
	}

	private void updatePlayersData(Vector<Vector<String>> newData){
		for(int i = 0; i < newData.size(); i++){
			playersData.add(new Vector<String>());
			playersData.get(i).add(newData.get(i).get(2));
			playersData.get(i).add(newData.get(i).get(3));
			playersData.get(i).add(newData.get(i).get(4));
		}
	}

	private void updateConnectionsData(Vector<Vector<String>> newData){
		for(int i = 0; i < newData.size(); i++){
			connectionsData.add(new ConnectionNodeDTO());
			connectionsData.get(i).setName(newData.get(i).get(2));
			connectionsData.get(i).setAddress(newData.get(i).get(0));
			connectionsData.get(i).setPort(Integer.parseInt(newData.get(i).get(1)));
		}
	}
}
