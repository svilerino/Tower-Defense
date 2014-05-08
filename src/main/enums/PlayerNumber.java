package main.enums;

public enum PlayerNumber {
	PLAYER_1(0), PLAYER_2(1);
	
	private int playerNumber;
	
	private PlayerNumber(int playerNumber){
		this.playerNumber = playerNumber;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
}
