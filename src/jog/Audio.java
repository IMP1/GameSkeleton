package jog;

import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

public abstract class Audio {

	public static class Source {
		
		OggClip clip;
		
		private Source(String filename) {
			FileInputStream fis = Filesystem.getInputStream(filename);
			try {
				clip = new OggClip(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void play() {
			clip.play();
		}
		
		public void pause() {
			clip.pause();
		}
		
		public void stop() {
			clip.stop();
		}
		
		public void resume() {
			clip.resume();
		}
		
	}

	public static Source newSource(String filename) {
		return new Source(filename);
	}
	
}
