package com.example.wallet10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckTransactions extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;

    private Button BACK ;
    private String ownPhone;
    ArrayList<String> l = new ArrayList<>();
    // User -> phone number
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUser = mRootRef.child("User");
    DatabaseReference ref = mUser.child("Phone Number");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_transactions);



        ref.keepSynced(true);
        recyclerView = findViewById(R.id.rv);

        searchView = findViewById(R.id.searchView);
        BACK = findViewById(R.id.back);
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckTransactions.this,Transaction.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        // get the phone of current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        String [] parts = email.split("@");
        ownPhone = parts[0];
        DatabaseReference hadaaaa = ref.child(ownPhone);
        DatabaseReference hadahada = hadaaaa.child("mes Transactions");
        if(hadahada != null){
            hadahada.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){


                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            String date,amount,receiver,sender;
                            date = "date : "+ds.child("date").getValue().toString();
                            amount = "amount : "+ds.child("amount").getValue().toString();

                            if(ds.child("receiverPhone").exists()){
                                receiver = "to : "+ds.child("receiverPhone").getValue().toString();
                                l.add(date+"\n"+amount+"\n"+receiver);
                            }else{
                                sender = "from : "+ds.child("senderPhone").getValue().toString();
                                l.add(date+"\n"+amount+"\n"+sender);
                            }




                        }
                        AdapterClass adapterClass = new AdapterClass(l);

                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<String> myList = new ArrayList<>();
        for(String object : l){
            if(object.toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(myList);
        recyclerView.setAdapter(adapterClass);
    }
}
