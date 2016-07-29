package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 19.07.16.
 * Copyright by inFullMobile
 */
@SuppressWarnings("unused")
public interface CommandBuilder {
    CommandBuilder overwriteOutput();
    CommandBuilder addInputPath(String inputFilePath);
    CommandBuilder addOutputPath(String outputPath);
    CommandBuilder trimForDuration(int startPosition, int duration);
    CommandBuilder withoutAudio();
    CommandBuilder copyVideoCodec();
    CommandBuilder addCrop(int x, int y, int width, int height);
    CommandBuilder addCustomCommand(String customCommand);
    CommandBuilder limitVideoBitrate(String bitrate);
    CommandBuilder addExperimentalFlag();
    CommandBuilder setAutoThreadingFlag();
    CommandBuilder limitFrameRate(int framerate);
    CommandBuilder setTuneToFast();
    Command build();
}
