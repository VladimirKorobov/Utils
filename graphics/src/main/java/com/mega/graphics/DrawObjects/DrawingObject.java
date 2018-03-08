package com.mega.graphics.DrawObjects;

import android.graphics.RectF;
import com.mega.graphics.Renderers.IRenderer;

/**
 * Created by Vladimir on 21.03.2016.
 */
public abstract class DrawingObject {

    public static final int DRAW = 1;
    public static final int FILL = 2;
    public static final int DRAW_AND_FILL = DRAW | FILL;

    public abstract void Draw(IRenderer renderer);
    public void Dispose() {}
    public abstract RectF getRect();
    public void ResetRenderer() {}
}
