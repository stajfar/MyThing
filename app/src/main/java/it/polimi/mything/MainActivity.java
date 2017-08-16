package it.polimi.mything;

//activity default imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import it.polimi.myUserDrivers.GpioOut;
import it.polimi.myUserDrivers.Pcf8591;
import it.polimi.myUserDrivers.GpioButton;
import it.polimi.myUserDrivers.Bmp180;
import android.util.Log;
import android.os.Handler;
import java.util.List;
import java.io.IOException;




public class MainActivity extends AppCompatActivity  {

    //define constants here
    private static final String TAG = AppCompatActivity.class.getSimpleName();

    private GpioOut gpioout;
    private static final int led1_INTERVAL = 1000;
    private Handler led1Handler = new Handler();
    private Pcf8591 pcf8591;
    private GpioButton gpiobutton;
    private Bmp180 bmp180;
    private static final int pressureSensor_INTERVAL = 2000;
    private Handler pressureSensorHandler = new Handler();
    private static final int temperatureSensor_INTERVAL = 1000;
    private Handler temperatureSensorHandler = new Handler();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gpioout = new GpioOut("BCM12");
        led1Handler.post(led1HandlerRunnable);
        pcf8591 = new Pcf8591("I2C1");
        gpiobutton = new GpioButton("BCM26");
        bmp180 = new Bmp180("I2C1");
        pressureSensorHandler.post(pressureSensorHandlerRunnable);
        temperatureSensorHandler.post(temperatureSensorHandlerRunnable);






        try {
            setOnButtonEventListener();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    private Runnable led1HandlerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (gpioout != null) {
                    //reset the handler
                    led1Handler.postDelayed(led1HandlerRunnable, led1_INTERVAL);
                    toggleGpio();
                    Log.i(TAG, "toggleGpio: Executed."  );

                    //TODO to be extented by the developer
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable pressureSensorHandlerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (bmp180 != null) {
                    //reset the handler
                    pressureSensorHandler.postDelayed(pressureSensorHandlerRunnable, pressureSensor_INTERVAL);
                    float readPressure = readPressure();

                    //TODO to be extented by the developer
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable temperatureSensorHandlerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (bmp180 != null) {
                    //reset the handler
                    temperatureSensorHandler.postDelayed(temperatureSensorHandlerRunnable, temperatureSensor_INTERVAL);
                    float readTemperature = readTemperature();

                    //TODO to be extented by the developer
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    private void toggleGpio() throws IOException {
        ///TODO to be extented by the developer
        gpioout.toggleGpio();
    }

    private float readChannel() throws IOException {
        return pcf8591.readChannel(0);

    }

    private void setOnButtonEventListener() throws IOException {
        ///TODO to be extented by the developer
        gpiobutton.setOnButtonEventListener(new GpioButton.onButtonEventListener() {

            @Override
            public void onButtonEvent(GpioButton gpioButton, boolean currentState)
            {

            }


        });
    }

    private float readPressure() throws IOException {
        return bmp180.readPressure();

    }

    private float readTemperature() throws IOException {
        return bmp180.readTemperature();

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
    protected void onPause() {
        //TODO Auto-generated method stub
        super.onPause();
        try {
            led1Handler.removeCallbacks(led1HandlerRunnable);

            gpioout.close();
            gpioout = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            pcf8591.close();
            pcf8591 = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            gpiobutton.close();
            gpiobutton = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pressureSensorHandler.removeCallbacks(pressureSensorHandlerRunnable);
            temperatureSensorHandler.removeCallbacks(temperatureSensorHandlerRunnable);

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
