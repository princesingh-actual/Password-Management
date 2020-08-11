package com.example.passwordsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button regButton;
    Button loginButton;
    TextView registerText;
    EditText usernameText;
    EditText passwordText;
    EditText emailText;
    Boolean logedin;
    Boolean Alwaysloggedin;
    Boolean Fingerprint;
    public void clickedReg(View view){
       // Log.i("Register","Clicker");
        if(loginButton.getVisibility()==loginButton.VISIBLE){
            //Log.i("Inside","Register");
        loginButton.setVisibility(View.INVISIBLE);
        regButton.setVisibility(View.VISIBLE);
        emailText.setVisibility(View.VISIBLE);
        registerText.setText("Login");}
        else{
            loginButton.setVisibility(View.VISIBLE);
            regButton.setVisibility(View.INVISIBLE);
            emailText.setVisibility(View.INVISIBLE);
            registerText.setText("Register");
        }
    }
    public void registerUser(View view){
        //password Length
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.passwordsystem", Context.MODE_PRIVATE);
        if(passwordText.getText().toString().length()<6){
            Toast.makeText(this, "Password Must be atleast 6 Letters", Toast.LENGTH_SHORT).show();
        }
        //Valid Email
       else if(emailText.getText().toString().indexOf("@")==-1 || emailText.getText().toString().indexOf(".com")==-1){
            Toast.makeText(this, "Enter Valid Email id", Toast.LENGTH_SHORT).show();
        }
        //User Already exists or not
       else if(usernameText.getText().toString().equals(sharedPreferences.getString("userName","Emptyashgd"))){
            Toast.makeText(this, "User Already Exists", Toast.LENGTH_SHORT).show();
        }
        else{
        if((usernameText.getText().toString().equals("")) || (passwordText.getText().toString().equals(""))|| emailText.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show();
        }
        else{
            sharedPreferences.edit().putString("userName", usernameText.getText().toString()).apply();
            sharedPreferences.edit().putString("password", passwordText.getText().toString()).apply();
            Log.i("User","Registered");
            Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show();
            regiButton();
        }}
    }
    public void regiButton(){
        loginButton.setVisibility(View.VISIBLE);
        regButton.setVisibility(View.INVISIBLE);
        emailText.setVisibility(View.INVISIBLE);
        registerText.setText("Register");
    }
    public void  loginUser(View view){
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.passwordsystem", Context.MODE_PRIVATE);
        if((usernameText.getText().toString().equals(sharedPreferences.getString("userName","")))&&
        passwordText.getText().toString().equals(sharedPreferences.getString("password",""))){
            if(Fingerprint){
                Log.i("Msg","jumpto Fingerprint");
                sharedPreferences.edit().putBoolean("LoggedIn",true).apply();
                Intent intent=new Intent(getApplicationContext(),FringerPrint.class);
                intent.putExtra("username",usernameText.getText().toString());
                startActivity(intent);
            }
            else{
            Log.i("Login","Confirmed");
            sharedPreferences.edit().putBoolean("LoggedIn",true).apply();
            Intent intent=new Intent(getApplicationContext(),MainList.class);
            intent.putExtra("username",usernameText.getText().toString());
            startActivity(intent);}
        }
        else{
            Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regButton=findViewById(R.id.regiter);
        loginButton=findViewById(R.id.logIn);
        registerText=findViewById(R.id.regId);
        usernameText=findViewById(R.id.userNameText);
        passwordText=findViewById(R.id.passwordText_view);
        emailText=findViewById(R.id.emailEdittext);
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.passwordsystem", Context.MODE_PRIVATE);




        //Logout
        Intent intent=new Intent();
        Boolean logout=(Boolean)intent.getBooleanExtra("Logout",false);
        if(logout){
            sharedPreferences.edit().putBoolean("LoggedIn",false).apply();
            usernameText.setText("");
            passwordText.setText("");
        }

        //login thing
        logedin=sharedPreferences.getBoolean("LoggedIn",false);
        Alwaysloggedin=sharedPreferences.getBoolean("AlwaysLoggedIn",false);
        Fingerprint=sharedPreferences.getBoolean("Fingerprint",false);



        Boolean FingerPrinterror=sharedPreferences.getBoolean("fingerPrinterror",false);
        Log.i("here",String.valueOf(FingerPrinterror));
        if(FingerPrinterror){
            Fingerprint=false;
            sharedPreferences.edit().putBoolean("Fingerprint",false).apply();
        }



        if (logedin){
            if(Alwaysloggedin){
                usernameText.setText("");
                passwordText.setText("");
            }
            else{
                if(Fingerprint){
                    Log.i("Msg","Fingerprint Screen");
                    sharedPreferences.edit().putBoolean("LoggedIn",true).apply();
                    intent=new Intent(getApplicationContext(),FringerPrint.class);
                    intent.putExtra("username",sharedPreferences.getString("userName",""));
                    startActivity(intent);
                }
                else{
                    Log.i("Login","Confirmed");
                    sharedPreferences.edit().putBoolean("LoggedIn",true).apply();
                    intent=new Intent(getApplicationContext(),MainList.class);
                    intent.putExtra("username",sharedPreferences.getString("userName",""));
                    startActivity(intent);
                }
            }
        }



    }
}
