package audio;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

import asset.SceneFileReader;
import core.App;
import core.Window;
import scene.Globals;
import scene.IRender;

public class AudioHandler {
	private Map<Source, IRender> reactors;
	
	public static long playTimeMs;
	
	private long initTimeMs;
	private boolean isPlaying = false;
	
	private float avgGain;
	private double songDuration, elapsedTime;
	private int remainingSongDuration;
	
	public AudioHandler(String sceneFile, Map<String, IRender> renderables) {
		try {
			AL.create();
		} catch (LWJGLException e) {
			App.handleCrash(new Exception("Failed to load OpenAL"));
		}
		
		Map<String, List<Source>> sources = SceneFileReader.load(sceneFile);
		this.reactors = new HashMap<>();
		
		for(String key : sources.keySet()) {
			List<Source> sourceList = sources.get(key);
			
			for(Source source : sourceList) {
				songDuration = Math.max(songDuration, source.getDuration());
				reactors.put(source, renderables.get(key));
			}
		}

		remainingSongDuration = (int)songDuration;
		initTimeMs = System.currentTimeMillis();
	}
	
	public double getSongDuration() {
		return songDuration;
	}
	
	public void update() {
		long currentTime = System.currentTimeMillis() - initTimeMs;
		
		if (!isPlaying) {
			if (currentTime >= playTimeMs) {
				isPlaying = true;
				Globals.titleOpacity = 1f;
				for(Source source : reactors.keySet()) {
					source.play();
				}
			} else {
				Globals.titleOpacity = currentTime / (float)playTimeMs;
				return;
			}
		}

		elapsedTime += Window.deltaTime;
		double timeRemaining = (songDuration - elapsedTime);
		remainingSongDuration = (int)timeRemaining;
		
		if (timeRemaining <= 0) {
			remainingSongDuration = 0;
			Globals.titleOpacity = 1f - (-(float)timeRemaining / (playTimeMs/1000));
		}
		
		avgGain = 0f;
		for(Source source : reactors.keySet()) {
			float value = source.getReaction();
			avgGain += value;
			IRender reactor = reactors.get(source);
			reactor.setReactionValue(source.getReactionIndex(), value);
		}
		avgGain /= reactors.keySet().size();
	}

	public void clean() {
		for(Source source : reactors.keySet()) {
			source.clean();
		}
		
		AL.destroy();
	}

	public float getAverageGain() {
		return avgGain;
	}

	public int getRemainingSongDuration() {
		return remainingSongDuration;
	}
}
