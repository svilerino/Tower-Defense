package main.consts;

import main.enums.AimingAIStrategy;

/**
 * Clase que contiene constantes relacionadas con el juego propiamente dicho,
 * utilizadas en todo el programa.
 *
 * @author Silvio Vilerino
 */
public class GameConsts {

	public static final Integer STARTER_BONUS = 0;
	public static final Integer STARTER_LIFES = 50;
	public static final Integer STARTER_MONEY = 10000;
	public static final Integer STARTER_POINTS = 0;
	public static final Integer LIFE_BONUS_AMOUNT = -3;
	public static final Integer MONEY_BONUS_AMOUNT = 500;
	public static final Integer SCORE_BONUS_AMOUNT = 150;
	public static final Integer BONUS_ENEMY_SPEED_TARGET = 20;
	public static final Long BONUS_ENEMY_SPEED_DELAY = 10000L;
	public static final Integer BONUS_TOWER_SPEED_TARGET = 500;
	public static final Long BONUS_TOWER_SPEED_DELAY = 15000L;
	public static final Integer BONUS_KILLING_TARGET = 5;
	public static final Integer UPGRADE_TOWER_DAMAGE = 50;
	public static final Integer UPGRADE_TOWER_RADIO = 10;
	public static final Integer UPGRADE_TOWER_DELAY = 50;
	public static final String PREFERRED_BACKGROUND_NAME = "back dark side moon.jpg";
	public static final AimingAIStrategy PREFERRED_AIMING_STRATEGY = AimingAIStrategy.CLOSEST_ENEMY;
	public static final Integer TOTAL_ENEMIES_PER_ROUND = 30;
	public static final Integer ENEMY_ADDING_DELAY_BASE = 300;
	public static final Integer ENEMY_ADDING_DELAY_TOP = 600;
	public static final Boolean TOWERS_PAINTED_BASE = false;


	public static final Boolean CACHED_THREAD_EXECUTORS=true;//false stands for fixed with below max threads.
	public static final Integer MAX_THREADS_PER_FIXED_EXECUTOR=75;

	public static final Long EXPLOSION_DELAY=20L;
	public static final Integer SHOOT_MOVE_DELAY=10;
}
