package com.mega.graphics.DrawObjects;

import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 22.03.2016.
 */
public class OffsetObject extends DrawingObject {
    private float x;
    private float y;
    private DrawingObject drawingObject;

    public OffsetObject(DrawingObject drawingObject){
        this.drawingObject = drawingObject;
    }
    public OffsetObject(float x, float y, DrawingObject drawingObject){
        this.x = x;
        this.y = y;
        this.drawingObject = drawingObject;
    }
    public float getX() { return x;}
    public float getY() { return y;}

    public void setX(float x) { this.x = x;}
    public void setY(float y) { this.y = y;}

    @Override
    public void Draw(IRenderer renderer) {
        renderer.DrawOffsetObject(this);
    }

    @Override
    public void Dispose() {
        this.drawingObject.Dispose();
    }

    @Override
    public RectF getRect() {
        RectF rect = drawingObject.getRect();
        rect.offset(x, y);
        return rect;
    }

    @Override
    public void ResetRenderer() {
        drawingObject.ResetRenderer();
    }

    public DrawingObject getDrawingObject(){
        return this.drawingObject;
    }
}
