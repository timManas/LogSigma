package com.illustrostudios.spirit.logsigma;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;




public class Main extends Activity implements View.OnClickListener
{

    ImageButton splashButton;
    TextView poweredBy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);
        initialize();

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case (R.id.imgbSplash):
            {
                Intent setDaysIntent = new Intent("android.intent.action.DASHBOARD");
                startActivity(setDaysIntent);
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);

                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize()
    {

        splashButton = (ImageButton) findViewById(R.id.imgbSplash);
        splashButton.setOnClickListener(this);

        poweredBy = (TextView) findViewById(R.id.tPoweredBy);
    }


}
