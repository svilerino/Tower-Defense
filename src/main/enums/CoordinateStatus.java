package main.enums;

/**
 *
 * @author Guido Tagliavini
 * @since v1.0.0
 */

public enum CoordinateStatus{
	FREE(0), OCCUPIED(1);

	private int status;

	private CoordinateStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}
