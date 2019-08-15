package ca.thesource.sourcerewards;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sourcelogin extends AppCompatActivity implements View.OnClickListener {
    private ImageButton Signin;
    private EditText Email;
    private ImageButton Forgetpass;
    private EditText Pass;
    private ProgressDialog progressDialog;
    public FirebaseAuth firebaseAuth; //Used for firebase auth related commands

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sourcelogin);

        firebaseAuth = FirebaseAuth.getInstance(); //Creates a new instance for data retrieval/insertion
        progressDialog = new ProgressDialog(this); //Used so that user knows something is taking place in the background(ie;account creation)

        if (firebaseAuth.getCurrentUser()!=null){ //If currently logged in, intent to MaincActivity page
            //Already logged in
            //Intent to the MainActivity page
            finish();//end any previous tasks before moving to next page
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        Email = (EditText)findViewById(R.id.userlogintxt); //brackets EditText are needed when casting a ui element
        Pass = (EditText)findViewById(R.id.userpasstxt);
        Signin = (ImageButton) findViewById(R.id.signin);
        Forgetpass = (ImageButton)findViewById(R.id.forgotpass);

        Signin.setOnClickListener(this);
        Forgetpass.setOnClickListener(this);

    }




    private void Login(){
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

        //If user email and password validations are good, move onto reading data from database for authentication
        // First, show a progressDialog message since takes some time to communicate to database.
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(emailtext,passtext) //Authenticates user and pass credentials
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //used to check status of login
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss(); //Close progressdialog once successful authentication
                            Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_SHORT).show();
                            finish(); //Before starting next activity, end the current one
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }





    @Override
    public void onClick(View view) { //view used to see which buttons are clicked
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);
        if(view == Signin){
            view.startAnimation(buttonClick);
            Login();
        }

        if(view == Forgetpass){
            view.startAnimation(buttonClick);
            startActivity(new Intent(getApplicationContext(),Forgotpassword.class));
        }

    }

}
