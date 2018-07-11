package dEMDRC;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioStim {
	byte[] toneBuffer;
	
	//constructor
	public AudioStim() {
		toneBuffer = new byte[(int)((Options.AStimDurInMS * Options.ASampleRate) / 1000)];
		
		createSinWaveBuffer();
	}
	
	/**
	 * I really haven't taken the time to understand the maf magic of what's going on in here yet;
	 * it wasn't that difficult in college, I just don't care about the accoustic energy physics
	 * right now :P
	 */
	private void createSinWaveBuffer() {
		int samples = (int)((Options.AStimDurInMS * Options.ASampleRate) / 1000);
		
		double period = (double)(Options.ASampleRate / Options.AStimFreq);
		for (int ouah = 0; ouah < toneBuffer.length; ouah++) {
			double angle = 2.0 * Math.PI * (ouah / period);	//yeah, I'm retentive like that
			toneBuffer[ouah] = (byte)(Math.sin(angle) * 127f);	//wut?
		}
	}
	
	/**
	 * Method fuh-ruckin' plays the tone generated above
	 * 
	 * @throws LineUnavailableException
	 */
	public void playTone() throws LineUnavailableException {
		final AudioFormat af = new AudioFormat(Options.ASampleRate,8, 1, true, true);
		SourceDataLine line = AudioSystem.getSourceDataLine(af);
		
		line.open(af, Options.ASampleRate);
		line.start();
		line.write(toneBuffer, 0, toneBuffer.length);
		line.drain();
		line.close();
	}
}
