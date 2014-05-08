package main.view.frame;

import java.awt.Container;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.consts.FilePathConsts;
import main.consts.LookAndFeelConsts;
import main.event.ComponentSFX;
import main.event.ComponentGFX;
import main.event.WindowSwitchEvent;
import main.runnable.sound.MP3SoundPlay;
import main.runnable.sound.MidiWavSoundPlay;
import main.util.FileUtil;
import main.util.UserScreenUtil;
import main.view.panel.TexturedPanel;

/**
 * Vista en donde se podrá elegir crear una nueva partida, o unirse a una existente.
 *
 * @author Pablo Labín
 * @author Ignacio Maldonado
 *
 */

public class NewGameFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final Integer DEFAULT_WIDTH = 420, DEFAULT_HEIGHT = 300;
	private JButton cmdNewGame, cmdJoinGame, cmdComeBack;
	private JPanel main;
	private JLabel lblSelection;

	private CreateGameFrame createGameView;
	private JoinGameFrame joinGameView;

	public NewGameFrame(JFrame prevFrame, MP3SoundPlay mp3Runnable, MidiWavSoundPlay midiwavRunnable){
		super();

		this.setLayout(null);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setBounds((UserScreenUtil.getWidth()/2)-(DEFAULT_WIDTH/2),
				(UserScreenUtil.getHeight()/2)-(DEFAULT_HEIGHT/2),
				DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setTitle("Nuevo juego");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = this.getContentPane();

		this.createGameView = new CreateGameFrame(this, mp3Runnable, midiwavRunnable);
		this.joinGameView = new JoinGameFrame(this, mp3Runnable, midiwavRunnable);

		this.insertButtons();
		this.insertLabels();

		this.main = new TexturedPanel(FileUtil.readImage(FilePathConsts.menuImagesPath + "/Main_Menu_Background.jpg"));
		this.main.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.main.setLayout(null);

		this.main.add(cmdNewGame);
		this.main.add(cmdJoinGame);
		this.main.add(cmdComeBack);
		this.main.add(lblSelection);

		container.add(main);
		this.setContentPane(container);
		this.setVisible(false);
		this.cmdComeBack.addActionListener(new WindowSwitchEvent(this, prevFrame));
	}

	public void updateSoundReferences(MP3SoundPlay mp3Runnable, MidiWavSoundPlay midiwavRunnable){
		this.createGameView.updateSoundReferences(mp3Runnable, midiwavRunnable);
		this.joinGameView.updateSoundReferences(mp3Runnable, midiwavRunnable);
	}

	private void insertButtons(){
		this.cmdNewGame = new JButton("Crear nueva partida");
		this.cmdNewGame.setBounds(80, 70, 250, 40);
		this.cmdNewGame.addActionListener(new WindowSwitchEvent(this, createGameView));
		this.cmdNewGame.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 12));
		this.cmdNewGame.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdNewGame.addMouseListener(new ComponentSFX());
		this.cmdNewGame.addMouseListener(new ComponentGFX(this.cmdNewGame));

		this.cmdJoinGame = new JButton("Unirse a una partida existente");
		this.cmdJoinGame.setBounds(80, 130, 250, 40);
		this.cmdJoinGame.addActionListener(new WindowSwitchEvent(this, joinGameView));
		this.cmdJoinGame.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 12));
		this.cmdJoinGame.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdJoinGame.addMouseListener(new ComponentSFX());
		this.cmdJoinGame.addMouseListener(new ComponentGFX(this.cmdJoinGame));

		this.cmdComeBack = new JButton("Volver");
		this.cmdComeBack.setBounds(232, 205, 100, 20);
		this.cmdComeBack.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 12));
		this.cmdComeBack.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdComeBack.addMouseListener(new ComponentSFX());
		this.cmdComeBack.addMouseListener(new ComponentGFX(this.cmdComeBack));
	}

	private void insertLabels(){
		this.lblSelection = new JLabel("Seleccione el tipo de juego:");
		this.lblSelection.setBounds(30, 20, 200, 30);
		this.lblSelection.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 12));
	}

}
