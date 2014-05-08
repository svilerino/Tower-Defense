package main.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import main.consts.FilePathConsts;
import main.runnable.sound.MP3SoundPlay;

public class ComponentSFX extends MouseAdapter{

	private MP3SoundPlay MP3SoundPlayRun;

	public void mouseEntered(MouseEvent e) {
		File sound= new File(FilePathConsts.componentSFXPath + "/button_entered1.mp3");
		MP3SoundPlayRun=new MP3SoundPlay(sound.getAbsolutePath(), false);
		new Thread(MP3SoundPlayRun).start();
	}

	/*public void mouseExited(MouseEvent e) {
		File sound= new File(FilePathConsts.componentSFXPath + "/button_exited1.mp3");
		MP3SoundPlayRun=new MP3SoundPlay(sound.getAbsolutePath(), false);
		new Thread(MP3SoundPlayRun).start();
	}*/

	public void mouseReleased(MouseEvent e) {
		File sound= new File(FilePathConsts.componentSFXPath + "/button_click1.mp3");
		MP3SoundPlayRun=new MP3SoundPlay(sound.getAbsolutePath(), false);
		new Thread(MP3SoundPlayRun).start();
	}
}
