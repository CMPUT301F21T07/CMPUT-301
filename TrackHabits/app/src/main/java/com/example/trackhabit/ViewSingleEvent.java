package com.example.trackhabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewSingleEvent extends AppCompatActivity implements ManageHabitEventsFragment.EditEventListener{
    private static final String TAG = "TAG" ;




    private TextView Title;

    private TextView Reason;
    private TextView StartDate;
    private TextView locationPermissionText;
    private TextView userNameText;
    private Button showLocation;
    private ImageView imageView;

    //    private TextView consistency;
    private Button Editing;
    private Button Deleting;



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitsRef = db.collection("Habit Events");
    final private FirebaseStorage storage = FirebaseStorage.getInstance();
    final private StorageReference storageRef = storage.getReference();
    private StorageReference photoRef;


    private String habitName;
    private String userName;
    private String date;
    private String comment;
    private String location;
    private Boolean locationPermission;
    private Boolean photoUploaded;
    int index;


    @Override
    public void onOkPressed() {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);
//get all the data from ViewEvent
        Intent mIntent=getIntent();
        habitName = mIntent.getExtras().getString("habitName");
        System.out.println(habitName);
        userName = mIntent.getExtras().getString("userName");
        date = mIntent.getExtras().getString("date");
        comment = mIntent.getExtras().getString("comment");
        location = mIntent.getExtras().getString("location");
        locationPermission = mIntent.getExtras().getBoolean("Permission");
        photoUploaded = mIntent.getExtras().getBoolean("photoUploaded");

        String dataName = habitName + " " + userName + " " + date;


        if (photoUploaded) {
            photoRef = storageRef.child(dataName + ".jpg");
            imageView = findViewById(R.id.imageView);
            final long mb = 1024 * 1024;
            photoRef.getBytes(mb).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(photo);
                }
            });
        }
//setting up the single event activity
        index = mIntent.getExtras().getInt("index");

        Title=findViewById(R.id.showTitle);
        Title.setText("Habit: " + habitName);

        Reason=findViewById(R.id.showReason);
        Reason.setText("Comment: " + comment);

        locationPermissionText = findViewById(R.id.location_permission);
        locationPermissionText.setText("Location Permission: " + locationPermission.toString());

        userNameText = findViewById(R.id.username);
        userNameText.setText("User Name: " + userName);

        StartDate=findViewById(R.id.Start_date);
        StartDate.setText("Date: " + date);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //show the place where the event happened
        showLocation = findViewById(R.id.show_location);
        if(locationPermission){
        showLocation.setOnClickListener(newView -> showCurrentLocation());}
        else{
            Toast.makeText(this,"Location permission not granted",Toast.LENGTH_SHORT);
        }

        Editing=findViewById(R.id.Edit);
        Deleting=findViewById(R.id.Delete);

        Editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManageHabitEventsFragment editHabitDialog = new ManageHabitEventsFragment(
                        habitName, userName, "Edit", comment, photoUploaded,
                        locationPermission, date, location);
                editHabitDialog.show(getSupportFragmentManager(), "EDIT NEW HABIT EVENT");

            }
        });
//Deleting a event
        Deleting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataName = habitName + " " + userName + " " + date;
                if (photoUploaded) {
                    storageRef.child(dataName + ".jpg").delete()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "Photo was not deleted!");
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "Photo was successfully deleted!");
                                }
                            });
                }
                HashMap<String, String> data=new HashMap<>();
                habitsRef
                        //delete from firestore
                        .document(dataName)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"Data has been deleted successfully!");
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Cannot delete data"+e.toString());
                        finish();
                    }
                });
            }
        });



    }
    /**
     * Put the coordinates in so the marker shows on the map
     * */
    private void showCurrentLocation(){
        Intent startMapsActivity=new Intent(ViewSingleEvent.this,MapsActivity.class);

        String[] coordinates = location.split(",");
        double longitude=Double.parseDouble(coordinates[0]);
        double latitude=Double.parseDouble(coordinates[1]);
        startMapsActivity.putExtra("longitude",longitude);
        startMapsActivity.putExtra("latitude",latitude);
        startMapsActivity.putExtra("isViewSingleEvent",true);
        startActivityForResult(startMapsActivity,300);
    }

}
