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

/**
 * Represents an activity for viewing a single habit
 */

public class ViewSingleEvent extends AppCompatActivity implements ManageHabitEventsFragment.EditEventListener{
    private static final String TAG = "TAG" ;

    private static final String KEY_TITLE   = "Title";
    private static final String KEY_DATE    = "Start Date";
    private static final String KEY_REASON  = "Reason";
    private static final String KEY_DAYS    = "Days";

    private Habit habit;
    private TextView Title;
    private TextView habitNameText;
    private TextView Reason;
    private TextView StartDate;
    private TextView locationPermissionText;
    private TextView userNameText;
    private Button showLocation;
    private ImageView imageView;
    private Bitmap photo;
    //    private TextView consistency;
    private Button Editing;
    private Button Deleting;


    private String habitTitle;
    private String habitReason;
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
    boolean toDelete=false;

    /**
     * function that checks if ok is pressed
     */
    @Override
    public void onOkPressed() {
        finish();
    }
    /**
     * Creates an instance that creates the dialog for viewing a habit
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_event);

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
                /**
                 * function taht checks if the photo is referenced successfully
                 * @param bytes byte[]
                 */
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(photo);
                }
            });
        }

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
            /**
             * Function that occurs when the back button is pressed
             * @param v View
             */
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showLocation = findViewById(R.id.show_location);
        if(locationPermission){
        showLocation.setOnClickListener(newView -> showCurrentLocation());}
        else{
            Toast.makeText(this,"Location permission not granted",Toast.LENGTH_SHORT);
        }

        Editing=findViewById(R.id.Edit);
        Deleting=findViewById(R.id.Delete);

        Editing.setOnClickListener(new View.OnClickListener() {
            /**
             * Function that occurs when the Editing button is pressed
             * @param view View
             */
            @Override
            public void onClick(View view) {
                ManageHabitEventsFragment editHabitDialog = new ManageHabitEventsFragment(
                        habitName, userName, "Edit", comment, photoUploaded,
                        locationPermission, date, location);
                editHabitDialog.show(getSupportFragmentManager(), "EDIT NEW HABIT EVENT");

            }
        });

        Deleting.setOnClickListener(new View.OnClickListener() {
            /**
             * Function that occurs when the Deleting button is pressed
             * @param view View
             */
            @Override
            public void onClick(View view) {
                String dataName = habitName + " " + userName + " " + date;
                if (photoUploaded) {
                    storageRef.child(dataName + ".jpg").delete()
                            .addOnFailureListener(new OnFailureListener() {
                                /**
                                 * Function that checks exceptions
                                 * @param e Exception
                                 */
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "Photo was not deleted!");
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                /**
                                 * Function that checks for exception failure
                                 */
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "Photo was successfully deleted!");
                                }
                            });
                }
                HashMap<String, String> data=new HashMap<>();
                habitsRef
                        .document(dataName)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            /**
                             * Function that checks if data is added successfully
                             */
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"Data has been deleted successfully!");
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    /**
                     * Function that checks if data is added not successfully
                     */
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
     * Function that shows the current location
     */
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

