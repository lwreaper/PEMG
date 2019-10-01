package com.nabil.phonemergencyjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class settingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        final SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        final EditText upn_input= findViewById(R.id.updatePhoneNumberInput);
        Button upn_button = findViewById(R.id.updatePhoneNumberButton);

        String phone_number = sp.getString("phoneNumberValue", "null");
        upn_input.setText(phone_number);

        upn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(sp.getAll());
                if(verifyPhoneNumber(upn_input.getText().toString())){
                    sp.edit().putString("phoneNumberValue", upn_input.getText().toString()).apply();
                    goHome();
                    Toast.makeText(getApplicationContext(), "Updated Phone Number!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Enter a PROPER Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goHome(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public boolean verifyPhoneNumber(String number){
        return PhoneNumberUtils.isGlobalPhoneNumber(number);
    }
}
