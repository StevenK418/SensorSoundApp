package com.example.sensorsoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    TextView tvx, tvy, tvz;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean isFlat = false;
    View appWindow;

    //Create a new mediaSource for the sound effect
    private MediaPlayer player;

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

        //Initialize the mediaplayer here to avoid crashes.
        player = MediaPlayer.create(this, R.raw.soundfile);

        //Disable the looping behaviour
        player.setLooping(false);

        //Get a reference to the app window
        appWindow = findViewById(R.id.appWindow);
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

        //Feed the values of the axes into the variables
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        //Assign the values to the textviews for feedback
        tvx.setText(String.valueOf(x));
        tvy.setText(String.valueOf(y));
        tvz.setText(String.valueOf(z));

        //Call the detect method
        DetectWhenFlat(event, x,y,z);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not using
    }

    //Detects when the phone is on a flat surface
    public void DetectWhenFlat(SensorEvent event, float x, float y, float z)
    {
        //Detect all non minus values
        x = Math.abs(event.values[0]);
        y = Math.abs(event.values[1]);
        z = Math.abs(event.values[2]);

        //Check if phone is flat using the sense events
        if (x < 1 && y < 1 && z > 9)
        {
            //Change the colour of teh background to indicate it's flat
            appWindow.setBackgroundColor(getColor(R.color.purple_200));

            //Print some feedback to user
            Toast.makeText(this, "Phone is currently flat", Toast.LENGTH_SHORT).show();

            if(isFlat == false)
            {
                isFlat = true;

                //Check if audio is playing
                if(player.isPlaying())
                {
                    //Stop playing the sound effect
                    player.stop();
                }
            }

        }
        else
        {
            //Play the sound effect when the phone is picked up
            player.start();
            appWindow.setBackgroundColor(getColor(R.color.white));
        }
    }


    public void doReset(View view)
    {
        //Call the Reset method.
        Reset();
    }

    //Resets isFlat to true to reCalibrate
    public void Reset()
    {
        isFlat = true;
    }
}