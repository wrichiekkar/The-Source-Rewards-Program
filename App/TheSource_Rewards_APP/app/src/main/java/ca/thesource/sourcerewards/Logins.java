package ca.thesource.sourcerewards;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Logins extends AppCompatActivity implements View.OnClickListener {
    ImageButton Google; //Button that is put over top of Google SignInButton to get a better UI for the button
    ImageButton Othersign;
    ImageButton Source;
    public FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;

    //SignInButton signInButton; //Special Button needed for Google Signins. May not be required

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logins);

        Google = (ImageButton)findViewById(R.id.google);
        Othersign = (ImageButton)findViewById(R.id.othersign);
        Source = (ImageButton)findViewById(R.id.source);
        //signInButton = (SignInButton)findViewById(R.id.sign_in_button);

        Google.setOnClickListener(this);
        Othersign.setOnClickListener(this);
        Source.setOnClickListener(this); // Can use "this" method using if statements for button click verification or use the traditional new View.OnClickListener() autofill method.

        firebaseAuth = FirebaseAuth.getInstance(); //Creates new Auth instance

        if (firebaseAuth.getCurrentUser()!=null){
            //Already logged in using Google or TheSource Email Account
            //Intent to the MainActivity page, no need to do the authentication process below
            finish();//end any previous tasks before moving to next page
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso); //Creating an instance of GoogleSignIn Client just like firebase auth.

    }

    private void signIn() { //Used for Google Signin
        Intent signInIntent = mGoogleSignInClient.getSignInIntent(); //Used to do Google and Firebase Authentication
        startActivityForResult(signInIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //Runs after signIn function runs.
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data); //Gets the data to verify if Google credentials are true
            try {
                // Google Sign In was successful, authenticate with Firebase using firebaseAuthWithGoogle() function below
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed
                Toast.makeText(getApplicationContext(), "Google sign in Failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser(); //Get Current user instance info from database
                            finish(); //End activity before moving onto next activity
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    @Override
    public void onClick(View view) { //used to see which buttons are clicked

        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //AlphaAnimation can be used as a fader. In this case, it will be used to fade a button when clicked.

        if (view == Source){
            view.startAnimation(buttonClick);
            startActivity(new Intent(this,sourcelogin.class));
        }

        else if(view == Google){ //If signing in using Google Account
            view.startAnimation(buttonClick);
            signIn(); //Run signIn function which has inner functions that will do authentication process
        }

        else if(view == Othersign){
            view.startAnimation(buttonClick);
            startActivity(new Intent(this,signup.class));
        }
    }
}
