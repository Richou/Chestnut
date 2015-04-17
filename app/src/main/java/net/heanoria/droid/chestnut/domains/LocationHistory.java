package net.heanoria.droid.chestnut.domains;

import java.util.Date;

public class LocationHistory {

    public String location = null;
    public Date dateRequest = null;

    public String toString() {
        return "location : " + location + ", requested : " + dateRequest.toString();
    }

    public static LocationHistory emptyLocationHistory() {
        return new LocationHistory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationHistory)) return false;

        LocationHistory that = (LocationHistory) o;

        return (!location.equals(that.location));
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
}
