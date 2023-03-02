package com.example.hike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText username,full_name,email,password;
    Button register;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hike-3397d-default-rtdb.europe-west1.firebasedatabase.app/");
    ;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        full_name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(Register.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_username = username.getText().toString();
                String str_full_name = full_name.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_full_name)
                        || TextUtils.isEmpty(str_password))
                {
                    Toast.makeText(Register.this,"All fields must be filled",Toast.LENGTH_SHORT).show();
                }
                else if(str_password.length()<6)
                {
                    Toast.makeText(Register.this,"Password must have at least 6 characters",Toast.LENGTH_SHORT).show();
                } else {
                    register(str_username,str_full_name,str_email,str_password);
                }

            }
        });
    }





    private void register(final String username, final String full_name, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();


                            //https://www.youtube.com/watch?v=kMEkP6f9_kE&t=853s

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);


                            reference.child("Users").child(userid).child("id").setValue(userid);
                            reference.child("Users").child(userid).child("username").setValue(username);
                            reference.child("Users").child(userid).child("full_name").setValue(full_name);
                            reference.child("Users").child(userid).child("bio").setValue("");
                            reference.child("Users").child(userid).child("imageurl").setValue("https://firebasestorage.googleapis.com/v0/b/hike-3397d.appspot.com/o/blank-profile-picture.jpg?alt=media&token=7185e737-7355-49e1-b1cd-14f852713e8e");



                            //HashMap<String, Object> hashMap = new HashMap<>();
                            //hashMap.put("id",userid);
                            //hashMap.put("username",username.toLowerCase());
                            //hashMap.put("full_name",full_name);
                            //hashMap.put("bio","");
                            //hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/hike-3397d.appspot.com/o/blank-profile-picture.jpg?alt=media&token=7185e737-7355-49e1-b1cd-14f852713e8e");

                            pd.dismiss();
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                        } else {
                            pd.dismiss();
                            Toast.makeText(Register.this,"You can't register with this email or password",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
