package com.example.wallet10;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginAndReg extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initiate the firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        //check current auth state
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            // if already log in, go to Transaction
            startActivity(new Intent(getApplicationContext(), Transaction.class));
        }
        /* onClickListener of the buttons*/
        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        Button regButton = (Button) findViewById(R.id.regButton);
        regButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signInButton) {

            Intent goToSignIn = new Intent(this, SignIn.class);
            startActivity(goToSignIn);
        }
        if(v.getId() == R.id.regButton) {

            Intent goToReg = new Intent(this, Registration.class);
            //Toast.makeText(LoginAndReg.this,"aiiie", Toast.LENGTH_SHORT).show();
            startActivity(goToReg);
        }

    }
    public void onBackPressed() {
        // do not allow android backward button
    }
}
