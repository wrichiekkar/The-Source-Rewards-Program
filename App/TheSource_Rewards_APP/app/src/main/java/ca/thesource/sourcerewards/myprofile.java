package ca.thesource.sourcerewards;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class myprofile extends AppCompatActivity {

    BottomNavigationView bottomNav;
    ImageButton Editprofile;
    ImageButton Signout;
    ImageButton Support;
    public FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser currentuser;
    DatabaseReference myRef;

    TextView NAME;
    TextView PHONE;
    TextView BIRTHDAY;

    String Phonenumber1;
    String Name1;
    String birthday1;

    int counter = 0;

    public List<String> ProfileDATA = new ArrayList<>(); //list/arraylist to hold new data of Profile such as Name,Phone,Birthday

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        firebaseAuth = FirebaseAuth.getInstance(); //Creates a FirbaseAuth instance
        database = FirebaseDatabase.getInstance();

        currentuser = firebaseAuth.getCurrentUser(); //Locates to current user data
        myRef = database.getReference("users");

        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navlistener);
        bottomNav.getMenu().getItem(4).setChecked(true); //Sets the current button viewed as selected

        Editprofile = (ImageButton) findViewById(R.id.Editprofile);
        Signout = (ImageButton) findViewById(R.id.Signout);
        Support = (ImageButton)findViewById(R.id.suport);

        NAME = (TextView)findViewById(R.id.NAMe);
        PHONE = (TextView)findViewById(R.id.PHONe);
        BIRTHDAY = (TextView)findViewById(R.id.BIRTHDAy);




        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);
        Editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                startActivity(new Intent(getApplicationContext(), EditProfile.class));

            }
        });


        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                finish();
                firebaseAuth.signOut();
                Toast.makeText(getApplicationContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Logins.class));

            }
        });

        Support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                startActivity(new Intent(getApplicationContext(), Chatbot.class));

            }
        });

        DataRead(new firebaseCallback() {

            @Override
            public void onCallback(List<String> ProfileDATA) { //Runs the callback once the function has been executed
                NAME.setText(String.valueOf(ProfileDATA.get(0))); //ProfileData contains all the various profile data elements.
                PHONE.setText(String.valueOf(ProfileDATA.get(1)));
                BIRTHDAY.setText(String.valueOf(ProfileDATA.get(2)));

            }

        });

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home: //If the home navigation drawer button below is clicked
                            Intent homepage = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(homepage);
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

                            break;
                    }
                    return false;
                }
            };

    private void DataRead(final firebaseCallback firebaseCallback) {
        //Used for Reading Points data from database using .addListenerForSingleValueEvent similar to .setOnClickListener
        myRef.child(currentuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) { //Goes through all the childnodes where childSnapshot is the incrementor in the loop containing each data
                    String ChildNodeKey = String.valueOf(childSnapshot.getValue(String.class));

                    ProfileDATA.add(ChildNodeKey);

                }
                firebaseCallback.onCallback(ProfileDATA);
                //Log.d("DATALIST", String.valueOf(ProfileDATA));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface firebaseCallback{ //Callback needed due to UI loading before firebase extracts data.
                                      //Once DataRead executes once, the callback will run and extract the data in the form of a list/array

        void onCallback(List<String> ProfileDATA);
    }
}