package ca.thesource.sourcerewards;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;

import com.applozic.mobicommons.commons.core.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.kommunicate.KmChatBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;

public class Chatbot extends AppCompatActivity {

    ImageButton Contactusbutton;

    ImageButton backpagebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        Contactusbutton = (ImageButton)findViewById(R.id.contactusbutton);
        backpagebutton = (ImageButton)findViewById(R.id.backpagebutton);

        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);

        Kommunicate.init(this, "ab1371818037dea6d4c3eea9b98b35e7"); // APP KEY Required for chatbot. Can be extracted from Kommunicate website. afyer

        List<String> agentList = new ArrayList();
        agentList.add("TheSourceSupport"); //Agent ID

        List<String> botList = new ArrayList();
        botList.add("thesourcesupport-oj2eq"); //Bot ID extracted from Kommunicate website to specify the bot

        Kommunicate.launchSingleChat(Chatbot.this, "Support", Kommunicate.getVisitor(), false, true, agentList, botList, new KmCallback(){
            @Override
            public void onSuccess(Object message) {
                Utils.printLog(Chatbot.this, "ChatLaunch", "Success : " + message);
            }

            @Override
            public void onFailure(Object error) {
                Utils.printLog(Chatbot.this, "ChatLaunch", "Failure : " + error);
            }
        });


        Contactusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.thesource.ca/contactUs")));

            }
        });

        backpagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent MainPage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainPage);
            }
        });
    }
}
