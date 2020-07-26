package com.example.myapplicationchirag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private EditText email;
    private EditText pwd;
    private Button login;
    private Button SignOut;
    private Button Create_Account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.emailID);
        pwd = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginbutton);
        SignOut = (Button) findViewById(R.id.SignOut);
        Create_Account=(Button) findViewById(R.id.create);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(MainActivity.this,value,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user !=null)
                    Toast.makeText(MainActivity.this,"sigin in ",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this,"not signed in",Toast.LENGTH_LONG).show();

            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstring = email.getText().toString();
                String pwdstring = pwd.getText().toString();
                if(emailstring!=null)
                    mAuth.signInWithEmailAndPassword(emailstring,pwdstring).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                                Toast.makeText(MainActivity.this,"failed sign in",Toast.LENGTH_LONG).show();
                            else {
                                Toast.makeText(MainActivity.this,"signed In",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            }
        });

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this,"you are signed out",Toast.LENGTH_LONG).show();

            }
        });

        Create_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstring = email.getText().toString();
                String pwdstring = pwd.getText().toString();
                if(emailstring!=null)
                    mAuth.createUserWithEmailAndPassword(emailstring,pwdstring).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                                Toast.makeText(MainActivity.this,"failed sign in",Toast.LENGTH_LONG).show();
                            else {
                                Toast.makeText(MainActivity.this,"signed In",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null)
            mAuth.removeAuthStateListener(mAuthListner);
    }
}
