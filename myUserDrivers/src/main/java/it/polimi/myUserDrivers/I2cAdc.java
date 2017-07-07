package it.polimi.myUserDrivers;

import android.os.Handler;
import android.os.HandlerThread;

import com.google.android.things.pio.I2cDevice;

import it.polimi.myUserDrivers.it.polimi.peripheral.I2cDeviceBase;


public class I2cAdc extends I2cDeviceBase implements Adc {

    /**
     * read ADC every 500 ms by default. Change this with {@link I2cAdcBuilder#withConversionRate(int)}
     */
    private static final int DEFAULT_RATE = 500;

    private static final int NUM_CHANNELS = 4;
    private static final int CHANNEL_MAX = 3;
    private static final int CHANNEL_MIN = 0;


    private int[] values = new int[NUM_CHANNELS];

    private final HandlerThread handlerThread;
    private final Handler handler;
    private final AdcReaderRunnable adcReaderRunnable = new AdcReaderRunnable();
    private final Pcf8591 pcf8591;
    private final int conversionRate;

   

    private I2cAdc(int address, int mode, int conversionRate, String bus,I2cDevice device) {
        super(device);


        this.conversionRate = conversionRate;
        handlerThread = new HandlerThread(I2cAdc.class.getSimpleName());
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        if (bus != null) {
            pcf8591 = Pcf8591.create(address, bus);
        } else {
            pcf8591 = Pcf8591.create(address);
        }
        pcf8591.configure(Pcf8591.ANALOG_OUTPUT_ENABLE | mode);

    }

    @Override
    public int readChannel(int channel) {
        if (channel < CHANNEL_MIN || channel > CHANNEL_MAX) return -1;
        return values[channel];
    }

    @Override
    public void startConversions() {
        handler.post(adcReaderRunnable);
    }

    @Override
    public void stopConversions() {
        handler.removeCallbacks(adcReaderRunnable);
    }

    @Override
    public void close() {
        stopConversions();
        handlerThread.quitSafely();
        pcf8591.close();
    }

    public static I2cAdcBuilder builder() {
        return new I2cAdcBuilder();
    }

    public static I2cAdc createI2cAdc(String i2cAdcI2cPin) {
        I2cAdc.I2cAdcBuilder builder = I2cAdc.builder();
        I2cAdc adc = builder.address(0).fourSingleEnded().withConversionRate(100).build();
        adc.startConversions();
        
        return adc;
    }


  

    public static class I2cAdcBuilder {

        private int address;
        private int mode;
        private int rate = DEFAULT_RATE;
        private String bus = null;
        private I2cDevice device=null;

        public I2cAdcBuilder address(int address) {
            this.address = address;
            return this;
        }

        public I2cAdcBuilder fourSingleEnded() {
            mode = Pcf8591.MODE_FOUR_SINGLE_ENDED;
            return this;
        }

        public I2cAdcBuilder threeDifferential() {
            mode = Pcf8591.MODE_THREE_DIFFERENTIAL;
            return this;
        }

        public I2cAdcBuilder twoSingleOneDifferential() {
            mode = Pcf8591.MODE_TWO_SINGLE_ONE_DIFFERENTIAL;
            return this;
        }

        public I2cAdcBuilder twoDifferential() {
            mode = Pcf8591.MODE_TWO_DIFFERENTIAL;
            return this;
        }

        public I2cAdcBuilder withConversionRate(int rate) {
            this.rate = rate;
            return this;
        }

        public I2cAdcBuilder withBus(String bus) {
            this.bus = bus;
            return this;
        }

        public I2cAdc build() {
            return new I2cAdc(address, mode, rate, bus,device);
        }
    }

    private class AdcReaderRunnable implements Runnable {
        @Override
        public void run() {
            int[] rawValues = pcf8591.readAllChannels();
            for (int i = 0; i < CHANNEL_MAX; i++) {
                values[i] = (values[i] + rawValues[i]) / 2;
            }
            handler.postDelayed(this, conversionRate);
        }
    }
}