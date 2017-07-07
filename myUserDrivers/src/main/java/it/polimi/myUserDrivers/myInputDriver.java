package it.polimi.myUserDrivers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;

import com.google.android.things.userdriver.InputDriver;
import com.google.android.things.userdriver.UserDriverManager;

import static android.content.ContentValues.TAG;

/**
 * Created by saeed on 5/5/2017.
 */

public class myInputDriver extends Service {

    private static final String DRIVER_NAME="mybottomDriver";
    private static final int DRIVER_VERSION=1;

    private static final int KEY_CODE= KeyEvent.KEYCODE_ESCAPE;


    private InputDriver buttonDriver;

    @Override
    public void onCreate() {
        super.onCreate();

        buttonDriver=InputDriver.builder(InputDevice.SOURCE_CLASS_BUTTON)
                .setName(DRIVER_NAME)
                .setVersion(DRIVER_VERSION)
                .setKeys(new int[] {KEY_CODE})
                .build();


        //register with the framework
        UserDriverManager manager=UserDriverManager.getManager();
        manager.registerInputDriver(buttonDriver);
    }

    // A state change has occurred
    private void triggerEvent(boolean pressed) {
        int action = pressed ? KeyEvent.ACTION_DOWN : KeyEvent.ACTION_UP;
        KeyEvent[] events = new KeyEvent[] {new KeyEvent(action, KEY_CODE)};

        if (!buttonDriver.emit(events)) {
            Log.w(TAG, "Unable to emit key event");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //unregister with the framework
        UserDriverManager manager=UserDriverManager.getManager();
        manager.unregisterInputDriver(buttonDriver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
