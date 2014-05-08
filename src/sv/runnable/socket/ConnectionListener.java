package sv.runnable.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

import main.dto.HostNodeDTO;
import main.util.DataLoadUtil;

public class ConnectionListener implements Runnable {

	//private static final Integer PORT = 7000;
	private static final Integer PORT = DataLoadUtil.getMainServerProperties().getPort();

	private Vector<Vector<String>> data;

	public ConnectionListener() {
		this.data = new Vector<Vector<String>>();
	}

	@Override
	public void run() {
		try {
			ServerSocket s = new ServerSocket(PORT);

			while (true) {
				Socket socket = s.accept();

				System.out.println("[ INFO] Connection stablished with "
						+ Arrays.toString(socket.getInetAddress().getAddress()));

				Runnable player = new PlayerConnection(socket, this);
				Thread t = new Thread(player);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	public Vector<Vector<String>> getData(){
		return data;
	}
	
	public void removeData(HostNodeDTO playerData){
		synchronized(this.data){
			this.data.remove(getHostPlayerIndex(playerData.getAddress()).intValue());
		}
	}
	
	public void addData(HostNodeDTO playerData){
		synchronized(this.data){
			Vector<String> data = new Vector<String>();
			data.add(playerData.getAddress());
			data.add(String.valueOf(playerData.getPort()));
			data.add(playerData.getName());
			data.add(playerData.getGameMode().toString());
			data.add(playerData.getServerName());
			
			this.data.add(data);
		}
	}
	
	private Integer getHostPlayerIndex(String address){
		
		for(int i = 0; i < data.size(); i++){
			if(data.get(i).get(0).equals(address)){
				return i;
			}
		}
		
		return -1;
	}
	
}
