package javax.microedition.m3g;
public class Camera extends Node {
    public static final int GENERIC=0, PARALLEL=1, PERSPECTIVE=2;
    public void setGeneric(Transform t){}
    public void setParallel(float h,float w,float near,float far){}
    public void setPerspective(float fov,float aspect,float near,float far){}
}
