package javax.microedition.media;
import java.io.InputStream;
public class Manager {
    public static Player createPlayer(InputStream is, String type) throws MediaException { return new PlayerImpl(); }
    public static Player createPlayer(String locator) throws MediaException { return new PlayerImpl(); }
    static class PlayerImpl implements Player {
        public void realize() throws MediaException {}
        public void prefetch() throws MediaException {}
        public void start() throws MediaException {}
        public void stop() throws MediaException {}
        public void deallocate(){}
        public void close(){}
        public long setMediaTime(long t) throws MediaException {return 0;}
        public long getMediaTime(){return 0;}
        public int getState(){return 0;}
        public long getDuration(){return 0;}
        public String getContentType(){return null;}
        public void setLoopCount(int c){}
        public Control getControl(String type){return null;}
        public Control[] getControls(){return new Control[0];}
        public void addPlayerListener(PlayerListener l){}
        public void removePlayerListener(PlayerListener l){}
    }
}
