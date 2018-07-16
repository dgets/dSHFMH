package dEMDRC;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioStim {
	private byte[] monoToneBuffer;
	private byte[] leftStereoToneBuffer, rightStereoToneBuffer;
	private int bufferLength;
	
	//constructor
	public AudioStim() {
		bufferLength = (int)((Options.AStimDurInMS * Options.ASampleRate) / 1000);
		
		if (!Options.StereoAudio) { 
			monoToneBuffer = new byte[bufferLength];
		} else {
			bufferLength *= 2;	//ouah ouah ouah
			leftStereoToneBuffer = new byte[bufferLength * 2];
			rightStereoToneBuffer = new byte[bufferLength * 2];
		}
		
		createSinWaveBuffer();
	}
	
	/**
	 * I really haven't taken the time to understand the maf magic of what's going on in here yet;
	 * it wasn't that difficult in college, I just don't care about the acoustic energy physics
	 * right now :P
	 */
	private void createSinWaveBuffer() {
		//int samples = (int)((Options.AStimDurInMS * Options.ASampleRate) / 1000);
		double period = (double)(Options.ASampleRate / Options.AStimFreq);
		
		for (int ouah = 0; ouah < bufferLength; ouah += 2) {
			double angle = 2.0 * Math.PI * (ouah / period);	//yeah, I'm retentive like that
			if (!Options.StereoAudio) { 
				monoToneBuffer[ouah] = (byte)(Math.sin(angle) * 127f);	//wut?
			} else {
				short nakk = (short)(Math.sin(angle) * 32767);		//not sure about this const value
				
				/*
				 * Haven't tested it conclusively, but I believe that these tones are playing on both channels every time there
				 * is a bounce
				 */
				rightStereoToneBuffer[ouah] = (byte)(nakk & 0xFF);	//not sure if the channel side is correct here, going
				leftStereoToneBuffer[ouah] = (byte)(nakk >> 8);		//off of a shitty text diagram in code example comments
				rightStereoToneBuffer[ouah + 1] = 0;
				leftStereoToneBuffer[ouah + 1] = 0;
			}
		}
	}
	
	/**
	 * Method fuh-ruckin' plays the tone generated above
	 * 
	 * @param handed Options.StereoSide enum signifying handedness of the stereo channel to play
	 * @throws LineUnavailableException
	 */
	public void playTone(Options.StereoSide handed) throws LineUnavailableException {
		final AudioFormat af;
		
		//I'm thinking that the AudioFormat & SourceDataLine should probably be set up beforehand in order to decrease
		//latency as much as possible during operations
		if (!Options.StereoAudio) { 
			af = new AudioFormat(Options.ASampleRate, 8, 1, true, true);
		} else {
			af = new AudioFormat(Options.ASampleRate, 16, 2, true, false);
		}
		
		SourceDataLine line = AudioSystem.getSourceDataLine(af);
		
		line.open(af, Options.ASampleRate);
		line.start();
		
		if (!HeadsUp.userPrefsDisplay.uSet.MyStereoAudio) {  
			line.write(monoToneBuffer, 0, bufferLength);
		} else if (handed == Options.StereoSide.RIGHT) {
			line.write(rightStereoToneBuffer, 0, (bufferLength * 2));
		} else {
			line.write(leftStereoToneBuffer, 0, (bufferLength * 2));
		}
		
		line.drain();
		line.close();
	}
}
