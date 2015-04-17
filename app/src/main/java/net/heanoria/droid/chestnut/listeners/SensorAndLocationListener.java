package net.heanoria.droid.chestnut.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import net.heanoria.droid.chestnut.domains.Position;

public class SensorAndLocationListener implements LocationListener, SensorEventListener, OnPositionTaskFinishedListener {

    static final float ALPHA = 0.5f;

    private OnTargetDirectionChangedListener listener = null;

    private Double targetAngle = null;
    private Position targetPosition = null;

    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;

    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;

    private Long oldTimestamp = null;
    private Double oldBearing = null;

    private final int ROTATION_OFFSET = 500;
    private final int ROTATION_TOLERANCE = 3;

    public SensorAndLocationListener() {

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];

        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];

        oldTimestamp = System.currentTimeMillis();
        oldBearing = 0d;
    }

    @Override
    public void onPositionTaskFinishedListener(Position computedPosition) {
        targetPosition = computedPosition;
    }

    @Override
    public void onLocationChanged(Location location) {
        Position currentPosition = new Position();
        currentPosition.latitude = location.getLatitude();
        currentPosition.longitude = location.getLongitude();

        if(targetPosition != null) {
            Double distance = computeDistance(currentPosition, targetPosition);
            targetAngle = computeAngle(currentPosition, targetPosition);

            listener.onTargetDistanceChanged(distance);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        initializeSensorValues(event);

        if(valuesAccelerometer != null && valuesMagneticField != null) {

            SensorManager.getRotationMatrix(matrixR, matrixI, valuesAccelerometer, valuesMagneticField);
            SensorManager.getOrientation(matrixR, matrixValues);

            Double deviceBearing = Math.toDegrees(matrixValues[0]);

            if(null != targetAngle) {
                if(!isRotationOffsetReached())
                    return;

                Double arrowBearing = computeArrowAngle(deviceBearing, targetAngle);

                if(!isRotationToleranceReached(arrowBearing))
                    return;

                listener.onTargetDirectionChanged(arrowBearing);

            }
        }
    }

    protected float[] lowPass( float[] input, float[] oldValues ) {
        if ( oldValues == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            oldValues[i] = oldValues[i] + ALPHA * (input[i] - oldValues[i]);
        }
        return oldValues;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setOnTargetDirectionChangedListener(OnTargetDirectionChangedListener listener) {
        this.listener = listener;
    }

    private void initializeSensorValues(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                valuesAccelerometer = lowPass(event.values.clone(), valuesAccelerometer);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                valuesMagneticField = lowPass(event.values.clone(), valuesMagneticField);
                break;
        }
    }

    private boolean isRotationOffsetReached() {
        Long currentTime = System.currentTimeMillis();

        if(currentTime - oldTimestamp < ROTATION_OFFSET) {
            return false;
        } else {
            oldTimestamp = currentTime;
            return true;
        }
    }

    private boolean isRotationToleranceReached(Double currentBearing) {
        Double delta = Math.abs(Math.abs(oldBearing) - Math.abs(currentBearing));

        if(delta < ROTATION_TOLERANCE) {
            return false;
        } else {
            oldBearing = -currentBearing;
            return true;
        }
    }

    private Double computeDistance(Position fromPosition, Position targetPosition) {
        Double earthRadius = 6371D;
        Double deltaLatitude = Math.toRadians(targetPosition.latitude - fromPosition.latitude);
        Double deltaLongitude = Math.toRadians(targetPosition.longitude - fromPosition.longitude);

        Double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                + Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2) * Math.cos(Math.toRadians(fromPosition.latitude)) * Math.cos(Math.toRadians(targetPosition.latitude));
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    private Double computeAngle(Position fromPosition, Position targetPosition) {
        Double deltaLongitude = Math.toRadians(targetPosition.longitude - fromPosition.longitude);

        Double y = Math.sin(deltaLongitude) * Math.cos(Math.toRadians(targetPosition.latitude));
        Double x = Math.cos(Math.toRadians(fromPosition.latitude)) * Math.sin(Math.toRadians(targetPosition.latitude))
                - Math.sin(Math.toRadians(fromPosition.latitude)) * Math.cos(Math.toRadians(targetPosition.latitude)) * Math.cos(deltaLongitude);

        return Math.toDegrees(Math.atan2(y, x));
    }

    private Double computeArrowAngle(Double deviceBearing, Double targetBearing) {
        return (deviceBearing - targetBearing) % 360 ;
    }

}
