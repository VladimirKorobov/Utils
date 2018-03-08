package com.mega.graphics.DrawObjects;

import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 24.03.2016.
 */
public class ScaleObject extends DrawingObject  {
    float kx;
    float ky;

    public ScaleObject(float kx, float ky, DrawingObject drawingObject) {
        this.kx = kx;
        this.ky = ky;
        this.drawingObject = drawingObject;
    }
    public void setKx(float kx) {
        this.kx = kx;
    }
    public void setKy(float ky) {
        this.ky = ky;
    }
    public void setDrawingObject(DrawingObject drawingObject) {
        this.drawingObject = drawingObject;
    }
    public float getKx() {
        return kx;
    }
    public float getKy() {
        return ky;
    }
    public DrawingObject getDrawingObject() {
        return drawingObject;
    }
    private DrawingObject drawingObject;

    @Override
    public void Draw(IRenderer renderer) {
        renderer.DrawScaleObject(this);
    }

    @Override
    public RectF getRect() {
        if(drawingObject != null) {
            RectF rect = drawingObject.getRect();
            rect.left = rect.left * kx;
            rect.right = rect.right * kx;
            rect.top = rect.top * kx;
            rect.bottom = rect.bottom * kx;
            return rect;
        }
        return null;
    }
}
