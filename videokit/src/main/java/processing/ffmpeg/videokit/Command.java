package processing.ffmpeg.videokit;

import java.util.List;

/**
 * Created by Ilja on 25.07.16.
 * Copyright by inFullMobile
 */
public class Command {
    private static final String FFMPEG_PROGRAM_NAME = "ffmpeg";

    private final List<String>  flags;
    private final String outputPath;

    Command(List<String> flags, String outputPath) {
        this.flags = flags;
        this.outputPath = outputPath;

    }

    String[] getAsArray() {
        final String ffmpegArguments[] = new String[flags.size() + 1];
        for (int i = 0; i < flags.size(); i++) {
            ffmpegArguments[i + 1] = flags.get(i);
        }

        ffmpegArguments[0] = FFMPEG_PROGRAM_NAME;

        return ffmpegArguments;
    }

    public String getOutputPath() {
        return outputPath;
    }
}
