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

public class Forgotpassword extends AppCompatActivity {
    ImageButton resetbutton;
    EditText Emailreset;
    FirebaseAuth auth;
    String Emailaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        auth = FirebaseAuth.getInstance();

        resetbutton = (ImageButton)findViewById(R.id.Resetbutton);
        Emailreset = (EditText) findViewById(R.id.emailreset);
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                EmailReset();
            }
        });


    }

    private void EmailReset(){

        Emailaddress = Emailreset.getText().toString();

        if(TextUtils.isEmpty(Emailaddress)){
            //Email field is empty
            Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_SHORT).show();
            return; //To avoid submitting empty email field
        }

        auth.sendPasswordResetEmail(Emailaddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Email reset send.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),sourcelogin.class));
                }

                else{
                    Toast.makeText(getApplicationContext(), "Email reset failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
