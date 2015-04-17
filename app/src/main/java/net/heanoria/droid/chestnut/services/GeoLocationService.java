package net.heanoria.droid.chestnut.services;

import net.heanoria.droid.chestnut.domains.Position;

import java.io.IOException;

public interface GeoLocationService {
    public Position computeLocationsFromAddress(String address) throws IOException;
}
