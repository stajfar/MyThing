package it.polimi.mything;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



import android.os.Handler;

import android.util.Log;

import android.widget.TextView;

import it.polimi.myUserDrivers.I2cAdc;

public class SecondActivity extends AppCompatActivity {


    private static final String TAG ="Second Activity" ;
    private Handler lightSensorHandler=new Handler();
    TextView txtView;
    Button btnGoBack;
    I2cAdc adc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        txtView= (TextView) findViewById(R.id.textViewSecondActivity);
        btnGoBack = (Button) findViewById(R.id.buttonGoBack);

        //getActionBar().setDisplayShowHomeEnabled(true);


        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        //Read the light sensor
        readLightSensor();

    }

    private void readLightSensor() {
        createAdc();

        lightSensorHandler.post(runnable);



    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            lightSensorHandler.postDelayed(runnable,1000);
            int value = adc.readChannel(0);
            int valueNoise=adc.readChannel(1);
            //int valueMagnet=adc.readChannel(2);
            Log.i(TAG, "light Sensor Value: "+value);
            txtView.setText("Light value: "+ value+ " noise value: "+valueNoise);


        }
    };


    private void createAdc() {
        I2cAdc.I2cAdcBuilder builder = I2cAdc.builder();
        adc = builder.address(0).fourSingleEnded().withConversionRate(100).build();
        adc.startConversions();
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        lightSensorHandler.removeCallbacks(runnable);
        if (adc != null) {
            adc.stopConversions();
            adc.close();
        }
    }
}
