package ca.thesource.sourcerewards;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import io.kommunicate.Kommunicate;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String textscoreString;
    Integer textScoree = 0; //score that is read from memory
    ImageButton Chatbotbutton;
    ImageButton Scans;
    ImageButton Rewards;
    ImageButton Logout;
    public FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser currentuser;
    DatabaseReference myRef;
    BottomNavigationView bottomNav;

    TextView UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance(); //Creates a FirbaseAuth instance
        database = FirebaseDatabase.getInstance();

        currentuser = firebaseAuth.getCurrentUser(); //Locates to current user data
        myRef = database.getReference("users"); //Writes to the "users" node instead of writing to root of node tree


        textView = (TextView) findViewById(R.id.Score); //Used for displaying points to screen

        Scans = (ImageButton) findViewById(R.id.scan);
        Rewards = (ImageButton) findViewById(R.id.rewards);
        Logout = (ImageButton) findViewById(R.id.logout);

        Chatbotbutton = (ImageButton)findViewById(R.id.chatbotbutton);

        CheckIfUserExists(); //Checks if a useraccount currently exists in database. If not, make new one with 0 points

        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //AlphaAnimation can be used as a fader. In this case, it will be used to fade a button when clicked.

        Scans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick); // When the Scan button is pressed, the AlphaAnimation runs simulating button click effect by fading
                Intent Scanspage = new Intent(getApplicationContext(), ScanCode.class);
                startActivity(Scanspage);
            }
        });

        Rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick); // When the Rewards button is pressed, the AlphaAnimation runs simulating button click effect by fading
                Intent rewardspage = new Intent(getApplicationContext(), RewardPage.class);
                startActivity(rewardspage);
            }
        });


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Toast.makeText(getApplicationContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), Logins.class));
            }
        });

        Chatbotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick); // When the Rewards button is pressed, the AlphaAnimation runs simulating button click effect by fading
                Intent chatbot = new Intent(getApplicationContext(), Chatbot.class);
                startActivity(chatbot);
            }
        });


        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navlistener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home: //If the home navigation drawer button below is clicked
                            break;

                        case R.id.nav_rewards:
                            Intent rewardspage = new Intent(getApplicationContext(), Redemptions.class);
                            startActivity(rewardspage);
                            break;

                        case R.id.nav_card: //If the home navigation drawer button below is clicked
                            Intent transactionspage = new Intent(getApplicationContext(), transactions.class);
                            startActivity(transactionspage);
                            break;

                        case R.id.nav_stores:
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.thesource.ca/en-ca/store-finder")));
                            break;

                        case R.id.nav_support: //support/profile button pressed
                            Intent profilepage = new Intent(getApplicationContext(), myprofile.class);
                            startActivity(profilepage);
                            break;
                    }
                    return false;
                }
            };



    private void CheckIfUserExists(){ //Checks if a user exists

        myRef.child(currentuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    DataRead();
                }

                else{
                    myRef.child(currentuser.getUid()).child("points").setValue("0"); //user.getUid gets the unique User ID for the currentuser
                    myRef.child(currentuser.getUid()).child("Name").setValue(String.valueOf("Name"));
                    myRef.child(currentuser.getUid()).child("phonenumber").setValue(String.valueOf("phone number"));

                    myRef.child(currentuser.getUid()).child("birthday").setValue(String.valueOf(" birthday "));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Failed to read value

            }
        });
    }





    private void DataRead(){ //Used for reading data from database

        myRef.child(currentuser.getUid()).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                textscoreString = dataSnapshot.getValue(String.class); //Gets the value associated with the the specific UID points key

                textView.setText(textscoreString); //Display points read from database to screen

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Failed to read value

            }
        });
    }




}
