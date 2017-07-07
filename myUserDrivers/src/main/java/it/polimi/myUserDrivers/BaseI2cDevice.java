package it.polimi.myUserDrivers;

        import android.support.annotation.Nullable;

        import com.google.android.things.pio.I2cDevice;
        import com.google.android.things.pio.PeripheralManagerService;

        import java.io.IOException;
        import java.util.List;

public abstract class BaseI2cDevice {

    protected final I2cDevice device;

    protected BaseI2cDevice(I2cDevice device) {
        this.device = device;

    }

    protected static String getBus() {
        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        List<String> deviceList = peripheralManagerService.getI2cBusList();
        String bus;
        if (deviceList.isEmpty()) {
            bus = "I2C1";
        } else {
            bus = deviceList.get(0);
        }
        return bus;
    }

    @Nullable
    protected static I2cDevice getDevice(String bus, int address) {
        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        I2cDevice device = null;
        try {
            device = peripheralManagerService.openI2cDevice(bus, address);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return device;
    }

    protected void close() {
        if (device != null) {
            try {
                device.close();
            } catch (IOException e) {
                // Boo!
            }
        }
    }
}