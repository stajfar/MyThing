package it.polimi.mything;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import it.polimi.myUserDrivers.GpioButton;


public class ThirdActivity extends AppCompatActivity{
    private static final String TAG ="Third Activity" ;
    GpioButton gpioButton;
    int counter=0;

    Button testbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        final TextView textView=(TextView)findViewById(R.id.textView);


        List<String> gpioList=GpioButton.getBusList();

        gpioButton= GpioButton.createGpioButton("BCM26"); //GpioButton.creatGpioButton(gpioList.get(0));


        gpioButton.setOnButtonEventListener(new GpioButton.onButtonEventListener() {
            @Override
            public void onButtonEvent(GpioButton gpioButton, boolean currentState) {
                //here we would handle the button click event
                counter++;
                Log.i(TAG,"Button has clicked. Counter: "+ counter);
                textView.setText("Button has clicked. Counter: "+ counter);
                if(counter ==5)
                    gotoMainActivity();

            }
        });






    }

    private void gotoMainActivity() {
        Intent intent=new Intent(ThirdActivity.this,MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            gpioButton.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gpioButton=null;
    }


}
