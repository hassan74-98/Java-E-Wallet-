package com.example.wallet10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.prefs.BackingStoreException;

public class DebitCredit extends AppCompatActivity {

    Button BACK;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_credit);

        BACK = (Button) findViewById(R.id.back);

        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DebitCredit.this,Transaction.class);
                startActivity(i);
            }
        });

    }
}
