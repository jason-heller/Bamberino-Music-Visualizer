package audio;

import java.io.File;
import java.net.MalformedURLException;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.WaveData;

import core.App;

public class Source {
	private int sourceId;
	private int bufferId;
	
	private int reactionIndex;
	private double duration;
	
	private byte[] data;
	
	public Source(String audioFile) {
		bufferId = AL10.alGenBuffers();
		sourceId = AL10.alGenSources();
		try {
			WaveData waveFile = WaveData.create(new File(audioFile).toURL());
			AL10.alBufferData(bufferId, waveFile.format, waveFile.data, waveFile.samplerate);
			
			data = new byte[waveFile.data.remaining()];
			waveFile.data.get(data);

			long frames = data.length;
			duration = (frames + 0.0) / (waveFile.samplerate * 2.0);

			waveFile.dispose();
		} catch (MalformedURLException e) {
			App.handleCrash(e);
		} catch (NullPointerException e) {
			App.handleCrash(new Exception("Failed to find audio file " + audioFile));
		}
	}
	
	public double getDuration() {
		return duration;
	}
	
	public float getReaction() {
		int time = AL10.alGetSourcei(sourceId, AL11.AL_SAMPLE_OFFSET);
		
		float volume = (float) volumeRMS(time, 100) / 60f;
		return volume;//(float) (Math.floor(volume * 3f) / 3f);
	}
	
	public double volumeRMS(int start, int length) {
		long sum = 0;
		int end = start + length;
		int len = length;
		if (end > data.length) {
			end = data.length;
			len = end - start;
		}
		if (len == 0) {
			return 0;
		}
		for (int i = start; i < end; i++) {
			sum += data[i];
		}
		double average = (double) sum / len;

		double sumMeanSquare = 0;
		;
		for (int i = start; i < end; i++) {
			double f = data[i] - average;
			sumMeanSquare += f * f;
		}
	    double averageMeanSquare = sumMeanSquare/len;
	    double rootMeanSquare = Math.sqrt(averageMeanSquare);

	    return rootMeanSquare;
	}
	
	public void play() {
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
		AL10.alSourcePlay(sourceId);
	}
	
	public void stop() {
		AL10.alSourceStop(sourceId);
	}

	public void setReactionIndex(int reactionIndex) {
		this.reactionIndex = reactionIndex;
	}
	
	public int getReactionIndex() {
		return reactionIndex;
	}

	public void clean() {
		stop();

		AL10.alSourceStop(sourceId);
		AL10.alDeleteSources(sourceId);
		AL10.alDeleteBuffers(bufferId);
	}
}
