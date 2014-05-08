package main.view.frame;

import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.consts.FilePathConsts;
import main.event.WindowSwitchEvent;
import main.util.FileUtil;
import main.util.UserScreenUtil;

/**
 * Vista en donde se pueden ver las instrucciones del juego.
 *
 * @author Pablo Labín
 * @author Ignacio Maldonado
 *
 */

public class InstructionsFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Integer DEFAULT_WIDTH = 505, DEFAULT_HEIGHT = 525;
	private JPanel main, instructionsPanel;

	public InstructionsFrame(JFrame prevFrame) {
		super();

		// mainView = new MainFrame();
		// mainView.setVisible(false);

		this.setLayout(null);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setBounds((UserScreenUtil.getWidth() / 2) - (DEFAULT_WIDTH / 2),
				(UserScreenUtil.getHeight() / 2) - (DEFAULT_HEIGHT / 2),
				DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setTitle("Instrucciones");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		Container container = this.getContentPane();

		this.instructionsPanel = new JPanel(){
			public void paintComponent(Graphics g){
				g.drawImage(FileUtil.readImage(FilePathConsts.menuImagesPath + "/instructions.jpg"), 0, 0, null);
			}
		};
	
		this.instructionsPanel.setLayout(null);
		this.instructionsPanel.setBounds(0, 0, 500, 600);

		this.main = new JPanel();
		this.main.setLayout(null);
		this.main.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

		this.main.add(instructionsPanel);
		container.add(main);

		this.setContentPane(container);
		this.setVisible(false);
		this.addWindowListener(new WindowSwitchEvent(this, prevFrame));
	}
}
