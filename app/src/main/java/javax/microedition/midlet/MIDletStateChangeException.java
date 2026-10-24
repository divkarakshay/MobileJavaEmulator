package javax.microedition.midlet;

public class MIDletStateChangeException extends Exception {
    public MIDletStateChangeException(String reason) {
        super(reason);
    }
}