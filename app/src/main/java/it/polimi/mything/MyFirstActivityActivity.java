package it.polimi.mything;

/**
 * Created by saeed on 6/15/2017.
 */


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import it.polimi.myUserDrivers.Bmp180;
import android.util.Log;
import android.os.Handler;
import java.util.List;
import java.io.IOException;




import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import it.polimi.myUserDrivers.Bmp180;
import android.util.Log;
import android.os.Handler;
import java.util.List;
import java.io.IOException;




public class MyFirstActivityActivity extends AppCompatActivity  {

    //define constants here
    private static final String TAG = AppCompatActivity.class.getSimpleName();

    private Bmp180 bmp180;
    private static final int bmp180_INTERVAL = 1000;
    private Handler bmp180Handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> i2cBoardList = Bmp180.getBusList();
        String firstI2cPin = i2cBoardList.get(0);
        bmp180 = Bmp180.createBmp180(firstI2cPin);
        bmp180Handler.post(bmp180HandlerRunnable);

    }



    private Runnable bmp180HandlerRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                if (bmp180 != null) {
                    //reset the handler

                    bmp180Handler.postDelayed(bmp180HandlerRunnable, bmp180_INTERVAL);

                    float temp = bmp180.readTemperature();
                    float pressure = bmp180.readPressure();
                    float attitude = bmp180.readAltitude();



                    Log.i(TAG, "Current Temp: " + temp + " pressure: " + pressure + " attitude: " + attitude);
                    //to be extented by the developers


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };








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
    protected void onPause() {
        //TODO Auto-generated method stub
        super.onPause();
        try {
            bmp180Handler.removeCallbacks(bmp180HandlerRunnable);
            bmp180.close();
            bmp180 = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onStop() {
        //TODO Auto-generated method stub
        super.onStop();
    }
}