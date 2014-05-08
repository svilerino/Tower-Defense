package main.util;

import main.consts.FilePathConsts;
import main.dto.ConnectionNodeDTO;
import main.dto.EnemyDTO;
import main.dto.TowerDTO;
import main.enums.EnemyType;
import main.enums.TowerType;

public class DataLoadUtil {

	public static EnemyDTO getEnemyProperties(EnemyType enemyType){
		String result = FileUtil.readText(FilePathConsts.enemyPropertiesPath).get(enemyType.getEnemyType());
		String[] tokens = result.split(";");

		EnemyDTO enemyProps = new EnemyDTO();

		enemyProps.setTotalLife(Integer.parseInt(tokens[0]));
		enemyProps.setDelay(Integer.parseInt(tokens[1]));
		enemyProps.setSizeInPix(Integer.parseInt(tokens[2]));
		enemyProps.setImageName(tokens[3]);
		enemyProps.setMoneyReward(Integer.parseInt(tokens[4]));
		enemyProps.setScoreReward(Integer.parseInt(tokens[5]));

		return enemyProps;
	}

	public static TowerDTO getTowerProperties(TowerType towerType){
		String result = FileUtil.readText(FilePathConsts.towerPropertiesPath).get(towerType.getTowerType());
		String[] tokens = result.split(";");

		TowerDTO towerProps = new TowerDTO();

		towerProps.setShootRadius(Integer.parseInt(tokens[0]));
		towerProps.setShootDelay(Integer.parseInt(tokens[1]));
		towerProps.setShootDamage(Integer.parseInt(tokens[2]));
		towerProps.setColor(ConversorUtil.stringToColor(tokens[3]));
		towerProps.setSizeInPix(Integer.parseInt(tokens[4]));
		towerProps.setImageName(tokens[5]);
		towerProps.setBaseImageName(tokens[6]);
		towerProps.setShootStyle(ConversorUtil.stringToShoot(tokens[7]));
		towerProps.setTowerCost(Integer.parseInt(tokens[8]));
		towerProps.setUpgradeCost(Integer.parseInt(tokens[9]));

		return towerProps;
	}

	public static ConnectionNodeDTO getMainServerProperties(){
		String result = FileUtil.readText(FilePathConsts.serverPropertiesPath).get(0);
		String[] tokens = result.split(";");

		ConnectionNodeDTO svProps = new ConnectionNodeDTO();

		svProps.setAddress(tokens[0]);
		svProps.setPort(Integer.parseInt(tokens[1]));

		return svProps;
	}

}
