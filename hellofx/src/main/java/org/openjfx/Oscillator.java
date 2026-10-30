package org.openjfx;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Oscillator {
    
    private static final int SAMPLE_RATE = 44100;

    // Samples per chunk sent to speakers
    private static final int BUFFER_SIZE = 512;

    // volatile: these are changed by the UI thread and read by the audio thread
    private volatile Waveform waveform = Waveform.SINE;
    private volatile double frequency = 440.0;
    private volatile double volume = 0.2;
    private volatile boolean playing = false;
    private volatile boolean gate = false;
    
    private Thread audioThread;
    private double phase = 0.0;

    public void start() throws LineUnavailableException {
        if (playing) {
            return;
        }

        // 44100 samples/sec, 16 bits each, mono, signed, little-endian
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        SourceDataLine line = AudioSystem.getSourceDataLine(format);
        line.open(format, BUFFER_SIZE * 2 * 4);
        line.start();

        playing = true;
        audioThread = new Thread(() -> run(line), "audio");
        audioThread.setDaemon(true);
        audioThread.start();
    }

    public void stop() {
        playing = false;
        if (audioThread != null) {
            try {
                audioThread.join(); // wait for the audio thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            audioThread = null;
        }
    }

    private void run(SourceDataLine line) {
        byte[] buffer = new byte[BUFFER_SIZE * 2]; // 2 bytes per 16 bit sample

        while (playing) {
            for (int i = 0; i < BUFFER_SIZE; i++) {
                double value = gate ? waveform.sample(phase) * volume : 0;
                short sample = (short) (value * Short.MAX_VALUE);

                buffer[2 * i] = (byte) sample;              // low byte
                buffer[2 * i + 1] = (byte) (sample >> 8);   // high byte

                phase += frequency / SAMPLE_RATE;
                if (phase >= 1.0) {
                    phase -= 1.0;
                }
            }
            line.write(buffer, 0, buffer.length); // waits until the speakers need more
        }

        line.stop();
        line.close();
    } 

    public void setWaveform(Waveform waveform) {
        this.waveform = waveform;
    }

    public void setFrequency(double frequency){
        this.frequency = frequency;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void noteOn(double frequency) {
        this.frequency = frequency;
        gate = true;
    }

    public void noteOff(){
        gate = false;
    }
}
