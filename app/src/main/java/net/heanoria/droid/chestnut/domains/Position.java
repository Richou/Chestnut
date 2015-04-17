package net.heanoria.droid.chestnut.domains;

public class Position {

    public double latitude;
    public double longitude;

    @Override
    public String toString() {
        return "latitude : " + latitude + ", longitude : " + longitude;
    }

    public static Position emptyPosition(){
        return new Position();
    }
}
