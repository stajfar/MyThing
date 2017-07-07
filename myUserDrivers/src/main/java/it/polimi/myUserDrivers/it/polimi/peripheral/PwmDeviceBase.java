package it.polimi.myUserDrivers.it.polimi.peripheral;

import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;
import java.util.List;

/**
 * Created by saeed on 5/7/2017.
 */

public abstract class PwmDeviceBase {

    private static final String TAG ="GpioDeviceBase";
    Pwm pwmDevice;

    public PwmDeviceBase(Pwm pwmDevice) {
        this.pwmDevice = pwmDevice;
    }


    public static List<String> getPwmList() {
        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> portList = manager.getPwmList();
        if (portList.isEmpty()) {
            Log.i(TAG, "No PWM port available on this device.");
        } else {
            Log.i(TAG, "List of available ports: " + portList);
        }
        return portList;
    }


    public static Pwm getGpioDevice(String pwmName) {
        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        Pwm device = null;
        try {
            device = peripheralManagerService.openPwm(pwmName);

        } catch (IOException e) {
            Log.w(TAG, "Unable to access PWM device", e);
        }
        return device;
    }


    public static void initializePwm(Pwm pwm,int pwmFrequency,int PwmDutyCycleinPercentage) throws IOException {
        pwm.setPwmFrequencyHz(pwmFrequency);
        pwm.setPwmDutyCycle(PwmDutyCycleinPercentage);
        // Enable the PWM signal
        pwm.setEnabled(true);
    }

    public static void temporaryDisablePwm(Pwm pwm) throws IOException {
        //Temporary Enable the PWM signal
        pwm.setEnabled(false);
    }


    public void close() {
        if (pwmDevice != null) {
            try {
                pwmDevice.close();
                pwmDevice = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close PWM", e);
            }
        }
    }



}
