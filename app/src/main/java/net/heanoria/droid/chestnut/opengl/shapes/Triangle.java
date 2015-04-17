package net.heanoria.droid.chestnut.opengl.shapes;

public class Triangle extends AbstractShape{

    private Float[] coords = null;

    public Triangle() {
        super();
    }

    @Override
    public void draw(float[] matrix) {

    }

    @Override
    public float[] getShapeCoords() {
        float[] triangleCoords = {
                // in counterclockwise order:
                0.0f,  0.622008459f, 0.0f,   // top
                -0.5f, -0.311004243f, 0.0f,   // bottom left
                0.5f, -0.311004243f, 0.0f    // bottom right
        };
        return triangleCoords;
    }

    @Override
    public int getNumberVertex() {
        return 0;
    }

    @Override
    public float[] getColor() {
        return new float[0];
    }
}
