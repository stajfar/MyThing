package it.polimi.myUserDrivers.it.polimi.peripheral;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.List;

/**
 * Created by saeed on 5/5/2017.
 */

public abstract class I2cDeviceBase {
    private static final String TAG="I2CDeviceBase";
    public I2cDevice mI2cdevice;


    public I2cDeviceBase(I2cDevice device) {
        this.mI2cdevice = device;
    }

    public I2cDeviceBase(String i2C1) {

    }


    public static List<String> getBusList() {
        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> bustList = manager.getI2cBusList();
        if (bustList.isEmpty()) {
            Log.i(TAG, "No I2C port available on this device.");
        } else {
            Log.i(TAG, "List of I2C available ports: " + bustList);
        }
        return bustList;
    }


    public static I2cDevice getI2cDevice(String busName, int address) {
        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        I2cDevice device = null;
        try {
            device = peripheralManagerService.openI2cDevice(busName, address);


        } catch (IOException e) {
            Log.w(TAG, "Unable to access I2C device", e);
        }
        return device;
    }


    public void closeI2cDevice() {
        if (mI2cdevice != null) {
            try {
                mI2cdevice.close();
                mI2cdevice = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close I2C device", e);

            }
        }
    }


    //writing the registers of I2C peripheral
    public static void write(I2cDevice i2cDevice,byte[] buffer,int length) throws IOException {
        i2cDevice.write(buffer,length);
    }


    public static void writeRegByte(I2cDevice i2cDevice,int reg,byte data) throws IOException {
        i2cDevice.writeRegByte(reg,data);
    }


    public static void writeRegBuffer(I2cDevice i2cDevice,int reg,byte[] buffer,int length) throws IOException {
        i2cDevice.writeRegBuffer(reg,buffer,length);
    }


    public static void writeRegWord(I2cDevice i2cDevice,int reg,short data) throws IOException {
        i2cDevice.writeRegWord(reg,data);
    }







    //reading the registers of I2C peripheral
    public static void read(I2cDevice i2cDevice,byte[] buffer,int length) throws IOException {
        i2cDevice.read(buffer,length);
    }


    @NonNull
    public static Byte readRegByte(I2cDevice i2cDevice, int reg) throws IOException {
       return i2cDevice.readRegByte(reg);
    }


    public static void readRegBuffer(I2cDevice i2cDevice,int reg,byte[] buffer,int length) throws IOException {
        i2cDevice.readRegBuffer(reg,buffer,length);
    }


    public static short readRegWord(I2cDevice i2cDevice,int reg) throws IOException {
      return  i2cDevice.readRegWord(reg);
    }



}
