package net.heanoria.droid.chestnut.renderers;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

/*    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";*/
        private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.

            + "attribute vec4 vPosition;     \n"		// Per-vertex position information we will pass in.
            + "attribute vec4 aColor;        \n"		// Per-vertex color information we will pass in.

            + "varying vec4 vColor;          \n"		// This will be passed into the fragment shader.

            + "void main()                    \n"		// The entry point for our vertex shader.
            + "{                              \n"
            + "   vColor = aColor;          \n"		// Pass the color through to the fragment shader. It will be interpolated across the triangle.
            + "   gl_Position = uMVPMatrix   \n" 	// gl_Position is a special variable used to store the final position.
            + "               * vPosition;   \n"     // Multiply the vertex by the matrix to get the final point in
            + "}                              \n";    // normalized screen coordinates.

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer mVertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static final int NUMBER_OF_VERTEX = 362;
/*    static float triangleCoords[] = {
            // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f,   // top
            -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
    };*/
    private static float triangleCoords[] = new float[NUMBER_OF_VERTEX * COORDS_PER_VERTEX];
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    //float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.0f };
    float color[] = {0.0f, 1.0f, 0.14117647f, 1.0f};

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Triangle() {
        initializeCircleMatrix();
        // initialize vertex byte buffer for shape coordinates
        Log.v("Thread", "" + triangleCoords[0] + "," + triangleCoords[1] + "," + triangleCoords[2]);
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = vertexByteBuffer.asFloatBuffer();
        mVertexBuffer.put(triangleCoords);
        mVertexBuffer.position(0);
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw (float[] mvpMatrix){

        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,12 ,mVertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, NUMBER_OF_VERTEX);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    private void initializeCircleMatrix() {

        for(int i =0; i < NUMBER_OF_VERTEX; i++){
            triangleCoords[(i * 3)] = (float) (0.5 * Math.cos((Math.PI/180) * (float)i ));
            triangleCoords[(i * 3)+ 1] = (float) (0.5 * Math.sin((Math.PI/180) * (float)i ));
            triangleCoords[(i * 3)+ 2] = 0;
        }
    }
}
