package it.polimi.mything;

//activity default imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import it.polimi.myUserDrivers.Bmp180;
import it.polimi.myUserDrivers.I2cAdc;
import it.polimi.myUserDrivers.BlinkLED;
import it.polimi.myUserDrivers.BlinkLED;
import android.util.Log;
import android.os.Handler;
import java.util.List;
import java.io.IOException;




public class MainActivity extends AppCompatActivity  {

    //define constants here
    private static final String TAG = AppCompatActivity.class.getSimpleName();

    private Bmp180 bmp180temperature01;
    private static final int bmp180temperature01_INTERVAL = 1000;
    private Handler bmp180temperature01Handler = new Handler();
    private I2cAdc i2cadclight01;
    private static final int i2cadclight01_INTERVAL = 6000;
    private Handler i2cadclight01Handler = new Handler();

    private BlinkLED blinkledon_off01;
    private static final int blinkledon_off01_INTERVAL = 2000;
    private Handler blinkledon_off01Handler = new Handler();
    private BlinkLED blinkledon_off02;
    private static final int blinkledon_off02_INTERVAL = 6000;
    private Handler blinkledon_off02Handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        List<String> i2cBoardListBmp180temperature01 = Bmp180.getBusList();
        String Bmp180temperature01I2cPin = i2cBoardListBmp180temperature01.get(0);
        bmp180temperature01 = Bmp180.createBmp180(Bmp180temperature01I2cPin);
        bmp180temperature01Handler.post(bmp180temperature01HandlerRunnable);

        List<String> i2cBoardListI2cAdclight01 = I2cAdc.getBusList();
        String I2cAdclight01I2cPin = i2cBoardListI2cAdclight01.get(0);
        i2cadclight01 = I2cAdc.createI2cAdc(I2cAdclight01I2cPin);
        i2cadclight01Handler.post(i2cadclight01HandlerRunnable);



        List<String> gpioBoardListBlinkLEDon_off01 = BlinkLED.getBusList();
        String BlinkLEDon_off01GpioPin = gpioBoardListBlinkLEDon_off01.get(1);
        blinkledon_off01 = BlinkLED.createBlinkLED(BlinkLEDon_off01GpioPin);
        blinkledon_off01Handler.post(blinkledon_off01HandlerRunnable);
        List<String> gpioBoardListBlinkLEDon_off02 = BlinkLED.getBusList();
        String BlinkLEDon_off02GpioPin = gpioBoardListBlinkLEDon_off02.get(0);
        blinkledon_off02 = BlinkLED.createBlinkLED(BlinkLEDon_off02GpioPin);
        blinkledon_off02Handler.post(blinkledon_off02HandlerRunnable);

    }



    private Runnable bmp180temperature01HandlerRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                if (bmp180temperature01 != null) {
                    //reset the handler
                    bmp180temperature01Handler.postDelayed(bmp180temperature01HandlerRunnable, bmp180temperature01_INTERVAL);
                    float readPressure = bmp180temperature01.readPressure();
                    Log.i(TAG, "readPressuretemperature01: " + readPressure );
                    float readTemperature = bmp180temperature01.readTemperature();
                    Log.i(TAG, "readTemperaturetemperature01: " + readTemperature );
                    //TODO to be extented by the developer

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable i2cadclight01HandlerRunnable = new Runnable() {
        @Override
        public void run() {

            if (i2cadclight01 != null) {
                //reset the handler
                i2cadclight01Handler.postDelayed(i2cadclight01HandlerRunnable, i2cadclight01_INTERVAL);
                float readChannel = i2cadclight01.readChannel(0);
                Log.i(TAG, "readChannellight01: " + readChannel );
                //TODO to be extented by the developer

            }
        }
    };


    private Runnable blinkledon_off01HandlerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (blinkledon_off01 != null) {
                    //reset the handler
                    blinkledon_off01Handler.postDelayed(blinkledon_off01HandlerRunnable, blinkledon_off01_INTERVAL);
                    blinkledon_off01.toggleGpio();
                    Log.i(TAG, "toggleGpio: Executed."  );

                    //to be extented by the developer
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable blinkledon_off02HandlerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (blinkledon_off02 != null) {
                    //reset the handler
                    blinkledon_off02Handler.postDelayed(blinkledon_off02HandlerRunnable, blinkledon_off02_INTERVAL);
                    blinkledon_off02.toggleGpio();
                    Log.i(TAG, "toggleGpio: Executed."  );

                    //to be extented by the developer
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
            bmp180temperature01Handler.removeCallbacks(bmp180temperature01HandlerRunnable);
            bmp180temperature01Handler.removeCallbacks(bmp180temperature01HandlerRunnable);
            bmp180temperature01.close();
            bmp180temperature01 = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        i2cadclight01Handler.removeCallbacks(i2cadclight01HandlerRunnable);
        i2cadclight01Handler.removeCallbacks(i2cadclight01HandlerRunnable);
        i2cadclight01.close();
        i2cadclight01 = null;
        try {
            blinkledon_off01Handler.removeCallbacks(blinkledon_off01HandlerRunnable);
            blinkledon_off01.close();
            blinkledon_off01 = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            blinkledon_off02Handler.removeCallbacks(blinkledon_off02HandlerRunnable);
            blinkledon_off02.close();
            blinkledon_off02 = null;
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
