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
import android.widget.Toast;

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
/**
 * Represents the activity of signing up
 */
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
    int success;

    /**
     * Creates an instance that shows a sign up activity
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting variables to ui
        signUp = findViewById(R.id.signUp);
        suUserName = findViewById(R.id.suUserName);
        suPassword = findViewById(R.id.suPassword);
        suConPassword = findViewById(R.id.suConPassword);
        Login = findViewById(R.id.Login);
        //accessing database and its collections
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");

        //when sign up button is pressed
        signUp.setOnClickListener( new View.OnClickListener() {
            /**
             * function that attempts to log in user when the sign up button is clicked
             * @param view VIew
             */
            @Override
            public void onClick(View view) {
                final String userName = suUserName.getText().toString();
                final String password = suPassword.getText().toString();
                final String conPassword = suConPassword.getText().toString();
                HashMap<String, String> data = new HashMap<>();
                // if user already exists
                for (User user: userDataList){
                    if (userName.equals(user.getUserName())){
                        Log.d(TAG, "User exists already");
                        Toast.makeText(MainActivity.this, "User exists already", Toast.LENGTH_SHORT ).show();
                        success = 1;


                    }


                }
                //if user is valid
                if (success == 0){
                    Log.d(TAG, "New User is Valid");
                    Toast.makeText(MainActivity.this, "New User is Valid", Toast.LENGTH_SHORT ).show();



                }
                //add user to user database
                if (userName.length()>0 && password.length()>0 && password.equals(conPassword) && success == 0) {
                    data.put("Password", password);
                    collectionReference
                            .document(userName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                //if user is added successfully
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "User has been added successfully!");
                                    Toast.makeText(MainActivity.this, "User has been added successfully!", Toast.LENGTH_SHORT ).show();
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
                                //if user is not added successfully
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "User could not be added!" + e.toString());
                                    Toast.makeText(MainActivity.this, "User could not be added!", Toast.LENGTH_SHORT ).show();
                                }
                            });

                }
                //if passwords don't match
                if (!password.equals(conPassword)){
                    Log.d(TAG, "Password Does Not Match");
                    Toast.makeText(MainActivity.this, "Password Does Not Match", Toast.LENGTH_SHORT ).show();
                }

            }
        });
        //adds user to the list of existing users
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
        //takes user to log in activity when clicked
        Login.setOnClickListener( new View.OnClickListener() {
            /**
             * function that takes the user to log in activity when clicked
             * @param view View
             */
            @Override
            public void onClick(View view){
                Intent myIntent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(myIntent);
                finish();


            }
        });

    }


}
