package net.heanoria.droid.chestnut.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import net.heanoria.droid.chestnut.R;
import net.heanoria.droid.chestnut.adapters.PlaceAutoCompleteAdapter;
import net.heanoria.droid.chestnut.components.ClearableAutocompleteTextView;
import net.heanoria.droid.chestnut.domains.Address;
import net.heanoria.droid.chestnut.listeners.OnLocationSelectedListener;

public class SearchLocationFragment extends Fragment implements AdapterView.OnItemClickListener, TextWatcher {

    private OnLocationSelectedListener onLocationSelectedListener = null;
    private ClearableAutocompleteTextView autoCompleteTextView = null;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_location_fragment, container, false);

        autoCompleteTextView = (ClearableAutocompleteTextView) view.findViewById(R.id.locationTextView);
        autoCompleteTextView.setOnItemClickListener(this);
        autoCompleteTextView.addTextChangedListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Address selectedAddress = (Address) parent.getItemAtPosition(position);
        onLocationSelectedListener.onLocationSelected(selectedAddress.getAddress());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        autoCompleteTextView.clearButtonHandler();
        if (autoCompleteTextView.isJustCleared()){
            autoCompleteTextView.setJustCleared(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setAutoCompleteAdapter(ArrayAdapter adapter) {
        autoCompleteTextView.setAdapter(adapter);
    }
}
