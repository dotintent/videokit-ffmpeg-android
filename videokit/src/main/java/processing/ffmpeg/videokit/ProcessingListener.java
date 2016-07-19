package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 06.07.2016.
 * Copyright by inFullMobile
 */
public interface ProcessingListener {
    void onSuccess(String path);
    void onFailure(int returnCode);
}
