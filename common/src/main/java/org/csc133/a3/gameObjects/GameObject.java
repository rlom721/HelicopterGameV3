package org.csc133.a3.gameObjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.interfaces.Drawable;

public abstract class GameObject implements Drawable {
    private Transform myTranslation, myRotation, myScale;
    private int myColor;

    private Point location;
    private Dimension dimension;
    private int color;
    private Dimension worldSize;

    public GameObject(){
        myTranslation = Transform.makeIdentity();
        myRotation = Transform.makeIdentity();
        myScale = Transform.makeIdentity();
    }

    public void setLocation(Point location){ this.location = location; }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setColor(int color) { this.color = color; }

    public Point getLocation(){ return this.location; }

    public Dimension getDimension() { return this.dimension; }

    public int getWidth() { return this.dimension.getWidth(); }

    public int getHeight() { return this.dimension.getHeight(); }

    public int getColor() { return this.color; }

    public void setWorldSize(Dimension worldSize) {
        this.worldSize = worldSize;
    }

    public void rotate(double degrees) {
        myRotation.rotate((float)Math.toRadians(degrees), 0, 0);
    }

    public void scale(double sx, double sy) {
        myScale.scale((float)sx, (float)sy);
    }

    public void translate(double tx, double ty) {
        myTranslation.translate((float)tx, (float)ty);
    }

    private Transform gOrigXForm;
    Transform preLTTransform(Graphics g, Point originScreen){
        Transform gXForm = Transform.makeIdentity();

        // get the current transform and save it
        //
        g.getTransform(gXForm);
        gOrigXForm = gXForm.copy();

        // move the drawing coordinates back
        //
        gXForm.translate(originScreen.getX(),originScreen.getY());
        return gXForm;
    }

    void postLTTransform(Graphics g, Point originScreen, Transform gXForm) {
        // move drawing coordinates so local origin coincides w/ screen origin
        // post local transforms
        gXForm.translate(-originScreen.getX(),-originScreen.getY());
        g.setTransform(gXForm);
    }

    void restoreOriginalTransforms(Graphics g){
        //restore original xform
        g.setTransform(gOrigXForm);
    }

    void localTransforms(Transform gXForm) {
        gXForm.translate(myTranslation.getTranslateX(),
                         myTranslation.getTranslateY());
        gXForm.concatenate(myRotation);
        gXForm.scale(myScale.getScaleX(),myScale.getScaleY());
    }

    protected void containerTranslate(Graphics g, Point parentOrigin){
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(parentOrigin.getX(), parentOrigin.getY());
        g.setTransform(gxForm);
    }

    protected void cn1ForwardPrimitiveTranslate(Graphics g, Dimension pDimension){
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(-pDimension.getWidth()/2, -pDimension.getHeight()/2);
        g.setTransform(gxForm);
    }

    void cn1ReversePrimitiveTranslate(Graphics g, Dimension pDimension){
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(pDimension.getWidth()/2, pDimension.getHeight()/2);
        g.setTransform(gxForm);
    }

    public abstract void localDraw(Graphics g, Point parentOrigin,
                                   Point originScreen);

    public void draw(Graphics g, Point originParent, Point originScreen) {
        // get the current transform and save it
        //
        Transform gXForm = preLTTransform(g, originScreen);
        localTransforms(gXForm);
        postLTTransform(g, originScreen, gXForm);
        localDraw(g, originParent, originScreen);
        restoreOriginalTransforms(g);
    }

    public void updateLocalTransforms() {
    }
}
