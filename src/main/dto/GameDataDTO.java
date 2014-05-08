package main.dto;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameDataDTO implements JSONSerializable{

	private Integer actualEnemy;
	private Integer nextEnemy;
	private Vector<TowerDataDTO> towerDataDTO;

	public GameDataDTO(){
		super();
	}

	/**
	 * @return the actualEnemy
	 */
	public Integer getActualEnemy() {
		return actualEnemy;
	}

	/**
	 * @param actualEnemy the actualEnemy to set
	 */
	public void setActualEnemy(Integer actualEnemy) {
		this.actualEnemy = actualEnemy;
	}

	/**
	 * @return the nextEnemy
	 */
	public Integer getNextEnemy() {
		return nextEnemy;
	}

	/**
	 * @param nextEnemy the nextEnemy to set
	 */
	public void setNextEnemy(Integer nextEnemy) {
		this.nextEnemy = nextEnemy;
	}

	/**
	 * @return the towerPropertiesDTO
	 */
	public Vector<TowerDataDTO> getTowerDataDTO() {
		return towerDataDTO;
	}

	/**
	 * @param towerDataDTO the towerPropertiesDTO to set
	 */
	public void setTowerDataDTO(Vector<TowerDataDTO> towerDataDTO) {
		this.towerDataDTO = towerDataDTO;
	}
	
	@Override
	public String buildJSONString() {
		String jsonString = "";
		
		try {
			jsonString = new JSONObject().put("actualEnemy", this.getActualEnemy())
												.put("nextEnemy", this.getNextEnemy())
												.put("towerDataDTO", this.getTowerDataDTO())
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
			this.setNextEnemy((Integer) jsonObject.get("nextEnemy"));
			this.setActualEnemy((Integer) jsonObject.get("actualEnemy"));
			
			Vector<TowerDataDTO> towerDataDTO = new Vector<TowerDataDTO>();
			JSONArray jsonArray = (JSONArray) jsonObject.get("towerDataDTO"); 
			
			for(int i = 0; i < jsonArray.length(); i++){
				TowerDataDTO t = new TowerDataDTO();
				JSONObject jsonObjectElem = (JSONObject)jsonArray.get(i);
				t.setLevel((Integer) jsonObjectElem.get("level"));
				t.setTowerType((Integer) jsonObjectElem.get("towerType"));
				t.setX((Integer) jsonObjectElem.get("x"));
				t.setY((Integer) jsonObjectElem.get("y"));
				
				towerDataDTO.add(t);
			}
			
			this.setTowerDataDTO(towerDataDTO);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
