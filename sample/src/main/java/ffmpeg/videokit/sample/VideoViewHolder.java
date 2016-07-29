package ffmpeg.videokit.sample;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import video_processing.ffmpeg.testing.R;

/**
 * Created by Ilja Kosynkin on 07.07.2016.
 * Copyright by inFullMobile
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {
    private final ImageView image;

    public VideoViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
    }

    public void bind(final VideoFile videoFile, final Callbacks callbacks) {
        if (videoFile != null) {
            final Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(
                    itemView.getContext().getContentResolver(), videoFile.videoId,
                    MediaStore.Video.Thumbnails.MINI_KIND, null);

            image.setImageBitmap(thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.onMediaSelected(videoFile);
                }
            });
        }
    }

    public interface Callbacks {
        void onMediaSelected(@NonNull VideoFile videoFile);
    }
}
