package net.heanoria.droid.chestnut.services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import net.heanoria.droid.chestnut.domains.Position;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocationServiceImpl implements GeoLocationService{

    private Geocoder geocoder = null;

    public GeoLocationServiceImpl(Context context){
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public Position computeLocationsFromAddress(String address) throws IOException {
        Position position = new Position();

        List<Address> addresses = geocoder.getFromLocationName(address, 1);

        if (addresses.size() > 0){

            position.latitude = addresses.get(0).getLatitude();
            position.longitude = addresses.get(0).getLongitude();
        }
        return position;
    }
}
