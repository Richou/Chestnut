package net.heanoria.droid.chestnut.services;

import net.heanoria.droid.chestnut.domains.Address;

import java.util.ArrayList;
import java.util.List;

public interface AddressesSearchService {
    public List<Address> findAddresses(String input);

}
