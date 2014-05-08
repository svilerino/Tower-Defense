package main.view.frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import main.consts.FilePathConsts;
import main.consts.LookAndFeelConsts;
import main.util.FileUtil;
import main.util.UserScreenUtil;
import main.view.panel.TexturedPanel;

/**
 * Vista en la cual se despliega una pantalla de carga, en la espera a que un jugador se conecte a una partida creada.
 *
 * @author Ignacio Maldonado
 *
 */

public class LoadGameFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final Integer DEFAULT_WIDTH = 500, DEFAULT_HEIGHT = 300;
	private JPanel main, logoPanel;
	private JProgressBar pgbProgressBar;
	private JFrame targetFrame;
	private Color pbarColor;

	public LoadGameFrame(JFrame prevFrame, Color pbarColor){
		super();

		setUndecorated(true);
		this.targetFrame=prevFrame;
		this.pbarColor=pbarColor;
		this.setLayout(null);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setBounds((UserScreenUtil.getWidth()/2)-(DEFAULT_WIDTH/2),
				(UserScreenUtil.getHeight()/2)-(DEFAULT_HEIGHT/2),
				DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = this.getContentPane();

		this.pgbProgressBar = new JProgressBar();
		this.pgbProgressBar.setFont(new Font(LookAndFeelConsts.FONT, LookAndFeelConsts.FONT_STYLE, 22));
		this.pgbProgressBar.setOpaque(false);
		this.pgbProgressBar.setStringPainted(true);
		this.pgbProgressBar.setForeground(pbarColor);
		this.pgbProgressBar.setBorder(null);
		this.pgbProgressBar.setBorderPainted(true);
		this.pgbProgressBar.setMinimum(0);
		this.pgbProgressBar.setMaximum(100);
		this.pgbProgressBar.setValue(0);
		this.pgbProgressBar.setBounds(5, 210, 485, 35);

		this.insertPanels();

		this.main = new TexturedPanel(FileUtil.readImage(FilePathConsts.menuImagesPath + "/Main_Menu_Background.jpg"));
		this.main.setLayout(null);
		this.main.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.main.add(logoPanel);
		this.main.add(pgbProgressBar);

		container.add(main);

		this.setContentPane(container);
		this.setVisible(true);
	}

	private void insertPanels(){
		this.logoPanel = new TexturedPanel(FileUtil.readImage(FilePathConsts.menuImagesPath+ "/loading.jpg"));
		this.logoPanel.setLayout(null);
		this.logoPanel.setBounds(5, 5, 485, 200);
		this.logoPanel.setBackground(Color.black);

	}

	public void setProgress(Integer progress){
		this.pgbProgressBar.setValue(progress);
	}

	public void progressCompleted(){
		this.setVisible(false);
		targetFrame.setVisible(true);
		if(targetFrame instanceof GameFrame){
			((GameFrame) targetFrame).changeSounds();
		}
		if(targetFrame instanceof MainFrame){
			((MainFrame) targetFrame).startSound();
		}
	}
}
