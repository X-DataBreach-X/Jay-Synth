package org.openjfx;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardController {
    
    // Ableton Live's computer MIDI keyboard layout: semitones above the octave's C
    private static final Map<KeyCode, Integer> KEY_TO_SEMITONE = Map.ofEntries(
        Map.entry(KeyCode.A, 0),          // C
        Map.entry(KeyCode.W, 1),          // C#
        Map.entry(KeyCode.S, 2),          // D
        Map.entry(KeyCode.E, 3),          // D#
        Map.entry(KeyCode.D, 4),          // E
        Map.entry(KeyCode.F, 5),          // F
        Map.entry(KeyCode.T, 6),          // F#
        Map.entry(KeyCode.G, 7),          // G
        Map.entry(KeyCode.Y, 8),          // G#
        Map.entry(KeyCode.H, 9),          // A
        Map.entry(KeyCode.U, 10),         // A#
        Map.entry(KeyCode.J, 11),         // B
        Map.entry(KeyCode.K, 12),         // C
        Map.entry(KeyCode.O, 13),         // C#
        Map.entry(KeyCode.L, 14),         // D
        Map.entry(KeyCode.P, 15),         // D#
        Map.entry(KeyCode.SEMICOLON, 16), // E
        Map.entry(KeyCode.QUOTE, 17)      // F
    );

    private static final int MIN_OCTAVE = -2;
    private static final int MAX_OCTAVE = 7;

    private final Oscillator oscillator;

    // Keys currently held down and the MIDI note each one started, in the order they were pressed
    private final LinkedHashMap<KeyCode, Integer> heldKeys = new LinkedHashMap<>();

    private int octave = 3; // Ableton's default: the A key plays C3 (MIDI note 60)

    public KeyboardController(Oscillator oscillator) {
        this.oscillator = oscillator;
    }

    public void attach(Scene scene) {
        // Event filters run before any button sees the key, so focus doesn't matter
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, this::onKeyReleased);
    }

    private void onKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();

        if (code == KeyCode.Z) {
            octave = Math.max(MIN_OCTAVE, octave - 1);
            event.consume();
            return;
        }
        if (code == KeyCode.X) {
            octave = Math.min(MAX_OCTAVE, octave + 1);
            event.consume();
            return;
        }

        Integer semitone = KEY_TO_SEMITONE.get(code);
        if (semitone == null || heldKeys.containsKey(code)) {
            return; // not a note key, or the OS is auto-repeating a held key
        }

        int note = (octave + 2) * 12 + semitone;
        heldKeys.put(code, note);
        oscillator.noteOn(midiToFrequency(note));
        event.consume();

        
    }

    private void onKeyReleased(KeyEvent event) {
        if (heldKeys.remove(event.getCode()) == null) {
            return;
        }

        if (heldKeys.isEmpty()) {
            oscillator.noteOff();
        } else {
            // Fall back to the most recently pressed key that's still held
            oscillator.noteOn(midiToFrequency(heldKeys.lastEntry().getValue()));
        }
        event.consume();
    }

    public void allNotesOff() {
        heldKeys.clear();
        oscillator.noteOff();
    }

    private static double midiToFrequency(int note) {
        return 440.0 * Math.pow(2, (note - 69) / 12.0);
    }

}
