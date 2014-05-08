/**
 * @author Silvio Vilerino
 * @author http://www.cs.princeton.edu/
 * Uses JLayer MP3 Library to play MP3 sounds.
 * Code extracted from http://www.cs.princeton.edu/introcs/faq/mp3/MP3.java.html
 * and modified by Silvio Vilerino.
 */
package main.runnable.sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import main.sound.RandomSoundSelector;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class MP3SoundPlay implements Runnable{
    private String filename;
    private RandomSoundSelector rss;
    private Player player;
    private boolean repeat;

    public MP3SoundPlay(String filename, boolean repeat) {
        this.filename = filename;
        this.rss=null;
        this.repeat=repeat;
    }

    public MP3SoundPlay(RandomSoundSelector rss, boolean repeat) {
        this.repeat=repeat;
        this.rss=rss;
        this.filename=rss.getRandomRoundSound().getAbsolutePath();
    }

    public void close() {
    	if (player != null){
    		repeat=false;
    		player.close();
    	}
    }

    public void play() {
    	try{
        	FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            player.play();
        }catch(IOException e){
        	e.printStackTrace(System.err);
        } catch (JavaLayerException e) {
        	e.printStackTrace(System.err);
		}
    }

	@Override
	public void run() {
		do{
			this.play();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
			if(rss!=null){
				filename=rss.getRandomRoundSound().getAbsolutePath();
			}
		}while(repeat);
	}
}

