package com.example.nezwerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nezwerk.Adapters.AllUserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityCalling extends AppCompatActivity {
    RecyclerView recyclerview;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    SinchClient sinchClient;
    Call call;
    ArrayList<User> usersArrayList;
    DatabaseReference reference;
    Button mprofileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        auth= FirebaseAuth.getInstance();
        mprofileButton=findViewById(R.id.mprofileButton);
        firebaseUser=auth.getCurrentUser();
        usersArrayList=new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        mprofileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        sinchClient= Sinch.getSinchClientBuilder().context(this)
                .userId(firebaseUser.getUid())
                .applicationKey("3da2fbf1-4138-4d4d-85c2-aa933d32273d")
                .applicationSecret("IMzXUNrcyUK8dv2/0ydRkQ==")
                .environmentHost("clientapi.sinch.com")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener(){});
        sinchClient.start();
        fetchAllUsers();
    }

    private void fetchAllUsers() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();

                for(DataSnapshot dss : snapshot.getChildren()){
                    User nuser=dss.getValue(User.class);
                    usersArrayList.add(nuser);

                }
                AllUserAdapter adapter=new AllUserAdapter(ActivityCalling.this,usersArrayList);

                /*AllUserAdapter adapter=new AllUserAdapter(ActivityCalling.this,usersArrayList);*/
                recyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityCalling.this, "error" +error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    private class SinchCallListener implements CallListener{

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(getApplicationContext(),"Ringing...",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(getApplicationContext(),"call established",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCallEnded(Call endedCall) {
            Toast.makeText(getApplicationContext(),"call ended",Toast.LENGTH_LONG).show();
            call=null;
            endedCall.hangup();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_logout){
           if (firebaseUser !=null ){
               auth.signOut();
               finish();
               //startActivity(new Intent(ActivityCalling.this,Login.class));
               Intent i =new Intent(ActivityCalling.this,Login.class);
               startActivity(i);
           }
        }
        return onOptionsItemSelected(item);
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, final Call incomingCall) {
            AlertDialog alertDialog=new AlertDialog.Builder(ActivityCalling.this).create();
            alertDialog.setTitle("CALLING");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    call.hangup();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Pick", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    call=incomingCall;
                    call.answer();
                    call.addCallListener(new SinchCallListener());
                    Toast.makeText(ActivityCalling.this, "Call is started", Toast.LENGTH_LONG).show();
                }
            });
            alertDialog.show();

        }
    }

    public void callUser(User nuser) {
        if (call==null){
            call=sinchClient.getCallClient().callUser(firebaseUser.getUid());
            call.addCallListener(new SinchCallListener());
            openCallerDialog(call);
            
        }
    }

    private void openCallerDialog(final Call call) {
        AlertDialog alertDialogCall=new AlertDialog.Builder(ActivityCalling.this).create();
        alertDialogCall.setTitle("ALERT");
        alertDialogCall.setMessage("CALLING");
        alertDialogCall.setButton(AlertDialog.BUTTON_NEUTRAL, "Hang Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                call.hangup();
            }
        });
        alertDialogCall.show();
    }
}