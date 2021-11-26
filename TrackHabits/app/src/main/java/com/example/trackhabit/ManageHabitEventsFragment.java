package com.example.trackhabit;

import static android.app.Activity.RESULT_OK;

import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ManageHabitEventsFragment extends DialogFragment {
    private LocationRequest locationRequest;

    private TextView dateText;
    private EditText commentEditText;
    private Button selectImageButton;
    private TextView locationText;
    private ImageView optionalPhoto;
    private ToggleButton locationPermissionButton;

    private HabitEvent editableHabitEvent;
    private String habitName;
    private String userName;
    private String manageType = "Add";
    private String date;
    private String comment;
    private Bitmap photo;
    private Boolean locationPermission;
    String location;
    private EditEventListener listener;
    double longtitude;
    double latitude;

    private Boolean isOkPressed = false;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference habitEventsRef = db.collection("Habit Events");

    FusedLocationProviderClient client;

    public interface EditEventListener {
        void onOkPressed();
    }

    public ManageHabitEventsFragment(String habitName, String userName, String manageType) {
        this.habitName = habitName;
        this.userName = userName;
        this.manageType = manageType;
    }

    public ManageHabitEventsFragment(String habitName, String userName, String manageType,
                                     String comment, Bitmap photo, Boolean locationPermission,
                                     String date, String location) {
        this.habitName = habitName;
        this.userName = userName;
        this.manageType = manageType;
        this.comment = comment;
        this.photo = photo;
        this.locationPermission = locationPermission;
        this.date = date;
        this.location = location;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_manage_habit_events,
                null);
        dateText = view.findViewById(R.id.date_text);
        commentEditText = view.findViewById(R.id.comment_editText);
        selectImageButton = view.findViewById(R.id.select_image_button);
        optionalPhoto = view.findViewById(R.id.optionalPhoto);
        locationPermissionButton = view.findViewById(R.id.location_permission_button);
        locationText=view.findViewById(R.id.show_location);
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        String currentDateStr = dateFormat.format(currentDate);
        System.out.println(currentDateStr);
        System.out.println(manageType);
        if (manageType.equals("Add")) {
            date = currentDateStr;
            dateText.setText(currentDateStr);
        }
//
        String title = "Add HabitEvent Info";

        if (manageType.equals("Edit")) {
            dateText.setText(date);
            commentEditText.setText(comment);
            optionalPhoto.setImageBitmap(photo);
            locationPermissionButton.setChecked(locationPermission);
            title = "Edit HabitEvent Info";

//            if (locationPermission.equals(true)) {
//                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    getCurrentLocation();
//
//                }else{
//                    //request permission
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.ACCESS_COARSE_LOCATION},100);
//                }
//
//            }else{
//                location="Location denied";
//            }
        }
        locationPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationPermissionButton.isChecked()) {
                    startMaps();
                    System.out.println("Location: "+location);
                }else{
                    location="Location Hidden";
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        comment = commentEditText.getText().toString();
                        optionalPhoto.setDrawingCacheEnabled(true);
                        if (manageType.equals("Add")) {
                            photo = optionalPhoto.getDrawingCache();
                        }
                        locationPermission = locationPermissionButton.isChecked();
//                        if (locationPermission.equals(true)) {
//                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                                getCurrentLocation();
//
//                            }else{
//                                //request permission
//                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                                Manifest.permission.ACCESS_COARSE_LOCATION},100);
//                            }
//
//                        }else{
//                            location="Location denied";
//                        }
                        System.out.println("Location "+location);
                        checkInputCorrectness();
                        System.out.println(date);
                        HabitEvent newHabitEvent = new HabitEvent(habitName, userName, date, comment, photo,
                                locationPermission, location);
                        HashMap<String, Object> habitEventData = new HashMap<>();
                        habitEventData.put("HabitName", habitName);
                        habitEventData.put("UserName", userName);
                        habitEventData.put("Date", date);
                        habitEventData.put("OptionalComment", comment);
                        habitEventData.put("OptionalPhoto", photo);
                        habitEventData.put("LocationPermission", locationPermission);
                        habitEventData.put("Location", location);
                        String dataName = habitName + " " + userName + " " + date;
                        habitEventsRef.document(dataName)
                                .set(habitEventData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "Habit Event Successfully Added!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Habit Successfully Added!");
                                    }
                                });
                        if (manageType.equals("Edit")) {
                            isOkPressed = true;
                        }
                    }
                }).create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //condition
        if(requestCode==100&&(grantResults.length>0)&&
                (grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED))
        {
            getCurrentLocation();

        }else{
            Toast.makeText(getActivity(),"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
//        final String[] returnLocation = {null};
        //intialize Location manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location coordinates = task.getResult();
                    if(coordinates != null){
                        location="Latitude: "+String.valueOf(longtitude)+" Longitude: "+String.valueOf(latitude);
                        System.out.println("Statement "+location);


//
                    }else{
                        LocationRequest locationRequest=new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback=new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                System.out.println("location1"+String.valueOf(location1.getLongitude()));
                                location="Latitude: "+String.valueOf(latitude)+" Longitude: "+String.valueOf(longtitude);

                            }
                        };
                        client.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        } else{
        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS).
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (isOkPressed) {
            listener.onOkPressed();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (manageType.equals("Edit")){
        try {
            listener = (EditEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement listener");
        }}
    }

    public void checkInputCorrectness() {
        String comment = commentEditText.getText().toString();
        boolean isInputCorrect = (comment.length() <= 20);

        if (!isInputCorrect) {
            commentEditText.setError("Comment Length Is Exceeded!");
            checkInputCorrectness();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        System.out.println("Activity Result Test");
        if(requestCode==200){
            System.out.println("Request Code pass");
            if(resultCode==201){
                System.out.println("Result Code Pass");
                longtitude=data.getExtras().getDouble("Longitude",0);
                latitude=data.getExtras().getDouble("Latitude",0);
                System.out.println(longtitude);
                System.out.println(latitude);
                location = "Longitude: " + longtitude + " Latitude: " + latitude;


            }
        }

//
    }
    private void startMaps(){
        Intent startMapsActivity=new Intent(getContext(),MapsActivity.class);
        startActivityForResult(startMapsActivity,200);

    }
}
