package com.example.passwordsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FringerPrint extends AppCompatActivity {
    private static final String KEY_NAME = "AndroidKey";
    FingerprintManager fingerprintManager;
    KeyguardManager keyguardManager;
    KeyStore keyStore;
    Cipher cipher;
    String username;
    public void FingerprintError(){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.passwordsystem", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("fingerPrinterror",true).apply();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fringer_print);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        // android should be greater than marshmallow
        // weather device has a finger print scanner or not
        //you have permission to use finger print scanner in app
        //lockscreen is secured atleast on type of lock
        //atleast one fingerprint is registered in device
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            fingerprintManager=(FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
            keyguardManager=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
            if(!fingerprintManager.isHardwareDetected()){
                Toast.makeText(this, "No Finger Print Scanner Found!!", Toast.LENGTH_SHORT).show();
                FingerprintError();
            }
            else if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Not Granted to use fingerPrint Scanner", Toast.LENGTH_SHORT).show();
                FingerprintError();}
            else if(!keyguardManager.isKeyguardSecure()){
                Toast.makeText(this, "Secure the  Key Guard!", Toast.LENGTH_SHORT).show();
                FingerprintError();}
            else if(!fingerprintManager.hasEnrolledFingerprints()){
                Toast.makeText(this, "Please Add At least One finger Print to use this!", Toast.LENGTH_SHORT).show();
                FingerprintError();
            }
            else {
                try {
                    generateKey();
                    if(initCipher()){
                        FingerprintManager.CryptoObject cryptoObject=new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler fingerprintHandler= new FingerprintHandler (FringerPrint.this);
                        fingerprintHandler.startAuth(fingerprintManager,cryptoObject);

                    }
                } catch (FingerprintException e) {
                    e.printStackTrace();
                    FingerprintError();
                }

            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintException {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new

                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }
}