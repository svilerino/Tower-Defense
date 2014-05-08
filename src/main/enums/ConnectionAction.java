package main.enums;

public enum ConnectionAction {
	SEND_MESSAGE(0), SEND_GAME_DATA(1), SEND_READY(2);
	
	private int connectionAction;
	
	private ConnectionAction(int connectionAction){
		this.connectionAction = connectionAction;
	}

	public int getConnectionAction() {
		return connectionAction;
	}

	public void setConnectionAction(int connectionAction) {
		this.connectionAction = connectionAction;
	}
}
