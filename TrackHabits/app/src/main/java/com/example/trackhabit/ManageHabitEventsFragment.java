package com.example.trackhabit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Fragment that extends Dialog Fragment and manages events
 */

public class ManageHabitEventsFragment extends DialogFragment  {

    private TextView dateText;
    private EditText commentEditText;
    private Button selectImageButton;

    private ImageView optionalPhoto;
    private ToggleButton locationPermissionButton;

    private String habitName;
    private String userName;
    private String dataName;
    private String manageType = "Add";
    private String date;
    private String comment;
    private String location = "";
    private Bitmap photo;
    private Boolean photoUploaded = false;
    private double longitude;
    private double latitude;
    private Boolean locationPermission;
    private EditEventListener listener;
    private Context context;
    private Boolean imageButtonPressed=false;

    private Boolean isOkPressed = false;

    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private CollectionReference habitEventsRef = db.collection("Habit Events");
    final private FirebaseStorage storage = FirebaseStorage.getInstance();
    final private StorageReference storageRef = storage.getReference();

    StorageReference optionalPhotoRef;

    /**
     * Interface that is implemented in ViewSingleEvent to close it
     */
    public interface EditEventListener {
        void onOkPressed();
    }

    /**
     * Returns the instance of ManageHabitEventsFragment when it is 'Add' mode
     * @param habitName string of selected habit's name
     * @param userName string of selected habit's user
     * @param manageType "Add" if Dialog Fragment adds a habit event, "Edit" if DialogFragment edits
     */
    public ManageHabitEventsFragment(String habitName, String userName, String manageType) {
        this.habitName = habitName;
        this.userName = userName;
        this.manageType = manageType;
    }

    /**
     * Returns the instance of ManageHabitEventsFragment in "Edit" mode
     * @param habitName string of selected habit event's habit
     * @param userName string of selected habit event's user
     * @param manageType "Add" if Dialog Fragment adds a habit event, "Edit" if DialogFragment edits
     * @param comment string of selected habit event's comment
     * @param photoUploaded True if habit event's image was stored, false otherwise
     * @param locationPermission True if user habit event's user allowed
     * @param date string of selected habit event's date
     */
    public ManageHabitEventsFragment(String habitName, String userName, String manageType,
                                     String comment, Boolean photoUploaded, Boolean locationPermission,
                                     String date, String location) {
        this.habitName = habitName;
        this.userName = userName;
        this.manageType = manageType;
        this.comment = comment;
        this.photoUploaded = photoUploaded;
        this.locationPermission = locationPermission;
        this.date = date;
        this.location = location;
    }

    /**
     * Creates an instance that shows the fragment
     * will be check on creation of instance.
     * @param savedInstanceState This is the instance state from the previous creation of habits activity
     */

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

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        String currentDateStr = dateFormat.format(currentDate);
        System.out.println(currentDateStr);
        System.out.println(manageType);
        if (manageType.equals("Add")){
            date=currentDateStr;
            dateText.setText(currentDateStr);
        }

        String title = "Add HabitEvent Info";

        selectImageButton.setOnClickListener(newView -> takePicture());

        locationPermissionButton.setOnClickListener(new View.OnClickListener() {
            /**
             * when the location permission button is clicked
             * @param view View
             */
            @Override
            public void onClick(View view) {
                if(locationPermissionButton.isChecked()) {
                    startMaps();
                    System.out.println("Location: "+location);
                }
            }
        });

        if (manageType.equals("Edit")) {
            dateText.setText(date);
            commentEditText.setText(comment);
            optionalPhoto.setImageBitmap(photo);
            locationPermissionButton.setChecked(locationPermission);
            if (photoUploaded) {
                dataName = habitName + " " + userName + " " + date;
                optionalPhotoRef = storageRef.child(dataName + ".jpg");
                final long mb = 1024 * 1024;
                optionalPhotoRef.getBytes(mb).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    /**
                     * set image to optional photo
                     * @param bytes  byte[]
                     */
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        optionalPhoto.setImageBitmap(photo);
                    }
                });
            }
            title = "Edit HabitEvent Info";
        }

        // checking if event already exists
        dataName = habitName + " " + userName + " " + date;
        DocumentReference eventRef = habitEventsRef.document(dataName);
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            /**
             * checks if event is already added on to firebase successfully
             * @param task Task<DocumentSnapshot
             */
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && manageType.equals("Add")) {
                        Toast.makeText(getContext(), "Event already exists for this day",
                                        Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    /**
                     * when ok button is clicked
                     * @param dialogInterface DialogInterface
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        comment = commentEditText.getText().toString();
                        optionalPhoto.setDrawingCacheEnabled(true);
                        locationPermission = locationPermissionButton.isChecked();

                        checkInputCorrectness();
                        context=getContext();
                        // Storing image to Storage
                        if (photoUploaded && imageButtonPressed) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();
                            optionalPhotoRef = storageRef.child(dataName + ".jpg");
                            UploadTask uploadTask = optionalPhotoRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                /**
                                 * when photo is not stored
                                 * @param exception Exception
                                 */
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.d("TAG", "Photo Was Not Stored!");
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                /**
                                 * when photo is not stored
                                 * @param taskSnapshot Exception
                                 */
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("TAG", "Photo Successfully Stored!");
                                Toast.makeText(context,"Photo Successfully "+manageType+"ed",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                        // Storing habit event to Firestore
                        HashMap<String, Object> habitEventData = new HashMap<>();
                        habitEventData.put("HabitName", habitName);
                        habitEventData.put("UserName", userName);
                        habitEventData.put("Date", date);
                        habitEventData.put("OptionalComment", comment);
                        habitEventData.put("LocationPermission", locationPermission);
                        habitEventData.put("PhotoUploaded", photoUploaded);
                        habitEventData.put("Location", location);
                        String dataName = habitName + " " + userName + " " + date;
                        habitEventsRef.document(dataName)
                                .set(habitEventData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    /**
                                     * when event successfully added
                                     * @param unused Void
                                     */
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "Habit Event Successfully Added!");
                                        Toast.makeText(context,"Event "+manageType+"ed successfully",Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    /**
                                     * when event is not successfully added
                                     * @param e Exception
                                     */
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Habit Event Was Not Added!");
                                    }
                                });
                        if (manageType.equals("Edit")) {
                            isOkPressed = true;
                        }
                    }
                }).create();
    }

    /**
     * Starts a TakePictureActivity to get the picture from camera
     */
    private void takePicture() {
        imageButtonPressed=true;
        Intent takePictureIntent = new Intent(getContext(), TakePictureActivity.class);
        startActivityForResult(takePictureIntent, 100);
    }

    /**
     * Starts a MapsActivity to get the location
     */
    private void startMaps(){
        Intent startMapsActivity=new Intent(getContext(),MapsActivity.class);
        startMapsActivity.putExtra("isViewSingleEvent",false);
        startActivityForResult(startMapsActivity,200);

    }

    /**
     * starts events depending on result
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Toast.makeText(getContext(), "Permission for Camera is denied!", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 100 && resultCode == 102) {
            photo = (Bitmap)data.getExtras().get("data");
            optionalPhoto.setImageBitmap(photo);
            photoUploaded = true;
        }
        if(requestCode==200){
            if(resultCode==201){
                longitude=data.getExtras().getDouble("Longitude",0);
                latitude=data.getExtras().getDouble("Latitude",0);
                location = longitude + "," + latitude;
            }
        }
    }

    /**
     * checks if ok button is pressed
     */

    @Override
    public void onDetach() {
        super.onDetach();
        if (isOkPressed) {
            listener.onOkPressed();
        }
    }

    /**
     * checks if the context is edit
     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (manageType.equals("Edit")){
            try {
                listener = (EditEventListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + "Must implement listener");

            }
        }

    }

    /**
     * Checks if commentEditText's input does not exceed 20 characters
     */
    public void checkInputCorrectness() {
        String comment = commentEditText.getText().toString();
        boolean isInputCorrect = (comment.length() <= 20);

        if (!isInputCorrect) {
            commentEditText.setError("Comment Length Is Exceeded!");
            checkInputCorrectness();
        }
    }
}