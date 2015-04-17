package net.heanoria.droid.chestnut.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.heanoria.droid.chestnut.domains.Position;
import net.heanoria.droid.chestnut.listeners.OnPositionTaskFinishedListener;
import net.heanoria.droid.chestnut.services.GeoLocationService;
import net.heanoria.droid.chestnut.services.GeoLocationServiceImpl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PositionTask extends AsyncTask<String, Void, Position>{

    private GeoLocationService geoLocationService = null;
    private List<OnPositionTaskFinishedListener> listeners = null;

    public PositionTask(Context context){
        geoLocationService = new GeoLocationServiceImpl(context);
    }

    @Override
    protected Position doInBackground(String... addresses) {
        assert addresses != null : "The addresses list must not be null";
        try{
            if(addresses.length > 0){
                return geoLocationService.computeLocationsFromAddress(addresses[0]);
            }
        }catch(IOException ex){
            Log.e("POSITION_TASK", ex.getMessage());
        }
        return Position.emptyPosition();
    }

    @Override
    protected void onPostExecute(Position position) {
        for(OnPositionTaskFinishedListener listener : listeners) {
            listener.onPositionTaskFinishedListener(position);
        }
    }

    public void setOnLocationTaskFinishedListener(OnPositionTaskFinishedListener listener) {
        if(listeners == null)
            listeners = new LinkedList<OnPositionTaskFinishedListener>();

        this.listeners.add(listener);
    }

}
