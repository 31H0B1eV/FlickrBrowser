package com.zinoviev.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FlickrRecycleViewAdapter extends RecyclerView.Adapter<FlickrImageViewHolder>
{
    private static final String LOG_TAG = GetRawData.class.getSimpleName();
    private List<Photo> photoList;
    private Context mContext;

    public FlickrRecycleViewAdapter(List<Photo> photoList, Context context)
    {
        this.photoList = photoList;
        this.mContext = context;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, null);

        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position)
    {
        Photo photoItem = photoList.get(position);
        Log.d(LOG_TAG, " Processing: " + photoItem.getTitle() + " --->" + Integer.toString(position));

        Picasso.with(mContext).load(photoItem.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        holder.title.setText(photoItem.getTitle());
    }

    @Override
    public int getItemCount()
    {
        return null != photoList ? photoList.size() : 0;
    }

    public void loadNewData(List<Photo> newPhotos)
    {
        photoList = newPhotos;
        notifyDataSetChanged();
    }
}
