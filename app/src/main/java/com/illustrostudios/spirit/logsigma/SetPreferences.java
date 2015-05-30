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
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;


/**
 * Created by Spirit on 5/07/2015.
 */
public class SetPreferences extends Activity implements View.OnClickListener
{
    ArrayList<String> setPreferences;

    ImageButton SavePreferences;
    ImageButton everyDay;
    ImageButton everyWeek;
    ImageButton everyMonth;
    TextView howOften;


    boolean Daily, Weekly,Monthly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeFullScreen();

        setContentView(R.layout.setpreferences);
        initialize();

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case(R.id.imgbSavePref):
            {
                Intent setTimeSheetIntent = new Intent("android.intent.action.SETTIMESHEET");
                startActivity(setTimeSheetIntent);
                CheckDVMPresent();
                SaveDVMToSharedPreferences();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }

            case(R.id.imgbEveryDay):
            {
                String everyDay = "Daily";
                CheckAddOrRemoveList(everyDay);
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
                break;
            }

            case(R.id.imgbEveryWeek):
            {
                String everyWeek = "Weekly";
                CheckAddOrRemoveList(everyWeek);
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
                break;
            }

            case(R.id.imgbEveryMonth):
            {
                String everyMonth = "Monthly";
                CheckAddOrRemoveList(everyMonth);
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
                break;
            }

        }
    }


    private void CheckAddOrRemoveList(String frequency)
    {
        System.out.println("Inside CheckAddOrRemoveList ---> SetPreferences");
        boolean allReadyInList = false;
        for(String strOften:setPreferences)
        {
            if(strOften.equals(frequency))
            {
                removeDay(frequency);
                allReadyInList = true;
                break;
            }
        }
        if (allReadyInList == false)
        {
            addDay(frequency);
        }
    }

    private void addDay(String frequency)
    {
        setPreferences.add(frequency);
        System.out.println("Added frequency: " + frequency);
    }

    private void removeDay(String frequency)
    {
        setPreferences.remove(frequency);
        System.out.println("Removed Frequency: " + frequency);
    }

    //==============================================================================================================

    private void initialize()
    {
        System.out.println("|||||||||||||||||||||||||||||||||||||||||| SetPrferences ||||||||||||||||||||||||||||||||||||||||||||||||||||");

        SavePreferences = (ImageButton) findViewById(R.id.imgbSavePref);
        everyDay = (ImageButton) findViewById(R.id.imgbEveryDay);
        everyWeek = (ImageButton) findViewById(R.id.imgbEveryWeek);
        everyMonth = (ImageButton) findViewById(R.id.imgbEveryMonth);
        howOften = (TextView) findViewById(R.id.tvDVM);


        SavePreferences.setOnClickListener(this);
        everyDay.setOnClickListener(this);
        everyWeek.setOnClickListener(this);
        everyMonth.setOnClickListener(this);

        setPreferences = new ArrayList<String>(3);

    }

    private void makeFullScreen()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void CheckDVMPresent()
    {
        for(String temp: setPreferences )
        {
            if(temp.equals("Daily"))
            {
                Daily = true;
            }

            if(temp.equals("Weekly"))
            {
                Weekly = true;
            }

            if(temp.equals("Monthly"))
            {
                Monthly = true;
            }

        }
    }

    private void SaveDVMToSharedPreferences()
    {
        String DVMKeyDaily = "DVMKeyDaily";
        String DVMKeyWeekly = "DVMKeyWeekly";
        String DVMKeyMonthly = "DVMKeyMonthly";

        String SharedPreferenceName = "SharedPref";

        SharedPreferences WifiSharedPref = getApplicationContext().getSharedPreferences(SharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = WifiSharedPref.edit();
        if(Daily)
        {
            editor.putBoolean(DVMKeyDaily, true);
        }
        else
        {
            editor.putBoolean(DVMKeyDaily, false);
        }
        if(Weekly)
        {
            editor.putBoolean(DVMKeyWeekly, true);
        }
        else
        {
            editor.putBoolean(DVMKeyWeekly, false);
        }
        if(Monthly)
        {
            editor.putBoolean(DVMKeyMonthly, true);
        }
        else
        {
            editor.putBoolean(DVMKeyMonthly, false);
        }

        editor.commit();
        setPreferences.clear();
        setPreferences.removeAll(setPreferences);
    }

} // End of program
