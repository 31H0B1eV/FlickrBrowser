package com.zinoviev.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK };

public class GetRawData
{
    private static final String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;

    public GetRawData(String mRawUrl)
    {
        this.mRawUrl = mRawUrl;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    public String getmData()
    {
        return mData;
    }

    public void setmRawUrl(String mRawUrl)
    {
        this.mRawUrl = mRawUrl;
    }

    public void execute()
    {
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawUrl);
    }

    public DownloadStatus getmDownloadStatus()
    {
        return mDownloadStatus;
    }

    public void reset()
    {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mData = null;
        this.mRawUrl = null;
    }

    public class DownloadRawData extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPostExecute(String webData)
        {
            super.onPostExecute(webData);

            mData = webData;
            Log.d(LOG_TAG, "Data return was: " + mData);

            if (mData == null)
            {
                if (mRawUrl == null)
                {
                    mDownloadStatus = DownloadStatus.NOT_INITIALISED;
                } else
                {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else
            {
                mDownloadStatus = DownloadStatus.OK;
            }
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if (params == null) return null;

            try
            {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) return null;

                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (IOException e)
            {
                Log.d(LOG_TAG, "Error", e);
                return null;
            } finally
            {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    } catch (IOException e)
                    {
                        Log.d(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }
    }
}
