package javax.microedition.media.control;
import javax.microedition.media.Control;
public interface RecordControl extends Control {
    void setRecordLocation(String loc);
    String getContentType();
    void startRecord();
    void stopRecord();
    void commit();
}
