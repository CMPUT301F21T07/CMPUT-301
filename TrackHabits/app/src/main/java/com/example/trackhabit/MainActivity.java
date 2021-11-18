package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Declare the variables so that you will be able to reference it later.
    ArrayList<User> userDataList;

    final String TAG = "Sample";
    Button signUp;
    EditText suUserName;
    EditText suPassword;
    EditText suConPassword;
    Button Login;
    FirebaseFirestore db;
    TextView suErrView;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = findViewById(R.id.signUp);
        suUserName = findViewById(R.id.suUserName);
        suPassword = findViewById(R.id.suPassword);
        suConPassword = findViewById(R.id.suConPassword);
        Login = findViewById(R.id.Login);
        suErrView = findViewById(R.id.errorView);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        signUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = suUserName.getText().toString();
                final String password = suPassword.getText().toString();
                final String conPassword = suConPassword.getText().toString();
                HashMap<String, String> data = new HashMap<>();
                for (User user: userDataList){
                    if (userName.equals(user.getUserName())){
                        Log.d(TAG, "User exists already");
                        suErrView.setText("User exists already");
                        success = 1;


                    }


                }
                if (success == 0){
                    Log.d(TAG, "New User is Valid");
                    suErrView.setText("New User is Valid");



                }
                if (userName.length()>0 && password.length()>0 && password.equals(conPassword) && success == 0) {
                    data.put("Password", password);
                    collectionReference
                            .document(userName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "User has been added successfully!");
                                    suErrView.setText("User has been added successfully!");
                                    suUserName.setText("");
                                    suPassword.setText("");
                                    suConPassword.setText("");
                                    Intent HabitsIntent = new Intent(getApplicationContext(), HabitsActivity.class);
                                    HabitsIntent.putExtra("name_key", userName);
                                    startActivity(HabitsIntent);
                                    finish();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "User could not be added!" + e.toString());
                                    suErrView.setText("User could not be added!");
                                }
                            });

                }
            }
        });
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                userDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("Password")));
                    String userName = doc.getId();
                    String password = (String) doc.getData().get("Password");
                    userDataList.add(new User(userName, password)); // Adding the cities and provinces from FireStore
                }
            }
        });


        userDataList = new ArrayList<>();

        Login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent myIntent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(myIntent);
                finish();


            }
        });

    }


}
