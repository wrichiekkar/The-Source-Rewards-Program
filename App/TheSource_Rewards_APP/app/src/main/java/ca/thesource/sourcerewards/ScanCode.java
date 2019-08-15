package ca.thesource.sourcerewards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
//This one needed for Users rewards app so that the custom ID QR code can be generated
public class ScanCode extends AppCompatActivity {
    ImageView qrImage;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    public FirebaseAuth firebaseAuth;
    FirebaseUser currentuser;
    ImageButton backbutton;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        firebaseAuth = FirebaseAuth.getInstance(); //Creates a FirbaseAuth instance
        currentuser = firebaseAuth.getCurrentUser(); //Locates to current user data
        UserID = currentuser.getUid();
        qrImage = (ImageView) findViewById(R.id.Code);
        backbutton = (ImageButton)findViewById(R.id.Backbutton);



        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder( //Used to encode the QR code with current logged in UserID data
                UserID, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        try {
            Log.d("UID",UserID);
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
        }

        backbutton.setOnClickListener(new View.OnClickListener() { //Backbutton so intents so MainActivity with updated points afterscan
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

}
