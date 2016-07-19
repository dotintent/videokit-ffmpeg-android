package processing.ffmpeg.videokit;

import java.util.List;

/**
 * Created by Ilja on 19.07.16.
 * Copyright by inFullMobile
 */
public class SyncCommandExecutor extends AbstractCommandExecutor {
    SyncCommandExecutor(String outputPath, String inputPath, boolean deleteInput,
                        List<String> flags) {
        super(outputPath, inputPath, deleteInput, flags);
    }

    public VideoProcessingResult execute() {
        final int returnCode = VideoKit.getInstance().process(flagsToArgs());
        processResultCode(returnCode);
        return new VideoProcessingResult(returnCode, outputPath);
    }
}
