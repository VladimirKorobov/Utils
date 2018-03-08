package com.mega.graphics.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.mega.graphics.DrawObjects.*;
import com.mega.graphics.Renderers.Renderer2D;

/**
 * Created by Vladimir on 20.03.2016.
 */
public class ViewSurface2D extends SurfaceView implements IViewSurface, SurfaceHolder.Callback{
    Renderer2D renderer;
    float width;
    float height;
    DrawingModel model;

    public ViewSurface2D(Context context, DrawingModel model) {
        super(context);
        this.model = model;
        renderer = new Renderer2D(context);
        getHolder().addCallback(this);
    }

    public void DrawModel() {
        if(model != null)
            model.Draw(renderer);
    }

    public void Draw(Canvas canvas)
    {
        renderer.setCanvas(canvas);
        DrawModel();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
    {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        width = xNew;
        height = yNew;
        if(model != null)
            model.Create(width, height);
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (model != null)
            model.Dispose();
    }

    @Override
    public void Invalidate() {
        Canvas canvas = null;
        SurfaceHolder surfaceHolder = null;
        try {
            surfaceHolder = getHolder();
            if(surfaceHolder != null) {
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas != null) {
                    synchronized (surfaceHolder) {
                        Draw(canvas);
                    }
                }
            }
        } catch (Exception e) {
            // если не получилось, то будем пытаться еще и еще
        }
        finally
        {
            if(canvas != null)
                surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    @Override
    public SurfaceView getView() {
        return this;
    }
}
