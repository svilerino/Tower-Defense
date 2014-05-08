package main.view.frame;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import main.consts.FilePathConsts;
import main.consts.LookAndFeelConsts;
import main.dto.ConnectionNodeDTO;
import main.enums.GameMode;
import main.enums.PlayerNumber;
import main.event.ComponentSFX;
import main.event.ComponentGFX;
import main.event.WindowSwitchEvent;
import main.runnable.socket.ConnectionInput;
import main.runnable.socket.ConnectionOutput;
import main.runnable.socket.ServerListRequest;
import main.runnable.sound.MP3SoundPlay;
import main.runnable.sound.MidiWavSoundPlay;
import main.runnable.view.LoadFrameRunnable;
import main.session.SessionObject;
import main.util.DataLoadUtil;
import main.util.FileUtil;
import main.util.UserScreenUtil;
import main.view.panel.TexturedPanel;

/**
 * Vista en la que se visualizan las partidas existentes para entablar un nuevo juego.
 *
 * @author Pablo Labín
 *
 */

public class JoinGameFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final Integer DEFAULT_WIDTH = 670, DEFAULT_HEIGHT = 510;

	private JPanel main;
	private JLabel labelIP, lblLocalIp, labelCliName, lblClientName, labelPlaName;
	private JTable jtbServersTable;
	private JScrollPane scrollServersTable;
	private JButton cmdJoinGame, cmdComeBack, cmdRefresh;
	private JTextField txtUserName;
	private JTableHeader jtbHeader;

	private Vector<String> columnNames;
	private Vector<Vector<String>> playersData;	//JTable columns

	private Vector<ConnectionNodeDTO> connectionsData;

	MP3SoundPlay mp3Runnable;
	MidiWavSoundPlay midiWavRunnable;

	public JoinGameFrame(JFrame prevFrame, MP3SoundPlay mp3Runnable, MidiWavSoundPlay midiwavRunnable){
		super();

		this.setLayout(null);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setBounds((UserScreenUtil.getWidth()/2)-(DEFAULT_WIDTH/2),
				(UserScreenUtil.getHeight()/2)-(DEFAULT_HEIGHT/2),
				DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setTitle("Unirse a una Partida Existente");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = this.getContentPane();

		this.columnNames = new Vector<String>();
		this.columnNames.add("Nombre Jugador");
		this.columnNames.add("Tipo Juego");
		this.columnNames.add("Nombre Servidor");
		this.playersData = new Vector<Vector<String>>();


		this.jtbServersTable = new JTable();
		this.jtbServersTable.setModel(new DefaultTableModel(null,columnNames));

		jtbHeader = this.jtbServersTable.getTableHeader();
		jtbHeader.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));

		//this.jtbServersTable.setDefaultRenderer(String.class, new TableFont());

		this.scrollServersTable = new JScrollPane(jtbServersTable);
		this.scrollServersTable.setBounds(30, 30, 600, 300);

		this.mp3Runnable=mp3Runnable;
		this.midiWavRunnable=midiwavRunnable;

		this.intertLabels();
		this.insertButtons();
		this.insertTextFields();

		this.main = new TexturedPanel(FileUtil.readImage(FilePathConsts.menuImagesPath + "/Main_Menu_Background.jpg"));
		this.main.setLayout(null);
		this.main.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.main.add(scrollServersTable);
		this.main.add(labelIP);
		this.main.add(lblLocalIp);
		this.main.add(labelCliName);
		this.main.add(lblClientName);
		this.main.add(labelPlaName);
		this.main.add(txtUserName);
		this.main.add(cmdJoinGame);
		this.main.add(cmdComeBack);
		this.main.add(cmdRefresh);

		container.add(main);
		this.setContentPane(container);
		this.setVisible(false);
		this.cmdComeBack.addActionListener(new WindowSwitchEvent(this, prevFrame));
	}

	public void updateSoundReferences(MP3SoundPlay mp3Runnable, MidiWavSoundPlay midiwavRunnable){
		this.mp3Runnable=mp3Runnable;
		this.midiWavRunnable=midiwavRunnable;
	}

	private void insertButtons(){
		this.cmdJoinGame = new JButton("Unirse");
		this.cmdJoinGame.setBounds(30, 435, 180, 25);
		this.cmdJoinGame.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));
		this.cmdJoinGame.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdJoinGame.addActionListener(new JoinAction());
		this.cmdJoinGame.addMouseListener(new ComponentSFX());
		this.cmdJoinGame.addMouseListener(new ComponentGFX(this.cmdJoinGame));
		this.cmdComeBack = new JButton("Volver");
		this.cmdComeBack.setBounds(450, 435, 180, 25);
		this.cmdComeBack.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));
		this.cmdComeBack.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdComeBack.addMouseListener(new ComponentSFX());
		this.cmdComeBack.addMouseListener(new ComponentGFX(this.cmdComeBack));

		this.cmdRefresh = new JButton("Refresh");
		this.cmdRefresh.setBounds(240, 435, 180, 25);
		this.cmdRefresh.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));
		this.cmdRefresh.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdRefresh.addActionListener(new RefreshAction());
		this.cmdRefresh.addMouseListener(new ComponentSFX());
		this.cmdRefresh.addMouseListener(new ComponentGFX(this.cmdRefresh));
	}

	private void intertLabels(){
		this.labelIP = new JLabel("IP Local:");
		this.labelIP.setBounds(77,350,200,30);
		this.labelIP.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));

		this.lblLocalIp = new JLabel();
		this.lblLocalIp.setBounds(160, 350, 125, 25);
		this.lblLocalIp.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.lblLocalIp.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));

		this.labelCliName = new JLabel("Nombre Cliente:");
		this.labelCliName.setBounds(30, 390, 150, 25);
		this.labelCliName.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));

		this.lblClientName = new JLabel();
		this.lblClientName.setBounds(160, 390, 125, 25);
		this.lblClientName.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.lblClientName.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));

		this.labelPlaName = new JLabel("Nombre Jugador:");
		this.labelPlaName.setBounds(320, 350, 125, 25);
		this.labelPlaName.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));
	}

	private void insertTextFields(){
		this.txtUserName = new JTextField();
		this.txtUserName.setBounds(450, 350, 125, 25);
		this.txtUserName.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 15));
	}

	/*private class TableFont extends JLabel implements TableCellRenderer{

		public Component getTableCellRendererComponent(JTable arg0, Object arg1,
				boolean arg2, boolean arg3, int arg4, int arg5) {
			JLabel lblModelo = new JLabel();
			lblModelo.setFont(new Font(MiscUtil.FONT, MiscUtil.FONT_STYLE, 14));

			return lblModelo;
		}
	}*/



	/**
	 * @author Guido Tagliavini
	 *
	 */
	private class RefreshAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			ServerListRequest listRequest = new ServerListRequest(DataLoadUtil.getMainServerProperties());
			Thread t = new Thread(listRequest);
			t.start();

			try {
				t.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			playersData = listRequest.getPlayersData();
			connectionsData = listRequest.getConnectionsData();

			if(playersData.size() == 0){
				jtbServersTable.setModel(new DefaultTableModel(null, columnNames));
			}else{
				jtbServersTable.setModel(new DefaultTableModel(playersData, columnNames));
			}
		}
	}

	private class JoinAction implements ActionListener{

		private ConnectionInput connectionInput;
		private ConnectionOutput connectionOutput;

		@Override
		public void actionPerformed(ActionEvent e) {
			ConnectionNodeDTO node = new ConnectionNodeDTO();
			node.setAddress(connectionsData.get(jtbServersTable.getSelectedRow()).getAddress());
			node.setPort(connectionsData.get(jtbServersTable.getSelectedRow()).getPort());

			Socket socket = new Socket();

			try{
				//socket.setSoTimeout(10000);
				socket.connect(new InetSocketAddress(node.getAddress(), node.getPort()));
				//socket.setSoTimeout(0);

				connectionInput = new ConnectionInput(socket);
				connectionOutput = new ConnectionOutput(socket);

			}catch(IOException e1){
				e1.printStackTrace(System.err);
			}

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					SessionObject sessionObject = new SessionObject();
					sessionObject.setGameMode(GameMode.MULTI_PLAYER);
					sessionObject.setPlayerNumber(PlayerNumber.PLAYER_2);

					GameFrame gameFrame = new GameFrame(sessionObject, JoinGameFrame.this, mp3Runnable, midiWavRunnable);

					gameFrame.setConnectionOutput(connectionOutput);
					gameFrame.setConnectionInput(connectionInput);
					connectionOutput.setGameFrame(gameFrame);
					connectionInput.setGameFrame(gameFrame);

					gameFrame.startConnectionInput();

					setVisible(false);
					new Thread(new LoadFrameRunnable(LookAndFeelConsts.LOAD_PROGRESSBAR_DELAY, new LoadGameFrame(gameFrame, LookAndFeelConsts.LOAD_PROGRESSBAR_COLOR))).start();
				}
			});
		}
	}

}

