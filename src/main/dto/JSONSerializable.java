package main.dto;

public interface JSONSerializable {
	
	public abstract String buildJSONString();
	
	public abstract void buildObject(String jsonString);
}
