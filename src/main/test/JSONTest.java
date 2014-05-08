package main.test;

import static org.junit.Assert.*;

import java.util.Vector;

import main.dto.GameDataDTO;
import main.dto.HostNodeDTO;
import main.dto.TowerDataDTO;
import main.enums.GameMode;

import org.junit.Before;
import org.junit.Test;
import org.json.*;

public class JSONTest {
	
	private GameDataDTO gameDataDTO;
	private HostNodeDTO hostNodeDTO;
	private Vector<Vector<String>> table;
	
	@Before
	public void setUp(){
		TowerDataDTO towerDataDTO = new TowerDataDTO();
		towerDataDTO.setLevel(3);
		towerDataDTO.setTowerType(4);
		towerDataDTO.setX(10);
		towerDataDTO.setY(10);
				
		Vector<TowerDataDTO> towerDataDTOArray = new Vector<TowerDataDTO>();
		
		towerDataDTOArray.add(towerDataDTO);
		
		gameDataDTO = new GameDataDTO();
		gameDataDTO.setActualEnemy(3);
		gameDataDTO.setNextEnemy(2);
		gameDataDTO.setTowerDataDTO(towerDataDTOArray);
		
		////////
		
		hostNodeDTO = new HostNodeDTO();
		hostNodeDTO.setAddress("127.0.0.1");
		hostNodeDTO.setGameMode(GameMode.SINGLE_PLAYER);
		hostNodeDTO.setName("Guido");
		hostNodeDTO.setPort(123);
		hostNodeDTO.setServerName("Server");
				
		////////
		
		table = new Vector<Vector<String>>();
		Vector<String> row = new Vector<String>();
		row.add("Col11");
		row.add("Col12");
		row.add("");
		row.add("");
		row.add("Col15");
		
		table.add(row);
		
		row = new Vector<String>();
		row.add("");
		row.add("Col22");
		row.add("Col23");
		row.add("");
		row.add("Col25");
		
		table.add(row);
	}
	
	@Test
	public void test(){
		String jsonString = "";
		JSONObject jsonObject = null;
		try {
			
			/*
			 * GameDataDTO test
			 */
			
			//String
			jsonString = new JSONObject().put("actualEnemy", gameDataDTO.getActualEnemy())
												.put("nextEnemy", gameDataDTO.getNextEnemy())
												.put("towerDataDTO", gameDataDTO.getTowerDataDTO())
												.toString();

			//Object
			jsonObject = new JSONObject(jsonString);
			GameDataDTO gameDataDTOTest = new GameDataDTO();
			gameDataDTOTest.setNextEnemy((Integer) jsonObject.get("nextEnemy"));
			gameDataDTOTest.setActualEnemy((Integer) jsonObject.get("actualEnemy"));
			
			Vector<TowerDataDTO> towerDataDTO = new Vector<TowerDataDTO>();
			JSONArray jsonArray = (JSONArray) jsonObject.get("towerDataDTO"); 
			
			for(int i = 0; i < jsonArray.length(); i++){
				TowerDataDTO t = new TowerDataDTO();
				t.setLevel((Integer) ((JSONObject)jsonArray.get(i)).get("level"));
				t.setTowerType((Integer) ((JSONObject)jsonArray.get(i)).get("towerType"));
				t.setX((Integer) ((JSONObject)jsonArray.get(i)).get("x"));
				t.setY((Integer) ((JSONObject)jsonArray.get(i)).get("y"));
				
				towerDataDTO.add(t);
			}
			
			gameDataDTOTest.setTowerDataDTO(towerDataDTO);
			
			//Test
			assertEquals(gameDataDTO.getActualEnemy(), gameDataDTOTest.getActualEnemy());
			assertEquals(gameDataDTO.getNextEnemy(), gameDataDTOTest.getNextEnemy());
			
			for(int i = 0; i < gameDataDTO.getTowerDataDTO().size(); i++){
				TowerDataDTO a = gameDataDTO.getTowerDataDTO().get(i);
				TowerDataDTO b = gameDataDTOTest.getTowerDataDTO().get(i);
				
				assertEquals(a.getLevel(), b.getLevel());
				assertEquals(a.getTowerType(), b.getTowerType());
				assertEquals(a.getX(), b.getX());
				assertEquals(a.getY(), b.getY());
			}
					
			/*
			 * HostNodeDTO test
			 */
			
			//String
			jsonString = new JSONObject().put("address", hostNodeDTO.getAddress())
											.put("gameMode", hostNodeDTO.getGameMode())
											.put("name", hostNodeDTO.getName())
											.put("port", hostNodeDTO.getPort())
											.put("serverName", hostNodeDTO.getServerName())
											.toString();
			
			//Object
			jsonObject = new JSONObject(jsonString);
			HostNodeDTO hostNodeDTOTest = new HostNodeDTO();
			hostNodeDTOTest.setAddress((String) jsonObject.get("address"));
			hostNodeDTOTest.setGameMode(GameMode.valueOf((String) jsonObject.get("gameMode")));
			hostNodeDTOTest.setName((String) jsonObject.getString("name"));
			hostNodeDTOTest.setPort((Integer) jsonObject.get("port"));
			hostNodeDTOTest.setServerName((String) jsonObject.getString("serverName"));
			
			//Test
			assertEquals(hostNodeDTO.getAddress(), hostNodeDTOTest.getAddress());
			assertEquals(hostNodeDTO.getGameMode(), hostNodeDTOTest.getGameMode());
			assertEquals(hostNodeDTO.getName(), hostNodeDTOTest.getName());
			assertEquals(hostNodeDTO.getPort(), hostNodeDTOTest.getPort());
			assertEquals(hostNodeDTO.getServerName(), hostNodeDTOTest.getServerName());
			
			/*
			 * Vector<Vector<String>> test
			 */
			
			//String
			jsonString = new JSONObject().put("table", table).toString();

			//Object
			Vector<Vector<String>> tableTest = new Vector<Vector<String>>();
			jsonObject = new JSONObject(jsonString);
			jsonArray = (JSONArray) jsonObject.getJSONArray("table");
			for(int i = 0; i < jsonArray.length(); i++){
				JSONArray jsonArrayRow = (JSONArray) jsonArray.get(i);
				Vector<String> row = new Vector<String>();
				for(int j = 0; j < jsonArrayRow.length(); j++){
					row.add((String) jsonArrayRow.getString(j));
				}
				
				tableTest.add(row);
			}
			
			//Test
			for(int i = 0; i < table.size(); i++){
				for(int j = 0; j < table.get(i).size(); j++){
					String a = table.get(i).get(j);
					String b = tableTest.get(i).get(j);
					
					assertEquals(a,b);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
