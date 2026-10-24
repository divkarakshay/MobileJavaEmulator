package javax.microedition.rms;
import java.util.Vector;
public class RecordStore {
    public static final int AUTHMODE_PRIVATE=0, AUTHMODE_ANY=1;
    private String name;
    private Vector<byte[]> records=new Vector<>();
    private RecordStore(String n){ name=n; }
    public static RecordStore openRecordStore(String name, boolean createIfNecessary) throws RecordStoreException { return new RecordStore(name); }
    public static RecordStore openRecordStore(String name, boolean createIfNecessary, int authmode, boolean writable) throws RecordStoreException { return new RecordStore(name); }
    public static RecordStore openRecordStore(String name, String vendorName, String suiteName) throws RecordStoreException { return new RecordStore(name); }
    public static void deleteRecordStore(String name) throws RecordStoreException {}
    public static String[] listRecordStores(){ return new String[0]; }
    public String getName(){ return name; }
    public int getNumRecords(){ return records.size(); }
    public int getSize(){ int s=0; for(byte[] b: records) s+=b.length; return s; }
    public int getSizeAvailable(){ return 1024*1024; }
    public int getVersion(){ return 1; }
    public long getLastModified(){ return 0; }
    public void closeRecordStore() throws RecordStoreException {}
    public int addRecord(byte[] data,int off,int len) throws RecordStoreException { byte[] b=new byte[len]; System.arraycopy(data,off,b,0,len); records.add(b); return records.size(); }
    public void deleteRecord(int id) throws RecordStoreException { records.remove(id-1); }
    public byte[] getRecord(int id) throws RecordStoreException { return records.get(id-1); }
    public void setRecord(int id, byte[] d,int off,int len) throws RecordStoreException { byte[] b=new byte[len]; System.arraycopy(d,off,b,0,len); records.set(id-1,b); }
    public int getRecordSize(int id) throws RecordStoreException { return getRecord(id).length; }
    public int getNextRecordID(){ return records.size()+1; }
    public void setMode(int m,boolean w) throws RecordStoreException {}
    public void addRecordListener(RecordListener l){}
    public void removeRecordListener(RecordListener l){}
    public int enumerateRecords(RecordFilter f, RecordComparator c, boolean keepUpdated){ return 0; }
}
