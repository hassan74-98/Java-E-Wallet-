package com.example.wallet10;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;



    private IntentIntegrator qrScan;

    private Button PAY,RECEIVE,TRANSACTION;


    final Context context = this;
    private TextView balance;
    private String ownPhone, transactionAmount;

    // initiate firebase
    // User -> phone number
    private FirebaseAuth firebaseAuth;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUser = mRootRef.child("User");
    DatabaseReference mPhoneNo = mUser.child("Phone Number");



    private static final String TAG = "track Phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);



        //initiate the firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //check if user sign out
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, SignIn.class));
        }
        // get the phone of current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        String [] parts = email.split("@");
        ownPhone = parts[0];


        /*mPhoneNo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.hasChild(ownPhone))){
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LogInAndReg.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        // get the database reference of current user
        DatabaseReference mCurrentUser = mPhoneNo.child(ownPhone);
        DatabaseReference mFirstName = mCurrentUser.child("firstName");
        DatabaseReference mBalance = mCurrentUser.child("balanceAmount");

        balance = (TextView)findViewById(R.id.balance);




        /* font */
        TextView balanceText = (TextView)findViewById(R.id.balance);




        final TextView tranUserNameText = (TextView)findViewById(R.id.tranUserName);



        /* listeners */
        RECEIVE = (Button) findViewById(R.id.receive);
        RECEIVE.setOnClickListener(this);
        PAY = (Button) findViewById(R.id.pay);
        PAY.setOnClickListener(this);

        TRANSACTION = (Button) findViewById(R.id.transaction);
        TRANSACTION.setOnClickListener(this);


        Button signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);

        qrScan = new IntentIntegrator(this);


        //retrieve the Name of User from database in realtime
        mFirstName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tvfirstName = dataSnapshot.getValue(String.class);
                Log.i(TAG,tvfirstName);
                tranUserNameText.setText(tvfirstName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(Transaction.this, "Cannot find your name",Toast.LENGTH_SHORT).show();
            }
        });
        //retrieve the balance from database in realtime
        mBalance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tvBalance = dataSnapshot.getValue(String.class);
                int oldBalance = Integer.parseInt(tvBalance);
                int inte = oldBalance/100;
                int deci = oldBalance%100;
                if(deci < 10){
                    String newBalance = inte + "." + "0" + deci;
                    balance.setText(newBalance);
                }
                else{
                    String newBalance = inte + "." + deci;
                    balance.setText(newBalance);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(Transaction.this, "Cannot find your balance",Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        // do not allow android backward button
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signOutButton) {
            firebaseAuth.signOut();    //sign out
            finish();
            Intent goBackLogIn = new Intent(this, LoginAndReg.class);
            startActivity(goBackLogIn);
        }
        if(view.getId() == R.id.receive){
            Toast.makeText(Transaction.this,"aiiie", Toast.LENGTH_SHORT).show();
            Intent goToPayReceive = new Intent(view.getContext(), payAndReceive.class);
            startActivity(goToPayReceive);
        }
        if(view.getId() == R.id.pay){
            Toast.makeText(Transaction.this,"scan", Toast.LENGTH_SHORT).show();
            qrScan.initiateScan();

        }
        if(view.getId() == R.id.transaction){
            Toast.makeText(Transaction.this,"transactions", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Transaction.this,CheckTransactions.class);
            startActivity(i);

        }

    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Transaction Incomplete", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());


                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    String[] separatedMessage = result.getContents().split(" ");
                    final String senderPhone = separatedMessage[0];
                    final String tranAmount = separatedMessage[1];
                    transactionAmount = tranAmount;
                    //Log.i("The sender is", senderPhone);
                    //Log.i("The amount is", tranAmount);
                    if(tranAmount.contains(".")){
                        String[] separatedAmount = tranAmount.split("\\.");
                        String inte = separatedAmount[0];
                        String deci = separatedAmount[1];
                        //Log.i("The amount is", inte);
                        //Log.i("The amount is", deci);
                        int integ = Integer.parseInt(inte);
                        int decimal = Integer.parseInt(deci);
                        if(deci.length() == 1){decimal = decimal * 10;}
                        int total = integ*100 + decimal;
                        Log.i("The amount is", total+"");
                        transactBalance(ownPhone, senderPhone, total);
                    }
                    else{
                        int total = Integer.parseInt(tranAmount) * 100;
                        transactBalance(ownPhone, senderPhone, total);
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Transfer Amount  fromPhone -> toPhone
     * @param phoneToPay
     * @param phoneToReceive
     * @param Amount
     */
    public void transactBalance(String phoneToPay, String phoneToReceive, final int Amount){
        //get the database ref from  fromPhone
        DatabaseReference mFromUser = mPhoneNo.child(phoneToPay);
        final DatabaseReference mFromBalance = mFromUser.child("balanceAmount");
        //get the database ref  from toPhone
        DatabaseReference mToUser = mPhoneNo.child(phoneToReceive);
        final DatabaseReference mToBalance = mToUser.child("balanceAmount");
        final String sender = phoneToPay;
        final String receiver = phoneToReceive;

        mFromBalance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //retrieve the balance for fromPhone from database
                int fromBalance = Integer.parseInt(dataSnapshot.getValue(String.class));
                if(sender.equals(receiver)){Toast.makeText(Transaction.this, "llayhdik aaa smitk",Toast.LENGTH_SHORT).show(); return;}
                if(fromBalance >= Amount) {   // if enough money, deduct the value
                    fromBalance = fromBalance - Amount;
                }else{
                    Toast.makeText(Transaction.this, "Not enough money",Toast.LENGTH_SHORT).show();
                    return;
                }
                final int fromBalanceCopy = fromBalance;
                //retrieve the balance for toPhone from database
                mToBalance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int toBalance = Integer.parseInt(dataSnapshot.getValue(String.class));
                        toBalance = toBalance + Amount;   //add value
                        mFromBalance.setValue(fromBalanceCopy+"");   //update the balance for both account to database
                        mToBalance.setValue(toBalance + "");
                        //inform user transaction has been done
                        AlertDialog.Builder resultBuilder = new AlertDialog.Builder(context);
                        resultBuilder
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .setCancelable(true)
                                .setTitle("Transaction Complete")
                                .setMessage("You paid " + transactionAmount + " dirham(s)");
                        AlertDialog resultDialog = resultBuilder.create();
                        resultDialog.show();
                        //add the transaction to sender receiver and the firebase
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String date = dtf.format(now);
                        String TransactionId = sender+receiver+transactionAmount+date;
                        TransactionToFirebase PayT = new TransactionToFirebase(receiver,"- "+transactionAmount+" dh",date,0);
                        TransactionToFirebase ReceiverT = new TransactionToFirebase(sender,"+ "+transactionAmount+" dh",date);
                        TransactionToFirebase T = new TransactionToFirebase(sender,receiver,transactionAmount+" dh",date);
                        try {
                            mPhoneNo.child(sender).child("mes Transactions").child(hash(TransactionId)).setValue(PayT);
                            mPhoneNo.child(receiver).child("mes Transactions").child(hash(TransactionId)).setValue(ReceiverT);
                            mUser.child("les Transactions").child(hash(TransactionId)).setValue(T);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }





                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  String hash(String txt) throws NoSuchAlgorithmException {



        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(txt.getBytes(StandardCharsets.UTF_8));

        // bytes to hex
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();

    }





}
