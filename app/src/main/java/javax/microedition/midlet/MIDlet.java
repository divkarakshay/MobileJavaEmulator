package javax.microedition.midlet;

public abstract class MIDlet {
    protected MIDlet() {}

    protected abstract void startApp() throws MIDletStateChangeException;
    protected abstract void pauseApp();
    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;

    public final void notifyDestroyed() {}
    public final void notifyPaused() {}
    public final boolean platformRequest(String URL) { return false; }
    public final int checkPermission(String permission) { return 0; }
}