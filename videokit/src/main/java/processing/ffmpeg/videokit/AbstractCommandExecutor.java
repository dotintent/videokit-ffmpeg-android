package processing.ffmpeg.videokit;

/**
 * Created by Ilja on 19.07.16.
 * Copyright by inFullMobile
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
abstract class AbstractCommandExecutor {
    protected final Command command;
    protected final VideoKit videoKit;

    AbstractCommandExecutor(Command command, VideoKit videoKit) {
        this.command = command;
        this.videoKit = videoKit;
    }
}
