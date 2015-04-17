package net.heanoria.droid.chestnut.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import net.heanoria.droid.chestnut.R;
import net.heanoria.droid.chestnut.domains.LocationHistory;

import java.util.List;

public class LocationHistoryAdapter extends ArrayAdapter<LocationHistory> {

    private List<LocationHistory> locationHistories = null;

    public LocationHistoryAdapter(Context context, int resource, List<LocationHistory> items) {
        super(context, resource, items);
        this.locationHistories = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.location_history_row, null);
        }

        LocationHistory locationHistory = locationHistories.get(position);

        if(locationHistory != null) {
            // TODO fill layout stuff...
        }

        return view;
    }
}
