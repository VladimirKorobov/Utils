package com.mega.graphics.DrawObjects;

import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 21.03.2016.
 */
public class RectObject extends DrawingObject {
    float left;
    float top;
    float right;
    float bottom;
    int color;
    float weight;
    int drawFlag = DrawingObject.FILL;

    public int getDrawFlag() { return drawFlag; }
    public void setDrawFlag(int drawFlag) { this.drawFlag = drawFlag; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public int getColor() { return color;}
    public void setColor(int color) {this.color = color;}

    @Override
    public void Draw(IRenderer renderer) {
        if((drawFlag | DrawingObject.FILL) != 0)
            renderer.FillRectObject(this);
        if((drawFlag | DrawingObject.DRAW) != 0)
            renderer.DrawRectObject(this);
    }

    @Override
    public RectF getRect() {
        return new RectF(left, top, right, bottom);
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getLeft() {
        return this.left;
    }

    public float getTop() {
        return this.top;
    }

    public float getRight() {
        return this.right;
    }

    public float getBottom() {
        return this.bottom;
    }

    public float getWidth() {
        return this.right - this.left;
    }

    public float getHeight() {
        return this.bottom - this.top;
    }
}
