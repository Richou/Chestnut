package net.heanoria.droid.chestnut.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import net.heanoria.droid.chestnut.R;
import net.heanoria.droid.chestnut.listeners.OnTargetDirectionChangedListener;
import net.heanoria.droid.chestnut.listeners.SensorAndLocationListener;
import net.heanoria.droid.chestnut.tasks.PositionTask;

public class PositionActivity extends Activity implements OnTargetDirectionChangedListener {

    public static final String ADDRESS_EXTRA = "address";

    private ImageView compassArrow = null;

    private PositionTask positionTask = null;
    private LocationManager locationManager = null;
    private SensorManager sensorManager = null;
    private Sensor sensorAccelerometer = null;
    private Sensor sensorMagneticField = null;

    private SensorAndLocationListener sensorAndLocationListener = null;

    private Double oldBearing = 0D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sensorAndLocationListener = new SensorAndLocationListener();
        sensorAndLocationListener.setOnTargetDirectionChangedListener(this);

        String address = getIntent().getStringExtra(ADDRESS_EXTRA);
        positionTask = new PositionTask(this);
        positionTask.setOnLocationTaskFinishedListener(sensorAndLocationListener);
        positionTask.execute(address);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, sensorAndLocationListener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        compassArrow = (ImageView) findViewById(R.id.compassArrow);

    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(sensorAndLocationListener, sensorAccelerometer, SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        sensorManager.registerListener(sensorAndLocationListener, sensorMagneticField, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorAndLocationListener, sensorAccelerometer);
        sensorManager.unregisterListener(sensorAndLocationListener, sensorMagneticField);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        positionTask.cancel(true);
        locationManager.removeUpdates(sensorAndLocationListener);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        getMenuInflater().inflate(R.menu.location, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return (id == R.id.action_settings) || (super.onOptionsItemSelected(item));
    }

    @Override
    public void onTargetDirectionChanged(Double targetBearing) {

        Log.i("PositionActivity", "fromDegrees : " + Math.round(oldBearing.floatValue()) + ", toDegrees : " + Math.round(-(targetBearing.floatValue())));
        RotateAnimation rotation = new RotateAnimation(Math.round(oldBearing.floatValue()),
                Math.round(-(targetBearing.floatValue())), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotation.setDuration(500);
        rotation.setFillEnabled(true);
        rotation.setFillAfter(true);
        compassArrow.startAnimation(rotation);
        oldBearing = -targetBearing;
    }

    @Override
    public void onTargetDistanceChanged(Double distance) {
        TextView oldTextView = (TextView) findViewById(R.id.bearingTextView);
        oldTextView.setText("Distance : " + distance);
    }
}
