package net.heanoria.droid.chestnut.adapters;

import static net.heanoria.droid.chestnut.utils.AddressType.getAddressTypeDrawable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.heanoria.droid.chestnut.R;
import net.heanoria.droid.chestnut.domains.Address;
import net.heanoria.droid.chestnut.services.AddressesSearchService;
import net.heanoria.droid.chestnut.services.AddressesSearchServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class PlaceAutoCompleteAdapter extends ArrayAdapter<Address> implements Filterable {
    private static String TAG = "PlaceAutoCompleteAdapter";
    private List<Address> resultList = null;
    private AddressesSearchService addressesSearchService = null;

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Address getItem(int index) {
        return resultList.get(index);
    }

    public PlaceAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        resultList = new ArrayList<>();
        addressesSearchService = new AddressesSearchServiceImpl();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.address_item, parent, false);
        }

        Address currentAddress = getItem(position);
        if(currentAddress != null) {
            TextView addressTextView = (TextView) view.findViewById(R.id.addressSearchTextView);

            Log.i(TAG, "address type : " + currentAddress.getType());
            addressTextView.setText(currentAddress.getAddress());
            addressTextView.setCompoundDrawables(getAddressTypeDrawable(view.getResources(), R.drawable.rail), null, null, null);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = addressesSearchService.findAddresses(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
    }
}

