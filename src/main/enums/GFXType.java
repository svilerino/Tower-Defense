package main.enums;

public enum GFXType {
	SHOOT_BULLET(0), EXPLOSION(1), TOWER_SHOOT(2);

	private int gfxType;

	private GFXType(int gfxType){
		this.gfxType = gfxType;
	}

	public int getGfxType() {
		return gfxType;
	}

	public void setGfxType(int gfxType) {
		this.gfxType = gfxType;
	}
}
