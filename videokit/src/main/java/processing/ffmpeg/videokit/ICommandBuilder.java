package processing.ffmpeg.videokit;

/**
 * Created by Ilja on 19.07.16.
 * Copyright by inFullMobile
 */
@SuppressWarnings("unused")
public interface ICommandBuilder {
    ICommandBuilder overwriteOutput();
    ICommandBuilder addInputPath(String inputFilePath);
    ICommandBuilder addOutputPath(String outputPath);
    ICommandBuilder trimForDuration(int startPosition, int duration);
    ICommandBuilder withoutAudio();
    ICommandBuilder copyVideoCodec();
    ICommandBuilder addCrop(int x, int y, int width, int height);
    ICommandBuilder addCustomCommand(String customCommand);
    ICommandBuilder limitVideoBitrate(String bitrate);
    ICommandBuilder addExperimentalFlag();
    ICommandBuilder deleteInput(boolean delete);
    ICommandBuilder setAutoThreadingFlag();
    ICommandBuilder limitFrameRate(int framerate);
    ICommandBuilder setTuneToFast();
    Command build();
    SyncCommandExecutor buildAndPassToSyncExecutor();
    AsyncCommandExecutor buildAndPassToAsyncExecutor(ProcessingListener listener);
}
