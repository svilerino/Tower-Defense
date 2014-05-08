package main.enums;

public enum GameMode {
	SINGLE_PLAYER(0), MULTI_PLAYER(1);
	
	private int gameType;
	
	private GameMode(int gameType){
		this.gameType = gameType;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
}
