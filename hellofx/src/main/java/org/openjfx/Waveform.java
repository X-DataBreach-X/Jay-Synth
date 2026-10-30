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

    // Returns the wave's value (-1 to 1) at a point in its cycle (phase 0 to 1)
    public double sample(double phase){
        switch (this) {
            case SINE:      return Math.sin(2 * Math.PI * phase);
            case SAW:       return 2 * phase - 1;
            case SQUARE:    return phase < 0.5 ? 1 : -1;
            case TRIANGLE:  return 1 - 4 * Math.abs(phase - 0.5);
            default:        return 0;
        }
    }
}