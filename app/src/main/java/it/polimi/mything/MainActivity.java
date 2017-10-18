package it.polimi.mything;


//activity default imports
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import it.polimi.myUserDrivers.GpioOut;
import it.polimi.myUserDrivers.Bmp180;
import it.polimi.myUserDrivers.Pcf8591;
import it.polimi.myUserDrivers.GpioButton;
import android.util.Log;
import android.os.Handler;
import java.util.List;
import java.io.IOException;

import it.polimi.myUserDrivers.Bmp180SensorDriver;


public class MainActivity   extends Activity {

    //define constants here
    private static final String TAG = Activity.class.getSimpleName();


    private Bmp180SensorDriver bmx280sensordriver;

    private float temperatureListenerLastValue;
    private float pressureListenerLastValue;

    private SensorManager mSensorManager;



    // Callback used when we register a sensor driver with the system's SensorManager.
    private SensorManager.DynamicSensorCallback mDynamicSensorCallback
            = new SensorManager.DynamicSensorCallback() {
        @Override
        public void onDynamicSensorConnected(Sensor sensor) {
            if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                // Our sensor is connected. Start receiving  data.
                mSensorManager.registerListener(temperatureListener, sensor,
                        (int) (10.0*1000*1000) );
            }
            if (sensor.getType() == Sensor.TYPE_PRESSURE) {
                // Our sensor is connected. Start receiving  data.
                mSensorManager.registerListener(pressureListener, sensor,
                        (int) (20.0*1000*1000) );
            }

        }

        @Override
        public void onDynamicSensorDisconnected(Sensor sensor) {
            super.onDynamicSensorDisconnected(sensor);
        }
    };


    // Callback when SensorManager delivers temperatureListener sensor data.
    private SensorEventListener temperatureListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            temperatureListenerLastValue = event.values[0];
            Log.d(TAG, "temperature sensor changed: " + temperatureListenerLastValue);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d(TAG, "accuracy changed: " + accuracy);
        }
    };

    // Callback when SensorManager delivers pressureListener sensor data.
    private SensorEventListener pressureListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            pressureListenerLastValue = event.values[0];
            Log.d(TAG, "pressure sensor changed: " + pressureListenerLastValue);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d(TAG, "accuracy changed: " + accuracy);
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Started myApplication application");

        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mSensorManager.registerDynamicSensorCallback(mDynamicSensorCallback);




        try {
            bmx280sensordriver = new Bmp180SensorDriver("I2C1");

            bmx280sensordriver.registerTemperatureSensor();
            bmx280sensordriver.registerPressureSensor();

            Log.d(TAG, "Initialized bmx280sensordriver");
        } catch (IOException e) {
            throw new RuntimeException("Error initializing bmx280sensordriver", e);
        }




    }





    @Override
    protected void onStart() {
        //TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        //TODO Auto-generated method stub
        super.onResume();
    }


    @Override
    protected void onStop() {
        //TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clean up sensor registrations
        mSensorManager.unregisterListener(temperatureListener);
        mSensorManager.unregisterListener(pressureListener);


        mSensorManager.unregisterDynamicSensorCallback(mDynamicSensorCallback);

        // Clean up peripheral.
        if (bmx280sensordriver != null) {
            try {
                bmx280sensordriver.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmx280sensordriver = null;
        }
    }

}

