package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 06.07.2016.
 * Copyright by inFullMobile
 */
public class VideoProcessingResult {
    private final int returnCode;
    private final String pathToFile;

    public VideoProcessingResult(int code, String path) {
        returnCode = code;
        pathToFile = path;
    }

    public boolean isSuccessfull() {
        return returnCode == VideoKit.FFMPEG_SUCCESS_RETURN_CODE;
    }

    public String getPath() {
        return pathToFile;
    }
}
