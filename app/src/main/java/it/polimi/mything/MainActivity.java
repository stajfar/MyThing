package it.polimi.mything;

//activity default imports
import android.app.Activity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//Peripheral Drivers
import it.polimi.myUserDrivers.Bmp180SensorDriver;
import android.util.Log;
//Cloud Access Libraries
import it.polimi.cloudaccesslibrary.MqttHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
import java.io.IOException;


public class MainActivity extends Activity  {

    //define constants here
    private static final String TAG = Activity.class.getSimpleName();


    private Bmp180SensorDriver bmp180sensordriver;

    private float temperatureListenerLastValue;
    private float pressureListenerLastValue;

    private SensorManager mSensorManager;
    private  MqttHelper mqtthelper;






    // Callback used when we register a sensor driver with the system's SensorManager.
    private SensorManager.DynamicSensorCallback mDynamicSensorCallback
            = new SensorManager.DynamicSensorCallback() {
        @Override
        public void onDynamicSensorConnected(Sensor sensor) {
            if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                // Our sensor is connected. Start receiving  data.
                mSensorManager.registerListener(temperatureListener, sensor,
                        (int) (6.0*1000000) );
            }
            if (sensor.getType() == Sensor.TYPE_PRESSURE) {
                // Our sensor is connected. Start receiving  data.
                mSensorManager.registerListener(pressureListener, sensor,
                        (int) (3.0*1000000) );
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
            Log.d(TAG, "sensor temperatureListener changed: " + temperatureListenerLastValue);
            mqtthelper.publish("polimi/feeds/temperature",Float.toString(temperatureListenerLastValue),2,false);

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
            Log.d(TAG, "sensor pressureListener changed: " + pressureListenerLastValue);
           // mqtthelper.publish("polimi/feeds/pressure",Float.toString(pressureListenerLastValue),1,false);

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
            bmp180sensordriver = new Bmp180SensorDriver("I2C1");

            bmp180sensordriver.registerTemperatureSensor();
            bmp180sensordriver. registerPressureSensor();

            Log.d(TAG, "Initialized bmp180sensordriver");
        } catch (IOException e) {
            throw new RuntimeException("Error initializing bmp180sensordriver", e);
        }

        try {
            //// TODO: developer should add username and password if Mqtt broker asks for.
            mqtthelper = new MqttHelper(this,"tcp://io.adafruit.com",1883,true,60,"polimi","4add1af7a8024e1eafcf73103cc1db3a");
        } catch (IOException e) {
            e.printStackTrace();
        }


        //mqqtTopicSubscribe();
        // private void mqqtTopicSubscribe(){
        mqtthelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.i(TAG, "MQTT connection complete");
                mqtthelper.subscribeToTopic("polimi/feeds/temperature",1);
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(TAG, "a new message arrived from topic: "+topic+" "+ message.toString());


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "MQTT delivery complete");

            }
        });
        // }







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
        if (bmp180sensordriver != null) {
            try {
                bmp180sensordriver.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmp180sensordriver = null;
        }

        //clean up Cloud Access
        mqtthelper.close();

    }

}

