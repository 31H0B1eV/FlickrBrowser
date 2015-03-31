package com.zinoviev.flickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity
{
    private static final String LOG_TAG = GetRawData.class.getSimpleName();
    private List<Photo> photoList = new ArrayList<Photo>();
    private RecyclerView recyclerView;
    private FlickrRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FlickrRecycleViewAdapter(new ArrayList<Photo>(), MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Toast.makeText(MainActivity.this, "Normal click: " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position)
                    {
                        Toast.makeText(MainActivity.this, "Long click: " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        String query = getSavedPreferenceData(FLICKR_QUERY);
        if (query.length() > 0)
        {
            ProcessPhotos processPhotos = new ProcessPhotos(query, true);
            processPhotos.execute();
        }
    }

    private String getSavedPreferenceData(String flickrQuery)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(flickrQuery, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        if (id == R.id.menu_search)
        {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ProcessPhotos extends GetFlickrJsonData
    {
        public ProcessPhotos(String searchCriteria, boolean matchAll)
        {
            super(searchCriteria, matchAll);
        }

        @Override
        public void execute()
        {
            super.execute();
            ProcessData processedData = new ProcessData();
            processedData.execute();
        }

        public class ProcessData extends DownloadJsonData
        {
            @Override
            protected void onPostExecute(String webData)
            {
                super.onPostExecute(webData);
                adapter.loadNewData(getPhotos());
            }
        }
    }
}









































