/**
 *
 */
package main.view.panel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.consts.FilePathConsts;
import main.consts.GameConsts;
import main.dto.GameDataDTO;
import main.dto.TowerDataDTO;
import main.enums.EnemyType;
import main.enums.GFXType;
import main.enums.GameMode;
import main.enums.PlayerNumber;
import main.enums.ShootStyle;
import main.enums.TowerType;
import main.model.ai.MovementAI;
import main.model.enemy.ShapedEnemy;
import main.runnable.gfx.Explosion;
import main.runnable.gfx.ShootRunnable;
import main.model.gfx.LaserShoot;
import main.model.gfx.ShapedGFX;
import main.model.gfx.Shoot;
import main.model.grid.Grid;
import main.model.tower.ShapedTower;
import main.runnable.model.EnemyAdd;
import main.runnable.model.EnemyMove;
import main.runnable.model.MapRepaint;
import main.runnable.model.TowerAttack;
import main.runnable.sound.MP3SoundPlay;
import main.util.AlgorithmUtil;
import main.util.ConversorUtil;
import main.util.FileUtil;
import main.util.coordinate.XYCoordinate;
import main.util.validator.GridValidator;
import main.view.dialog.TowerAction;
import main.view.frame.GameFrame;

/**
 * @author Silvio Vileriño
 * @author Guido Tagliavini
 *
 */

public class GridPanel extends JPanel {

	private static final Integer MARGIN_X = 10;
	private static final Integer MARGIN_Y = 30;
	private static final Integer SUP_BAR = 8;

	private static final Integer SHOOT_SLEEP = 1;

	private static final long serialVersionUID = 1L;

	private GameFrame ancestorContainer;
	private BufferedImage bufferedBackGround;
	private Integer compositeRule;
	private Float compositeValue;
	private Grid gridMap;
	private MapRepaint mapRepaint;
	private Thread thGameStatusUpdate;

	private Vector<ShapedEnemy> enemies;
	private Vector<EnemyMove> emEnemies;
	private Vector<TowerAttack> taTowers;
	private Vector<ShapedTower> towers;
	private Vector<ShootRunnable> shoots;
	private Vector<LaserShoot> laserShoots;
	private BlockingQueue<ShootRunnable> shootsQueue;

	private ThumbPanel thumbPanel;
	private Boolean roundStarted;


	//New thread implementation December 2010
	private ExecutorService towersExecutor;
	private ExecutorService enemiesExecutor;
	private ExecutorService gfxExecutor;
	private ExecutorService sfxExecutor;


	public GridPanel(GameFrame ancestorContainer) {
		super();
		this.setFocusable(true);
		this.ancestorContainer = ancestorContainer;
		setBackground(Color.WHITE);
		compositeRule = new Integer(AlphaComposite.SRC_OVER);
		enemies = new Vector<ShapedEnemy>();
		towers = new Vector<ShapedTower>();
		taTowers = new Vector<TowerAttack>();
		emEnemies = new Vector<EnemyMove>();
		shoots = new Vector<ShootRunnable>();
		laserShoots = new Vector<LaserShoot>();
		shootsQueue = new LinkedBlockingQueue<ShootRunnable>();
		gridMap = new Grid();
		bufferedBackGround = FileUtil
				.readImage(FilePathConsts.backgroundSpritesPath + "/"
						+ GameConsts.PREFERRED_BACKGROUND_NAME);
		TowerInfoListener eventListener = new TowerInfoListener();
		addMouseListener(eventListener);
		addKeyListener(eventListener);
		thumbPanel = null;
		roundStarted = false;
	}

	public void addEnemy(Integer xStart, Integer yStart, Integer xTarget,
			Integer yTarget, EnemyType enemyType, PlayerNumber attacker,
			Integer roundLevel) {
		Vector<XYCoordinate> path = AlgorithmUtil.calculatePath(xStart, yStart,
				xTarget, yTarget, gridMap, enemyType);
		MovementAI ai = new MovementAI(path);
		ShapedEnemy se = new ShapedEnemy(enemyType, xStart, yStart, ai,
				attacker);
		se.increaseLevel(roundLevel);
		enemies.add(se);
	}

	public Vector<TowerAttack> getTaTowers() {
		return taTowers;
	}

	public boolean towerZoneCheck(Integer x, Integer y) {
		boolean zoneCheck = false;
		if (getAncestorContainer().getSessionObject().getGameMode() == GameMode.MULTI_PLAYER) {
			if (getAncestorContainer().getSessionObject().getPlayerNumber() == PlayerNumber.PLAYER_1) {
				if (y < 21) {
					zoneCheck = true;
				}
			} else {
				if (y > 21) {
					zoneCheck = true;
				}
			}
		} else {
			zoneCheck = true;
		}
		return zoneCheck;
	}

	public Vector<TowerDataDTO> retrieveTowerInfoToSend() {
		Vector<TowerDataDTO> towersArray = new Vector<TowerDataDTO>();

		for (ShapedTower st : towers) {
			if (st.getOwnerPlayer() == this.getAncestorContainer()
					.getSessionObject().getPlayerNumber()) {
				towersArray.add(new TowerDataDTO(st.getX(), st.getY(), st
						.getLevel(), st.getTowerType().getTowerType()));
			}
		}
		return towersArray;
	}

	public boolean addTower(Integer x, Integer y, TowerType towerType) {
		boolean addingResult = false;
		if (towerZoneCheck(x, y)) {
			ShapedTower tower = new ShapedTower(towerType, x, y,
					getAncestorContainer().getSessionObject().getPlayerNumber());
			if (getAncestorContainer().getCurrentMoneyRemaining().intValue() >= tower
					.getTowerCost().intValue()) {
				towers.add(tower);
				getAncestorContainer().modifyScore(0, -tower.getTowerCost());
				addingResult = true;
				this.repaint();
			} else {
				JOptionPane.showMessageDialog(getAncestorContainer(),
						"You dont have enough money to buy this tower!");
			}
		} else {
			JOptionPane.showMessageDialog(getAncestorContainer(),
					"You cant put towers here!");
		}
		return addingResult;
	}

	public void cleanGridOnLost() {
		for (ShapedTower towerToRemove : towers) {
			gridMap.setFieldFree(towerToRemove.getY(), towerToRemove.getX(),
					towerToRemove.getSize());
		}
		towers.clear();
		repaint();
		System.out.println("Grid Clean");
	}

	public boolean deleteTower(ShapedTower towerToRemove) {
		boolean result = false;
		if (!isRoundStarted()) {
			if (towerZoneCheck(towerToRemove.getX(), towerToRemove.getY())) {
				if (removeTowerFromList(towerToRemove)) {
					gridMap.setFieldFree(towerToRemove.getY(), towerToRemove
							.getX(), towerToRemove.getSize());
					System.out.println("[ INFO] Tower Removed");
					this.repaint();
					File sound = new File(FilePathConsts.sfxPath
							+ "/remove1.mp3");
					new Thread(new MP3SoundPlay(sound.getAbsolutePath(), false))
							.start();
					result = true;
					getAncestorContainer().modifyScore(
							0,
							(towerToRemove.getTowerCost() + towerToRemove
									.getUpgradeCost()
									* (towerToRemove.getLevel() - 1)) / 2);
				} else {
					System.out.println("[ INFO] Tower Not Removed");
				}
			} else {
				JOptionPane.showMessageDialog(getAncestorContainer(),
						"You can't delete another player towers!");
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Cannot delete towers while round is started.", "PL3",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return result;
	}

	private boolean removeTowerFromList(ShapedTower st) {
		boolean success = false;
		for (int a = 0; a < towers.size(); a++) {
			ShapedTower t = towers.get(a);
			if ((t.getX().intValue() == st.getX().intValue())
					&& (t.getY().intValue() == st.getY().intValue())) {
				towers.remove(a);
				success = true;
			}
		}
		return success;
	}

	public void upgradeTower(ShapedTower towerToUpgrade) {
		if (!isRoundStarted()) {
			if (towerZoneCheck(towerToUpgrade.getX(), towerToUpgrade.getY())) {
				if (getAncestorContainer().getCurrentMoneyRemaining()
						.intValue() >= towerToUpgrade.getUpgradeCost()) {
					towerToUpgrade.upgrade(towerToUpgrade.getLevel() + 1);
					getAncestorContainer().modifyScore(0,
							-towerToUpgrade.getUpgradeCost());
					repaint();
					File sound = new File(FilePathConsts.sfxPath
							+ "/upgrade1.mp3");
					MP3SoundPlay mp3 = new MP3SoundPlay(
							sound.getAbsolutePath(), false);
					new Thread(mp3).start();
					getAncestorContainer().getTowerInfoViewer().towerPicked(
							towerToUpgrade);
				} else {
					JOptionPane
							.showMessageDialog(getAncestorContainer(),
									"You dont have enough money to upgrade this tower!");
				}
			} else {
				JOptionPane.showMessageDialog(getAncestorContainer(),
						"You can't upgrade another player towers!");
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Cannot upgrade towers while round is started.", "PL3",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public void finalizeRound() {
		// this.enemies.clear(); los enemy se limpian desde updateGame.
		ancestorContainer.getJbSendEnemies().setEnabled(true);
		ancestorContainer.getTowerSelector().setVisible(true);
		ancestorContainer.getJbFinalizeRound().setEnabled(false);

		stopThreads();
	}

	public void moveEnemy(Integer index, Integer x, Integer y) {
		enemies.get(index.intValue()).move(x, y);
	}

	public void moveEnemyTo(Integer index, Integer x, Integer y) {
		enemies.get(index).setX(x);
		enemies.get(index).setY(y);
	}

	@Override
	public void paintComponent(Graphics g) {
		if (g != null) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// Loading background image
			g2.drawImage(bufferedBackGround, null, 0, 0);

			// Lineas imaginarias de cuadricula
			if (this.getAncestorContainer().getSessionObject().getGameMode() == GameMode.MULTI_PLAYER) {
				paintMultiPlayerGrid(g2);
			} else {
				paintGrid(g2);
			}

			// Setting transparencies
			/*
			 * compositeValue = new Float(1.0F);// Valor de opacidad entre 0 y 1
			 * g2.setComposite(AlphaComposite.getInstance(
			 * compositeRule.intValue(), compositeValue.floatValue()));
			 * g2.setPaint(Color.WHITE);
			 */

			// Drawing the towers
			paintTowers(g2);

			// Drawing the enemies
			paintEnemies(g2);

			// Drawing shoots
			paintShoots(g2);

		}
	}

	public void shootEnemy(Point2D towerPosition, Point2D enemyPosition,
			Color shootColor, ShootStyle shootStyle) {
		if (getGraphics() != null) {
			if (shootStyle.equals(ShootStyle.LASER)) {
				synchronized (laserShoots) {
					laserShoots.add(new LaserShoot(towerPosition,
							enemyPosition, shootColor));
				}
			} else if (shootStyle.equals(ShootStyle.BALL)) {
				ShootRunnable shoot = new ShootRunnable(new Shoot(towerPosition, enemyPosition, gridMap,
						shootColor));
				shootsQueue.add(shoot);
				//new Thread(shoot).start();
				//New thread implementation December 2010
				gfxExecutor.submit(shoot);
			}
		}
	}

	public void enemyDeadEffect(Point2D enemyPosition) {
		if (getGraphics() != null) {
			Graphics2D g = (Graphics2D) getGraphics();
			//New thread implementation December 2010
			//new Thread(new Explosion(g, enemyPosition)).start();
			gfxExecutor.submit(new Explosion(g, enemyPosition, new ShapedGFX(GFXType.EXPLOSION)));
		}
	}

	public void createExecutors(){
		//New thread implementation December 2010
		if(GameConsts.CACHED_THREAD_EXECUTORS){
			towersExecutor=Executors.newCachedThreadPool();
			enemiesExecutor=Executors.newCachedThreadPool();
			gfxExecutor=Executors.newCachedThreadPool();
			sfxExecutor=Executors.newCachedThreadPool();
		}else{
			towersExecutor=Executors.newFixedThreadPool(GameConsts.MAX_THREADS_PER_FIXED_EXECUTOR);
			enemiesExecutor=Executors.newFixedThreadPool(GameConsts.MAX_THREADS_PER_FIXED_EXECUTOR);
			gfxExecutor=Executors.newFixedThreadPool(GameConsts.MAX_THREADS_PER_FIXED_EXECUTOR);
			sfxExecutor=Executors.newFixedThreadPool(GameConsts.MAX_THREADS_PER_FIXED_EXECUTOR);
		}

		//-----------------------
	}

	public void startRound() {
		setRoundStarted(true);

		for (ShapedTower st : towers) {
			TowerAttack ta = new TowerAttack(st, enemies, this);
			//New thread implementation December 2010
			//new Thread(ta).start();
			towersExecutor.submit(ta);
			taTowers.add(ta);
		}

		// Repainting thread
		mapRepaint = new MapRepaint(this);
		new Thread(mapRepaint).start();

		thGameStatusUpdate = new Thread(ancestorContainer.getGameStatusUpdate());
		thGameStatusUpdate.start();
	}

	public void shutdownExecutors(){
		//New thread implementation December 2010
		towersExecutor.shutdown();
		enemiesExecutor.shutdown();
		gfxExecutor.shutdown();
		sfxExecutor.shutdown();
	}

	public void stopThreads() {
		setRoundStarted(false);

		forceEnemyAddition();

		for (EnemyMove em : emEnemies) {
			em.setMoving(false);
		}

		for (TowerAttack ta : taTowers) {
			ta.setAttacking(false);
		}

		emEnemies.clear();
		enemies.clear();
		shoots.clear();
		laserShoots.clear();

		mapRepaint.setPainting(false);

		repaint();
	}

	public void updateTowers(GameDataDTO gameDataDTO) {
		PlayerNumber pn;
		if (ancestorContainer.getSessionObject().getPlayerNumber() == PlayerNumber.PLAYER_1) {
			pn = PlayerNumber.PLAYER_2;
		} else {
			pn = PlayerNumber.PLAYER_1;
		}

		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		int index = 0;

		for (ShapedTower st : towers) {
			if (st.getOwnerPlayer().equals(pn)) {
				toRemove.add(index);
			} else {
				++index;
			}
		}

		for (Integer i : toRemove) {
			towers.remove(i.intValue());
		}

		for (int i = 0; i < gameDataDTO.getTowerDataDTO().size(); i++) {
			ShapedTower st = new ShapedTower(TowerType.values()[gameDataDTO
					.getTowerDataDTO().get(i).getTowerType()], gameDataDTO
					.getTowerDataDTO().get(i).getX(), gameDataDTO
					.getTowerDataDTO().get(i).getY(), pn);

			st.upgrade(gameDataDTO.getTowerDataDTO().get(i).getLevel());

			towers.add(st);
		}
	}

	public void setEnemies(Vector<ShapedEnemy> enemies) {
		this.enemies = enemies;
	}

	public void setTowers(Vector<ShapedTower> towers) {
		this.towers = towers;
	}

	/**
	 * @return the ancestorContainer
	 */
	public GameFrame getAncestorContainer() {
		return ancestorContainer;
	}

	public Vector<ShapedEnemy> getEnemies() {
		return enemies;
	}

	/**
	 * @return the gridMap
	 */
	public Grid getGridMap() {
		return gridMap;
	}

	public Vector<ShapedTower> getTowers() {
		return towers;
	}

	/**
	 * override for keylistening
	 */
	public boolean isFocusable() {
		return true;
	}

	/**
	 * @return the towerBeingSelectedRef
	 */
	public ThumbPanel getThumbPanel() {
		return thumbPanel;
	}

	/**
	 * @param thumbPanel
	 *            the towerBeingSelectedRef to set
	 */
	public void setThumbPanel(ThumbPanel thumbPanel) {
		this.thumbPanel = thumbPanel;
	}

	/**
	 * @param gridMap
	 *            the gridMap to set
	 */
	public void setGridMap(Grid gridMap) {
		this.gridMap = gridMap;
	}

	/**
	 * @return the emEnemies
	 */
	public Vector<EnemyMove> getEmEnemies() {
		return emEnemies;
	}

	/**
	 * @return the roundStarted
	 */
	public Boolean isRoundStarted() {
		return roundStarted;
	}

	/**
	 * @param roundStarted
	 *            the roundStarted to set
	 */
	public void setRoundStarted(Boolean roundStarted) {
		this.roundStarted = roundStarted;
	}

	private void paintEnemies(Graphics2D g2) {
		for (ShapedEnemy enemy : enemies) {
			if (enemy.getRemainingLife() > 0 && enemy.isAdded()) {// si no
				// tiene
				// vida no
				// lo dibujo
				// y si todavía no lo agregué tampoco
				g2.drawImage(enemy.getRotatedImage(), null, (int) enemy
						.getPixPosition().getX(), (int) enemy.getPixPosition()
						.getY());
				enemy.setNextFrame();

				compositeValue = new Float(0.5F);// Valor de opacidad entre 0
				// y 1
				g2.setComposite(AlphaComposite.getInstance(compositeRule
						.intValue(), compositeValue.floatValue()));

				g2.setColor(Color.GREEN);
				g2.fill(enemy.getLifeShape());

				g2.setColor(Color.BLACK);

				compositeValue = new Float(1.0F);// Valor de opacidad entre 0
				// y 1
				g2.setComposite(AlphaComposite.getInstance(compositeRule
						.intValue(), compositeValue.floatValue()));
			}
		}
	}

	private void paintShoots(Graphics2D g2) {

		for (ShootRunnable s : shoots) {
			if (!s.isFinalized()) {
				if (s.getShootObj().getPosition() != null) {
					g2.drawImage(s.getShootObj().getRotatedRenderedBullet(null, s.getShootObj().getClr(),
							45), null, (int) s.getShootObj().getPosition().getX(), (int) s
							.getShootObj().getPosition().getY());
				}
			}
		}

		dequeueShoots();

		synchronized (laserShoots) {
			for (LaserShoot s : laserShoots) {
				g2.setPaint(s.getClr());
				g2.setStroke(new BasicStroke(2));
				g2.drawLine((int) s.getOrigin().getX(), (int) s.getOrigin()
						.getY(), (int) s.getTarget().getX(), (int) s
						.getTarget().getY());
				g2.setStroke(new BasicStroke());
			}
			laserShoots.clear();
		}

		g2.setPaint(Color.BLACK);
	}

	private void paintMultiPlayerGrid(Graphics2D g2) {
		this.paintGrid(g2);
		compositeValue = new Float(0.2F);// Valor de opacidad entre 0 y 1
		g2.setComposite(AlphaComposite.getInstance(compositeRule.intValue(),
				compositeValue.floatValue()));

		g2.setColor(Color.GREEN);
		g2.fillRect(0, 0, 300, 258);

		g2.setColor(Color.RED);
		g2.fillRect(0, 258, 300, 516);

		compositeValue = new Float(1.0F);// Valor de opacidad entre 0 y 1
		g2.setComposite(AlphaComposite.getInstance(compositeRule.intValue(),
				compositeValue.floatValue()));
		g2.setPaint(Color.BLACK);

	}

	private void paintGrid(Graphics2D g2) {
		compositeValue = new Float(0.5F);// Valor de opacidad entre 0 y 1
		g2.setComposite(AlphaComposite.getInstance(compositeRule.intValue(),
				compositeValue.floatValue()));
		g2.setPaint(Color.BLACK);
		for (int a = 0; a <= 24; a++) {
			g2.drawLine(a * 12, 0, a * 12, 516);
		}
		for (int a = 0; a <= 42; a++) {
			g2.drawLine(0, a * 12, 300, a * 12);
		}
		compositeValue = new Float(1.0F);// Valor de opacidad entre 0 y 1
		g2.setComposite(AlphaComposite.getInstance(compositeRule.intValue(),
				compositeValue.floatValue()));
	}

	private void paintTowers(Graphics2D g2) {

		for (ShapedTower st : towers) {
			compositeValue = new Float(0.25F);// Valor de opacidad entre 0 y 1
			if (st.isSelected()) {
				g2.setComposite(AlphaComposite.getInstance(compositeRule
						.intValue(), compositeValue.floatValue()));
				g2.setColor(st.getTowerClr());
				g2.fill(st.getRadioShape());
			} else {
				if (GameConsts.TOWERS_PAINTED_BASE) {
					g2.setColor(st.getBaseClr());
					g2.fill(st.getBaseShape());
				}
			}
		}

		for (ShapedTower st : towers) {
			compositeValue = new Float(1.0F);// Valor de opacidad entre 0 y 1
			g2.setComposite(AlphaComposite.getInstance(
					compositeRule.intValue(), compositeValue.floatValue()));

			g2.setColor(Color.BLACK);

			g2.drawImage(st.getBaseImage(), null, (int) st.getPixPosition()
					.getX(), (int) st.getPixPosition().getY());

			g2.drawImage(st.getRotatedImage(), null, (int) st.getPixPosition()
					.getX(), (int) st.getPixPosition().getY());
		}
	}

	private void dequeueShoots() {
		while (shootsQueue.size() > 0) {
			shoots.add(shootsQueue.poll());
		}
	}

	private void forceEnemyAddition() {
		EnemyAdd ea = ancestorContainer.getEnemyAdd();
		ea.setAddingDelay(0);

		while (emEnemies.size() != enemies.size()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	private ShapedTower getShapedTowerAtXY(Integer x, Integer y) {
		for (ShapedTower t : this.towers) {
			for (int i = 0; i < t.getSize(); i++) {
				for (int j = 0; j < t.getSize(); j++) {
					if (t.getX() + j == x && t.getY() + i == y) {
						return t;
					}
				}
			}
		}

		return null;
	}

	private void deselectTowers() {
		for (ShapedTower t : this.towers) {
			t.setSelected(false);
		}
	}

	private class TowerInfoListener extends MouseAdapter implements KeyListener {

		private ShapedTower selectedTower;

		public TowerInfoListener() {
			super();
		}

		public void mousePressed(MouseEvent click) {
			if ((getThumbPanel() != null)
					&& (click.getButton() == MouseEvent.BUTTON1)) {
				Integer xLogic = ConversorUtil.pixelsToLogic(click
						.getXOnScreen()
						- getAncestorContainer().getX() - MARGIN_X);
				Integer yLogic = ConversorUtil.pixelsToLogic(click
						.getYOnScreen()
						- getAncestorContainer().getY() - SUP_BAR - MARGIN_Y);

				if (GridValidator.validateCoordinate(xLogic, yLogic, thumbPanel
						.getSelectedTowerSize(), gridMap)) {
					if (addTower(xLogic, yLogic, thumbPanel
							.getSelectedTowerType())) {
						System.out.println("[ INFO] Tower Added (" + xLogic
								+ ", " + yLogic + ") Size: "
								+ thumbPanel.getSelectedTowerSize());

						gridMap.setFieldOccupied(yLogic, xLogic, thumbPanel
								.getSelectedTowerSize(), thumbPanel
								.getSelectedTowerType());

						File sound = new File(FilePathConsts.sfxPath
								+ "/drop1.mp3");
						new Thread(new MP3SoundPlay(sound.getAbsolutePath(),
								false)).start();
					}
				}

				if (!click.isControlDown()) {
					setThumbPanel(null);
					getAncestorContainer().setCursor(
							new Cursor(Cursor.DEFAULT_CURSOR));
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent click) {
			Integer logicX = new Integer(ConversorUtil
					.pixelsToLogic((int) click.getPoint().getX()));
			Integer logicY = new Integer(ConversorUtil
					.pixelsToLogic((int) click.getPoint().getY()));

			deselectTowers();

			repaint();

			selectedTower = getShapedTowerAtXY(logicX, logicY);

			if (selectedTower != null) {
				selectedTower.setSelected(true);

				getAncestorContainer().getTowerInfoViewer().towerPicked(
						selectedTower);

				if (click.getClickCount() == 2) {
					new TowerAction(GridPanel.this.getAncestorContainer(),
							GridPanel.this, selectedTower);
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
			requestFocus();
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				if (selectedTower != null) {
					deleteTower(selectedTower);
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_U) {
				if (selectedTower != null) {
					upgradeTower(selectedTower);
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				setThumbPanel(null);
				getAncestorContainer().setCursor(
						new Cursor(Cursor.DEFAULT_CURSOR));
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

		}
	}

	public ExecutorService getEnemiesExecutor() {
		return enemiesExecutor;
	}
}
