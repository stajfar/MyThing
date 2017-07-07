package it.polimi.myUserDrivers;

/**
 * Created by saeed on 5/3/2017.
 */

public interface Adc {
    /**
     * Start the ADC conversion process
     */
    void startConversions();

    /**
     * stop conversions
     */
    void stopConversions();

    /**
     * Non-blocking read of an ADC channel, will return the
     * latest valid ADC reading.
     * @param channel channel to read
     * @return last read channel value
     */
    int readChannel(int channel);

    /**
     * clean up any threads
     */
    void close();
}