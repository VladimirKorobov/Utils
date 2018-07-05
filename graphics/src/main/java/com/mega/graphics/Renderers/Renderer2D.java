package com.mega.graphics.Renderers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.mega.graphics.DrawObjects.*;

import java.util.List;

/**
 * Created by Vladimir on 20.03.2016.
 */
public class Renderer2D implements IRenderer {
    Context context;
    Paint paint = new Paint();
    Canvas canvas;

    public Renderer2D(Context context) {
        this.context = context;
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
    }
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void DrawTextObject(TextObject textObject) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textObject.getColor());
        paint.setTextSize(textObject.getSize());
        canvas.drawText(textObject.getText(), 0, 0, paint);
    }

    @Override
    public void DrawRectObject(RectObject rectObject) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(rectObject.getWeight());
        paint.setColor(rectObject.getColor());
        canvas.drawRect(rectObject.getLeft(), rectObject.getTop(), rectObject.getRight(), rectObject.getBottom(), paint);
    }

    @Override
    public void FillRectObject(RectObject rectObject) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(rectObject.getColor());
        canvas.drawRect(rectObject.getLeft(), rectObject.getTop(), rectObject.getRight(), rectObject.getBottom(), paint);
    }

    @Override
    public void DrawLineObject(LineObject lineObject) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(lineObject.getColor());
        paint.setStrokeWidth(lineObject.getWidth());
        canvas.drawLine(lineObject.getX1(), lineObject.getY1(), lineObject.getX2(), lineObject.getY2(), paint);
    }

    @Override
    public void DrawBitmapObject(BitmapObject bitmapObject) {
        canvas.drawBitmap(bitmapObject.getBitmap(), 0f, 0f, paint);
    }

    @Override
    public void DrawObjectSequence(ObjectSequence objectSequence) {
        List<DrawingObject> model = objectSequence.getModel();
        canvas.save();
        for(DrawingObject obj:  model) {
            obj.Draw(this);
            RectF rect = obj.getRect();
            if(rect != null) {
                canvas.translate(rect.width(), 0);
            }
        }
        canvas.restore();
    }

        @Override
    public void DrawOffsetObject(OffsetObject offsetObject) {
        if(offsetObject.getDrawingObject() != null) {
            canvas.save();
            canvas.translate(offsetObject.getX(), offsetObject.getY());
            offsetObject.getDrawingObject().Draw(this);
            canvas.restore();
        }
    }

    @Override
    public void DrawRotateObject(RotateObject rotateObject) {
        if(rotateObject.getDrawingObject() != null) {
            canvas.save();
            canvas.rotate(rotateObject.getDegrees(), rotateObject.getCx(), rotateObject.getCy());
            rotateObject.getDrawingObject().Draw(this);
            canvas.restore();
        }
    }

    @Override
    public void DrawScaleObject(ScaleObject scaleObject) {
        if(scaleObject.getDrawingObject() != null) {
            canvas.save();
            canvas.scale(scaleObject.getKx(), scaleObject.getKy());
            scaleObject.getDrawingObject().Draw(this);
            canvas.restore();
        }
    }

    @Override
    public void DrawTransformObject(TransformObject transformObject) {
        if(transformObject != null)
        {
            canvas.save();
            float[] m = new float[9];
            transformObject.getMatrix().getValues(m);
            canvas.concat(transformObject.getMatrix());
            transformObject.getDrawingObject().Draw(this);
            canvas.restore();
        }
    }

    @Override
    public void DrawTextAsBitmap(TextAsBitmap textAsBitmap) {

    }

    @Override
    public void DrawTextAsBitmapObject(TextAsBitmapObject textAsBitmapObject) {

    }
}
