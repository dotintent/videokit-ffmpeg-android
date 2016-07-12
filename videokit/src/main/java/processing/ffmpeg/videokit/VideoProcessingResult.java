package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 06.07.2016.
 */
public class VideoProcessingResult {
    private final int returnCode;
    private final String pathToFile;

    public VideoProcessingResult(int code, String path) {
        returnCode = code;
        pathToFile = path;
    }

    public boolean isSuccessfull() {
        return returnCode == 0;
    }

    public String getPath() {
        return pathToFile;
    }
}
