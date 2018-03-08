package com.mega.graphics.DrawObjects;

import android.graphics.*;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 03.04.2016.
 */
public class TextAsBitmap extends DrawingObject {
    private Bitmap bitmap;
    private int textureId = -1;
    private float[] textCoord;
    private float[] textSize;
    private static final int maxTextureWidth = 1024;
    private String[] text;

    public TextAsBitmap(String[] text, float[] size)
    {
        // TODO: Add verification...
        Paint paint = new Paint();
        int textureWidth = 0;
        int maxLineHeight = 0;
        int textureHeight = 0;
        Rect textRect = new Rect();
        this.text = text.clone();
        // Get bitmap size
        for(int i = 0; i < text.length; i ++) {
            paint.setTextSize(size[i]);
            paint.getTextBounds(text[i], 0, 0, textRect);
            if (textRect.width() > maxTextureWidth) {
                // TODO: process error
                return;
            }
            if (textureWidth + textRect.width() > maxTextureWidth) {
                textureHeight += maxLineHeight;
                maxLineHeight = 0;
                textureWidth = 0;
            } else {
                textureWidth += textRect.width();
                if (textRect.height() > maxLineHeight) {
                    maxLineHeight = textRect.height();
                }
            }
        }
        int maxTextureHeight = 1;
        while(maxTextureHeight < textureHeight)
            maxTextureHeight <<= 1;

        // Have textureWidth and textureHeight
        bitmap = Bitmap.createBitmap(maxTextureWidth, maxTextureHeight, Bitmap.Config.ALPHA_8);
        textCoord = new float[text.length * 2];
        textSize = new float[text.length * 2];
        Canvas canvas = new Canvas(bitmap);
        paint.setAlpha(0);
        canvas.drawPaint(paint);
        paint.setAlpha(255);

        textureWidth = 0;
        maxLineHeight = 0;
        textureHeight = 0;

        for(int i = 0; i < text.length; i ++) {
            paint.setTextSize(size[i]);
            paint.getTextBounds(text[i], 0, 0, textRect);
            if (textRect.width() > maxTextureWidth) {
                // TODO: process error
                return;
            }
            if (textureWidth + textRect.width() > maxTextureWidth) {
                textureHeight += maxLineHeight;
                maxLineHeight = 0;
                textureWidth = 0;
            } else {
                canvas.drawText(text[i], (float)textureWidth, (float)textureHeight + textRect.height(), paint);
                textCoord[i*2] = (float)textureWidth;
                textCoord[i*2 + 1] = (float)textureHeight + textRect.height();
                textSize[i*2] = textRect.width();
                textSize[i*2 + 1] = textRect.height();

                textureWidth += textRect.width();
                if (textRect.height() > maxLineHeight) {
                    maxLineHeight = textRect.height();
                }
            }
        }
    }
    public String getText(int index) {
        return text[index];
    }
    public void getTextRect(int Index, RectF rect) {
        rect.left = 0;
        rect.top = 0;
        rect.right = textSize[Index*2];
        rect.bottom = textSize[Index*2 + 1];
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }
    public int getTextureId() { return textureId; }
    public void setTextureId(int textureId) { this.textureId = textureId; }

    @Override
    public void Dispose() {
        bitmap.recycle();
    }
    @Override
    public void ResetRenderer() {
        textureId = -1;
    }

    @Override
    public void Draw(IRenderer renderer) {

    }

    @Override
    public RectF getRect() {
        return null;
    }
}
