package com.example.sensorsoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView tvx, tvy, tvz;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvx = findViewById(R.id.tvxval);  // this assumes there are three textviews
        tvy = findViewById(R.id.tvyval);  // in your xml file called tvxval, tvyval
        tvz = findViewById(R.id.tvzval);  // and tvzval

        // choose the sensor you want
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }
    /*
     * When the app is brought to the foreground - using app on screen
     */
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }

    /*
     * Called by the system every x milllisecs when sensor changes
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // called byt the system every x ms
        float x, y, z;

        x = event.values[0];    // get x value from sensor
        y = event.values[1];
        z = event.values[2];

        tvx.setText(String.valueOf(x));
        tvy.setText(String.valueOf(y));
        tvz.setText(String.valueOf(z));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not using
    }
}