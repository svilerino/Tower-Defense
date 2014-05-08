package main.runnable.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * @author Silvio Vileriño
 *
 */

public class MidiWavSoundPlay implements Runnable {

	private URL url;
	private Boolean repeat;
	private AudioClip clip;

	public MidiWavSoundPlay(URL url, Boolean repeat){
		this.url = url;
		this.repeat = repeat;
		this.clip = Applet.newAudioClip(url);
	}

	public void run() {
		if(repeat){
			clip.loop();
		}else{
			clip.play();//play one time
		}
	}

	public void stopSoundPlaying(){
		clip.stop();
	}
}
