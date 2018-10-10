
package z80emu;
    

public class Pin {
    
    private boolean in;
    private boolean out;
    public static final int LOW_LEVEL=0;
    public  static final int HIGH_LEVEL=1;
    public static final int Z=-1;
    private int activeIn;
    private int state;
    private String tag;

    public boolean isIn() {
        return in;
    }

    public Pin(boolean in, boolean out, int activeIn, int state, String tag) {
        this.in = in;
        this.out = out;
        this.activeIn = activeIn;
        this.state = state;
        this.tag = tag;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public int getActiveIn() {
        return activeIn;
    }

    public void setActiveIn(int activeIn) {
        this.activeIn = activeIn;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    
}
