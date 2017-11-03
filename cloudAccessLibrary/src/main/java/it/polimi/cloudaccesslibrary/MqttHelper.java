package it.polimi.cloudaccesslibrary;

/*
 * Copyright 2016 The Android Open Source Project and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *      Benjamin Cabé <benjamin@eclipse.org> - Adapt PubSubPublisher for MQTT
 *      Modified by Saeed Tajfar
 *
 */



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.util.Log;



import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import java.io.IOException;


public class MqttHelper {
    private static final String TAG = MqttHelper.class.getSimpleName();

    private final Context mContext;
    private MqttAndroidClient mMqttAndroidClient;
    private final String MQTT_SERVER_URI ;


    /**
     * @param context use this
     * @param mqttServerUri  The MQTT serverurl
     * @param mqttServerPort The port of the server
     * @param isCleanSession set the description
     * @param keepAliveInterval This value, measured in seconds, defines the maximum time interval between messages sent or received.
     *                          It enables the client to detect if the server is no longer available, without having to wait for the TCP/IP timeout
     */
    public MqttHelper(Context context, String mqttServerUri, int mqttServerPort, boolean isCleanSession, int keepAliveInterval, String userName, String password) throws IOException {
        mContext = context;
        MQTT_SERVER_URI=mqttServerUri+":"+mqttServerPort;

        mMqttAndroidClient = new MqttAndroidClient(mContext, MQTT_SERVER_URI, MqttClient.generateClientId());
        mMqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d(TAG, "MQTT connection complete");
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "MQTT connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, "MQTT message arrived");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "MQTT delivery complete");
            }
        });


        final MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        //the connection options to the broker
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(isCleanSession);
        mqttConnectOptions.setKeepAliveInterval(keepAliveInterval);
        mqttConnectOptions.setConnectionTimeout(3);



        // Connect to the broker
        try {
            mMqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "MQTT connection connected");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "MQTT connection failure", exception);
                }
            });
        } catch (MqttException e) {
            Log.d(TAG, "MQTT connection failure", e);
        }
    }

    /**
     * @param mTopic The topic that data should be published on; for example: {username}/feeds/temperature
     * @param valueToPublish The value to be published to MQTT broker.
     * @param mqttQoS  The MQTT Quality of Service 1,2, or 3
     * @param retain Specifies that if MQTT server/broker should keep the last value or not, this is useful for the new subscribers to be informed of the latest published value
     */
    public void publish(String mTopic,String valueToPublish,int mqttQoS,boolean retain) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            Log.e(TAG, "no active network");
            return;
        }

        try {

            Log.d(TAG, "publishing message: " + valueToPublish);

            MqttMessage m = new MqttMessage();
            m.setPayload(valueToPublish.getBytes());

            m.setQos(mqttQoS);
            m.setRetained(retain);

            if (mMqttAndroidClient != null && mMqttAndroidClient.isConnected()) {
                mMqttAndroidClient.publish(mTopic, m);
            }
        } catch (MqttException e) {
            Log.e(TAG, "Error publishing message", e);
        }
    }


    public void setCallback(MqttCallbackExtended callback) {
        mMqttAndroidClient.setCallback(callback);
    }



    public void subscribeToTopic(String topic,int qos) {

            try {
                mMqttAndroidClient.subscribe(topic, qos, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("Mqtt","Subscribed!");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Subscribed fail!");
                    }
                });

            } catch (MqttException ex) {
                System.err.println("Exceptionst subscribing");
                ex.printStackTrace();
            }


    }


    public void close() {
        try {
            mMqttAndroidClient.disconnect();
        } catch (MqttException e) {
            Log.d(TAG, "error disconnecting MQTT client");
        } finally {
            mMqttAndroidClient = null;
        }
    }


}