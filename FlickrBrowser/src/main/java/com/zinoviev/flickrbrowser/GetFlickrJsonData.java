package com.zinoviev.flickrbrowser;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFlickrJsonData extends GetRawData
{
    private static final String LOG_TAG = GetRawData.class.getSimpleName();
    private List<Photo> photos;
    private Uri destinationUri;

    public GetFlickrJsonData(String searchCriteria, boolean matchAll)
    {
        super(null);
        createAndUpdateUrl(searchCriteria, matchAll);
        photos = new ArrayList<Photo>();
    }

    public void execute()
    {
        super.setmRawUrl(destinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Build uri: " + downloadJsonData.toString());
        downloadJsonData.execute(destinationUri.toString());
    }

    private boolean createAndUpdateUrl(String searchCriteria, boolean matchAll)
    {
        final String FLICKR_API_BASE_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String TAGS_PARAM = "tags";
        final String TAG_MODE_PARAM = "tagmode";
        final String FORMAT_PARAM = "format";
        final String NO_JSON_CALLBACK_PARAM = "nojsoncallback";

        destinationUri = Uri.parse(FLICKR_API_BASE_URL).buildUpon()
                .appendQueryParameter(TAGS_PARAM, searchCriteria)
                .appendQueryParameter(TAG_MODE_PARAM, matchAll ? "ALL" : "ANY")
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(NO_JSON_CALLBACK_PARAM, "1")
                .build();

        return destinationUri != null;
    }

    public List<Photo> getPhotos()
    {
        return photos;
    }

    public class DownloadJsonData extends DownloadRawData
    {
        @Override
        protected void onPostExecute(String webData)
        {
            super.onPostExecute(webData);
            processResult();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String[] parameter = { destinationUri.toString() };

            return super.doInBackground(parameter);
        }
    }

    private void processResult()
    {
        if (getmDownloadStatus() != DownloadStatus.OK)
        {
            Log.e(LOG_TAG, "Error downloading raw data");
        }

        final String FLICKR_ITEMS = "items";
        final String FLICKR_TITLE = "title";
        final String FLICKR_MEDIA = "media";
        final String FLICKR_PHOTO_URL = "m";
        final String FLICKR_AUTHOR = "author";
        final String FLICKR_AUTHOR_ID = "author_id";
        final String FLICKR_LINK = "link";
        final String FLICKR_TAGS = "tags";

        try
        {
            JSONObject jsonObject = new JSONObject(getmData());
            JSONArray jsonArray = jsonObject.getJSONArray(FLICKR_ITEMS);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject item = jsonArray.getJSONObject(i);
                String title = item.getString(FLICKR_TITLE);
                String author = item.getString(FLICKR_AUTHOR);
                String authorId = item.getString(FLICKR_AUTHOR_ID);
//                String link = item.getString(FLICKR_LINK);
                String tags = item.getString(FLICKR_TAGS);

                JSONObject media = item.getJSONObject(FLICKR_MEDIA);
                String photoUrl = media.getString(FLICKR_PHOTO_URL);
                String link = photoUrl.replaceFirst("_m.", "_b.");

                Photo photo = new Photo(title, author, authorId, link, tags, photoUrl);

                if (!this.photos.contains(photo)) this.photos.add(photo);
            }

            for (Photo item : photos)
            {
                Log.i("PhotoTesting", item.getTitle());
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error processing json object");
        }
    }
}
