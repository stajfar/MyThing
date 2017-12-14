package it.polimi.mything;

//activity default imports
import android.app.Activity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//Peripheral Drivers

import it.polimi.cloudaccesslibrary.Component;
import it.polimi.cloudaccesslibrary.EventMessage;
import it.polimi.cloudaccesslibrary.EventProducer;
import it.polimi.cloudaccesslibrary.SensorHelper;
import it.polimi.cloudaccesslibrary.TimerHelper;
import it.polimi.myUserDrivers.Bmp180SensorDriver;
import android.util.Log;
//Cloud Access Libraries
import it.polimi.cloudaccesslibrary.MqttHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    SensorHelper temperatureSensorHelper;
    // Callback when SensorManager delivers temperatureListener sensor data.
    private SensorEventListener temperatureListener = new SensorEventListener() {
        long lastTemperatureMqttMsg=0;
        @Override
        public void onSensorChanged(SensorEvent event) {
            long now = System.currentTimeMillis();
            if (now - lastTemperatureMqttMsg > 6000) {
                lastTemperatureMqttMsg = now;

                temperatureListenerLastValue = event.values[0];
                Log.d(TAG, "sensor temperatureListener changed: " + temperatureListenerLastValue);
                Log.i("temp published 6000", temperatureListenerLastValue+"");
                temperatureSensorHelper.setLatestSensorValue(event.values[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d(TAG, "accuracy changed: " + accuracy);
        }
    };

    SensorHelper pressureSensorHelper;
    // Callback when SensorManager delivers pressureListener sensor data.
    private SensorEventListener pressureListener = new SensorEventListener() {
        long lastPressureMqttMsg=0;
        @Override
        public void onSensorChanged(SensorEvent event) {
            long now = System.currentTimeMillis();
            if (now - lastPressureMqttMsg > 3000) {
                lastPressureMqttMsg=now;
                pressureListenerLastValue = event.values[0];
                Log.d(TAG, "sensor pressureListener changed: " + pressureListenerLastValue);
                Log.i("latest pressure 3000", pressureListenerLastValue+"");
                pressureSensorHelper.setLatestSensorValue(event.values[0]);
            }
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
            MqttHelper.MqttPublisher mqttPublisherTemperature=mqtthelper.new MqttPublisher("polimi/feeds/temperature", 2, false);
            MqttHelper.MqttPublisher mqttPublisherPressure=mqtthelper.new MqttPublisher("polimi/feeds/pressure", 1, false);
            temperatureSensorHelper=new SensorHelper();
            pressureSensorHelper=new SensorHelper();
            TimerHelper timerHelper1=new TimerHelper(5000);
            TimerHelper timerHelper2=new TimerHelper(2000);





            //first flow
            List<Component> timer1NextComponents=new ArrayList<>();
            timer1NextComponents.add(temperatureSensorHelper);
            timerHelper1.addNextComponents(timer1NextComponents);


            //generate this for every component which its Wire is not empty in node-red
            List<Component> temperatureSensorNextComponents=new ArrayList<>();
            //add each component object (previously created as we travers the graph backwards) which is next to this component //.add(component)
            temperatureSensorNextComponents.add(mqttPublisherTemperature);
            //introduce to this node its recipient components(its next components)
            temperatureSensorHelper.addNextComponents(temperatureSensorNextComponents);



            //second flow
            List<Component> timer2NextComponents=new ArrayList<>();
            timer2NextComponents.add(pressureSensorHelper);
            timerHelper2.addNextComponents(timer2NextComponents);

           //wiring to previous component
            List<Component> pressureSensorNextComponents=new ArrayList<>();
            pressureSensorNextComponents.add(mqttPublisherPressure);
            pressureSensorHelper.addNextComponents(pressureSensorNextComponents);








            //trigger the timers
            timerHelper1.startTimer();
            timerHelper2.startTimer();





        } catch (IOException e) {
            e.printStackTrace();
        }


        mqtthelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.i(TAG, "MQTT connection complete");
                MqttHelper.MqttSubscriber mqttSubsriber=mqtthelper.new MqttSubscriber();
                mqttSubsriber.subscribe("polimi/feeds/temperature",1);
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

