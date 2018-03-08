package com.mega.graphics.Renderers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import com.mega.graphics.DrawObjects.*;
import com.mega.graphics.Views.ViewSurfaceGL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by Vladimir on 22.03.2016.
 */
public class RendererGL implements IRenderer, GLSurfaceView.Renderer {
    GL10 gl10 = null;
    private IntBuffer vertexBufferInt = null;
    private IntBuffer textureBufferInt = null;
    private float[] glTransformMatrix = new float[16];
    private float[] d2TransformMatrix = new float[9];

    private int[] intArray = new int[8];
    private ViewSurfaceGL surface;

    // Font texture
    private static float nativeTextSize = 50;
    static int textureWidth = 1024;
    static float[] textureCoord;
    static int fontTexture = -1;
    static int textureFontDx;
    static int textureFontDy;
    static int textureCharInRow;
    static int codepageRangeMin = 0x400;
    static int codepageRangeMax = 0x47F;
    static int codepageRange = codepageRangeMax - codepageRangeMin + 1;


    public RendererGL(ViewSurfaceGL surface) {
        this.surface = surface;
        glTransformMatrix[1] = glTransformMatrix[2] = glTransformMatrix[3] =
        glTransformMatrix[4] = glTransformMatrix[6] = glTransformMatrix[7] =
        glTransformMatrix[8] = glTransformMatrix[9] = glTransformMatrix[11] =
        glTransformMatrix[12] = glTransformMatrix[13] = glTransformMatrix[14] = 0;

        glTransformMatrix[0] = glTransformMatrix[5] = glTransformMatrix[10] = glTransformMatrix[15] = 1;
    }

    // Buffer
    private IntBuffer FillBufferInt(IntBuffer buffer, float[] vertices)
    {
        if( intArray.length < vertices.length)
        {
            intArray = new int[vertices.length];
        }
        for( int i = 0; i < vertices.length; i++ )
        {
            intArray[ i ] = Float.floatToIntBits(vertices[i]);
        }
        return FillBufferInt(buffer, intArray);
    }
    private IntBuffer FillBufferInt(IntBuffer buffer, int[] vertices)
    {
        buffer = ReallocateBufferInt(buffer, vertices.length);
        int capacity = buffer.capacity();

        buffer.put(intArray, 0, vertices.length);
        buffer.position(0);
        return buffer;
    }
    private IntBuffer ReallocateBufferInt(IntBuffer buffer, int newSize)
    {
        if(buffer == null || buffer.capacity() < newSize)
        {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(newSize * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            // allocates the memory from the byte buffer
            buffer = byteBuffer.asIntBuffer();
        }
        buffer.position(0);
        return buffer;
    }
    // Buffer
    void setColor(int r, int g, int b, int a) {
        if(gl10 != null)
            gl10.glColor4f((float)r / 255, (float)g / 255, (float)b / 255, (float)a / 255);
    }
    void setColor(int argb) {
        if(gl10 != null) {
            int a = (argb >> 24) & 0xFF;
            int r = (argb >> 16) & 0xFF;
            int g = (argb >> 8) & 0xFF;
            int b = (argb) & 0xFF;
            gl10.glColor4f((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
        }
    }

    static Bitmap generateFontTexture()
    {
        int i;
        Paint paint = new Paint();
        paint.setTextSize(nativeTextSize);
        float[] widths = new float[1];
        paint.getTextWidths("W", widths);
        textureFontDx = (int)(widths[0] + 0.5f);
        textureFontDy = (int)(nativeTextSize * 1.2f + 0.5f);
        int fontRange = codepageRange + 128 - 32;

        int bitmapWidth =  textureWidth;
        textureCharInRow = (int)(bitmapWidth / textureFontDx);
        int columnSize = (int)((fontRange + textureCharInRow - 1) / textureCharInRow);
        int h = columnSize * textureFontDy;
        int bitmapHeight = 1;
        while(bitmapHeight < h)
            bitmapHeight <<= 1;

        // Create an empty, mutable bitmap
        Bitmap bitmap = Bitmap.createBitmap((int)bitmapWidth, (int)bitmapHeight, Bitmap.Config.ALPHA_8);
        int w1 = bitmap.getWidth();
        int h1 = bitmap.getHeight();

        // get a canvas to paint over the bitmap
        Canvas canvas = new Canvas(bitmap);
        paint.setAlpha(0);
        canvas.drawPaint(paint);

        textureCoord = new float[fontRange * 4 * 2];
        // Draw the text
        paint.setAlpha(255);

        StringBuilder text = new StringBuilder(" ");
        i = 0;
        int texIndex = 0;
        float texXFactor = 1.f /  bitmapWidth;
        float texYFactor = 1.f /  bitmapHeight;

        for(int y = 0; y < columnSize && i < fontRange; y ++)
        {
            for(int x = 0; x < textureCharInRow && i < fontRange; x ++, i ++)
            {
                if(i + 32 > 128)
                    text.setCharAt(0, (char)(i + 32 - 128 + codepageRangeMin ));
                else
                    text.setCharAt(0, (char)(i + 32));

                String s = text.toString();
                canvas.drawText(s, x * textureFontDx, y * textureFontDy + nativeTextSize, paint);
                paint.getTextWidths(s, widths);

                float texLeft = x * textureFontDx * texXFactor;
                float texTop = y * textureFontDy * texYFactor;
                float texRight = texLeft + widths[0] * texXFactor;
                float texBottom = texTop + textureFontDy * texYFactor;

                // Left-bottom
                textureCoord[texIndex ++] = texLeft;
                textureCoord[texIndex ++] = texBottom;
                // Left-top
                textureCoord[texIndex ++] = texLeft;
                textureCoord[texIndex ++] = texTop;
                // Right-bottom
                textureCoord[texIndex ++] = texRight;
                textureCoord[texIndex ++] = texBottom;
                // Right-top
                textureCoord[texIndex ++] = texRight;
                textureCoord[texIndex ++] = texTop;
            }
        }

        return bitmap;
    }

    private void drawTextTextures(float[] vertices, float[] textures, float x, float y, float textSize)
    {
        vertexBufferInt = FillBufferInt(vertexBufferInt, vertices);
        textureBufferInt = FillBufferInt(textureBufferInt, textures);

        gl10.glEnable(GL10.GL_TEXTURE_2D);

        gl10.glBindTexture(GL10.GL_TEXTURE_2D, fontTexture);

        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl10.glEnable(GL10.GL_BLEND);
        gl10.glEnable(GL10.GL_ALPHA_TEST);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBufferInt);
        gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBufferInt);

        gl10.glPushMatrix();

        float scale = textSize / nativeTextSize;

        gl10.glTranslatef(x, y - textSize, 0);
        gl10.glScalef(scale, scale, 1);

        gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 2);

        gl10.glPopMatrix();

        gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glDisable(GL10.GL_TEXTURE_2D);
        gl10.glDisable(GL10.GL_BLEND);
    }

    private void getTextArrays(String text, float[] vertices, float[] textures)
    {
        int texIndex = 0;
        int vertIndex = 0;
        int i, j;

        int x0 = 0;
        for(i = 0; i < text.length(); i ++)
        {
            char ch = text.charAt(i);
            int fontTextureIndex;
            if(ch >= codepageRangeMin && ch <= codepageRangeMax) {
                fontTextureIndex = (ch - codepageRangeMin + 128 - 32) * 8;
            }
            else {
                if(ch < 32 || ch > 255) {
                    ch = '?';
                }
                fontTextureIndex = (ch - 32) * 8;
            }
            for(j = 0; j < 8; j ++) {
                textures[texIndex ++] = textureCoord[fontTextureIndex ++];
            }

            float width = (textures[texIndex - 4] - textures[texIndex - 8]) * textureWidth;

            // Left-bottom
            vertices[vertIndex ++] = x0;
            vertices[vertIndex ++] = textureFontDy;
            // Left-top
            vertices[vertIndex ++] = x0;
            vertices[vertIndex ++] = 0;
            // Right-bottom
            vertices[vertIndex ++] = x0 + width;
            vertices[vertIndex ++] = textureFontDy;
            // Right-top
            vertices[vertIndex ++] = x0 + width;
            vertices[vertIndex ++] = 0;

            x0 += width;
        }
    }

    @Override
    public void DrawTextObject(TextObject textObject) {
        if(textObject != null && textObject.getText() != null && fontTexture >= 0) {
            setColor(textObject.getColor());
            // Get texture coord of every symbol
            float[] textures = new float[textObject.getText().length() * 4 * 2];
            float[] vertices = new float[textures.length];

            getTextArrays(textObject.getText(), vertices, textures);
            drawTextTextures(vertices, textures, 0, 0, textObject.getSize());
        }
    }

    @Override
    public void DrawRectObject(RectObject rectObject) {

    }

    @Override
    public void FillRectObject(RectObject rectObject) {

    }

    @Override
    public void DrawLineObject(LineObject lineObject) {

    }

    @Override
    public void DrawBitmapObject(BitmapObject bitmapObject) {
        if(gl10 == null)
            return;
        Bitmap bitmap = bitmapObject.getBitmap();
        int textureWidth = 1;

        while(textureWidth < bitmap.getWidth())
            textureWidth <<= 1;
        int textureHeight = 1;
        while(textureHeight < bitmap.getHeight())
            textureHeight <<= 1;

        gl10.glEnable(GL10.GL_TEXTURE_2D);
        if(bitmapObject.getTextureId() == -1) {
            // Create texture
            int textureId[] = new int[1];

            gl10.glGenTextures(1, textureId, 0);

            //...and bind it to our array
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);

            if(textureWidth != bitmap.getWidth() || textureHeight != bitmap.getHeight()) {
                Bitmap image = Bitmap.createBitmap(textureWidth, textureHeight, bitmap.getConfig());
                Canvas canvas = new Canvas(image);
                canvas.drawBitmap(bitmap, 0, 0, null);
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, image, 0);
                int error = gl10.glGetError();
                if(error != 0)
                    error = 0;

                image.recycle();
            }
            else
            {
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap, 0);
                int error = gl10.glGetError();
                if(error != 0)
                    error = 0;
            }
            bitmapObject.setTextureId(textureId[0]);
        }
        // Draw texture
        gl10.glEnable(GL10.GL_TEXTURE_2D);

        float[] vertices = new float[4 * 2];
        float[] textures = new float[4 * 2];

        vertices[0] = 0;
        vertices[1] = bitmap.getHeight();

        vertices[2] = 0;
        vertices[3] = 0;

        vertices[4] = bitmap.getWidth();
        vertices[5] = bitmap.getHeight();

        vertices[6] = bitmap.getWidth();
        vertices[7] = 0;

        float factorX = (float)bitmap.getWidth() / textureWidth;
        float factorY = (float)bitmap.getHeight() / textureHeight;

        textures[0] = 0;
        textures[1] = factorY;

        textures[2] = 0;
        textures[3] = 0;

        textures[4] = factorX;
        textures[5] = factorY;

        textures[6] = factorX;
        textures[7] = 0;

        gl10.glEnable(GL10.GL_BLEND);
        gl10.glEnable(GL10.GL_ALPHA_TEST);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        setColor(255, 255, 255, 255);

        //...and bind it to our array
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, bitmapObject.getTextureId());

        //Create  Filtered Texture
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        //Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
        vertexBufferInt = FillBufferInt(vertexBufferInt, vertices);
        textureBufferInt = FillBufferInt(textureBufferInt, textures);

        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBufferInt);
        gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBufferInt);

        gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 2);

        gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glDisable(GL10.GL_TEXTURE_2D);
        gl10.glDisable(GL10.GL_BLEND);
    }

    static int textureFromBmp(GL10 gl, Bitmap bmp)
    {
        gl.glEnable(GL10.GL_TEXTURE_2D);

        //Generate one texture pointer...
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        //...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        //Create  Filtered Texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        //gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        //gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        //Use the Android GLUtils to specify a two-dimensional texture image from our bitmap

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
        gl.glDisable(GL10.GL_TEXTURE_2D);

        return textures[0];
    }

    @Override
    public void DrawOffsetObject(OffsetObject offsetObject) {
        if(offsetObject.getDrawingObject() != null) {
            gl10.glPushMatrix();
            gl10.glTranslatef(offsetObject.getX(), offsetObject.getY(), 0);
            offsetObject.getDrawingObject().Draw(this);
            gl10.glPopMatrix();
        }
    }

    @Override
    public void DrawRotateObject(RotateObject rotateObject) {
        if(rotateObject.getDrawingObject() != null) {
            gl10.glPushMatrix();
            gl10.glTranslatef(rotateObject.getCx(), rotateObject.getCy(), 0);
            gl10.glRotatef(rotateObject.getDegrees(), 0, 0, 1);
            gl10.glTranslatef(-rotateObject.getCx(), -rotateObject.getCy(), 0);
            rotateObject.getDrawingObject().Draw(this);
            gl10.glPopMatrix();
        }
    }

    @Override
    public void DrawScaleObject(ScaleObject scaleObject) {
        if(scaleObject.getDrawingObject() != null) {
            gl10.glPushMatrix();
            gl10.glScalef(scaleObject.getKx(), scaleObject.getKy(), 1);
            scaleObject.getDrawingObject().Draw(this);
            gl10.glPopMatrix();
        }
    }

    @Override
    public void DrawTransformObject(TransformObject transformObject) {
        if (transformObject.getDrawingObject() != null) {
            gl10.glPushMatrix();
            transformObject.getMatrix().getValues(d2TransformMatrix);
            glTransformMatrix[0] = d2TransformMatrix[0];
            glTransformMatrix[4] = d2TransformMatrix[1];
            glTransformMatrix[12] = d2TransformMatrix[2];

            glTransformMatrix[1] = d2TransformMatrix[3];
            glTransformMatrix[5] = d2TransformMatrix[4];
            glTransformMatrix[13] = d2TransformMatrix[5];

            gl10.glMultMatrixf(glTransformMatrix, 0);
            transformObject.getDrawingObject().Draw(this);
            gl10.glPopMatrix();
        }
    }

    @Override
    public void DrawTextAsBitmap(TextAsBitmap textAsBitmap) {
        // Just create a texture
        if(gl10 == null)
            return;

        if(textAsBitmap.getTextureId() == -1) {
            // Create texture
            gl10.glEnable(GL10.GL_TEXTURE_2D);
            int textureId[] = new int[1];

            gl10.glGenTextures(1, textureId, 0);

            //...and bind it to our array
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textAsBitmap.getBitmap(), 0);
            gl10.glDisable(GL10.GL_TEXTURE_2D);

            textAsBitmap.setTextureId(textureId[0]);
        }
    }

    @Override
    public void DrawTextAsBitmapObject(TextAsBitmapObject textAsBitmapObject) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // Re-generate text texture
        Bitmap bmp = generateFontTexture();
        fontTexture = textureFromBmp(gl10, bmp);
        bmp.recycle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Set window dimensions
        gl10.glViewport(0, 0, width, height);
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        GLU.gluOrtho2D(gl10, 0, width, height, 0);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
        this.gl10 = gl10;
        surface.UpdateLayout(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        this.gl10 = gl10;
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        surface.Draw();
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
