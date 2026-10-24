package javax.microedition.lcdui.game;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
public class TiledLayer extends Layer {
    private int cols, rows, tileW, tileH;
    private int[][] cells;
    private Image tiles;
    public TiledLayer(int cols,int rows,Image tiles,int w,int h){ super(cols*w, rows*h); this.cols=cols; this.rows=rows; this.tiles=tiles; this.tileW=w; this.tileH=h; cells=new int[rows][cols]; }
    public int createAnimatedTile(int staticTileIndex){ return -1; }
    public void setAnimatedTile(int ani,int stat){ }
    public int getAnimatedTile(int ani){ return 0; }
    public void setCell(int col,int row,int idx){ if(row>=0&&row<rows&&col>=0&&col<cols) cells[row][col]=idx; }
    public int getCell(int col,int row){ return cells[row][col]; }
    public void setStaticTileSet(Image img,int w,int h){ tiles=img; tileW=w; tileH=h; }
    public int getCellWidth(){ return tileW; }
    public int getCellHeight(){ return tileH; }
    public int getColumns(){ return cols; }
    public int getRows(){ return rows; }
    public void fillCells(int c,int r,int w,int h,int idx){ for(int yy=r; yy<r+h&&yy<rows; yy++) for(int xx=c; xx<c+w&&xx<cols; xx++) cells[yy][xx]=idx; }
    @Override public void paint(Graphics g){}
}
