package com.example.gnc.loginwithsocialappsdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String EMAIL = "email";
    TextView facebookloginstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        facebookloginstatus = findViewById(R.id.tvVIew);
        printHashKey(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

    //    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            //    loginResult.getAccessToken();
                Toast.makeText(getApplicationContext(),"in success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(),"in cancel",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),"in error",Toast.LENGTH_SHORT).show();
                // App code
                exception.printStackTrace();
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Set<String> set = loginResult.getRecentlyGrantedPermissions();
                        for (String s : set) {
                            Toast.makeText(getApplicationContext(),"in success"+s,Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        facebookloginstatus.setText("facebook is logged in: " + isLoggedIn);



    }

    public  void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("print-hash", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("print-hash", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("print-hash", "printHashKey()", e);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
