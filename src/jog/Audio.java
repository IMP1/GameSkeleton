package jog;

import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

import run.Game;

public abstract class Audio {

	public static class Source {
		
		OggClip clip;
		private boolean isStopped;
		private boolean isPaused;
		
		private Source(String filename) {
			FileInputStream fis = Filesystem.getInputStream(filename);
			try {
				clip = new OggClip(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (Game.LOGGING)
				System.out.println("[Audio] " + filename + " loaded.");
			isStopped = true;
			isPaused = false;
		}
		
		public void setVolume(float volume) {
			clip.setGain(volume);
		}
		
		public void play() {
			if (isPaused) {
				resume();
				return;
			}
			clip.play();
			isStopped = false;
			isPaused = false;
		}
		
		public void pause() {
			clip.pause();
			isPaused = true;
		}
		
		public void stop() {
			if (isStopped) return;
			clip.stop();
			isStopped = true;
			isPaused = false;
		}
		
		public void resume() {
			if (!isPaused) return;
			clip.resume();
			isPaused = false;
		}
		
	}

	public static Source newSource(String filename) {
		return new Source(filename);
	}
	
}
