package ca.thesource.sourcerewards;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {
    ImageButton Cancel;
    ImageButton Save;

    private EditText Name;
    private EditText Phone;
    private EditText Birthday;

    public FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser currentuser;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance(); //Creates a FirbaseAuth instance
        database = FirebaseDatabase.getInstance();

        currentuser = firebaseAuth.getCurrentUser(); //Locates to current user data
        myRef = database.getReference("users"); //Writes to the "users" node instead of writing to root of node tree

        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);

        Cancel = (ImageButton)findViewById(R.id.cancel);
        Save = (ImageButton) findViewById(R.id.save);

        Name = (EditText) findViewById(R.id.name);
        Phone = (EditText)findViewById(R.id.phone);
        Birthday = (EditText)findViewById(R.id.birthday);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                startActivity(new Intent(getApplicationContext(),myprofile.class));
            }
        });


        Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                ProfileSave();

            }
        });
    }


    private void ProfileSave(){
        String name = Name.getText().toString();
        String phonenumber = Phone.getText().toString();
        String birthday = Birthday.getText().toString();

        if(TextUtils.isEmpty(name)){
            // if Name field is empty
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(phonenumber)){
            Toast.makeText(getApplicationContext(), "Please enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(birthday)){
            Toast.makeText(getApplicationContext(), "Please enter birth date", Toast.LENGTH_SHORT).show();
            return;
        }


        //Save the data to the database
        myRef.child(currentuser.getUid()).child("Name").setValue(String.valueOf(name));
        myRef.child(currentuser.getUid()).child("phonenumber").setValue(String.valueOf(phonenumber));

        myRef.child(currentuser.getUid()).child("birthday").setValue(String.valueOf(birthday))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Successfully written to the database
                            finish();
                            Toast.makeText(getApplicationContext(),"Profile Updated ", Toast.LENGTH_SHORT).show();

                            Intent homepage = new Intent(getApplicationContext(), myprofile.class); //Redirect to Profile if successful scan
                            startActivity(homepage);
                        } else {
                            Toast.makeText(getApplicationContext(),"Profile Update Failed ", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
    }
}
