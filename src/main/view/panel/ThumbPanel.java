package main.view.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.consts.FilePathConsts;
import main.enums.TowerType;
import main.event.ComponentSFX;
import main.util.DataLoadUtil;
import main.util.FileUtil;

public class ThumbPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private BufferedImage baseImage;
	private GridPanel gamePanelReference;
	private TowerInfo infoShowerReference;
	private Cursor towerCursor;
	private Integer selectedTowerSize;
	private TowerType selectedTowerType;

	public ThumbPanel(TowerType type, GridPanel gamepanel,
			TowerInfo infoShower, Integer towerSize) {
		super();
		this.setSize(24, 24);
		this.selectedTowerType = type;
		this.selectedTowerSize = towerSize;
		this.image = FileUtil.readImage(FilePathConsts.towersSpritesPath + "\\"
				+ DataLoadUtil.getTowerProperties(type).getImageName());
		this.baseImage = FileUtil.readImage(FilePathConsts.towersSpritesPath
				+ "\\"
				+ DataLoadUtil.getTowerProperties(type).getBaseImageName());
		this.gamePanelReference = gamepanel;
		this.infoShowerReference = infoShower;
		this.setBackground(Color.LIGHT_GRAY);
		this.setToolTipText(infoToolTip());
		this.addMouseListener(new TowerMouseAction());
		this.addMouseListener(new ComponentSFX());
		this.setOpaque(false);
	}

	public String infoToolTip(){
		String data="<html>";
		data+="Tipo: " + getSelectedTowerType() + "<br>";
		data+= "Cost: " + DataLoadUtil.getTowerProperties(getSelectedTowerType()).getTowerCost() + "<br>";
		data+= "Upgrade cost: " + DataLoadUtil.getTowerProperties(getSelectedTowerType()).getUpgradeCost() + "<br>";
		data+= "Damage: " + DataLoadUtil.getTowerProperties(getSelectedTowerType()).getShootDamage() + "<br>";
		data+= "Delay: " + DataLoadUtil.getTowerProperties(getSelectedTowerType()).getShootDelay() + "<br>";
		data+= "Radio: " + DataLoadUtil.getTowerProperties(getSelectedTowerType()).getShootRadius() + "<br>";
		data+= "Style: " + DataLoadUtil.getTowerProperties(getSelectedTowerType()).getShootStyle() + "<br>";
		return data;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.baseImage, null, 0, 0);
		g2.drawImage(this.image, null, 0, 0);

	}

	/**
	 * @return the type
	 */
	public TowerType getSelectedTowerType() {
		return selectedTowerType;
	}

	private class TowerMouseAction extends MouseAdapter {

		public TowerMouseAction() {
			this.createTowerCursor();
		}

		public void mousePressed(MouseEvent e) {
			System.out.println("[ INFO] Tower " + getSelectedTowerType() + " selected");
			gamePanelReference.setThumbPanel(ThumbPanel.this);
			gamePanelReference.getAncestorContainer().setCursor(towerCursor);
			gamePanelReference.setCursor(towerCursor);
		}

		private void createTowerCursor() {
			Toolkit tk = Toolkit.getDefaultToolkit();
			BufferedImage cursorImage = new BufferedImage(image.getWidth(),
					image.getHeight(), BufferedImage.TRANSLUCENT);
			cursorImage.createGraphics().drawImage(baseImage, null, 0, 0);
			cursorImage.createGraphics().drawImage(image, null, 0, 0);
			towerCursor = tk.createCustomCursor(cursorImage, new Point(0, 0),
					getSelectedTowerType().toString());

		}
	}

	/**
	 * @return the towerSize
	 */
	public Integer getSelectedTowerSize() {
		return selectedTowerSize;
	}
}
