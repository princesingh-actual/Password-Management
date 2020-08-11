package com.example.passwordsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    Context context;
    public FingerprintHandler(Context context1){
        this.context=context1;
    }
    public void startAuth(FingerprintManager fingerprintManager,FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal=new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an Auth Error."+errString,false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Failed",false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update(String.valueOf(helpString),false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Done",true);
        //return true;
    }

    private void update(String s, boolean b) {
        if(!b){
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Passed",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this.context,MainList.class);
            intent.putExtra("username","nothing");

            context.startActivity(intent);
        }
    }
}

