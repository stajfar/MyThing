package it.polimi.cloudaccesslibrary;

import android.util.Base64;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by saeed on 5/8/2017.
 */

public class OneM2mPost {

    public static String POST(float temp, float pressure, float hight, float light, String serverUrl) {


        URL url = null;
        HttpURLConnection client = null;

        try {
            url = new URL(serverUrl);
            client = (HttpURLConnection) url.openConnection();

            String encoded = Base64.encodeToString(("jolscube"+":"+"jol_scube").getBytes(StandardCharsets.UTF_8),Base64.NO_WRAP);
            client.setRequestProperty("Authorization",encoded);

            client.setRequestMethod("POST");
            client.setDoOutput(true);


            client.setRequestProperty("Content-Type","application/vnd.onem2m-res+json;ty=4");
            client.setRequestProperty("X-M2M-Origin", "jolmilano_prod");
            client.setRequestProperty("X-M2M-RI",String.valueOf(Math.random()));
            client.connect();




            JSONArray arrayLbl=new JSONArray();
            arrayLbl.put("C");
            arrayLbl.put("Lum");
            arrayLbl.put("Pascal");
            arrayLbl.put("Meter");

            JSONObject con2=new JSONObject();
            con2.put("temperature",temp);
            con2.put("light",light);
            con2.put("pressure",pressure);
            con2.put("altitude",hight);

            JSONObject obj = new JSONObject();
            obj.put("con",con2);
            obj.put("cnf","application/json:0");
            obj.put("lbl",arrayLbl);
            JSONObject m2m_cin=new JSONObject();
            m2m_cin.put("m2m:cin",obj);


          String jsonParam=  m2m_cin.toString();


            OutputStreamWriter wr= new OutputStreamWriter(client.getOutputStream());
            wr.write(jsonParam);
            wr.flush();


            //display what returns the POST request
            int HttpResult = client.getResponseCode();
            Log.i(TAG,client.getResponseMessage());
            if (HttpResult == 201) {
                Log.i(TAG,client.getResponseMessage()+" Record added to One-M2M");
            } else {
                Log.i(TAG,client.getResponseMessage()+" Record NOT added to One-M2M");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }

      return null;
    }




    public static String GET(String serverUrl) {


        URL url = null;
        HttpURLConnection client = null;

        try {
            url = new URL(serverUrl);
            client = (HttpURLConnection) url.openConnection();

            String encoded = Base64.encodeToString(("jolscube"+":"+"jol_scube").getBytes(StandardCharsets.UTF_8),Base64.NO_WRAP);
            client.setRequestProperty("Authorization", "Basic "+encoded);

            //String user = Base64.encodeToString(("jolscube").getBytes(StandardCharsets.UTF_8),Base64.NO_WRAP);
            //String password = Base64.encodeToString(("jol_scube").getBytes(StandardCharsets.UTF_8),Base64.NO_WRAP);
            //client.setRequestProperty("user", user);
           // client.setRequestProperty("password", password);

            client.setRequestMethod("GET");


            //client.setRequestProperty("Content-Type","application/vnd.onem2m-res+json; ty=4");
            client.setRequestProperty("X-M2M-Origin", "jolmilano_cons");
            client.setRequestProperty("X-M2M-RI",String.valueOf(Math.random()));
            client.setRequestProperty("Accept", "application/json");



            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println(response.toString());
            Log.i("TAG", response.toString());





        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }



        return null;
    }
}

