package ca.thesource.sourcerewards;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class RewardPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_page);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navlistener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home: //If the home navigation drawer button below is clicked
                            Intent homepage = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(homepage);
                            break;

                        case R.id.nav_rewards:
                            break;


                        case R.id.nav_card: //If the home navigation drawer button below is clicked
                            Intent transactionspage = new Intent(getApplicationContext(), transactions.class);
                            startActivity(transactionspage);
                            break;

                        case R.id.nav_stores:
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.thesource.ca/en-ca/store-finder")));
                            break;

                        case R.id.nav_support:
                            Intent profilepage = new Intent(getApplicationContext(), myprofile.class);
                            startActivity(profilepage);
                            break;
                    }
                return false;
                }
            };
}
