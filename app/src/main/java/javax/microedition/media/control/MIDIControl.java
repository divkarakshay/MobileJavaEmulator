package javax.microedition.media.control;
import javax.microedition.media.Control;
public interface MIDIControl extends Control {
    int[] getProgramList(int bank);
    int getChannelVolume(int ch);
    int isBankQuerySupported();
}
