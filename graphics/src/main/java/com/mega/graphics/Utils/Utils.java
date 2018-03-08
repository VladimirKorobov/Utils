package com.mega.graphics.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Vladimir on 01.04.2016.
 */
public class Utils{
    private Paint paint = new Paint();
    public Bitmap TextToBitmap(String text, int color, float size) {
        Rect rect = new Rect();

        paint.setTextSize(size);
        paint.getTextBounds(text, 0, text.length(), rect);

        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paint.setColor(color);
        canvas.drawText(text, 0, size, paint);
        return bitmap;
    }
}
