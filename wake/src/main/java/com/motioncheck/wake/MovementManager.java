package com.motioncheck.wake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by haseeb on 29/4/17.
 */

public class MovementManager implements SensorEventListener {
    SensorManager sensorManager;
    Sensor accelerometer;
    private float vibrateThreshold = 0;
    List<Double> data = new ArrayList<>();


    public float X = 0, Y = 0, Z = 0;
    private static MovementManager ourInstance = null;
    EventCommunication eventcommunication;

    public MovementManager(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;

        }

    }

    public static MovementManager getInstance(Context context) {
        if (ourInstance == null && context != null) {
            ourInstance = new MovementManager(context);
        }
        return ourInstance;
    }


    public void register() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    public void setOnDataChanged(EventCommunication eventcommunicat) {
        this.eventcommunication = eventcommunicat;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        X = event.values[0];
        Y = event.values[1];
        Z = event.values[2];
        Float[] data = {X, Y, Z};
        double accelleration = Math.sqrt(X * X + Y * Y + Z * Z);
        Operation(accelleration,Z);


    }

    private void Operation(double accelleration, float Z) {
        if (data.size() == 25) {
            CheckMovement(data, Z);
            data.clear();
            data.add(accelleration);
        } else {
            data.add(accelleration);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void CheckMovement(List<Double> data, Float Z) {
        Double min = Collections.min(data);
        Double max = Collections.max(data);
        Double diff = max - min;
        String statusText = null;
        if (diff > 12) {
            if (Z > 25){
                statusText = "Jumping";
            }
            else {
                statusText = "Running";
            }
        } else if (diff > 3 && diff < 10) {
            statusText = "Walking";
        }
//        else if (diff < 3 && diff > 2){
//            statusText = "Moving";
//        }
        else if (diff < 2 && diff > 0.1) {
            statusText = "Idle";
        } else if (diff <= 0.1) {
            statusText = "Phone Detached";
        }
        String text = "Min : "+min +" , Max : "+max;
        eventcommunication.getValues(statusText, diff.toString() +" , "+Z);

    }


}
