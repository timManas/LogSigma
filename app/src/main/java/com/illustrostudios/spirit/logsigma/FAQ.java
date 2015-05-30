package com.illustrostudios.spirit.logsigma;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by Spirit on 5/07/2015.
 */
public class FAQ extends Activity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);

        //makeFullScreen();

        initialize();
    }

    @Override
    public void onClick(View view)
    {

    }

    private void initialize()
    {
    }

    private void makeFullScreen()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
