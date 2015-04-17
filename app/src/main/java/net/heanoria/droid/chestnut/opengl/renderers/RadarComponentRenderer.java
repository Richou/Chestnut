package net.heanoria.droid.chestnut.opengl.renderers;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import net.heanoria.droid.chestnut.components.RadarComponent;
import net.heanoria.droid.chestnut.opengl.shapes.Circle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RadarComponentRenderer implements GLSurfaceView.Renderer{

    private static String TAG = RadarComponentRenderer.class.getName();

    private Circle radarEffectCircle = null;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRadarEffectMatrix = new float[16];


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        radarEffectCircle = new Circle();
        radarEffectCircle.setColor(new float[]{0.0f, 1.0f, 0.14117647f, 1.0f});
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        float [] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setIdentityM(mRadarEffectMatrix, 0);

        Matrix.translateM(mRadarEffectMatrix, 0, 0, -0.3f, 0);

        long time = SystemClock.uptimeMillis() % 2000L;
        float scale = (2.5f / 2000.0f) * ((int) time);

        Matrix.scaleM(mRadarEffectMatrix, 0, scale, scale, 1.0f);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRadarEffectMatrix, 0);

        radarEffectCircle.draw(scratch);
    }
}
