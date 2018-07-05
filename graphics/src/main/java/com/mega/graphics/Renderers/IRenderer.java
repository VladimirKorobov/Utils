package com.mega.graphics.Renderers;

import com.mega.graphics.DrawObjects.*;

import java.util.List;

/**
 * Created by Vladimir on 20.03.2016.
 */
public interface IRenderer {
    void DrawTextObject(TextObject textObject);
    void DrawRectObject(RectObject rectObject);
    void FillRectObject(RectObject rectObject);
    void DrawLineObject(LineObject lineObject);
    void DrawBitmapObject(BitmapObject bitmapObject);
    void DrawObjectSequence(ObjectSequence objectSequence);
    void DrawOffsetObject(OffsetObject offsetObject);
    void DrawRotateObject(RotateObject rotateObject);
    void DrawScaleObject(ScaleObject scaleObject);
    void DrawTransformObject(TransformObject transformObject);
    void DrawTextAsBitmap(TextAsBitmap textAsBitmap);
    void DrawTextAsBitmapObject(TextAsBitmapObject textAsBitmapObject);
}
