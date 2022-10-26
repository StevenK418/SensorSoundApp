package com.example.sensorsoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{

    TextView tvx, tvy, tvz;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean isFlat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get references to the Textviews
        tvx = findViewById(R.id.tvxval);
        tvy = findViewById(R.id.tvyval);
        tvz = findViewById(R.id.tvzval);

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


        DetectWhenFlat(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not using
    }

    //Detects when the phone is on a flat surface
    public void DetectWhenFlat(SensorEvent event)
    {
        float x,y,z;

        //Detect all non minus values
        x = Math.abs(event.values[0]);
        y = Math.abs(event.values[1]);
        z = Math.abs(event.values[2]);

        //Check if phone is flat using the sense events
        if (x < 1 && y< 1 && z > 9)
        {
            if(isFlat == false)
            {
                isFlat = true;
                //Print some feedback to user
                Toast.makeText(this, "Phone is currently flat", Toast.LENGTH_SHORT).show();
            }
        }
    }
}