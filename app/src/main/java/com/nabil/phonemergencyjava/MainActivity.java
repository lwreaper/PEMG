package com.nabil.phonemergencyjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Boolean currentlyToggling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("com.nabil.phonemergencyjava", MODE_PRIVATE);
        final String phone_number = sp.getString("phoneNumberValue", "null");
        final Button callButton = findViewById(R.id.callButton);
        final CountDownTimer timer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String seconds = Long.toString(millisUntilFinished / 1000);
                callButton.setText(seconds);
            }

            @Override
            public void onFinish() {
//                The timer is completed,hence not toggling anymore
                currentlyToggling = false;
                Intent call_intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
                startActivity(call_intent);
                callButton.setText(getString(R.string.default_button));
            }
        };

        checkForNumber();
        checkForPermission();

        sp.edit().clear().apply();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Check if phone number is gotten
                if(phone_number.equals("null")){
                    Toast.makeText(MainActivity.this, "Update Phone Number!", Toast.LENGTH_SHORT).show();
                }else{

//                    If the call is currently toggled
                    if(currentlyToggling){

//                        Cancel the timer
                        timer.cancel();
                        Toast.makeText(MainActivity.this, "CANCELLED!", Toast.LENGTH_SHORT).show();
                        callButton.setText(getString(R.string.default_button));
                    }else{

//                        Start the timer
                        timer.start();
                    }

//                    Set the opposite
                    currentlyToggling = !currentlyToggling;
                }
            }
        });

        callButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                goToSettingsPage();
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkForNumber();
        checkForPermission();
    }

    public void checkForNumber(){
        SharedPreferences sp = getSharedPreferences("com.nabil.phonemergencyjava", MODE_PRIVATE);
        final String phone_number = sp.getString("phoneNumberValue", "null");

        if(phone_number.equals("null")){

//            Show alert!
            AlertDialog.Builder request_permission = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Set phone number")
                    .setMessage("Please set a phone number in settings to use this application")
                    .setPositiveButton("Set Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goToSettingsPage();
                        }
                    });
            request_permission.show();
        }
    }

    public void goToSettingsPage(){
        startActivity(new Intent(MainActivity.this, settingsPage.class));
    }

    public void checkForPermission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            AlertDialog.Builder request_permission = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Allow Call Permission")
                    .setMessage("Please allow permission for calling, We will need to access this phone's calling permission to call the specified contact")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                          Request permission handler
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        }
                    });
            request_permission.show();

        }
    }
}
