package com.zinoviev.flickrbrowser;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class BaseActivity extends ActionBarActivity
{
    private Toolbar toolbar;
    public static final String FLICKR_QUERY = "FLICKR_QUERY";

    protected Toolbar activateToolbar()
    {
        if (toolbar == null)
        {
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            if (toolbar != null)
            {
                setSupportActionBar(toolbar);
            }
        }

        return toolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled()
    {
        activateToolbar();
        if (toolbar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return toolbar;
    }
}
