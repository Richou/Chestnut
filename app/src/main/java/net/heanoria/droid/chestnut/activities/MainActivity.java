package net.heanoria.droid.chestnut.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import net.heanoria.droid.chestnut.R;
import net.heanoria.droid.chestnut.adapters.LocationHistoryAdapter;
import net.heanoria.droid.chestnut.adapters.PlaceAutoCompleteAdapter;
import net.heanoria.droid.chestnut.domains.LocationHistory;
import net.heanoria.droid.chestnut.fragments.SearchLocationFragment;
import net.heanoria.droid.chestnut.fragments.SearchLocationHistoryFragment;
import net.heanoria.droid.chestnut.listeners.OnLocationSelectedListener;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity implements OnLocationSelectedListener{

    private final String TAG = MainActivity.class.getName();

    private List<LocationHistory> locationHistory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        SearchLocationHistoryFragment searchLocationHistoryFragment = (SearchLocationHistoryFragment) fragmentManager.findFragmentById(R.id.searchLocationHistoryFragment);
        SearchLocationFragment searchLocationFragment = (SearchLocationFragment) fragmentManager.findFragmentById(R.id.searchLocationFragment);

        locationHistory = new LinkedList<LocationHistory>();

        LocationHistoryAdapter adapter = new LocationHistoryAdapter(this, R.layout.location_history_row, locationHistory);
        PlaceAutoCompleteAdapter autoCompleteAdapter = new PlaceAutoCompleteAdapter(this, R.layout.address_item);

        searchLocationHistoryFragment.setListAdapter(adapter);
        searchLocationFragment.setAutoCompleteAdapter(autoCompleteAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.action_test == item.getItemId()) {
            launchTestActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationSelected(String location) {
        saveLocationToHsitoryIfNotExists(bakeLocationHistory(location));
        Intent locationIntent = new Intent(MainActivity.this, PositionActivity.class);
        locationIntent.putExtra(PositionActivity.ADDRESS_EXTRA, location);

        startActivity(locationIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO : Sauvegarder l'historique dans le cache.
    }

    private void launchTestActivity() {
        Intent testIntent = new Intent(MainActivity.this, TestanceActivity.class);
        startActivity(testIntent);
    }

    private LocationHistory bakeLocationHistory(String location) {
        LocationHistory locationHistory = LocationHistory.emptyLocationHistory();
        locationHistory.location = location;
        locationHistory.dateRequest = new Date(System.currentTimeMillis());
        return locationHistory;
    }

    private void saveLocationToHsitoryIfNotExists(LocationHistory location) {
        if(!locationHistory.contains(location)) {
            locationHistory.add(0, location);
        } else {
            locationHistory.remove(locationHistory.indexOf(location));
            locationHistory.add(0, location);
        }
    }

    private void freeLocationHistory() {
        // TODO : Gerer un maximum dans la liste
    }
}
