package com.mega.graphics.DrawObjects;

import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

import java.util.ArrayList;
import java.util.List;

public class ObjectSequence extends DrawingObject {
    protected List<DrawingObject> model = new ArrayList<DrawingObject>();

    public List<DrawingObject> getModel() {
        return model;
    }

    @Override
    public void Draw(IRenderer renderer) {
        if(getVisible()) {
            renderer.DrawObjectSequence(this);
        }
    }

    @Override
    public RectF getRect() {
        RectF rect = new RectF(0, 0, 0, 0);
        for(DrawingObject drawObject : model )
            rect.union(drawObject.getRect());
        return rect;
    }
}
