package it.polimi.myUserDrivers;

import android.util.Log;

import com.google.android.things.pio.Gpio;

import java.io.IOException;

import it.polimi.myUserDrivers.it.polimi.peripheral.GpioDeviceBase;

/**
 * Created by saeed on 6/29/2017.
 */

public class GpioOut extends GpioDeviceBase{


    Gpio gpioDevice;

    public GpioOut(String mGpioPin) {
        super(mGpioPin);
        this.gpioDevice=createGpioOut(mGpioPin);

    }

    public static Gpio createGpioOut(String gpioName) {
        Gpio gpioDevice=getGpioDevice(gpioName);
        Log.i("GPIO DEVICE", gpioDevice.toString());
        try {
            configureOutput(gpioDevice,Gpio.ACTIVE_HIGH);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //GpioOut gpioGpioOut =new GpioOut(gpioDevice);
        return gpioDevice;
    }


    public void toggleGpio() throws IOException{
        // Toggle the GPIO state


            gpioDevice.setValue(!gpioDevice.getValue());
            Log.i("new value",String.valueOf(gpioDevice.getValue())  );

    }





}
