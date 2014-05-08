/**
 *
 */
package main.view.panel;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.enums.TowerType;
import main.util.ConversorUtil;
import main.util.DataLoadUtil;

/**
 * @author Silvio
 *
 */
public class TowerPicker extends JPanel {

	private static final long serialVersionUID = 5904577930039201969L;
	private Vector<ThumbPanel> tpTowers;
	private TowerInfo infoShower;

	public TowerPicker(Point2D position, GridPanel gamePanelRef,
			TowerInfo infoShower) {
		super();
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds((int) position.getX(), (int) position.getY(), 185, 60);
		this.setLayout(null);
		this.tpTowers = new Vector<ThumbPanel>();

		this.addTowerModel(new ThumbPanel(TowerType.AEREAL, gamePanelRef,
				infoShower, ConversorUtil.pixelsToLogic(DataLoadUtil
						.getTowerProperties(TowerType.AEREAL).getSizeInPix())));
		this.addTowerModel(new ThumbPanel(TowerType.CANNON, gamePanelRef,
				infoShower, ConversorUtil.pixelsToLogic(DataLoadUtil
						.getTowerProperties(TowerType.CANNON).getSizeInPix())));
		this
				.addTowerModel(new ThumbPanel(TowerType.CLASSIC, gamePanelRef,
						infoShower, ConversorUtil.pixelsToLogic(DataLoadUtil
								.getTowerProperties(TowerType.CLASSIC)
								.getSizeInPix())));
		this
				.addTowerModel(new ThumbPanel(TowerType.FREEZER, gamePanelRef,
						infoShower, ConversorUtil.pixelsToLogic(DataLoadUtil
								.getTowerProperties(TowerType.FREEZER)
								.getSizeInPix())));
		this
				.addTowerModel(new ThumbPanel(TowerType.MULTIPLE, gamePanelRef,
						infoShower, ConversorUtil.pixelsToLogic(DataLoadUtil
								.getTowerProperties(TowerType.MULTIPLE)
								.getSizeInPix())));
		this.addTowerModel(new ThumbPanel(TowerType.QUICK, gamePanelRef,
				infoShower, ConversorUtil.pixelsToLogic(DataLoadUtil
						.getTowerProperties(TowerType.QUICK).getSizeInPix())));
		this.addTowerModel(new ThumbPanel(TowerType.SLOW, gamePanelRef,
				infoShower, ConversorUtil.pixelsToLogic(DataLoadUtil
						.getTowerProperties(TowerType.SLOW).getSizeInPix())));

		JLabel jlbTowers = new JLabel("<html><b>Towers Selector</b>");
		jlbTowers.setBounds(45, 7, 100, 10);
		this.add(jlbTowers);
		this.loadTowers();
	}

	public void loadTowers() {
		int a = 0;
		for (ThumbPanel t : tpTowers) {
			t.setBounds(a * 24 + 8, 30, 24, 24);
			this.add(t);
			a++;
		}
	}

	public void addTowerModel(ThumbPanel tower) {
		this.tpTowers.add(tower);
	}

	/**
	 * @return the infoShower
	 */
	public TowerInfo getInfoShower() {
		return infoShower;
	}

	/**
	 * @param infoShower
	 *            the infoShower to set
	 */
	public void setInfoShower(TowerInfo infoShower) {
		this.infoShower = infoShower;
	}

	public Vector<ThumbPanel> getTpTowers() {
		return tpTowers;
	}
}
