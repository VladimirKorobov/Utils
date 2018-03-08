package com.mega.graphics.DrawObjects;

import android.graphics.Matrix;
import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 26.03.2016.
 */
public class TransformObject extends DrawingObject {
    DrawingObject drawingObject;
    Matrix matrix = new Matrix();

    public TransformObject(DrawingObject drawingObject) {
        this.drawingObject = drawingObject;
    }
    public Matrix getMatrix() {
        return this.matrix;
    }

    public DrawingObject getDrawingObject() {
        return drawingObject;
    }
    @Override
    public void Draw(IRenderer renderer) {
        renderer.DrawTransformObject(this);
    }

    @Override
    public RectF getRect() {
        RectF rect = drawingObject.getRect();
        matrix.mapRect(rect);
        return rect;
    }
}
