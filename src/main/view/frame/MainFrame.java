package main.view.frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import main.consts.FilePathConsts;
import main.consts.LookAndFeelConsts;
import main.event.ComponentSFX;
import main.event.ComponentGFX;
import main.event.WindowSwitchEvent;
import main.runnable.sound.MP3SoundPlay;
import main.runnable.sound.MidiWavSoundPlay;
import main.sound.RandomSoundSelector;
import main.util.ConversorUtil;
import main.util.FileUtil;
import main.util.UserScreenUtil;
import main.view.panel.TexturedPanel;

/**
 * Vista principal. Es esta se puede elegir visualizar las Instrucciones o comenzar el juego.
 *
 * @author Pablo Labín
 * @author Ignacio Maldonado
 *
 */

public class MainFrame extends JFrame{

	/**
	 * Para la pantalla de carga se podrían utilizar SplashScreen
	 */
	private static final long serialVersionUID = 1L;
	private static final Integer DEFAULT_WIDTH = 600, DEFAULT_HEIGHT = 360;
	private JButton cmdPlay, cmdInstructions;
	private JCheckBox jcbSound;
	private JPanel main, logoPanel;

	//Agregado por Silvio.
	MP3SoundPlay MP3SoundPlayRun;//metodo stop para parar musica.
	MidiWavSoundPlay MidiWavSoundPlayRun;//metodo stop para parar musica.

	private InstructionsFrame instructionsView;
	private NewGameFrame newGameView;

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){

				new MainFrame();
			}
		});
	}

	public MainFrame(){
		super();
		setLookAndFeels();
		this.setLayout(null);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setBounds((UserScreenUtil.getWidth()/2)-(DEFAULT_WIDTH/2),
				(UserScreenUtil.getHeight()/2)-(DEFAULT_HEIGHT/2),
				DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setTitle("PL3 Operation");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = this.getContentPane();

		//startSound();
		this.newGameView = new NewGameFrame(this, MP3SoundPlayRun, MidiWavSoundPlayRun);
		this.instructionsView = new InstructionsFrame(this);

		this.insertButtons();
		this.insertCheckBoxes();
		this.insertPanels();

		this.main = new TexturedPanel(FileUtil.readImage(FilePathConsts.menuImagesPath + "/Main_Menu_Background.jpg"));
		this.main.setLayout(null);
		this.main.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.main.add(cmdPlay);
		this.main.add(cmdInstructions);
		this.main.add(logoPanel);
		this.main.add(jcbSound);

		container.add(main);
		this.setContentPane(container);
		//this.setVisible(true);
	}

	/**
	 * @author Silvio Vilerino
	 */
	public void startSound(){
		RandomSoundSelector rss=new RandomSoundSelector();
		File sound = rss.getRandomBackgroundSound();
		if(sound.getAbsolutePath().endsWith(".mp3")){
			MP3SoundPlayRun=new MP3SoundPlay(sound.getAbsolutePath(), true);
			new Thread(MP3SoundPlayRun).start();
		}else{
			try {
				MidiWavSoundPlayRun=new MidiWavSoundPlay(ConversorUtil.fileToURL(sound), true);
				new Thread(MidiWavSoundPlayRun).start();
			} catch (MalformedURLException e) {
				e.printStackTrace(System.err);
			}
		}
		updateSoundRefToChildren();
	}

	/**
	 * @author Silvio Vileriño
	 */
	private void insertCheckBoxes(){
		jcbSound=new JCheckBox("Sound ON");
		jcbSound.setBounds(20, 300, 150, 20);
		jcbSound.addActionListener(new SoundOptionAction());
		jcbSound.addMouseListener(new ComponentSFX());
		jcbSound.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 14));
	}

	private void insertButtons(){
		this.cmdPlay = new JButton();
		this.cmdPlay.setBounds(200, 200, 200, 40);
		this.cmdPlay.addActionListener(new WindowSwitchEvent(this, newGameView));
		this.cmdPlay.setText("Jugar");
		this.cmdPlay.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 16));
		this.cmdPlay.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdPlay.addMouseListener(new ComponentSFX());
		this.cmdPlay.addMouseListener(new ComponentGFX(this.cmdPlay));

		this.cmdInstructions = new JButton("Instrucciones");
		this.cmdInstructions.setBounds(200, 260, 200, 40);
		this.cmdInstructions.addActionListener(new WindowSwitchEvent(this, instructionsView));
		this.cmdInstructions.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 16));
		this.cmdInstructions.setBackground(LookAndFeelConsts.BACK_BUTTONS_COLOR);
		this.cmdInstructions.addMouseListener(new ComponentSFX());
		this.cmdInstructions.addMouseListener(new ComponentGFX(this.cmdInstructions));
	}

	private void insertPanels(){
		this.logoPanel=new TexturedPanel(FileUtil.readImage(FilePathConsts.menuImagesPath+ "/logo.jpg"));
		this.logoPanel.setLayout(null);
		this.logoPanel.setBounds(10, 10, 575, 160);
		this.logoPanel.setBackground(Color.white);
	}

	private static void setLookAndFeels(){
		LookAndFeelInfo[] lookAndFeelsList = UIManager.getInstalledLookAndFeels();
		try {
			UIManager.setLookAndFeel(lookAndFeelsList[LookAndFeelConsts.MAIN_LOOKNFEEL].getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Silvio Vileriño
	 */
	private class SoundOptionAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(jcbSound.isSelected()){
				if(MidiWavSoundPlayRun!=null){
					MidiWavSoundPlayRun.stopSoundPlaying();
					MidiWavSoundPlayRun=null;
					jcbSound.setText("Sound OFF");
				}
				if(MP3SoundPlayRun!=null){
					MP3SoundPlayRun.close();
					MP3SoundPlayRun=null;
					jcbSound.setText("Sound OFF");
				}
			}else{
				startSound();
				updateSoundRefToChildren();
				jcbSound.setText("Sound ON");
			}
		}
	}

	public void updateSoundRefToChildren(){
		newGameView.updateSoundReferences(MP3SoundPlayRun, MidiWavSoundPlayRun);
	}

}

