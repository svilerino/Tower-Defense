package main.enums;

public enum AimingAIStrategy {
	CLOSEST_ENEMY(0), WEAKEST_ENEMY(1);
	
	private int aimingAIType;
	
	private AimingAIStrategy(int aimingAIType){
		this.aimingAIType = aimingAIType;
	}
	
	public int getAimingAIType(){
		return aimingAIType;
	}
}
