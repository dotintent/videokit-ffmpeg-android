package processing.ffmpeg.videokit;

/**
 * Created by Ilja on 19.07.16.
 * Copyright by inFullMobile
 */
public class SyncCommandExecutor extends AbstractCommandExecutor {
    SyncCommandExecutor(Command command, VideoKit videoKit) {
        super(command, videoKit);
    }

    public VideoProcessingResult execute() {
        final int returnCode = videoKit.process(command.getAsArray());
        return new VideoProcessingResult(returnCode, command.getOutputPath());
    }
}
