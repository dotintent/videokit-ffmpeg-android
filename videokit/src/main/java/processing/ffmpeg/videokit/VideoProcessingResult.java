package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 06.07.2016.
 * Copyright by inFullMobile
 */
public class VideoProcessingResult {
    public static final int SUCCESSFUL_RESULT = 0;

    private final int returnCode;
    private final String pathToFile;

    public VideoProcessingResult(int code, String path) {
        returnCode = code;
        pathToFile = path;
    }

    public boolean isSuccessful() {
        return returnCode == 0;
    }

    public String getPath() {
        return pathToFile;
    }

    public int getCode() {
        return returnCode;
    }
}
