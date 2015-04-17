package net.heanoria.droid.chestnut.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import net.heanoria.droid.chestnut.listeners.OnLocationSelectedListener;

public class SearchLocationHistoryFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private OnLocationSelectedListener onLocationSelectedListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onLocationSelectedListener = (OnLocationSelectedListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnLocationSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedAddress = (String) parent.getItemAtPosition(position);
        onLocationSelectedListener.onLocationSelected(selectedAddress);
    }
}
