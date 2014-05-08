package main.view.frame;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import main.consts.ConnectionConsts;
import main.consts.FilePathConsts;
import main.consts.LookAndFeelConsts;
import main.dto.HostNodeDTO;
import main.enums.GameMode;
import main.enums.PlayerNumber;

import main.event.ComponentSFX;
import main.event.ComponentGFX;
import main.event.WindowSwitchEvent;
import main.runnable.socket.ConnectionInput;
import main.runnable.socket.ConnectionOutput;
import main.runnable.socket.HostingRequest;
import main.runnable.socket.IncomingConnectionListener;
import main.runnable.sound.MP3SoundPlay;
import main.runnable.sound.MidiWavSoundPlay;
import main.runnable.view.LoadFrameRunnable;
import main.session.SessionObject;
import main.util.FileUtil;
import main.util.NetUtil;
import main.util.UserScreenUtil;
import main.view.panel.TexturedPanel;

/**
 * Vista que permite crear un nuevo juego.
 *
 * @author Pablo Labín
 * @author Ignacio Maldonado
 *
 */

public class CreateGameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Integer DEFAULT_WIDTH = 680, DEFAULT_HEIGHT = 550;
	private JPanel main, optionsPanel;
	private JLabel lblServerName, lblGameMode, lblGameName, lblUserName;
	private JTextField txtServerName, txtGameName, txtUserName;
	private JComboBox cmbGameMode;
	private JButton cmdCreateGame, cmdComeBack;
	private LoadGameFrame loadView;
	private NewGameFrame newGameView;
	private MP3SoundPlay mp3Runnable;
	private MidiWavSoundPlay midiWavRunnable;


	public CreateGameFrame(JFrame prevFrame, MP3SoundPlay mp3Runnable, MidiWavSoundPlay midiwavRunnable) {
		super();
		this.setLayout(null);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setBounds((UserScreenUtil.getWidth() / 2) - (DEFAULT_WIDTH / 2),
				(UserScreenUtil.getHeight() / 2) - (DEFAULT_HEIGHT / 2),
				DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setTitle("Crear partida");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = this.getContentPane();

		this.mp3Runnable=mp3Runnable;
		this.midiWavRunnable=midiwavRunnable;

		this.insertLabels();
		this.insertTextFields();
		this.insertButtons();

		this.cmbGameMode = new JComboBox();
		this.cmbGameMode.setBounds(200, 75, 200, 25);
		// Cambiado por silvio, a generico.no modificar, asi pasamos un GameType
		// al frame y desp lo proceso para la
		// logica multiplayer
		for (GameMode gt : GameMode.values()) {
			this.cmbGameMode.addItem(gt);
		}
		this.cmbGameMode.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 12));
		this.cmbGameMode.addMouseListener(new ComponentSFX());

		this.optionsPanel = new JPanel();
		this.optionsPanel.setLayout(null);
		this.optionsPanel.setBounds(30, 120, 600, 280);
		this.optionsPanel.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.LOWERED));

		this.main = new TexturedPanel(FileUtil.readImage(FilePathConsts.menuImagesPath + "/Main_Menu_Background.jpg"));
		this.main.setLayout(null);
		this.main.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.main.add(this.lblServerName);
		this.main.add(this.lblGameMode);
		this.main.add(this.lblGameName);
		this.main.add(this.lblUserName);
		this.main.add(this.txtServerName);
		this.main.add(this.txtUserName);
		this.main.add(this.txtGameName);
		this.main.add(this.cmdCreateGame);
		this.main.add(this.cmdComeBack);
		this.main.add(this.cmbGameMode);
		this.main.add(this.optionsPanel);

		container.add(this.main);

		this.setContentPane(container);
		this.setVisible(false);
		this.cmdComeBack.addActionListener(new WindowSwitchEvent(this, prevFrame));
	}

	public void updateSoundReferences(MP3SoundPlay mp3Runnable, MidiWavSoundPlay midiwavRunnable){
		this.mp3Runnable=mp3Runnable;
		this.midiWavRunnable=midiwavRunnable;
	}

	private void insertButtons() {
		this.cmdCreateGame = new JButton("Crear Partida");
		this.cmdCreateGame.setBounds(400, 420, 150, 25);
		this.cmdCreateGame.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		this.cmdCreateGame.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdCreateGame.addActionListener(new CreateGameAction());
		this.cmdCreateGame.addMouseListener(new ComponentSFX());
		this.cmdCreateGame.addMouseListener(new ComponentGFX(this.cmdCreateGame));

		this.cmdComeBack = new JButton("Volver");
		this.cmdComeBack.setBounds(400, 460, 150, 25);
		this.cmdComeBack.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
		this.cmdComeBack.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdComeBack.addMouseListener(new ComponentSFX());
		this.cmdComeBack.addMouseListener(new ComponentGFX(this.cmdComeBack));
	}

	private void insertLabels() {

		this.lblServerName = new JLabel("Nombre del Servidor:");
		this.lblServerName.setBounds(30, 45, 200, 30);
		this.lblServerName.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));

		this.lblUserName = new JLabel("Nombre Jugador:");
		this.lblUserName.setBounds(70, 15, 200, 30);
		this.lblUserName.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));

		this.lblGameMode = new JLabel("Modo de Juego:");
		this.lblGameMode.setBounds(78, 75, 200, 30);
		this.lblGameMode.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));

		this.lblGameName = new JLabel("Nombre de Partida:");
		this.lblGameName.setBounds(30, 420, 200, 30);
		this.lblGameName.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
	}

	private void insertTextFields() {
		this.txtServerName = new JTextField();
		this.txtServerName.setBounds(200, 45, 200, 25);
		this.txtServerName.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));

		this.txtUserName = new JTextField();
		this.txtUserName.setBounds(200, 15, 200, 25);
		this.txtUserName.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));

		this.txtGameName = new JTextField();
		this.txtGameName.setBounds(185, 420, 200, 25);
		this.txtGameName.setFont(new Font(LookAndFeelConsts.FONT,
				LookAndFeelConsts.FONT_STYLE, 14));
	}

	/**
	 *
	 * @author Guido Tagliavini
	 *
	 */
	private class CreateGameAction implements ActionListener {

		private ConnectionInput connectionInput;
		private ConnectionOutput connectionOutput;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (CreateGameFrame.this.cmbGameMode.getSelectedItem().equals(
					GameMode.MULTI_PLAYER)) {

				HostingRequest hr = new HostingRequest(buildHostNodeDTO());
				new Thread(hr).start();
				IncomingConnectionListener icl = new IncomingConnectionListener(hr.getCloseFlag());
				Thread t = new Thread(icl);
				t.start();

				try {
					t.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace(System.err);
				}

				connectionInput = new ConnectionInput(icl.getSocket());
				connectionOutput = new ConnectionOutput(icl.getSocket());
			}

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					SessionObject sessionObject = new SessionObject();
					sessionObject.setGameMode((GameMode) cmbGameMode.getSelectedItem());
					sessionObject.setPlayerNumber(PlayerNumber.PLAYER_1);

					GameFrame gameFrame = new GameFrame(sessionObject, CreateGameFrame.this, mp3Runnable, midiWavRunnable);

					if(sessionObject.getGameMode().equals(GameMode.MULTI_PLAYER)){
						gameFrame.setConnectionInput(connectionInput);
						gameFrame.setConnectionOutput(connectionOutput);
						connectionInput.setGameFrame(gameFrame);
						connectionOutput.setGameFrame(gameFrame);
						gameFrame.startConnectionInput();
					}

					setVisible(false);
					new Thread(new LoadFrameRunnable(LookAndFeelConsts.LOAD_PROGRESSBAR_DELAY, new LoadGameFrame(gameFrame, LookAndFeelConsts.LOAD_PROGRESSBAR_COLOR))).start();
				}
			});
		}

		private HostNodeDTO buildHostNodeDTO(){
			HostNodeDTO hostNodeDTO = new HostNodeDTO();
			hostNodeDTO.setGameMode(GameMode.MULTI_PLAYER);
			hostNodeDTO.setName(txtUserName.getText());
			hostNodeDTO.setServerName(txtServerName.getText());
			hostNodeDTO.setPort(ConnectionConsts.PEER_PORT);

			hostNodeDTO.setAddress("127.0.0.1");
			/*
			try {
				hostNodeDTO.setAddress(NetUtil.getPublicIP().getHostAddress());
			} catch (UnknownHostException e) {
				e.printStackTrace(System.err);
			}
			*/

			return hostNodeDTO;
		}
	}
}