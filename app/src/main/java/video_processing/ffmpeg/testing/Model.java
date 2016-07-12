package video_processing.ffmpeg.testing;

import android.content.Context;

import java.util.List;

/**
 * Created by Ilja Kosynkin on 07.07.2016.
 */
public class Model {
    private final GetVideosUseCase getVideosUseCase;

    public Model(Context context) {
        getVideosUseCase = new GetVideosUseCase(context);
    }

    public List<VideoFile> getVideos() {
        return getVideosUseCase.getVideos();
    }
}
