package ca.thesource.sourcerewards;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//Not needed here. This is needed for the POS only to scan the code. ScansPage is
public class ScansPage extends AppCompatActivity {
    SurfaceView surfaceView;
    int scancounter = 0;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    public FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser currentuser;
    DatabaseReference myRef;
    int ScoreValue; //Used as a running total of the points
    int textScoree; //score that is read from memory

    FileOutputStream outputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scans_page);

        firebaseAuth = FirebaseAuth.getInstance(); //Creates a FirbaseAuth instance
        database = FirebaseDatabase.getInstance();//Creates a Database instance

        currentuser = firebaseAuth.getCurrentUser(); //Locates to current user data
        myRef = database.getReference("users"); //Writes to the "users" node instead of writing to root of node tree
                                                //Can also do myRef.child(ChildName) to navigate to a specific node


        surfaceView = (SurfaceView) findViewById(R.id.CameraP); //needed for camera UI element stuff

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 710).build(); //Represents the size of the camera UI

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding

                    return;
                }
                try{
                    cameraSource.start(holder); //needed for camera source to start looking for codes
                }
                catch(IOException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() { //processes the data
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                // After detecting and scanning the QR code
                final SparseArray<Barcode> qrcodes =detections.getDetectedItems(); //detect the value of detetcted item


                //Run once only if some data read from QR code
                if ((qrcodes.size()!=0) && scancounter == 0)
                {
                            scancounter++; //SO that program only runs once when scanned
                            Vibrator vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //Initializes vibrator service from phone
                            vibrator.vibrate(10);
                            ScoreValue = Integer.parseInt(qrcodes.valueAt(0).displayValue); //Saves the reward points found from the barcode scan


                        //Used for Reading Points data from database
                        myRef.child(currentuser.getUid()).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            Log.d("UID",currentuser.getUid());
                            String textscoreString = dataSnapshot.getValue(String.class); //Gets the points value associated with the the specific UID points key
                            textScoree = Integer.parseInt(textscoreString); //Score extracted from database





                            ScoreValue += textScoree; //Adds new points to old points from database





                            //Used for writing/updating points in database
                            myRef.child(currentuser.getUid()).child("points").setValue(String.valueOf(ScoreValue))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Successfully written to the database
                                                finish();
                                                Toast.makeText(getApplicationContext(),"Data Upload Success ", Toast.LENGTH_SHORT).show();

                                                Intent homepage = new Intent(getApplicationContext(), MainActivity.class); //Redirect to homepage if succesful scan
                                                startActivity(homepage);
                                            } else {
                                                Toast.makeText(getApplicationContext(),"Data Upload Failed ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Failed to read value

                        }
                    });




                }







                }

            });
        }



}

