package main.dto;


import org.json.JSONException;
import org.json.JSONObject;

import main.enums.GameMode;

public class HostNodeDTO extends ConnectionNodeDTO implements JSONSerializable{

	private GameMode gameMode;
	private String serverName;
	
	/**
	 * @return the gameType
	 */
	public GameMode getGameMode() {
		return gameMode;
	}
	
	/**
	 * @param gameMode the gameType to set
	 */
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	
	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	@Override
	public String buildJSONString() {
		String jsonString = "";
		
		try {
			jsonString = new JSONObject().put("address", this.getAddress())
			.put("gameMode", this.getGameMode())
			.put("name", this.getName())
			.put("port", this.getPort())
			.put("serverName", this.getServerName())
			.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}

	@Override
	public void buildObject(String jsonString) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			this.setAddress((String) jsonObject.get("address"));
			this.setGameMode(GameMode.valueOf((String) jsonObject.get("gameMode")));
			this.setName((String) jsonObject.getString("name"));
			this.setPort((Integer) jsonObject.get("port"));
			this.setServerName((String) jsonObject.getString("serverName"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
