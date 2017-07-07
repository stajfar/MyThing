package it.polimi.myUserDrivers;

import android.util.Log;

import com.google.android.things.pio.Gpio;

import java.io.IOException;

import it.polimi.myUserDrivers.it.polimi.peripheral.GpioDeviceBase;

/**
 * Created by saeed on 6/29/2017.
 */

public class BlinkLED extends GpioDeviceBase{


    Gpio gpioDevice2;

    public BlinkLED(Gpio mGpio) {
        super(mGpio);
        this.gpioDevice2=mGpio;
    }

    public static BlinkLED createBlinkLED(String gpioName) {
        Gpio gpioDevice=getGpioDevice(gpioName);
        Log.i("GPIODEVICE haha hahah ", gpioDevice.toString());
        try {
            configureOutput(gpioDevice,Gpio.ACTIVE_HIGH);

        } catch (IOException e) {
            e.printStackTrace();
        }
        BlinkLED gpioBlinkLED=new BlinkLED(gpioDevice);
        return gpioBlinkLED;
    }


    public void toggleGpio() throws IOException{
        // Toggle the GPIO state


            gpioDevice2.setValue(!gpioDevice2.getValue());
            Log.i("new value",String.valueOf(gpioDevice2.getValue())  );

    }





}
