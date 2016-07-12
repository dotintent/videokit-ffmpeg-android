package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 06.07.2016.
 */
public interface ProcessingListener {
    void onSuccess(VideoProcessingResult processingResult);
    void onFailure(VideoProcessingResult processingResult);
}
