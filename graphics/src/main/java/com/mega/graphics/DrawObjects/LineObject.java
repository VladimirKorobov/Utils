package com.mega.graphics.DrawObjects;

import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 21.03.2016.
 */
public class LineObject extends DrawingObject  {
    float x1;
    float x2;
    float y1;
    float y2;
    float width;
    int color;
    public int getColor() { return color;}
    public void setColor(int color) {this.color = color;}
    @Override
    public void Draw(IRenderer renderer) {
        if(getVisible()) {
            renderer.DrawLineObject(this);
        }
    }

    @Override
    public RectF getRect() {
        return new RectF(x1, y1, x2, y2);
    }

    public void setX1(float x1) { this.x1 = x1;}
    public void setX2(float x2) { this.x2 = x2;}
    public void setY1(float y1) { this.y1 = y1;}
    public void setY2(float y2) { this.y2 = y2;}
    public void setWidth(float width) { this.width = width; }

    public float getX1() { return this.x1; }
    public float getX2() { return this.x2; }
    public float getY1() { return this.y1; }
    public float getY2() { return this.y2; }
    public float getWidth() { return this.width; }
}
