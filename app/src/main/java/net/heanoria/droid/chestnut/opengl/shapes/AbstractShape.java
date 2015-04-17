package net.heanoria.droid.chestnut.opengl.shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class AbstractShape {

    protected static int COORDS_PER_VERTEX = 3;

    protected int mProgram;

    protected final FloatBuffer mVertexBuffer;

    protected final String vertexShaderCode =
          "uniform mat4 uMVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.
        + "attribute vec4 vPosition;     \n"		// Per-vertex position information we will pass in.
        + "attribute vec4 aColor;        \n"		// Per-vertex color information we will pass in.

        + "varying vec4 vColor;          \n"		// This will be passed into the fragment shader.

        + "void main()                   \n"		// The entry point for our vertex shader.
        + "{                             \n"
        + "   vColor = aColor;           \n"		// Pass the color through to the fragment shader. It will be interpolated across the triangle.
        + "   gl_Position = uMVPMatrix   \n" 	// gl_Position is a special variable used to store the final position.
        + "               * vPosition;   \n"     // Multiply the vertex by the matrix to get the final point in
        + "}                             \n";    // normalized screen coordinates.

    protected final String fragmentShaderCode =
          "precision mediump float;"
        + "uniform vec4 vColor;"
        + "void main() {"
        + "  gl_FragColor = vColor;"
        + "}";

    public AbstractShape() {
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(getShapeCoords().length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());

        mVertexBuffer = vertexByteBuffer.asFloatBuffer();
        mVertexBuffer.put(getShapeCoords());
        mVertexBuffer.position(0);

        int vertexShader = loadVertexShader(GLES20.GL_VERTEX_SHADER);
        int fragmentShader = loadFragmentShader(GLES20.GL_FRAGMENT_SHADER);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
    }

    protected int loadVertexShader(int type){
        return loadShader(type, vertexShaderCode);
    }

    protected int loadFragmentShader(int type) {
        return loadShader(type, fragmentShaderCode);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void draw(float[] matrix) {
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,12 ,mVertexBuffer);

        // get handle to fragment shader's vColor member
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, getColor(), 0);

        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, matrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, getNumberVertex());

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public abstract float[] getShapeCoords();

    public abstract int getNumberVertex();

    public abstract float[] getColor();
}
