package ca.thesource.sourcerewards;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity implements View.OnClickListener {
    private EditText Email;
    private EditText Pass;
    private ImageButton Signup;
    private ProgressDialog progressDialog;
    public FirebaseAuth firebaseAuth; //Used for firebase auth related commands


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance(); //Creates a new instance for data retrieval/insertion
        progressDialog = new ProgressDialog(this); //Used so that user knows something is taking place in the background(ie;account creation)

        Email = (EditText)findViewById(R.id.userlogintxt2); //brackets EditText are needed when casting a ui element
        Pass = (EditText)findViewById(R.id.userpasstxt2);
        Signup = (ImageButton)findViewById(R.id.signup);

        Signup.setOnClickListener(this);

    }

    private void Register(){
    String emailtext = Email.getText().toString().trim();
    String passtext = Pass.getText().toString().trim();

    if(TextUtils.isEmpty(emailtext)){
        //Email field is empty
        Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_SHORT).show();
        return; //To avoid submitting empty email field, program will tell you to re-enter email and then terminate the function from moving onto uploading to database
    }

    if(TextUtils.isEmpty(passtext)){
        //Password field is empty
        Toast.makeText(getApplicationContext(), "Please enter Password", Toast.LENGTH_SHORT).show();
        //stops function from executing further submitting into database
        return;
    }

    //If user email and password validations are good, move onto uploading data to database
    // First, show a progressDialog message since takes some time to write data to database.
    progressDialog.setMessage("Creating Account...");
    progressDialog.show();
    firebaseAuth.createUserWithEmailAndPassword(emailtext,passtext) //creates a given user with a specific password on firebase console
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //used to check status of registration
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Successful Registration", Toast.LENGTH_SHORT).show();
                    finish(); //End current activity to move onto the next
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }





    @Override
    public void onClick(View view) { //view used to see which buttons are clicked

        if(view == Signup){
            Register();
        }

    }

}


