package com.illustrostudios.spirit.logsigma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Spirit on 5/07/2015.
 */
public class SetTimeSheet extends Activity implements View.OnClickListener
{
    ArrayList<String> setTimeSheet;

    ImageButton SaveTimeSheet;
    EditText email;
    String userEmail;


    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeFullScreen();

        setContentView(R.layout.settimesheet);

        initialize();
        getPrevEmail();


    }



    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {   case(R.id.imgTimeSheetSave):
        {
            userEmail = email.getText().toString();

            String setUpFinished = "fin";
            Intent setDashboardIntent = new Intent("android.intent.action.DASHBOARD");
            setDashboardIntent.putExtra("newlyStarted", setUpFinished);
            startActivity(setDashboardIntent);
            SaveEmailToSharedPreferences();
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
            overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
            break;
        }

        }
    }

    private void initialize()
    {
        System.out.println("|||||||||||||||||||||||||||||||||||||||||| SetTimeSheet ||||||||||||||||||||||||||||||||||||||||||||||||||||");

        SaveTimeSheet = (ImageButton) findViewById(R.id.imgTimeSheetSave);
        email = (EditText) findViewById(R.id.etEmail);


        SaveTimeSheet.setOnClickListener(this);
        setTimeSheet = new ArrayList<String>(3);


        test = (TextView) findViewById(R.id.tvEmail);


    }


    private void makeFullScreen()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void SaveEmailToSharedPreferences()
    {
        String EMAILKey = "EMAILKey";
        String SharedPreferenceName = "SharedPref";

        SharedPreferences WifiSharedPref = getApplicationContext().getSharedPreferences(SharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = WifiSharedPref.edit();
        editor.putString(EMAILKey, userEmail);
        editor.commit();
    }

    private void getPrevEmail()
    {
        String EMAILKey = "EMAILKey";
        String emailInfo = getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getString(EMAILKey, "");
        email.setText(emailInfo);
    }

} // End of class
