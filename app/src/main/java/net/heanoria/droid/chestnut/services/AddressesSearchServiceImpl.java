package net.heanoria.droid.chestnut.services;

import android.util.Log;
import android.widget.Toast;

import net.heanoria.droid.chestnut.domains.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddressesSearchServiceImpl implements AddressesSearchService {

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyAvPO2YJqqLpBk3aM7D_LymzzmU3vxGqW4";

    private static final String API_ADDRESS_ARG = "description";
    private static final String API_TYPES_ARG = "types";

    private HttpURLConnection connection = null;

    public List<Address> findAddresses(String input){

        try {
            InputStreamReader googleApiResponse = requestGoogleApiUrl(input);
            StringBuilder responseParsed = parseResponse(googleApiResponse);
            JSONArray responseAsJSONArray = convertToJSONArray(responseParsed);
            return convertToList(responseAsJSONArray);
        } catch (Exception e) {
            Log.e("API", e.getMessage());
        } finally {
            if(connection != null)
                connection.disconnect();
        }
        return Collections.<Address>emptyList();
    }

    private InputStreamReader requestGoogleApiUrl(String input) throws IOException {
        StringBuilder urlStringBuilder = buildGoogleApiUrl(input);
        URL url = new URL(urlStringBuilder.toString());
        connection = (HttpURLConnection) url.openConnection();
        InputStreamReader in = new InputStreamReader(connection.getInputStream());
        return in;
    }

    private StringBuilder parseResponse(InputStreamReader googleApiReponses) throws IOException {
        StringBuilder jsonResults = new StringBuilder();
        int read;
        char[] buff = new char[1024];
        while ((read = googleApiReponses.read(buff)) != -1) {
            jsonResults.append(buff, 0, read);
        }

        return jsonResults;
    }

    private JSONArray convertToJSONArray(StringBuilder jsonStringBuilder) throws JSONException{
        JSONObject jsonObj = new JSONObject(jsonStringBuilder.toString());
        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
        return predsJsonArray;
    }

    private ArrayList<Address> convertToList(JSONArray predsJsonArray) throws JSONException{
        ArrayList<Address> addressesResults = new ArrayList<Address>(predsJsonArray.length());
        for (int i = 0; i < predsJsonArray.length(); i++) {
            Address address = new Address();
            address.setAddress(predsJsonArray.getJSONObject(i).getString(API_ADDRESS_ARG));
            address.setType(predsJsonArray.getJSONObject(i).getString(API_TYPES_ARG));
            addressesResults.add(address);
        }
        return addressesResults;
    }

    private StringBuilder buildGoogleApiUrl(String input) throws UnsupportedEncodingException{
        StringBuilder googlePlaceApiUrl = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
        googlePlaceApiUrl.append("?sensor=false&key=" + API_KEY);
        googlePlaceApiUrl.append("&components=country:fr");
        googlePlaceApiUrl.append("&input=" + URLEncoder.encode(input, "utf8"));
        return googlePlaceApiUrl;
    }
}
