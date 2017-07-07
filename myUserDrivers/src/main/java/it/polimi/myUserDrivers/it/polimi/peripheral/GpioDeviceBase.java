package it.polimi.myUserDrivers.it.polimi.peripheral;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.List;

/**
 * Created by saeed on 5/5/2017.
 */

public abstract class GpioDeviceBase  {

    private static final String TAG ="GpioDeviceBase" ;
    Gpio mGpio;

    public GpioDeviceBase(Gpio mGpio) {
        this.mGpio = mGpio;
    }


    public static List<String> getBusList() {
        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> portList = manager.getGpioList();
        if (portList.isEmpty()) {
            Log.i(TAG, "No GPIO port available on this device.");
        } else {
            Log.i(TAG, "List of available ports: " + portList);
        }
        return portList;
    }


    public static Gpio getGpioDevice(String gpioName) {
        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        Gpio device = null;
        try {
            device = peripheralManagerService.openGpio(gpioName);

        } catch (IOException e) {
            Log.w(TAG, "Unable to access GPIO device", e);
        }
        return device;
    }




    public static void configureInput(Gpio gpio, int gpio_active_type) throws IOException {
        gpio.setDirection(Gpio.DIRECTION_IN);
        gpio.setActiveType(gpio_active_type);
    }


    public static void configureOutput(Gpio gpio, int gpio_active_type) throws IOException {
        if(gpio_active_type == Gpio.DIRECTION_OUT_INITIALLY_HIGH) {
            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
        }
        else if(gpio_active_type == Gpio.DIRECTION_OUT_INITIALLY_LOW) {
            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        }

    }


    public static void configureEdgeTriggerType(Gpio gpio, int gpio_edge_type) throws IOException {
        gpio.setEdgeTriggerType(gpio_edge_type);
    }



    public void close() throws IOException {
        if (mGpio != null) {
            try {
                mGpio.close();
                mGpio = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close GPIO", e);
            }
        }
    }


}
