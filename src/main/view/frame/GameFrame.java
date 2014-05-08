package main.view.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.UIManager.LookAndFeelInfo;

import main.consts.FilePathConsts;
import main.consts.GameConsts;
import main.consts.LookAndFeelConsts;
import main.dto.GameDataDTO;
import main.dto.RoundResultsDTO;
import main.enums.BonusType;
import main.enums.ConnectionAction;
import main.enums.EnemyType;
import main.enums.GameMode;
import main.enums.PlayerNumber;
import main.event.ComponentSFX;
import main.event.ComponentGFX;
import main.model.enemy.ShapedEnemy;
import main.runnable.model.GameStatusUpdate;
import main.runnable.model.EnemyAdd;
import main.runnable.socket.ConnectionInput;
import main.runnable.socket.ConnectionOutput;
import main.runnable.sound.MP3SoundPlay;
import main.runnable.sound.MidiWavSoundPlay;
import main.runnable.model.TowerAttack;
import main.session.SessionObject;
import main.sound.RandomSoundSelector;
import main.util.ConversorUtil;
import main.util.FileUtil;
import main.util.MathUtil;
import main.util.UserScreenUtil;
import main.util.validator.ChatValidator;
import main.view.dialog.BonusUsage;
import main.view.dialog.FinishedRoundDialog;
import main.view.panel.EnemyInfo;
import main.view.panel.GridPanel;
import main.view.panel.TexturedPanel;
import main.view.panel.TowerInfo;
import main.view.panel.TowerPicker;

/**
 * @author Silvio Vileriño
 * @author Guido Tagliavini
 *
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Integer DEFAULT_HEIGHT = 570;
	private static final Integer DEFAULT_WIDTH = 740;

	private Integer currentBonus = 0;
	private Integer currentLifesRemaining = GameConsts.STARTER_LIFES;

	private Integer currentMoneyRemaining = GameConsts.STARTER_MONEY;;
	private Integer currentScore = 0;
	private EnemyInfo actualEnemyInfoViewer;
	private EnemyInfo nextEnemyInfoViewer;

	private GridPanel gridPanel;

	private JButton jbSendChat;
	private JButton jbFinalizeRound;
	private JButton jbSendEnemies;
	private JButton jbUseBonus;

	private JPanel jpChat;
	private JPanel jpControl;
	private JPanel jpInfo;
	private JPanel jpMain;

	private JTextArea jtaChatRecord;

	private JScrollPane jspChatRecord;

	private JTextField jtfChat;
	private JTextField jtfBonus;
	private JTextField jtfLifes;
	private JTextField jtfMoney;
	private JTextField jtfPoints;
	private JTextField jtfLevel;

	private TowerInfo towerInfoViewer;

	private TowerPicker towerSelector;

	private GameStatusUpdate gameStatusUpdate;

	private EnemyAdd enemyAdd;

	private JFrame previousFrame;

	private SessionObject sessionObject;

	private ConnectionInput connectionInput;

	private ConnectionOutput connectionOutput;

	private Object flag;
	private Long roundTime;
	private MP3SoundPlay mp3Runnable;
	private MidiWavSoundPlay midiWavRunnable;

	public GameFrame(SessionObject sessionObject, JFrame previousFrame,
			MP3SoundPlay mp3Runnable, MidiWavSoundPlay midiwavRunnable) {
		super();
		this.setVisible(false);

		setLookAndFeel();

		this.sessionObject = sessionObject;
		this.flag = new Object();
		this.roundTime = 0L;
		this.previousFrame = previousFrame;

		// Window properties
		setTitle("Operation PL3");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		this.setBounds((UserScreenUtil.getWidth() / 2)
				- (GameFrame.DEFAULT_WIDTH / 2),
				(UserScreenUtil.getHeight() / 2)
						- (GameFrame.DEFAULT_HEIGHT / 2),
				GameFrame.DEFAULT_WIDTH, GameFrame.DEFAULT_HEIGHT);

		this.mp3Runnable = mp3Runnable;
		this.midiWavRunnable = midiwavRunnable;

		// Panels, objects
		addSecondaryPanels();
		addControls();
		addGamePanels();

		// Setting content pane
		getContentPane().add(jpMain);
		// this.initBackgroundSound();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if (JOptionPane.showConfirmDialog(null,
						"Desea cancelar la partida?",
						"Proyecto Laboratorio III", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					setVisible(false);
					if (jbFinalizeRound.isEnabled()) {
						jbFinalizeRound.doClick();
					}
					stopSounds();
					GameFrame.this.previousFrame.setVisible(true);
				}
			}
		});
		if (sessionObject.getGameMode() == GameMode.MULTI_PLAYER)
			setTitle("Player " + sessionObject.getPlayerNumber());
	}

	private void stopSounds() {
		if (midiWavRunnable != null) {
			midiWavRunnable.stopSoundPlaying();
			midiWavRunnable = null;
		}
		if (mp3Runnable != null) {
			mp3Runnable.close();
			mp3Runnable = null;
		}
	}

	private void startRoundSound() {
		RandomSoundSelector rss = new RandomSoundSelector();
		File sound = rss.getRandomRoundSound();
		if (sound.getAbsolutePath().endsWith(".mp3")) {
			// mp3Runnable=new MP3SoundPlay(sound.getAbsolutePath(), false);
			mp3Runnable = new MP3SoundPlay(rss, true);
			new Thread(mp3Runnable).start();
		} else {
			try {
				midiWavRunnable = new MidiWavSoundPlay(ConversorUtil
						.fileToURL(sound), true);
				new Thread(midiWavRunnable).start();
			} catch (MalformedURLException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	public void changeSounds() {

		new Thread(new Runnable() {

			public void run() {
				stopSounds();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace(System.err);
				}
				if (JOptionPane
						.showConfirmDialog(null,
								"Would you like background sound?",
								"PL3: The sound never dies.",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					startRoundSound();
				}
			}

		}).start();
	}

	public void applyBonus(BonusType bType) {
		System.out.println("[ INFO] " + bType + " Bonus Applied!");
		if (bType.equals(BonusType.ENEMY_SLOWDOWN)) {
			new Thread(new Runnable() {

				public void run() {
					/*
					 * for(EnemyMove em : gridPanel.getEmEnemies()){
					 * em.modifySpeed(GameConsts.BONUS_ENEMY_SPEED_TARGET); }
					 * try { Thread.sleep(GameConsts.BONUS_ENEMY_SPEED_DELAY); }
					 * catch (InterruptedException e) {
					 * e.printStackTrace(System.err); } for(EnemyMove em :
					 * gridPanel.getEmEnemies()){
					 * em.modifySpeed(-GameConsts.BONUS_ENEMY_SPEED_TARGET); }
					 */

					for (ShapedEnemy se : gridPanel.getEnemies()) {
						se.modifySpeed(GameConsts.BONUS_ENEMY_SPEED_TARGET);
					}
					try {
						Thread.sleep(GameConsts.BONUS_ENEMY_SPEED_DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace(System.err);
					}
					for (ShapedEnemy se : gridPanel.getEnemies()) {
						se.modifySpeed(-GameConsts.BONUS_ENEMY_SPEED_TARGET);
					}

				}

			}).start();
		}

		if (bType.equals(BonusType.LIFE)) {
			reduceLife(GameConsts.LIFE_BONUS_AMOUNT);
		}

		if (bType.equals(BonusType.MONEY)) {
			modifyScore(0, GameConsts.MONEY_BONUS_AMOUNT);
		}

		if (bType.equals(BonusType.SCORE)) {
			modifyScore(GameConsts.SCORE_BONUS_AMOUNT, 0);
		}

		if (bType.equals(BonusType.TOWER_ULTRAQUICK)) {
			new Thread(new Runnable() {

				public void run() {
					for (TowerAttack ta : gridPanel.getTaTowers()) {
						if (ta
								.modifySpeed(-GameConsts.BONUS_TOWER_SPEED_TARGET)) {
							ta.setBonusAffected(true);
						}
					}
					try {
						Thread.sleep(GameConsts.BONUS_TOWER_SPEED_DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace(System.err);
					}
					for (TowerAttack ta : gridPanel.getTaTowers()) {
						if (ta.isBonusAffected()) {
							ta.modifySpeed(GameConsts.BONUS_TOWER_SPEED_TARGET);
							ta.setBonusAffected(false);
						}
					}
				}

			}).start();
		}
	}

	public void reduceLife(Integer lifeLost) {
		currentLifesRemaining -= lifeLost;
		jtfLifes.setText(currentLifesRemaining.toString());
	}

	public void addBonus(Integer bonusCount) {
		currentBonus += bonusCount;
		jtfBonus.setText(currentBonus.toString());
		jbUseBonus.setEnabled(true);
	}

	public void removeBonus(Integer bonusCount) {
		currentBonus -= bonusCount;
		jtfBonus.setText(currentBonus.toString());
		if (currentBonus <= 0) {
			jbUseBonus.setEnabled(false);
			if (currentBonus < 0)
				currentBonus = 0;
		}
	}

	public void modifyScore(int sumPoints, int sumMoney) {
		currentScore += sumPoints;
		currentMoneyRemaining += sumMoney;
		jtfPoints.setText(currentScore.toString());
		jtfMoney.setText(currentMoneyRemaining.toString());
	}

	private void finishLostRound(RoundResultsDTO resultsInfo) {
		new FinishedRoundDialog(this, "You Lost! Closing this dialog will go back to previous menu", resultsInfo);
		//resetRound();  deprecated
		//back to prevFrame
		setVisible(false);
		GameFrame.this.previousFrame.setVisible(true);
	}

	private void finishWonRound(RoundResultsDTO resultsInfo) {
		new FinishedRoundDialog(this, "You Won!", resultsInfo);
	}

	/**
	 *@deprecated
	 */
	public void resetRound() {
		sessionObject.resetRounds();
		resetInfoValues();
		gridPanel.cleanGridOnLost();
	}

	public void finishAndDecideRoundResult(RoundResultsDTO resultsInfo) {
		resultsInfo
				.setRoundTime((System.currentTimeMillis() - roundTime) / 1000);
		if (getCurrentLifesRemaining() > 0) {
			finishWonRound(resultsInfo);
		} else {
			finishLostRound(resultsInfo);
		}
	}

	public void sendMessage() {
		String message = jtfChat.getText();
		jtfChat.setText("");
		writeMessage("I", message);
		connectionOutput.setData(message);
		connectionOutput.setConnectionAction(ConnectionAction.SEND_MESSAGE);
		new Thread(connectionOutput).start();
	}

	public void writeMessage(String author, String message) {
		jtaChatRecord.append(author + " said:\n\t" + message + "\n");
	}

	public void startConnectionInput() {
		connectionInput.setFlag(flag);
		new Thread(connectionInput).start();
	}

	public void updateGameData(GameDataDTO gameDataDTO) {
		// gridPanel.getGridMap().mergeGrid(gameDataDTO.getGridMap(),
		// sessionObject.getPlayerNumber());
		if (gameDataDTO.getTowerDataDTO() != null) {
			gridPanel.getGridMap().updateGrid(gameDataDTO,
					sessionObject.getPlayerNumber());
			gridPanel.updateTowers(gameDataDTO);
		}

		if (sessionObject.getPlayerNumber() == PlayerNumber.PLAYER_2) {
			sessionObject.setActualEnemy(EnemyType.values()[gameDataDTO
					.getActualEnemy()]);
			sessionObject.setNextEnemy(EnemyType.values()[gameDataDTO
					.getNextEnemy()]);
		}

		synchronized (flag) {
			flag.notifyAll();
		}
	}

	public EnemyAdd getEnemyAdd() {
		return enemyAdd;
	}

	/**
	 * @return the currentBonus
	 */
	public Integer getCurrentBonus() {
		return currentBonus;
	}

	/**
	 * @return the currentLifesRemaining
	 */
	public Integer getCurrentLifesRemaining() {
		return currentLifesRemaining;
	}

	/**
	 * @return the currentMoneyRemaining
	 */
	public Integer getCurrentMoneyRemaining() {
		return currentMoneyRemaining;
	}

	/**
	 * @return the currentScore
	 */
	public Integer getCurrentScore() {
		return currentScore;
	}

	/**
	 * @return the enemyInfoViewer
	 */
	public EnemyInfo getActualEnemyInfoViewer() {
		return actualEnemyInfoViewer;
	}

	/**
	 * @return the gridPanel
	 */
	public GridPanel getGridPanel() {
		return gridPanel;
	}

	/**
	 * @return the jbFinalizeRound
	 */
	public JButton getJbFinalizeRound() {
		return jbFinalizeRound;
	}

	/**
	 * @return the jbSendEnemies
	 */
	public JButton getJbSendEnemies() {
		return jbSendEnemies;
	}

	/**
	 * @return the towerInfoViewer
	 */
	public TowerInfo getTowerInfoViewer() {
		return towerInfoViewer;
	}

	/**
	 * @return the towerSelector
	 */
	public TowerPicker getTowerSelector() {
		return towerSelector;
	}

	/**
	 * @return the gameStatusUpdate
	 */
	public GameStatusUpdate getGameStatusUpdate() {
		return gameStatusUpdate;
	}

	/**
	 * @return the nextEnemyInfoViewer
	 */
	public EnemyInfo getNextEnemyInfoViewer() {
		return nextEnemyInfoViewer;
	}

	/**
	 * @param currentBonus
	 *            the currentBonus to set
	 */
	public void setCurrentBonus(Integer currentBonus) {
		this.currentBonus = currentBonus;
	}

	/**
	 * @param currentLifesRemaining
	 *            the currentLifesRemaining to set
	 */
	public void setCurrentLifesRemaining(Integer currentLifesRemaining) {
		this.currentLifesRemaining = currentLifesRemaining;
	}

	/**
	 * @param currentMoneyRemaining
	 *            the currentMoneyRemaining to set
	 */
	public void setCurrentMoneyRemaining(Integer currentMoneyRemaining) {
		this.currentMoneyRemaining = currentMoneyRemaining;
	}

	/**
	 * @param currentScore
	 *            the currentScore to set
	 */
	public void setCurrentScore(Integer currentScore) {
		this.currentScore = currentScore;
	}

	/**
	 * @param towerSelector
	 *            the towerSelector to set
	 */
	public void setTowerSelector(TowerPicker towerSelector) {
		this.towerSelector = towerSelector;
	}

	public ConnectionInput getConnectionInput() {
		return connectionInput;
	}

	public void setConnectionInput(ConnectionInput connectionInput) {
		this.connectionInput = connectionInput;
	}

	public ConnectionOutput getConnectionOutput() {
		return connectionOutput;
	}

	public void setConnectionOutput(ConnectionOutput connectionOutput) {
		this.connectionOutput = connectionOutput;
	}

	public SessionObject getSessionObject() {
		return sessionObject;
	}

	public void setSessionObject(SessionObject sessionObject) {
		this.sessionObject = sessionObject;
	}

	private void addControls() {
		jbSendEnemies = createButton("Start Round", jpControl,
				new SendEnemyAction(), new Rectangle(15, 415, 190, 40));
		jbFinalizeRound = createButton("Finalize Round", jpControl,
				new FinalizeRoundAction(), new Rectangle(210, 415, 190, 40));
		jbFinalizeRound.setEnabled(false);

		jbUseBonus = createButton("Use Bonus", jpControl, new UseBonusAction(),
				new Rectangle(210, 465, 190, 40));
		jbUseBonus.setEnabled(false);
	}

	private void addGamePanels() {

		gridPanel = new GridPanel(this);
		gridPanel.setBounds(10, 10, 300, 516);
		jpMain.add(gridPanel);
		towerInfoViewer = new TowerInfo(new Point2D.Double(15, 15));
		towerInfoViewer.setOpaque(false);
		towerSelector = new TowerPicker(new Point2D.Double(215, 115),
				gridPanel, towerInfoViewer);
		towerSelector.setOpaque(false);
		towerSelector.setInfoShower(towerInfoViewer);

		actualEnemyInfoViewer = new EnemyInfo(new Point2D.Double(15, 115),
				"         Actual; ");
		actualEnemyInfoViewer.setOpaque(false);
		nextEnemyInfoViewer = new EnemyInfo(new Point2D.Double(15, 185),
				"         Siguiente; ");
		nextEnemyInfoViewer.setOpaque(false);

		jpControl.add(towerSelector);
		jpControl.add(towerInfoViewer);
		jpControl.add(actualEnemyInfoViewer);
		jpControl.add(nextEnemyInfoViewer);
	}

	private void addInfoComponents() {
		jpInfo.add(new JLabel("Puntos:"));
		jtfPoints = new JTextField(GameConsts.STARTER_POINTS.toString());
		jtfPoints.setEditable(false);
		jpInfo.add(jtfPoints);
		jpInfo.add(new JLabel("Vidas:"));
		jtfLifes = new JTextField(GameConsts.STARTER_LIFES.toString());
		jtfLifes.setEditable(false);
		jpInfo.add(jtfLifes);
		jpInfo.add(new JLabel("Dinero:"));
		jtfMoney = new JTextField(GameConsts.STARTER_MONEY.toString());
		jtfMoney.setEditable(false);
		jpInfo.add(jtfMoney);
		jpInfo.add(new JLabel("Bonus:"));
		jtfBonus = new JTextField(GameConsts.STARTER_BONUS.toString());
		jtfBonus.setEditable(false);
		jpInfo.add(jtfBonus);
		jpInfo.add(new JLabel("Current Level:"));
		jtfLevel = new JTextField("1");
		jtfLevel.setEditable(false);
		jpInfo.add(jtfLevel);
	}

	private void resetInfoValues() {
		currentScore = GameConsts.STARTER_POINTS;
		currentLifesRemaining = GameConsts.STARTER_LIFES;
		currentMoneyRemaining = GameConsts.STARTER_MONEY;
		currentBonus = GameConsts.STARTER_BONUS;
		jtfPoints.setText(GameConsts.STARTER_POINTS.toString());
		jtfLifes.setText(GameConsts.STARTER_LIFES.toString());
		jtfMoney.setText(GameConsts.STARTER_MONEY.toString());
		jtfBonus.setText(GameConsts.STARTER_BONUS.toString());
		jtfLevel.setText(sessionObject.getRoundNumber().toString());
	}

	private void addSecondaryPanels() {
		jpMain = new TexturedPanel(FileUtil.readImage(FilePathConsts.menuImagesPath+ "/gameBack.jpg"));
		jpMain.setLayout(null);
		jpMain.setBackground(Color.DARK_GRAY);
		jpMain.setBounds(0, 0, GameFrame.DEFAULT_WIDTH,
				GameFrame.DEFAULT_HEIGHT);

		jpInfo = new JPanel();
		jpInfo.setOpaque(false);
		jpInfo.setBounds(215, 15, 185, 90);
		jpInfo.setBackground(Color.LIGHT_GRAY);
		jpInfo.setLayout(new GridLayout(0, 2));
		addInfoComponents();

		jpControl = new JPanel();
		jpControl.setOpaque(false);
		jpControl.setBounds(310, 10, GameFrame.DEFAULT_WIDTH - 25 - 300, 516);
		jpControl.setBackground(Color.GRAY);
		jpControl.setLayout(null);

		if (sessionObject.getGameMode() == GameMode.MULTI_PLAYER) {
			jpChat = new JPanel();
			jpChat.setOpaque(false);
			jpChat.setBounds(15, 260, jpControl.getWidth() - 30, 130);
			jpChat.setLayout(new BorderLayout());
			createChatInfoComponents();
			jpControl.add(jpChat);
		}

		jpControl.add(jpInfo);
		jpMain.add(jpControl);

	}

	private void setLookAndFeel() {
		LookAndFeelInfo[] lookAndFeelsList = UIManager
				.getInstalledLookAndFeels();
		try {
			UIManager
					.setLookAndFeel(lookAndFeelsList[LookAndFeelConsts.GAMEFRAME_LOOKNFEEL]
							.getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JButton createButton(String label, JPanel container,
			ActionListener evt, Rectangle r) {
		JButton btn = new JButton(label);
		btn.setBounds(r);
		btn.addActionListener(evt);
		btn.addMouseListener(new ComponentGFX(btn));
		btn.addMouseListener(new ComponentSFX());
		container.add(btn);
		return btn;
	}

	private void createChatInfoComponents() {
		jtaChatRecord = new JTextArea(5, 22);
		jtaChatRecord.setEditable(false);
		jtaChatRecord.setBackground(new Color(238, 233, 233));
		jtaChatRecord.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 12));

		jspChatRecord = new JScrollPane(jtaChatRecord);
		jspChatRecord.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						e.getAdjustable().setValue(
								e.getAdjustable().getMaximum());
					}
				});
		jpChat.add(jspChatRecord, BorderLayout.NORTH);

		JPanel jpInternalChat = new JPanel();
		jpInternalChat.setLayout(new GridLayout(0, 2));
		jtfChat = new JTextField();
		jtfChat.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					jbSendChat.doClick();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {}

		});
		jtfChat.setPreferredSize(new Dimension(jpControl.getWidth() / 2, 38));
		jpInternalChat.add(jtfChat);

		jbSendChat = createButton("Send Message", jpChat,
				new ChatButtonAction(), new Rectangle(0, 0, jpControl
						.getWidth() / 2, 38));
		jpInternalChat.add(jbSendChat);
		jpChat.add(jpInternalChat, BorderLayout.SOUTH);
	}

	private void setButtonsStatus() {
		jbSendEnemies.setEnabled(false);
		towerSelector.setVisible(false);
		jbFinalizeRound.setEnabled(true);
	}

	private class ChatButtonAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (ChatValidator.isValid(jtfChat.getText())) {
				sendMessage();
			}
		}
	}

	private class FinalizeRoundAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("[ INFO] Killing Current Round");
			//gridPanel.shutdownExecutors();
			gridPanel.finalizeRound();
		}
	}

	private class UseBonusAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			new BonusUsage(GameFrame.this);
		}
	}

	private class SendEnemyAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			sessionObject.newRound();

			if (sessionObject.getGameMode() == GameMode.MULTI_PLAYER) {

				System.out.println("[PEER_1/2] Sending ready");

				connectionOutput
						.setConnectionAction(ConnectionAction.SEND_READY);
				new Thread(connectionOutput).start();

				System.out.println("[PEER_1/2] Ready sent");

				synchronized (flag) {
					if (!connectionInput.isReady()) {
						try {
							System.out.println("[PEER_1/2] Waiting for PEER_1");
							flag.wait();
						} catch (InterruptedException e1) {
							e1.printStackTrace(System.err);
						}
					}

					connectionInput.setReady(false);

					GameDataDTO gameDataDTO = new GameDataDTO();
					// gameDataDTO.setGridMap(gridPanel.getGridMap());
					gameDataDTO.setTowerDataDTO(gridPanel
							.retrieveTowerInfoToSend());

					// if(sessionObject.getPlayerNumber() ==
					// PlayerNumber.PLAYER_1){
					gameDataDTO.setActualEnemy(sessionObject.getActualEnemy()
							.getEnemyType());
					gameDataDTO.setNextEnemy(sessionObject.getNextEnemy()
							.getEnemyType());
					// }else{
					// gameDataDTO.setActualEnemy(null);
					// gameDataDTO.setNextEnemy(null);
					// }

					connectionOutput
							.setConnectionAction(ConnectionAction.SEND_GAME_DATA);
					connectionOutput.setData(gameDataDTO);

					System.out.println("[PEER_1/2] Sending data");

					new Thread(connectionOutput).start();

					try {
						flag.wait(); // Espera a que llegue la información y
										// se upgradee todo
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					System.out.println("[PEER_1/2] Data received");
				}
			}

			setButtonsStatus();

			gameStatusUpdate = new GameStatusUpdate(GameFrame.this);
			if (sessionObject.getGameMode() == GameMode.MULTI_PLAYER) {
				for (int i = 0; i < GameConsts.TOTAL_ENEMIES_PER_ROUND
						.intValue(); i++) {
					gridPanel.addEnemy(MathUtil.random(10, 14), 42, 12, 0,
							sessionObject.getActualEnemy(),
							PlayerNumber.PLAYER_2, sessionObject
									.getRoundNumber());
					gridPanel.addEnemy(MathUtil.random(10, 14), 0, 12, 42,
							sessionObject.getActualEnemy(),
							PlayerNumber.PLAYER_1, sessionObject
									.getRoundNumber());
				}
			} else {
				for (int i = 0; i < GameConsts.TOTAL_ENEMIES_PER_ROUND
						.intValue(); i++) {
					gridPanel.addEnemy(MathUtil.random(10, 14), 42, 12, 0,
							sessionObject.getActualEnemy(),
							PlayerNumber.PLAYER_1, sessionObject
									.getRoundNumber());
				}
			}

			gridPanel.createExecutors();
			enemyAdd = new EnemyAdd(gridPanel, MathUtil.random(
					GameConsts.ENEMY_ADDING_DELAY_BASE,
					GameConsts.ENEMY_ADDING_DELAY_TOP));
			new Thread(enemyAdd).start();

			gridPanel.startRound();

			System.out.println("[ INFO] Round Started");

			ShapedEnemy dummyEnemyActual = new ShapedEnemy(sessionObject
					.getActualEnemy(), 0, 0, null, PlayerNumber.PLAYER_1);
			ShapedEnemy dummyEnemyNext = new ShapedEnemy(sessionObject
					.getNextEnemy(), 0, 0, null, PlayerNumber.PLAYER_1);

			dummyEnemyActual.increaseLevel(sessionObject.getRoundNumber());
			getActualEnemyInfoViewer().enemySelected(dummyEnemyActual);

			dummyEnemyNext.increaseLevel(sessionObject.getRoundNumber() + 1);
			getNextEnemyInfoViewer().enemySelected(dummyEnemyNext);

			roundTime = System.currentTimeMillis();
			jtfLevel.setText(sessionObject.getRoundNumber().toString());
		}
	}

}
