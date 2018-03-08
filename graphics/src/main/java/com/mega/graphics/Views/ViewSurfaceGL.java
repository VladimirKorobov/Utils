package com.mega.graphics.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.SurfaceView;
import com.mega.graphics.DrawObjects.DrawingModel;
import com.mega.graphics.Renderers.RendererGL;

/**
 * Created by Vladimir on 22.03.2016.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class ViewSurfaceGL extends GLSurfaceView implements IViewSurface {
    DrawingModel model;
    RendererGL renderer;

    public ViewSurfaceGL(Context context, DrawingModel model) {
        super(context);
        renderer = new RendererGL(this);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        getHolder().addCallback(this);
        this.model = model;
    }

    public void UpdateLayout(int width, int height)
    {
        if(model != null)
            model.Create((float)width, (float)height);
    }

    public void Draw() {
        if(model != null)
            model.Draw(renderer);
    }

    @Override
    public void Invalidate() {
        requestRender();
    }

    @Override
    public SurfaceView getView() {
        return this;
    }
}
