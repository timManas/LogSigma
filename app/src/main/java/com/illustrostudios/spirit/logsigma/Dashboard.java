package com.illustrostudios.spirit.logsigma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;



import java.io.File;
import java.util.List;
import java.util.Stack;

/**
 * Created by Spirit on 5/08/2015.
 */
public class Dashboard extends Activity implements View.OnClickListener
{
    String setTimeSheetTemp;

    ImageButton setWifi;
    ImageButton exportFile;
    ImageButton startServiceDash;
    ImageButton stopServiceDash;
    ImageButton faq;

    TextView dashboard;
    TextView wifi;
    TextView preference;
    TextView timeSheet;
    TextView Status;

    String Time;

    String fileNameTimeSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.dashboard);
        initialize();

        CheckWIFIDisplayOnScreen();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case (R.id.imgbDSetup):
            {
                Intent advancedSettingsIntent = new Intent("android.intent.action.SETUPWIFI");
                startActivity(advancedSettingsIntent);
                stopService(new Intent(this, BackgroundService.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            }
            case(R.id.imgbExport):
            {
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                setTimeSheetTemp = timeSheet.getText().toString();
                sendEmailToUser(setTimeSheetTemp);
                break;
            }
            case(R.id.imgbStartService):
            {
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                startService(new Intent(this, BackgroundService.class));
                ShowAlertBox();
                System.out.println("Background Service has been STARTED");
                break;
            }
            case(R.id.imgbStopService):
            {
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                stopService(new Intent(this, BackgroundService.class));
                System.out.println("Background Service has been ENDED");
                break;
            }
            case(R.id.imgbFAQ):
            {
                Intent FAQintent = new Intent(this, FAQ.class);
                startActivity(FAQintent);
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
                break;
            }

        }
    }



    // ==========================================================================================================================

    private void sendEmailToUser(String email)
    {
        System.out.println(email);
        fileNameTimeSheet = "TimeSheetReport.txt";
        File fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(fpath, fileNameTimeSheet);
        String fileLocation = file.toString();
        System.out.println("Sending File Path: " + fileLocation);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        String to[] = new String[]{email};

        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Time Sheet Report");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "TimeSheet");
        emailIntent.setType("message/rfc822");

        startActivity(createEmailOnlyChooserIntent(emailIntent, email));
        System.out.println("SENT EMAIL SUCCESS !");

    }

    public Intent createEmailOnlyChooserIntent(Intent source, CharSequence chooserTitle)
    {
        Stack<Intent> intents = new Stack<Intent>();
        Intent i = new Intent(Intent.ACTION_SEND, Uri.fromParts("mailto", "info@domain.com", null));
        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(i, 0);

        for(ResolveInfo ri : activities)
        {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if(!intents.isEmpty())
        {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));
            return chooserIntent;
        }
        else
        {
            return Intent.createChooser(source, chooserTitle);
        }
    }

    public void CheckWIFIDisplayOnScreen()
    {

        displayWifi();
        displayPreference();
        displayTimeSheet();

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();
        WifiManager wifiManager =  (WifiManager) getSystemService (Context.WIFI_SERVICE);

        if(wifiManager.isWifiEnabled()) // If the wifi is ON but not connected to anything
        {
            if(wifi.getText().toString().equals("") || preference.getText().toString().equals("") || timeSheet.getText().toString().equals("")) // if the fields are not filled out
            {
                Status.setText("Please fill out ALL info !");
                exportFile.setVisibility(View.INVISIBLE);
                startServiceDash.setVisibility(View.INVISIBLE);
                stopServiceDash.setVisibility(View.INVISIBLE);
            }
            else
            {
                GetTotalTimeToPreferences();
                Status.setTextColor(Color.YELLOW);
                Status.setText("Total Time: " + Time);
            }
        }
        else
        {
            Status.setText("Turn your Wifi ON");
            exportFile.setVisibility(View.INVISIBLE);
            startServiceDash.setVisibility(View.INVISIBLE);
            stopServiceDash.setVisibility(View.INVISIBLE);
            setWifi.setVisibility(View.INVISIBLE);
        }

    }

    private void displayWifi()
    {
        String wifiKey = "WifiKey";
        String onScreenSetWifi = getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getString(wifiKey, "");
        wifi.setText(onScreenSetWifi);
    }

    private void displayPreference()
    {

        String onScreenSetPreference = "";
        String DVMKeyDaily = "DVMKeyDaily";
        String DVMKeyWeekly = "DVMKeyWeekly";
        String DVMKeyMonthly = "DVMKeyMonthly";

        boolean DailyBool = getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getBoolean(DVMKeyDaily, false);
        boolean  WeeklyBool = getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getBoolean(DVMKeyWeekly, false);
        boolean MonthlyBool = getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getBoolean(DVMKeyMonthly, false);

        if(DailyBool)
        {
            onScreenSetPreference += "Daily X ";
        }
        if(WeeklyBool)
        {
            onScreenSetPreference += "Weekly X ";
        }
        if(MonthlyBool)
        {
            onScreenSetPreference += "Monthly ";
        }

        preference.setText(onScreenSetPreference);
    }

    private void displayTimeSheet()
    {
        String EMAILKey = "EMAILKey";
        String email = getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getString(EMAILKey, "");
        timeSheet.setText(email);
    }

    // ============================================================================================================================


    private void GetTotalTimeToPreferences()
    {
        String timeKey = "timeKey";
        long totalTime =  getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getLong(timeKey, 0);
        long diffMinutes = totalTime / (60 * 1000) % 60;
        long diffHours = totalTime / (60 * 60 * 1000) % 24;

        Time = diffHours + " hours " + diffMinutes + " mins";
    }

    private void ShowAlertBox()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Now tracking your Work Hours..");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void initialize()
    {
        System.out.println("|||||||||||||||||||||||||||||||||||||||||| Dashboard ||||||||||||||||||||||||||||||||||||||||||||||||||||");


        setWifi = (ImageButton) findViewById(R.id.imgbDSetup);
        exportFile = (ImageButton) findViewById(R.id.imgbExport);
        startServiceDash = (ImageButton) findViewById(R.id.imgbStartService);
        stopServiceDash = (ImageButton) findViewById(R.id.imgbStopService);
        faq = (ImageButton) findViewById(R.id.imgbFAQ);

        dashboard = (TextView) findViewById(R.id.tDashboard);
        wifi = (TextView) findViewById(R.id.tWifiDashboard);
        preference = (TextView) findViewById(R.id.tFrequencyDashboard);
        timeSheet = (TextView) findViewById(R.id.tTimeSheetDashboard);
        Status = (TextView) findViewById(R.id.tStatus);



        setWifi.setOnClickListener(this);
        exportFile.setOnClickListener(this);
        startServiceDash.setOnClickListener(this);
        stopServiceDash.setOnClickListener(this);
        faq.setOnClickListener(this);

    }

    private void makeFullScreen()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

} //    End of program
