package com.example.accelerometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView xtv,ytv,ztv;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable,isNotFirstTime= false;
    private float cx,cy,cz,lx,ly,lz,dx,dy,dz;
    private float st =7f;
    private Vibrator vibrator;
    private String msg1="CHANGES MADE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xtv = findViewById(R.id.x);
        ytv = findViewById(R.id.y);
        ztv = findViewById(R.id.z);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS} , PackageManager.PERMISSION_GRANTED);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable= true;

        }
        else
            {
                xtv.setText("Accelerometer not avialable");
                isAccelerometerSensorAvailable = false;
            }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xtv.setText(event.values[0]+"m/s2");
        ytv.setText(event.values[1]+"m/s2");
        ztv.setText(event.values[2]+"m/s2");

        cx = event.values[0];
        cy = event.values[1];
        cz = event.values[2];
        if(isNotFirstTime)
        {
            dx=Math.abs(lx-cx);
            dy=Math.abs(ly-cy);
            dz=Math.abs(lz-cz);
            if((dx>st && dy>st)||(dx>st && dz>st)||(dz>st && dy>st))
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                    Toast.makeText(MainActivity.this,msg1,Toast.LENGTH_LONG).show();
                    sendSms();}

                }else{vibrator.vibrate(500);
                    Toast.makeText(MainActivity.this,msg1,Toast.LENGTH_LONG).show();
                    sendSms();}

            }
        }

        lx=cx;
        ly=cy;
        lz=cz;
        isNotFirstTime = true;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAccelerometerSensorAvailable)
              sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);


    }
    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelerometerSensorAvailable)
            sensorManager.unregisterListener(this);


    }

    public void sendSms()
    {
        String msg ="HELLO"; //yaha par tuje GPS ka code send karna hai
        String number = "8310863570";
        String Flagmsg="Message Sent";
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number ,null,msg,null,null);//yeah part mai hoga seng GPS LOCATION
        Toast.makeText(MainActivity.this,Flagmsg,Toast.LENGTH_LONG).show();
    }
}
