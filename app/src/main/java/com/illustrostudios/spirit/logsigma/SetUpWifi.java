package com.illustrostudios.spirit.logsigma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spirit on 5/07/2015.
 */
public class SetUpWifi extends Activity implements View.OnClickListener
{
    String workSSID;                    // This is what you will be sending to the confirmation class
    String currentSSID;

    ImageButton saveWifi;
    ImageButton listWifi;
    TextView setWorkWifi;
    TextView prevWorkWifi;
    List<WifiConfiguration> configs;
    ArrayList<String> strNetworkSSID;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeFullScreen();

        setContentView(R.layout.setupwifi);

        initialize();
        getPreviousSsid(this);
        getCurrentSsid(this);   //Gets the list of all WIFI Networks stored on phone

        registerForContextMenu(setWorkWifi); // this is the textview I was talking about

    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case(R.id.imgbSetupWifi):
            {
                Intent setPreferencesIntent = new Intent("android.intent.action.SETPREFERENCES");
                startActivity(setPreferencesIntent);

                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;
            }
            case(R.id.imgbList):
            {
                view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
                openContextMenu(setWorkWifi); // this is the textview I was talking about
                break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Saved Wifi Configurations");

        for(String ssidName: strNetworkSSID)
        {
            menu.add(0, v.getId(), 0, ssidName);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        workSSID = item.getTitle().toString();
        Toast.makeText(this, workSSID, Toast.LENGTH_LONG).show();
        System.out.println("Your Network choice: " + workSSID);
        prevWorkWifi.setText(workSSID);
        SaveWifiToSharedPreferences();
        return true;
    }

    //=========================================================================================================================

    private void initialize()
    {
        System.out.println("|||||||||||||||||||||||||||||||||||||||||| SetUpWifi ||||||||||||||||||||||||||||||||||||||||||||||||||||");


        workSSID = "";
        saveWifi = (ImageButton) findViewById(R.id.imgbSetupWifi);
        setWorkWifi = (TextView) findViewById(R.id.tvSetupWifi);
        prevWorkWifi = (TextView) findViewById(R.id.tvStoredWifi);




        listWifi = (ImageButton) findViewById(R.id.imgbList);


        saveWifi.setOnClickListener(this);
        listWifi.setOnClickListener(this);

        strNetworkSSID = new ArrayList<String>();
    }

    public void getPreviousSsid(Context context)
    {
        String wifiKey = "WifiKey";
        String onScreenSetWifi = getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getString(wifiKey, "");
        System.out.println(onScreenSetWifi);
        if(onScreenSetWifi.equals(""))
        {
            prevWorkWifi.setText("None Selected");
        }
        else
        {
            prevWorkWifi.setText(onScreenSetWifi);
        }
    }

    public void getCurrentSsid(Context context)
    {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        currentSSID = wifiInfo.getSSID().replace("\"", "");
        configs = wifiManager.getConfiguredNetworks();

        if (wifiManager.isWifiEnabled())
        {
            for (WifiConfiguration config : configs)
            {
                String unformattedQuotationMarks = config.SSID.toString();
                String inputSSID = unformattedQuotationMarks.replace("\"", "");
                strNetworkSSID.add(inputSSID);
            }
        }
        else
        {
            System.out.println("WIFI IS NOT TURNED ON =============================================>");
        }

    }


    private void SaveWifiToSharedPreferences()
    {
        String wifiKey = "WifiKey";
        String SharedPreferenceName = "SharedPref";

        SharedPreferences WifiSharedPref = getApplicationContext().getSharedPreferences(SharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = WifiSharedPref.edit();
        editor.putString(wifiKey, workSSID);
        editor.commit();

    }


    private void makeFullScreen()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


} // End of Class
