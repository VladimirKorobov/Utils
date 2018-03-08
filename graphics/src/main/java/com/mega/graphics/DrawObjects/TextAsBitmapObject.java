package com.mega.graphics.DrawObjects;

import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 01.04.2016.
 */
public class TextAsBitmapObject extends DrawingObject {

    private TextAsBitmap textAsBitmap;
    private int index;
    private int color;
    public TextAsBitmapObject(TextAsBitmap textAsBitmap, int index) {
        this.textAsBitmap = textAsBitmap;
        this.index = index;
    }

    public void setColor(int color) {
        this.color = color;
    }
    private int getColor() {
        return color;
    }

    @Override
    public void Draw(IRenderer renderer) {

    }

    @Override
    public RectF getRect() {
        return null;
    }
}
