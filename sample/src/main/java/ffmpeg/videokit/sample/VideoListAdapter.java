package ffmpeg.videokit.sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import video_processing.ffmpeg.testing.R;

/**
 * Created by Ilja Kosynkin on 07.07.2016.
 * Copyright by inFullMobile
 */
class VideoListAdapter extends RecyclerView.Adapter<VideoListViewHolder>
        implements VideoListViewHolder.Callbacks {

    private Callback callback = Callback.EMPTY;
    private List<VideoListItem> videoListItems = new ArrayList<>();

    @Override
    public int getItemCount() {
        return videoListItems.size();
    }

    @Override
    public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new VideoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoListViewHolder holder, int position) {
        final VideoListItem mediaFile = videoListItems.get(position);
        holder.bind(mediaFile, this);
    }

    void setData(List<VideoListItem> videos) {
        videoListItems = videos;
        notifyDataSetChanged();
    }

    void setCallback(Callback callback) {
        this.callback = callback == null ? Callback.EMPTY : callback;
    }

    @Override
    public void onMediaSelected(@NonNull VideoListItem videoListItem) {
        callback.onMediaFileSelected(videoListItem.videoPath);
    }

    interface Callback {
        void onMediaFileSelected(String path);

        Callback EMPTY = new Callback() {
            @Override
            public void onMediaFileSelected(String path) { }
        };
    }
}
