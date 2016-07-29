package ffmpeg.videokit.sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
public class VideosAdapter extends RecyclerView.Adapter<VideoViewHolder>
        implements VideoViewHolder.Callbacks {

    private Callback callback = Callback.EMPTY;
    private List<VideoFile> videoFiles = new ArrayList<>();

    @Override
    public int getItemCount() {
        return videoFiles.size();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final VideoFile mediaFile = videoFiles.get(position);
        holder.bind(mediaFile, this);
    }

    public void setData(List<VideoFile> videos) {
        videoFiles = videos;
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        if (callback == null) {
            this.callback = Callback.EMPTY;
        } else {
            this.callback = callback;
        }
    }

    @Override
    public void onMediaSelected(@NonNull VideoFile videoFile) {
        callback.onMediaFileSelected(videoFile.videoPath);
    }

    public interface Callback {
        void onMediaFileSelected(String path);

        Callback EMPTY = new Callback() {
            @Override
            public void onMediaFileSelected(String path) { }
        };
    }
}
