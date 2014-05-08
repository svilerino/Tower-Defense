/**
 *
 */
package main.sound;

import java.io.File;
import java.io.FilenameFilter;

import main.consts.FilePathConsts;

/**
 * @author Silvio Vileriño Clase que permite la seleccion aleatoria de sonidos
 *         de fondo y de efectos Los archivos de background pueden tener
 *         cualquier nombre y tener extension mid,midi o wav Los archivos de
 *         Enemy deben COMENZAR(puede ser dead_enemy1,dead_enemy2,etc...) con
 *         dead_enemy o enemy_won segun corresponda y tener extension wav.
 *         
 *
 * Los archivos de Tower deben tener extension wav.
 *
 */
public class RandomSoundSelector {

	private File backgroundSoundsList[];
	private File roundSoundsList[];
	private File enemyWonSoundsList[];
	private File enemyDownSoundsList[];
	private File towerSoundsList[];

	/**
	 * Usa las constantes de FilePathUtil
	 */
	public RandomSoundSelector() {
		File backSoundsFolder = new File(FilePathConsts.backgroundSoundsPath);
		File roundSoundsFolder = new File(FilePathConsts.roundSoundsPath);
		File enemySoundsFolder = new File(FilePathConsts.enemiesSoundPath);
		File towerSoundsFolder = new File(FilePathConsts.towersSoundPath);

		if (backSoundsFolder.isDirectory()) {
			this.backgroundSoundsList = backSoundsFolder
					.listFiles(new LongSoundFileTypeFilter());
		}

		if (roundSoundsFolder.isDirectory()) {
			this.roundSoundsList = roundSoundsFolder
					.listFiles(new LongSoundFileTypeFilter());
		}

		if (enemySoundsFolder.isDirectory()) {
			this.enemyWonSoundsList = enemySoundsFolder
					.listFiles(new EnemyWonFileTypeFilter());
			this.enemyDownSoundsList = enemySoundsFolder
					.listFiles(new EnemyDownFileTypeFilter());
		}

		if (towerSoundsFolder.isDirectory()) {
			this.towerSoundsList = towerSoundsFolder
					.listFiles(new TowerFileTypeFilter());
		}

	}

	/**
	 *
	 * @return devuelve un archivo de sonido de fondo al azar
	 */
	public File getRandomBackgroundSound() {
		return this.backgroundSoundsList[(int) (Math.random() * this.backgroundSoundsList.length)];
	}

	/**
	 *
	 * @return devuelve un archivo de sonido de fondo de partida al azar
	 */
	public File getRandomRoundSound() {
		return this.roundSoundsList[(int) (Math.random() * this.roundSoundsList.length)];
	}

	/**
	 *
	 * @return devuelve un archivo de sonido de enemigo matado por torres
	 */
	public File getRandomEnemyDownSound() {
		return this.enemyDownSoundsList[(int) (Math.random() * this.enemyDownSoundsList.length)];
	}

	/**
	 *
	 * @return devuelve un archivo de sonido de enemigo que llego a la meta
	 */
	public File getRandomEnemyWonSound() {
		return this.enemyWonSoundsList[(int) (Math.random() * this.enemyWonSoundsList.length)];
	}

	/**
	 *
	 * @return devuelve un archivo de sonido de disparo de torre
	 */
	public File getRandomTowerSound() {
		return this.towerSoundsList[(int) (Math.random() * this.towerSoundsList.length)];
	}

	/**
	 *
	 * @author Silvio Vileriño Filtro de tipos de archivo soportados segun
	 *         corresponda
	 */
	private class LongSoundFileTypeFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String file) {
			if(file.startsWith("back")){
				if (file.endsWith(".midi"))
					return true;
				if (file.endsWith(".mid"))
					return true;
				if (file.endsWith(".mp3"))
					return true;
			}
			return false;
		}
	}

	/**
	 *
	 * @author Silvio Vileriño Filtro de tipos de archivo soportados segun
	 *         corresponda
	 */
	private class EnemyDownFileTypeFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String file) {
			if ((file.endsWith(".mp3")) && (file.startsWith("enemy_died")))
				return true;
			return false;
		}

	}

	/**
	 *
	 * @author Silvio Vileriño Filtro de tipos de archivo soportados segun
	 *         corresponda
	 */
	private class TowerFileTypeFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String file) {
			if (file.endsWith(".mp3"))
				return true;
			if (file.endsWith(".wma"))
				return true;
			return false;
		}

	}

	/**
	 * @author Silvio Vileriño Filtro de tipos de archivo soportados segun
	 *         corresponda
	 */
	private class EnemyWonFileTypeFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String file) {
			if ((file.endsWith(".mp3")) && (file.startsWith("enemy_won")))
				return true;
			return false;
		}

	}

	/**
	 *
	 * Main de prueba de la clase.
	 */
	public static void main(String[] args) {
		RandomSoundSelector rss = new RandomSoundSelector();
		System.out.println(rss.getRandomBackgroundSound());
		System.out.println(rss.getRandomEnemyDownSound());
		System.out.println(rss.getRandomEnemyWonSound());
		System.out.println(rss.getRandomTowerSound());
		// Ya existen metodos para obtener URL en FileUtil, me parecio más
		// apropiado utilizar los
		// de ConversorUtil
	}

}
