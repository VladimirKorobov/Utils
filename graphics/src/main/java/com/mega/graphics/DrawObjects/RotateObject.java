package com.mega.graphics.DrawObjects;

import android.graphics.PointF;
import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 23.03.2016.
 */
public class RotateObject extends DrawingObject {
    float degrees;
    float cx;
    float cy;
    private float[] mat = new float[9];
    private float sin;
    private float cos;

    private DrawingObject drawingObject;

    public void setDegrees(float degrees) {
        this.degrees = degrees;
        sin = (float) Math.sin(degrees);
        cos = (float) Math.cos(degrees);
    }
    public void setCx(float cx) {
        this.cx = cx;
    }
    public void setCy(float cy) {
        this.cy = cy;
    }
    public void setDrawingObject(DrawingObject drawingObject) {
        this.drawingObject = drawingObject;
    }
    public float getDegrees() {
        return degrees;
    }
    public float getRadians() {
        return (float)(degrees * Math.PI / 180);
    }

    public float getCx() {
        return cx;
    }
    public float getCy() {
        return cy;
    }
    public DrawingObject getDrawingObject() {
        return drawingObject;
    }

    private PointF RotatePoint(PointF point)
    {
        PointF newPoint = new PointF();
        newPoint.x = cos * (point.x - cx) - sin * (point.y - cy) + cx;
        newPoint.y = sin * (point.x - cx) + cos * (point.y - cy) + cy;
        return newPoint;
    }
    @Override
    public void Draw(IRenderer renderer) {
        if(getVisible()) {
            renderer.DrawRotateObject(this);
        }
    }

    @Override
    public RectF getRect() {
        if(drawingObject != null) {
            // Not verified....
            RectF rect = drawingObject.getRect();
            PointF pt1 = RotatePoint(new PointF(rect.left, rect.top));
            PointF pt2 = RotatePoint(new PointF(rect.left, rect.bottom));
            PointF pt3 = RotatePoint(new PointF(rect.right, rect.top));
            PointF pt4 = RotatePoint(new PointF(rect.right, rect.bottom));

            float xmin = Float.MAX_VALUE;
            float xmax = Float.MIN_VALUE;
            float ymin = Float.MAX_VALUE;
            float ymax = Float.MIN_VALUE;

            if(pt1.x < xmin) xmin = pt1.x;
            if(pt1.x > xmax) xmax = pt1.x;

            if(pt2.x < xmin) xmin = pt2.x;
            if(pt2.x > xmax) xmax = pt2.x;

            if(pt3.x < xmin) xmin = pt3.x;
            if(pt3.x > xmax) xmax = pt3.x;

            if(pt4.x < xmin) xmin = pt4.x;
            if(pt4.x > xmax) xmax = pt4.x;

            if(pt1.y < ymin) ymin = pt1.y;
            if(pt1.y > ymax) ymax = pt1.y;

            if(pt2.y < ymin) ymin = pt2.y;
            if(pt2.y > ymax) ymax = pt2.y;

            if(pt3.y < ymin) ymin = pt3.y;
            if(pt3.y > ymax) ymax = pt3.y;

            if(pt4.y < ymin) ymin = pt4.y;
            if(pt4.y > ymax) ymax = pt4.y;

            rect.set(xmin, ymin, xmax, ymax);
            return rect;
        }

        return null;
    }
}
