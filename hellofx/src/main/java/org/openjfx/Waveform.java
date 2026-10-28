package org.openjfx;

public enum Waveform {
    SINE("Sine"),
    SAW("Saw"),
    SQUARE("Square"),
    TRIANGLE("Triangle");

    private final String displayName;

    // Sets the display name to local variable
    Waveform(String displayName) {
        this.displayName = displayName;
    }

    // Returns the display name
    public String getDisplayName() {
        return displayName;
    }

    // Triangle -> Sine
    public Waveform next() {
        Waveform[] all = values();
        return all[(ordinal() + 1) % all.length];
    }

    // Sine -> Triangle
    public Waveform previous(){
        Waveform[] all = values();
        return all[(ordinal() - 1 + all.length) % all.length];
    }
}