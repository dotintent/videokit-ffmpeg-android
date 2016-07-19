package ffmpeg.videokit.sample;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilja Kosynkin on 07.07.2016.
 */
public class GetVideosUseCase {
    private static final MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    private final Context appContext;

    public GetVideosUseCase(Context context) {
        appContext = context.getApplicationContext();
    }

    public List<VideoFile> getVideos() {
        final Cursor cursor = getMedia();

        final List<VideoFile> videos = new ArrayList<>();
        while (cursor.moveToNext()) {
            int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            final String path = cursor.getString(colIndex);

            if (new File(path).exists()) {
                final String time = getVideoDuration(path);

                if (time != null) {
                    final VideoFile videoFile = new VideoFile();

                    colIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    videoFile.videoId = cursor.getInt(colIndex);
                    videoFile.videoPath = path;

                    videos.add(videoFile);
                }
            }
        }

        return videos;
    }

    private Cursor getMedia() {
        final Uri externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        final String mediaConditionTag = MediaStore.Video.Media.DATE_ADDED;

        final String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA
        };
        return appContext.getContentResolver().query(
                externalContentUri, projection,
                null, null, mediaConditionTag + " DESC ");
    }

    private String getVideoDuration(String path) {
        try {
            retriever.setDataSource(path);
            return retriever.
                    extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception e) {
            return null;
        }
    }
}
