package main.consts;

/**
 * Clase que contiene constantes relacionadas con los directorios, utilizadas en todo el programa.
 * 
 * @author Silvio Vileriño
 * @author Guido Tagliavini
 */

public class FilePathConsts {
	
	public static final String backgroundSoundsPath = System
			.getProperty("user.dir") + "/snd/background";

	public static final String roundSoundsPath = System
	.getProperty("user.dir") + "/snd/round";

	public static final String enemiesSoundPath = System
			.getProperty("user.dir") + "/snd/enemies";

	public static final String towersSoundPath = System.getProperty("user.dir")
			+ "/snd/towers";

	public static final String componentSFXPath= System.getProperty("user.dir")
	+ "/snd/buttons";

	public static final String sfxPath= System.getProperty("user.dir")
	+ "/snd/sfx";

	public static final String backgroundSpritesPath = System
			.getProperty("user.dir") + "/img/sprites/background";

	public static final String explosionSpritesPath = System
			.getProperty("user.dir") + "/img/sprites/gfx";

	public static final String menuImagesPath = System.getProperty("user.dir")
			+ "/img/menu";

	public static final String towersSpritesPath = System
			.getProperty("user.dir") + "/img/sprites/towers";

	public static final String enemyImagePath = System.getProperty("user.dir")
			+ "/img/sprites/enemies";

	public static final String gridPersistencePath = System
			.getProperty("user.dir") + "/data";

	public static final String serverPropertiesPath = System
			.getProperty("user.dir") + "/properties/server.properties";

	public static final String enemyPropertiesPath = System
			.getProperty("user.dir") + "/properties/enemy.properties";

	public static final String towerPropertiesPath = System
			.getProperty("user.dir") + "/properties/tower.properties";

	private FilePathConsts() {}
}
