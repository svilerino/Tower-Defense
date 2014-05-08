package main.dto;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TableDataDTO implements JSONSerializable{
	
	private Vector<Vector<String>> table;
	
	public TableDataDTO(){
		super();
	}

	public TableDataDTO(Vector<Vector<String>> table){
		this.table = table;
	}
	
	/**
	 * @return the table
	 */
	public Vector<Vector<String>> getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(Vector<Vector<String>> table) {
		this.table = table;
	}

	@Override
	public String buildJSONString() {
		String jsonString = "";
		
		try {
			jsonString = new JSONObject().put("table", table).toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}

	@Override
	public void buildObject(String jsonString) {
		Vector<Vector<String>> table = new Vector<Vector<String>>();
		
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = (JSONArray) jsonObject.getJSONArray("table");
			for(int i = 0; i < jsonArray.length(); i++){
				JSONArray jsonArrayRow = (JSONArray) jsonArray.get(i);
				Vector<String> row = new Vector<String>();
				for(int j = 0; j < jsonArrayRow.length(); j++){
					row.add((String) jsonArrayRow.getString(j));
				}
				
				table.add(row);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		this.table = table;
	}
}
