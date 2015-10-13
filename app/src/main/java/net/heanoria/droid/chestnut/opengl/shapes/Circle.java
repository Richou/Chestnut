package net.heanoria.droid.chestnut.opengl.shapes;

public class Circle extends AbstractShape {

    private static int NUMBER_OF_VERTEX = 360;

    private float[] color;

    public Circle() {
        super();
    }

    @Override
    public float[] getShapeCoords() {
        float[] circleCoords = new float[NUMBER_OF_VERTEX * COORDS_PER_VERTEX];

        for(int i =0; i < NUMBER_OF_VERTEX; i++){
            circleCoords[(i * 3)] = (float) (0.5 * Math.cos((Math.PI/180) * (float)i ));
            circleCoords[(i * 3)+ 1] = (float) (0.5 * Math.sin((Math.PI/180) * (float)i ));
            circleCoords[(i * 3)+ 2] = 0;
        }

        return circleCoords;
    }

    @Override
    public int getNumberVertex() {
        return NUMBER_OF_VERTEX;
    }

    @Override
    public float[] getColor() {
        return this.color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }
}
