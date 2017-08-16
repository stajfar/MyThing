package it.polimi.mything;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



import android.os.Handler;

import android.util.Log;

import android.widget.TextView;

import java.io.IOException;

public class SecondActivity extends AppCompatActivity {


    private static final String TAG ="Second Activity" ;
    private Handler lightSensorHandler=new Handler();
    TextView txtView;
    Button btnGoBack;
    //I2cAdc adc;

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
        //readLightSensor();

    }




}
