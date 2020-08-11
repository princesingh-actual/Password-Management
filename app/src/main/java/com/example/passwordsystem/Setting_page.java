package com.example.passwordsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class Setting_page extends AppCompatActivity {
    Switch switch2;
    Switch switch1;
    public void logout(View view){
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.passwordsystem", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("LoggedIn",false).apply();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("Logout",true);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);


        final SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.passwordsystem",MODE_PRIVATE);
        //fingerprint
        switch2= findViewById(R.id.switch2);
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch2.isChecked()){
                    Log.i("Msg","Clicked");
                    sharedPreferences.edit().putBoolean("Fingerprint",true).apply();
                }
                else{
                    sharedPreferences.edit().putBoolean("Fingerprint",false).apply();
                }
            }
        });

        //Always login
        switch1=findViewById(R.id.switch1);
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch1.isChecked()){
                    sharedPreferences.edit().putBoolean("AlwaysLoggedIn",true).apply();
                }
                else{
                    sharedPreferences.edit().putBoolean("AlwaysLoggedIn",false).apply();
                }
            }
        });


        switch1.setChecked(sharedPreferences.getBoolean("AlwaysLoggedIn",false));
        switch2.setChecked(sharedPreferences.getBoolean("Fingerprint",false));
    }
}
