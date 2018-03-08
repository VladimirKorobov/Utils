package com.mega.graphics.DrawObjects;

import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir on 21.03.2016.
 */
public class DrawingModel extends DrawingObject {
    protected List<DrawingObject> model = new ArrayList<DrawingObject>();
    public DrawingModel() {
    }
    public void Add(DrawingObject drawingObject) {
        model.add(drawingObject);
    }
    public void Remove(DrawingObject drawingObject) {
        model.remove(drawingObject);
    }
    public Boolean isEmpty() { return model.isEmpty(); }
    public void Create(float width, float height) {

    }

    @Override
    public void Draw(IRenderer renderer) {
        for(DrawingObject drawObject : model )
        {
            drawObject.Draw(renderer);
        }
    }
    @Override
    public void Dispose()
    {
        for(DrawingObject drawObject : model )
            drawObject.Dispose();
    }

    @Override
    public RectF getRect() {
        RectF rect = new RectF(0, 0, 0, 0);
        for(DrawingObject drawObject : model )
            rect.union(drawObject.getRect());
        return rect;
    }

    @Override
    public void ResetRenderer() {
        for(DrawingObject drawObject : model )
            drawObject.ResetRenderer();
    }
}
