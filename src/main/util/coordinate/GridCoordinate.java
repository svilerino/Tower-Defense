package main.util.coordinate;

import java.io.Serializable;

import main.enums.CoordinateStatus;
import main.enums.TowerType;

/**
 * Clase que compone a cada una de las coordenadas de la grilla
 * @author Guido Tagliavini
 */

public class GridCoordinate implements Serializable{

	private static final long serialVersionUID = 1901171054125457034L;
	private CoordinateStatus coordinateStatus;		//Estado de la coordenada: vacío/ocupado
	private TowerType towerType;

	public GridCoordinate(){
		super();
		towerType = null;
		coordinateStatus = CoordinateStatus.FREE;
	}

	public CoordinateStatus getCoordinateStatus() {
		return coordinateStatus;
	}

	public void setCoordinateStatus(CoordinateStatus coordinateStatus) {
		this.coordinateStatus = coordinateStatus;
	}

	public TowerType getTowerType() {
		return towerType;
	}

	public void setTowerType(TowerType towerType) {
		this.towerType = towerType;
	}
}
