package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents the activity of logging in
 */
public class LogInActivity extends AppCompatActivity {

    //set variable declarations
    Button logIn;
    EditText logUserName;
    EditText logPassword;
    FirebaseFirestore db;
    ArrayList<User> uDataList;
    final String TAG = "Sample";
    TextView errView;

    /**
     * Creates an instance that shows a log in activity
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in1);
        //setting variables to ui
        logIn = findViewById(R.id.logIn);
        logUserName = findViewById(R.id.logUserName);
        logPassword = findViewById(R.id.logPassword);
        errView = findViewById(R.id.errorView1);
        //accessing Firestore and it's collections
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        //getting list of users
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * function that adds all usernames and user password into a list of users from firebase
             * @param queryDocumentSnapshots QuerySnapshot from firebase collection
             * @param error FirebaseFirestoreException
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                if (uDataList != null){
                    uDataList.clear();
                }
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("Password")));
                    String userName = doc.getId();
                    String password = (String) doc.getData().get("Password");
                    uDataList.add(new User(userName, password)); // Adding the userName and password from FireStore
                }
            }
        });
        //when log in button is clicked
        logIn.setOnClickListener( new View.OnClickListener() {
            /**
             * function that attemps to log in user when the log in button is clicked
             * @param view VIew
             */
            @Override
            public void onClick(View view) {
                final String userName = logUserName.getText().toString();
                final String password = logPassword.getText().toString();
                int success = 0;
                if (userName.length()>0 && password.length()>0) {
                    for (User user: uDataList){
                        //if user logged in successfully
                        if (userName.equals(user.getUserName()) && password.equals(user.getPassword())){
                            Log.d(TAG, "User logged in successfully!");
                            Toast.makeText(LogInActivity.this, "User logged in successfully!", Toast.LENGTH_SHORT ).show();
                            success = 1;
                            Intent HabitsIntent = new Intent(getApplicationContext(), HabitsActivity.class);
                            HabitsIntent.putExtra("name_key", userName);
                            startActivity(HabitsIntent);
                            finish();
                        }
                    }
                    //if user was not logged in sucessfully
                    if (success == 0){
                        Log.d(TAG, "User does not exist or Password is not correct");
                        Toast.makeText(LogInActivity.this, "User does not exist or Password is not correct", Toast.LENGTH_SHORT ).show();

                    }

                }
            }
        });
        uDataList = new ArrayList<>();
    }
}