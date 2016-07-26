package processing.ffmpeg.videokit;

import java.io.File;

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

    protected void deleteOutputFile() {
        final File outputFile = new File(command.getOutputPath());
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }
}
