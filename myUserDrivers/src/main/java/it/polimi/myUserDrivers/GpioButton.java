package it.polimi.myUserDrivers;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;

import java.io.IOException;
import java.util.List;

import it.polimi.myUserDrivers.it.polimi.peripheral.GpioDeviceBase;

/**
 * Created by saeed on 5/6/2017.
 */

public class GpioButton extends GpioDeviceBase{

    static onButtonEventListener mlistener;
    static GpioButton gpioButton;
    static Gpio gpioDevice;


    public GpioButton(Gpio mGpio) {
        super(mGpio);
    }

    public GpioButton(String bcm21) {
        super(bcm21);
        this.gpioButton=createGpioButton(bcm21);
    }


    public static List<String> getBusList(){
       return GpioDeviceBase.getBusList();
   }


   public static GpioButton createGpioButton(String gpioName){
       gpioDevice=getGpioDevice(gpioName);
       try {
           configureInput(gpioDevice,Gpio.ACTIVE_HIGH);
           configureEdgeTriggerType(gpioDevice,Gpio.EDGE_FALLING);
           gpioDevice.registerGpioCallback(mIntruptCallBack);
       } catch (IOException e) {
           e.printStackTrace();
       }
       gpioButton=new GpioButton(gpioDevice);
       return gpioButton;
   }


   //for button callback

    //interface to be implemented in Activity to handle events
   public interface onButtonEventListener{
       // will be implemented by Activity
       void onButtonEvent(GpioButton gpioButton, boolean currentState);
   }


   public void setOnButtonEventListener(onButtonEventListener listener){
       mlistener=listener;
   }

    private static GpioCallback mIntruptCallBack= new IntruptCallBack();
 static  class IntruptCallBack extends GpioCallback{

       @Override
       public boolean onGpioEdge(Gpio gpio) {
           try {
               boolean currentState=gpio.getValue();
               performButtomEvent(currentState);
           } catch (IOException e) {
               e.printStackTrace();
           }

           // to continue listening to Gpio events
           return true;
       }
   }

    private static void performButtomEvent(boolean currentState) {
        if(mlistener != null){
            //put data on/call interface to be accessed by Activity
            mlistener.onButtonEvent(gpioButton, currentState);
        }
    }


    //Release Gpio port
    public void close()  throws IOException {
        if(gpioDevice!=null) {
            gpioDevice.unregisterGpioCallback(mIntruptCallBack);
        }
        super.close();
    }



}
