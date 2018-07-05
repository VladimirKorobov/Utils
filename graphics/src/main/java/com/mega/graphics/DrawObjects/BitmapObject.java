package com.mega.graphics.DrawObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;
import com.mega.graphics.Renderers.Renderer2D;

/**
 * Created by Vladimir on 21.03.2016.
 */
public class BitmapObject extends DrawingObject  {
    Bitmap bitmap;
    int textureId = -1;
    public BitmapObject(Context context, int resId)
    {
        bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
    }
    public BitmapObject(float width, float height, Bitmap.Config format)
    {
        bitmap = Bitmap.createBitmap((int) width, (int) height, format);
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }
    public int getTextureId() { return textureId; }
    public void setTextureId(int textureId) { this.textureId = textureId; }

    @Override
    public void Dispose()
    {
        if(bitmap != null)
            bitmap.recycle();
    }

    @Override
    public RectF getRect() {
        return new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    public void Draw(IRenderer renderer) {
        if(getVisible()) {
            renderer.DrawBitmapObject(this);
        }
    }

    @Override
    public void ResetRenderer() {
        textureId = -1;
    }

    public IRenderer CreateRenderer(Context context)
    {
        Renderer2D renderer = new Renderer2D(context);
        renderer.setCanvas(new Canvas(bitmap));
        return renderer;
    }
}
