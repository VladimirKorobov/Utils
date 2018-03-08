package com.mega.graphics.DrawObjects;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 20.03.2016.
 */
public class TextObject extends DrawingObject {
    String text;
    private float size;
    int color;
    public int getColor() { return color;}
    public void setColor(int color) {this.color = color;}

    @Override
    public void Draw(IRenderer renderer) {
        renderer.DrawTextObject(this);
    }

    @Override
    public RectF getRect() {
        Paint p = new Paint();
        p.setTextSize(size);
        Rect rect = new Rect();
        p.getTextBounds(text, 0, text.length(), rect);
        return new RectF(rect);
    }

    public String getText() { return text;}
    public float getSize() { return size;}

    public void setText(String text) {this.text = text;}
    public void setSize(float size) {this.size = size;}
}
