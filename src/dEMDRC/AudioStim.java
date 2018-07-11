package dEMDRC;

//import javax.swing.text.DefaultEditorKit;
import java.awt.Toolkit;

import com.sun.media.jfxmedia.track.AudioTrack;

public class AudioStim {
	private int numSamples = Options.ASampleRate * (Options.AStimDurInMS / 1000);
	private byte lSamples[] = new byte[numSamples * 2];
	private byte rSamples[] = new byte[numSamples * 2];
	/* whoops-- looks like this is only good on 'droid :(
	 * private AudioTrack leftTrack = new AudioTrack(AudioManger.STREAM_MUSIC, Options.ASampleRate, AudioFormat.CHANNEL_OUT_STEREO,
	 *											  AudioFormat.ENCODING_PCM_16BIT, (numSamples * 2), AudioTrack.MODE_STATIC); */
	//private DefaultEditorKit.BeepAction dekBeep = new DefaultEditorKit.BeepAction();
	
	AudioTrack wutTrack = new AudioTrack(false, numSamples, null, null, null, numSamples, numSamples, numSamples);
	
	/**
	 * Method initializes the samples array to be played
	 */
	private void initAudioSamples() {
		//so yeah, I took the time, at one point, to understand the code below, but I really don't remember it any more.  it's
		//just ripped from a textbook showing design of an audio clip and heavily condensed here
		for (int ouah = 0; ouah < (numSamples * 2); ouah++) {
			//wait a sec, this is left & right channel, right?
			lSamples[ouah] = (byte) (((short)(Math.sin(2 * Math.PI * ouah / (Options.ASampleRate / Options.AStimFreq)) * 32767))
								      & 0x00ff);
			lSamples[ouah + 1] = 0;
			rSamples[ouah++] = 0;
			rSamples[ouah] = (byte) ((((short)(Math.sin(2 * Math.PI * ouah / (Options.ASampleRate / Options.AStimFreq)) * 32767))
									 & 0xff00) >>> 8);
		}
	}
	
	/*public void initAudioTracks() {
		
	}*/
	
	//we're just going to focus on playing the beep right now, since we don't have net access to get better audio manipulation
	//libraries working here
	public static void playAudioStim(Options.StereoSide handed) throws Exception {
		if (Options.beepForAudio) {
			//well I guess we're out of luck for playing to just one side, at least with this implementation of beeping
			//((DefaultEditorKit) this.dekBeep).beepAction();
			Toolkit.getDefaultToolkit().beep();
		} else {
			throw new Exception("God ouah");
		}
	}
}
